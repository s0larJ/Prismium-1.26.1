package net.s0larj.prismium.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.s0larj.prismium.entity.ModEntityTypes;

public class SphereEntity extends Entity {

    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(SphereEntity.class, EntityDataSerializers.FLOAT);

    public SphereEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public SphereEntity(Level level) {
        super(ModEntityTypes.SPHERE_ENTITY, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        entityData.define(DATA_RADIUS, 3.0F);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        this.tickCount = input.getIntOr("Age", 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putInt("Age", this.tickCount);
    }
}
