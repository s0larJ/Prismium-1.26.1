package net.s0larj.prismium.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.ModEntityTypes;
import org.joml.Vector2f;

public class AnchorProjectileEntity extends AbstractArrow {

    private Entity hookedEntity;
    public double orgX;
    public double orgY;
    public double orgZ;
    public double xDiff;
    public double yDiff;
    public double zDiff;
    public double hookX;
    public double hookY;
    public double hookZ;
    public float orgXRot;
    public float orgYRot;
    public float xRotDiff;
    public float yRotDiff;
    public float hookXRot;
    public float hookYRot;

    public AnchorProjectileEntity(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    public AnchorProjectileEntity(Level level, Player player, ItemStack itemStack) {
        super(ModEntityTypes.ANCHOR_PROJECTILE, player, level, itemStack, itemStack);
        this.pickup = Pickup.DISALLOWED;
        this.setBaseDamage(2.0);
    }

    @Override
    public ItemStack getWeaponItem() {
        return super.getWeaponItem();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return null;
    }

    public Player getPlayerOwner() {
        Entity entity = this.getOwner();
        return entity instanceof Player ? (Player)entity : null;
    }

    public void tick() {
        super.tick();

        if (this.hookedEntity != null){
            this.setPos(this.hookedEntity.getX() - this.xDiff, this.hookedEntity.getY() + this.yDiff, this.hookedEntity.getZ() - this.zDiff);
            this.setRot(this.hookedEntity.getYRot(), this.hookedEntity.getXRot());
        }

        if (this.getOwner() == null){
            this.discard();
            return;
        }

        if (this.distanceToSqr(this.getOwner()) > 2048.0){
            this.discard();
            return;
        }

        if (this.getOwner() instanceof Player && this.getWeaponItem() != null) {
            if ( ((Player) this.getOwner()).getInventory().findSlotMatchingItem(this.getWeaponItem()) == -1 ){
                this.discard();
                return;
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        Entity currentOwner = this.getOwner();
        Level level = this.level();
        if(level instanceof ServerLevel serverLevel) {
            //hurt entity on the server
            entity.hurtServer(serverLevel, this.damageSources().thrown(this, this.getPlayerOwner()), 4);
            //stop the anchor
            this.setDeltaMovement(this.getDeltaMovement().scale(0));
            //set hooked to mob that was hit
            if(entity.isAlive()){
                this.orgX = this.getX();
                this.orgY = this.getY();
                this.orgZ = this.getZ();
                this.orgXRot = this.getXRot();
                this.orgYRot = this.getYRot();

                this.hookedEntity = entity;
                this.hookX = this.hookedEntity.getX();
                this.hookY = this.hookedEntity.getY();
                this.hookZ = this.hookedEntity.getZ();
                this.hookXRot = this.hookedEntity.getXRot();
                this.hookYRot = this.hookedEntity.getYRot();

                this.setNoGravity(true);
                this.xDiff = this.orgX - this.hookX;
                this.yDiff = this.orgY - this.hookY;
                this.zDiff = this.orgY - this.hookZ;

                /*
                if (this.orgXRot > this.hookXRot){
                    if (this.orgYRot > this.hookYRot){
                        this.xRotDiff = this.orgXRot - this.hookXRot;
                        this.yRotDiff = this.orgYRot - this.hookYRot;
                    }else if (this.orgYRot < this.hookYRot){
                        this.xRotDiff = this.orgXRot - this.hookXRot;
                        this.yRotDiff = this.orgYRot + this.hookYRot;
                    }

                }else if (this.orgXRot < this.hookXRot) {
                    if (this.orgYRot > this.hookYRot) {
                        this.xRotDiff = this.orgXRot + this.hookXRot;
                        this.yRotDiff = this.orgYRot - this.hookYRot;
                    } else if (this.orgYRot < this.hookYRot) {
                        this.xRotDiff = this.orgXRot + this.hookXRot;
                        this.yRotDiff = this.orgYRot + this.hookYRot;
                    }
                }

                 */
            }
        }
        if (currentOwner instanceof LivingEntity livingOwner) {
            livingOwner.setLastHurtMob(entity);
        }


    }

    public void pull(){
        Entity player = this.getOwner();
        if (this.hookedEntity != null) {
            Vec3 vec3d = new Vec3(player.getX() - this.getX(), player.getY() - this.getY(), player.getZ() - this.getZ()).scale(0.3);
            this.hookedEntity.setDeltaMovement(this.hookedEntity.getDeltaMovement().add(vec3d));
        }
    }

    public boolean isHooked(){
        return this.hookedEntity != null;
    }

}
