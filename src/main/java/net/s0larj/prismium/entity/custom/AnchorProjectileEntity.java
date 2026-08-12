package net.s0larj.prismium.entity.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.Optionull;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.attachment.AnchorAttachment;
import net.s0larj.prismium.attachment.AnchorDebugData;
import net.s0larj.prismium.entity.ModEntityTypes;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static net.s0larj.prismium.attachment.ModAttachments.HOOKED;

public class AnchorProjectileEntity extends AbstractArrow {

    private static final EntityDataAccessor<Boolean> IS_HOOKED =
            SynchedEntityData.defineId(AnchorProjectileEntity.class, EntityDataSerializers.BOOLEAN);

    private Entity hookedEntity;

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_HOOKED, false);
    }

    public AnchorProjectileEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public AnchorProjectileEntity(Level level, Player player, ItemStack itemStack) {
        super(ModEntityTypes.ANCHOR_PROJECTILE, player, level, itemStack, itemStack);
        this.pickup = Pickup.DISALLOWED;
        this.setBaseDamage(2.0);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    // Returns the owner only when the owner is a Player.
    public Player getPlayerOwner() {
        Entity owner = this.getOwner();
        if (owner instanceof Player player) {
            return player;
        }

        return null;
    }

    @Override
    public void tick() {

        if (!this.level().isClientSide() && this.hookedEntity == null) {
            EntityHitResult modelHit = this.findExpandedModelHit();

            if (modelHit != null) {
                this.onHitEntity(modelHit);
            }
        }

        super.tick();

        // If the anchor is attached to an entity, keep the projectile positioned on that entity.
        if (this.hookedEntity != null) {
            this.setPos(
                    this.hookedEntity.getX(),
                    this.hookedEntity.getY(),
                    this.hookedEntity.getZ()
            );
            this.setRot(
                    this.hookedEntity.getYRot(),
                    this.hookedEntity.getXRot()
            );
        }


        // Remove the anchor if its owner no longer exists.
        Entity owner = this.getOwner();

        if (owner == null) {
            this.removeAttachment();
            this.discard();
            return;
        }

        // Remove the anchor if it gets too far away from its owner.
        if (this.distanceToSqr(owner) > 2048.0D) {
            this.removeAttachment();
            this.discard();
            return;
        }


        // Remove the projectile if the player no longer has the anchor weapon in their inventory.
        if (owner instanceof Player player) {

            ItemStack weapon = this.getWeaponItem();

            // getWeaponItem() can return null, so check for null first.
            if (weapon != null
                    && !weapon.isEmpty()
                    && player.getInventory().findSlotMatchingItem(weapon) == -1) {

                this.removeAttachment();
                this.discard();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {

        if (this.hookedEntity != null) {
            return;
        }

        Entity hitEntity = hitResult.getEntity();
        Entity owner = this.getOwner();

        // Damage and attachment changes should happen on the server.
        if (this.level() instanceof ServerLevel serverLevel) {

            // Hurt the entity that the anchor hit.
            hitEntity.hurtServer(serverLevel,this.damageSources().thrown(this,this.getPlayerOwner()),4.0F);

            //Stop the anchor from continuing through the entity.
            this.setDeltaMovement(Vec3.ZERO);

            //Only attach the anchor if the entity survived the hit.
            if (hitEntity.isAlive()) {

                this.hookedEntity = hitEntity;

                // Synchronize the hooked state to every client.
                this.entityData.set(IS_HOOKED, true);

                Vec3 worldHitPosition = hitResult.getLocation();

                Vec3 worldOffset = worldHitPosition.subtract(hitEntity.position());

                float bodyYaw = this.getEntityBodyYaw(hitEntity);

                Vec3 localHitPosition = this.getLocalHitPosition(hitEntity, worldHitPosition);

                float headYaw = hitEntity instanceof LivingEntity living
                        ? living.yHeadRot
                        : hitEntity.getYRot();

                // Copy the entity's current anchor attachment list.
                List<AnchorAttachment> attachments = new ArrayList<>(hitEntity.getAttachedOrElse(HOOKED,List.of()));

                AABB boundingBox = hitEntity.getBoundingBox();

                double hitHeightPercent = worldOffset.y / hitEntity.getBbHeight();

                double horizontalDistanceFromCenter = Math.sqrt(worldOffset.x * worldOffset.x + worldOffset.z * worldOffset.z);

                double ownerDistance = owner == null ? -1.0D: owner.distanceTo(hitEntity);

                AnchorDebugData debugData = new AnchorDebugData(
                        hitEntity.getType().toString(),
                        hitEntity.getClass().getName(),
                        hitEntity.getUUID(),
                        hitEntity.getId(),

                        hitEntity.position(),
                        hitEntity.getDeltaMovement(),

                        worldHitPosition,
                        worldOffset,
                        localHitPosition,

                        this.position(),
                        this.getDeltaMovement(),

                        hitEntity.getYRot(),
                        hitEntity.getXRot(),
                        bodyYaw,
                        headYaw,

                        this.getYRot(),
                        this.getXRot(),

                        hitEntity.getBbWidth(),
                        hitEntity.getBbHeight(),
                        hitEntity.getEyeHeight(),

                        new Vec3(
                                boundingBox.minX,
                                boundingBox.minY,
                                boundingBox.minZ
                        ),

                        new Vec3(
                                boundingBox.maxX,
                                boundingBox.maxY,
                                boundingBox.maxZ
                        ),

                        hitHeightPercent,
                        horizontalDistanceFromCenter,
                        ownerDistance,

                        hitEntity.isAlive(),
                        hitEntity.isInvulnerable()
                );

                attachments.add(
                        new AnchorAttachment(
                                this.getUUID(),
                                localHitPosition,
                                -this.getXRot(),
                                this.getYRot() - bodyYaw,
                                debugData
                        )
                );


                // Save the new immutable attachment list.
                hitEntity.setAttached(HOOKED,List.copyOf(attachments));

                // The anchor should no longer fall after attaching.
                this.setNoGravity(true);
            }
        }

        //Tell Minecraft that the owner last attacked this entity.
        if (owner instanceof LivingEntity livingOwner) {
            livingOwner.setLastHurtMob(hitEntity);
        }
    }


    private float getEntityBodyYaw(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity.yBodyRot;
        }
        return entity.getYRot();
    }

    /*
     * Converts a world-space hit position into a position relative
     * to the entity.
     *
     * World coordinates stay fixed in the level.
     * Local coordinates rotate with the entity.
     */
    private Vec3 getLocalHitPosition(Entity entity, Vec3 worldHitPosition) {
        Vec3 offset = worldHitPosition.subtract(entity.position());

        double yaw = Math.toRadians(this.getEntityBodyYaw(entity));
        double cos = Math.cos(yaw);
        double sin = Math.sin(yaw);

        double localX = offset.x * cos + offset.z * sin;
        double localZ = -offset.x * sin + offset.z * cos;

        double heightPercent = offset.y / entity.getBbHeight();

        return new Vec3(localX, heightPercent, localZ);
    }

    // Pulls the hooked entity toward the owner.
    public void pull() {
        Entity owner = this.getOwner();

        if (owner == null || this.hookedEntity == null) {
            return;
        }

        // Remove this anchor from the hooked entity's rendered attachment list.
        this.removeAttachment();

        // Direction from the hooked entity toward the player.
        Vec3 pullDirection = owner.position().subtract(this.hookedEntity.position());

        // Reduce the strength so the entity moves toward the player instead of instantly reaching the player.
        Vec3 pullVelocity = pullDirection.scale(0.3D);

        // Add the pulling force to the entity's current movement.
        this.hookedEntity.setDeltaMovement(this.hookedEntity.getDeltaMovement().add(pullVelocity));
    }

    // Removes this projectile's attachment from the hooked entity.
    private void removeAttachment() {
        if (this.hookedEntity == null) {
            return;
        }

        List<AnchorAttachment> attachments = new ArrayList<>(this.hookedEntity.getAttachedOrElse(HOOKED,List.of()));

        // Remove the attachment that has this projectile's UUID.
        attachments.removeIf(attachment -> attachment.uuid().equals(this.getUUID()));

        this.hookedEntity.setAttached(HOOKED, List.copyOf(attachments));

        // Tell the client this projectile is no longer attached.
        this.entityData.set(IS_HOOKED, false);
    }

    private EntityHitResult findExpandedModelHit() {
        Vec3 start = this.position();
        Vec3 end = start.add(this.getDeltaMovement());

        AABB searchBox = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D);

        Entity closestEntity = null;
        Vec3 closestHitPosition = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : this.level().getEntities(this, searchBox, entity ->
                entity != this.getOwner() && entity.isAlive() && entity.canBeHitByProjectile())) {

            AABB entityBox = entity.getBoundingBox();

            // Dynamically expands around the sides for arms, heads,
            // ears, horns, horse heads, and similar model parts.
            double horizontalPadding = Math.max(
                    0.18D,
                    entity.getBbWidth() * 0.35D
            );

            // Only expands upward instead of below the entity's feet.
            double topPadding = Math.max(
                    0.15D,
                    entity.getBbHeight() * 0.20D
            );

            AABB expandedBox = new AABB(
                    entityBox.minX - horizontalPadding,
                    entityBox.minY,
                    entityBox.minZ - horizontalPadding,

                    entityBox.maxX + horizontalPadding,
                    entityBox.maxY + topPadding,
                    entityBox.maxZ + horizontalPadding
            );

            Optional<Vec3> possibleHit = expandedBox.clip(start, end);

            if (possibleHit.isEmpty()) {
                continue;
            }

            Vec3 hitPosition = possibleHit.get();
            double distance = start.distanceToSqr(hitPosition);

            if (distance < closestDistance) {
                closestDistance = distance;
                closestEntity = entity;
                closestHitPosition = hitPosition;
            }
        }

        if (closestEntity == null) {
            return null;
        }

        return new EntityHitResult(closestEntity, closestHitPosition);
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        AABB aoe = this.getBoundingBox().inflate(0.5d);
        for (Entity entity : this.level().getEntities(this, aoe, entity ->
                entity != this.getOwner() && entity.isAlive() && entity.canBeHitByProjectile())) {

            if (this.level() instanceof ServerLevel serverLevel) {
                // Hurt the entity that is in the AOE.
                entity.hurtServer(serverLevel, this.damageSources().thrown(this, this.getPlayerOwner()), 4.0F);
            }
        }

    }

    // Returns true after the projectile has attached to an entity.
    public boolean isHooked() {
        return this.entityData.get(IS_HOOKED);
    }

}
