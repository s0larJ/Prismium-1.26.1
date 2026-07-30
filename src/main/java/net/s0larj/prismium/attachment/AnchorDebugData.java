package net.s0larj.prismium.attachment;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public record AnchorDebugData(
        String entityType,
        String entityClass,
        UUID entityUuid,
        int entityId,

        Vec3 entityPosition,
        Vec3 entityVelocity,

        Vec3 worldHitPosition,
        Vec3 worldOffset,
        Vec3 localHitPosition,

        Vec3 projectilePosition,
        Vec3 projectileVelocity,

        float entityYaw,
        float entityPitch,
        float bodyYaw,
        float headYaw,

        float projectileYaw,
        float projectilePitch,

        float entityWidth,
        float entityHeight,
        float eyeHeight,

        Vec3 boundingBoxMin,
        Vec3 boundingBoxMax,

        double hitHeightPercent,
        double horizontalDistanceFromCenter,
        double ownerDistance,

        boolean entityAlive,
        boolean entityInvulnerable
) {

    public static final StreamCodec<ByteBuf, AnchorDebugData> STREAM_CODEC =
            new StreamCodec<>() {

                @Override
                public AnchorDebugData decode(ByteBuf buffer) {
                    String entityType =
                            ByteBufCodecs.STRING_UTF8.decode(buffer);

                    String entityClass =
                            ByteBufCodecs.STRING_UTF8.decode(buffer);

                    UUID entityUuid =
                            UUIDUtil.STREAM_CODEC.decode(buffer);

                    int entityId = buffer.readInt();

                    Vec3 entityPosition =
                            Vec3.STREAM_CODEC.decode(buffer);

                    Vec3 entityVelocity =
                            Vec3.STREAM_CODEC.decode(buffer);

                    Vec3 worldHitPosition =
                            Vec3.STREAM_CODEC.decode(buffer);

                    Vec3 worldOffset =
                            Vec3.STREAM_CODEC.decode(buffer);

                    Vec3 localHitPosition =
                            Vec3.STREAM_CODEC.decode(buffer);

                    Vec3 projectilePosition =
                            Vec3.STREAM_CODEC.decode(buffer);

                    Vec3 projectileVelocity =
                            Vec3.STREAM_CODEC.decode(buffer);

                    float entityYaw = buffer.readFloat();
                    float entityPitch = buffer.readFloat();
                    float bodyYaw = buffer.readFloat();
                    float headYaw = buffer.readFloat();

                    float projectileYaw = buffer.readFloat();
                    float projectilePitch = buffer.readFloat();

                    float entityWidth = buffer.readFloat();
                    float entityHeight = buffer.readFloat();
                    float eyeHeight = buffer.readFloat();

                    Vec3 boundingBoxMin =
                            Vec3.STREAM_CODEC.decode(buffer);

                    Vec3 boundingBoxMax =
                            Vec3.STREAM_CODEC.decode(buffer);

                    double hitHeightPercent = buffer.readDouble();
                    double horizontalDistanceFromCenter = buffer.readDouble();
                    double ownerDistance = buffer.readDouble();

                    boolean entityAlive = buffer.readBoolean();
                    boolean entityInvulnerable = buffer.readBoolean();

                    return new AnchorDebugData(
                            entityType,
                            entityClass,
                            entityUuid,
                            entityId,

                            entityPosition,
                            entityVelocity,

                            worldHitPosition,
                            worldOffset,
                            localHitPosition,

                            projectilePosition,
                            projectileVelocity,

                            entityYaw,
                            entityPitch,
                            bodyYaw,
                            headYaw,

                            projectileYaw,
                            projectilePitch,

                            entityWidth,
                            entityHeight,
                            eyeHeight,

                            boundingBoxMin,
                            boundingBoxMax,

                            hitHeightPercent,
                            horizontalDistanceFromCenter,
                            ownerDistance,

                            entityAlive,
                            entityInvulnerable
                    );
                }

                @Override
                public void encode(
                        ByteBuf buffer,
                        AnchorDebugData data
                ) {
                    ByteBufCodecs.STRING_UTF8.encode(
                            buffer,
                            data.entityType()
                    );

                    ByteBufCodecs.STRING_UTF8.encode(
                            buffer,
                            data.entityClass()
                    );

                    UUIDUtil.STREAM_CODEC.encode(
                            buffer,
                            data.entityUuid()
                    );

                    buffer.writeInt(data.entityId());

                    Vec3.STREAM_CODEC.encode(
                            buffer,
                            data.entityPosition()
                    );

                    Vec3.STREAM_CODEC.encode(
                            buffer,
                            data.entityVelocity()
                    );

                    Vec3.STREAM_CODEC.encode(
                            buffer,
                            data.worldHitPosition()
                    );

                    Vec3.STREAM_CODEC.encode(
                            buffer,
                            data.worldOffset()
                    );

                    Vec3.STREAM_CODEC.encode(
                            buffer,
                            data.localHitPosition()
                    );

                    Vec3.STREAM_CODEC.encode(
                            buffer,
                            data.projectilePosition()
                    );

                    Vec3.STREAM_CODEC.encode(
                            buffer,
                            data.projectileVelocity()
                    );

                    buffer.writeFloat(data.entityYaw());
                    buffer.writeFloat(data.entityPitch());
                    buffer.writeFloat(data.bodyYaw());
                    buffer.writeFloat(data.headYaw());

                    buffer.writeFloat(data.projectileYaw());
                    buffer.writeFloat(data.projectilePitch());

                    buffer.writeFloat(data.entityWidth());
                    buffer.writeFloat(data.entityHeight());
                    buffer.writeFloat(data.eyeHeight());

                    Vec3.STREAM_CODEC.encode(
                            buffer,
                            data.boundingBoxMin()
                    );

                    Vec3.STREAM_CODEC.encode(
                            buffer,
                            data.boundingBoxMax()
                    );

                    buffer.writeDouble(data.hitHeightPercent());
                    buffer.writeDouble(data.horizontalDistanceFromCenter());
                    buffer.writeDouble(data.ownerDistance());

                    buffer.writeBoolean(data.entityAlive());
                    buffer.writeBoolean(data.entityInvulnerable());
                }
            };
}
