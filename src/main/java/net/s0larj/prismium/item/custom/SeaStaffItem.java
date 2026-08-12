package net.s0larj.prismium.item.custom;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SeaStaffItem extends Item {

    public SeaStaffItem(Properties properties) {
        super(properties);
    }

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
                    }
                }

                if (level instanceof ServerLevel serverLevel) {
                    //create particle sphere?
                    particleSphere(serverLevel, livingEntity, aoe, ParticleTypes.HEART, 9);
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
        for(double x = aabb.minX; x <= aabb.maxX; x +=0.1){
            for(double y = aabb.minY; y <= aabb.maxY; y +=0.1){
                for(double z = aabb.minZ; z <= aabb.maxZ; z +=0.1){
                    if(livingEntity.distanceToSqr(x, y, z) == radius && level instanceof ServerLevel serverLevel){
                        serverLevel.addParticle(particleType, x, y, z, 0, 0, 0);
                    }
                }
            }
        }

    }

    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity user) {
        return 72000;
    }
}
