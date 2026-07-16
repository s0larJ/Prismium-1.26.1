package net.s0larj.prismium.attachment;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public record AnchorAttachment(UUID uuid, Vec3 pos, float xRot, float yRot) {

    public static final StreamCodec<ByteBuf, AnchorAttachment> STREAM_CODEC = StreamCodec.composite(UUIDUtil.STREAM_CODEC, AnchorAttachment::uuid,
            Vec3.STREAM_CODEC, AnchorAttachment::pos, ByteBufCodecs.FLOAT, AnchorAttachment::xRot, ByteBufCodecs.FLOAT, AnchorAttachment::yRot, AnchorAttachment::new);
}
