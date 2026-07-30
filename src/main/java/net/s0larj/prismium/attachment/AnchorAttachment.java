package net.s0larj.prismium.attachment;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;


public record AnchorAttachment(
        UUID uuid,
        Vec3 pos,
        float xRot,
        float yRot,
        AnchorDebugData debug
) {

    public static final StreamCodec<RegistryFriendlyByteBuf, AnchorAttachment> STREAM_CODEC =
            new StreamCodec<>() {

                @Override
                public AnchorAttachment decode(RegistryFriendlyByteBuf buffer) {
                    UUID uuid = buffer.readUUID();

                    Vec3 position = new Vec3(
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble()
                    );

                    float xRot = buffer.readFloat();
                    float yRot = buffer.readFloat();

                    AnchorDebugData debug =
                            AnchorDebugData.STREAM_CODEC.decode(buffer);

                    return new AnchorAttachment(
                            uuid,
                            position,
                            xRot,
                            yRot,
                            debug
                    );
                }

                @Override
                public void encode(
                        RegistryFriendlyByteBuf buffer,
                        AnchorAttachment attachment
                ) {
                    buffer.writeUUID(attachment.uuid());

                    buffer.writeDouble(attachment.pos().x);
                    buffer.writeDouble(attachment.pos().y);
                    buffer.writeDouble(attachment.pos().z);

                    buffer.writeFloat(attachment.xRot());
                    buffer.writeFloat(attachment.yRot());

                    AnchorDebugData.STREAM_CODEC.encode(
                            buffer,
                            attachment.debug()
                    );
                }
            };
}
