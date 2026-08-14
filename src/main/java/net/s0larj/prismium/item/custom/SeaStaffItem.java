package net.s0larj.prismium.item.custom;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.custom.SphereEntity;

import java.util.List;

public class SeaStaffItem extends Item {

    public SeaStaffItem(Properties properties) {
        super(properties);
    }

    public SphereEntity sphereEntity;

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int remainingTime) {
        if (!(livingEntity instanceof Player player)) {
             return false;
        } else {
            int timeHeld = this.getUseDuration(itemStack, livingEntity) - remainingTime;
            if(timeHeld >= 10){
                AABB aoe = livingEntity.getBoundingBox().inflate(3.0d);
                for (Entity entity : level.getEntities(livingEntity, aoe, entity -> entity.isAlive() && livingEntity.distanceToSqr(entity) <= 9)) {

                    if (level instanceof ServerLevel serverLevel) {
                        // Hurt the entity that is in the AOE.
                        entity.hurtServer(serverLevel, livingEntity.damageSources().magic(), 4.0f);

                        //knockback entity
                        entity.addDeltaMovement(livingEntity.position().vectorTo(entity.position()).normalize().multiply(2, 2, 2));
                    }
                }

                if (level instanceof ServerLevel serverLevel) {
                    //create particle sphere?
                    //particleSphere(serverLevel, livingEntity, aoe, ParticleTypes.HEART, 3);

                    sphereEntity = new SphereEntity(level);
                    sphereEntity.setPos(livingEntity.position());
                    level.addFreshEntity(sphereEntity);
                }





                level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.ARROW_SHOOT,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
                player.awardStat(Stats.ITEM_USED.get(this));
                return true;
            }
            return false;
        }
    }

    public static void particleSphere(Level level, LivingEntity livingEntity, AABB aabb, SimpleParticleType particleType, int radius){
        float i = 5;
        double x;
        double y;
        double z;
        //xRot is phi
        for (float xRot = -90; xRot <= 90; xRot += i){
            //yRot is theta
            for (float yRot = -180; yRot <= 180; yRot += i){
                y = Mth.sin(Math.toRadians(xRot)) * Mth.sin(Math.toRadians(yRot)) * radius;
                x = Mth.sin(Math.toRadians(yRot)) * Mth.cos(Math.toRadians(xRot)) * radius;
                z = Mth.cos(Math.toRadians(yRot)) * radius;
                if(level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(particleType, x + livingEntity.getX(), y + livingEntity.getY(), z + livingEntity.getZ(), 1, 0, 0, 0, 0);
                }
            }
        }

    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 72000;
    }
}
