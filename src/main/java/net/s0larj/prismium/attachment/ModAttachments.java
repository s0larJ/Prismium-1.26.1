package net.s0larj.prismium.attachment;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.Prismium;
import org.apache.commons.lang3.tuple.Triple;
import oshi.util.tuples.Triplet;

import java.util.List;
import java.util.UUID;

public class ModAttachments {

/*
    public static final AttachmentType<List<Pair<UUID, Pair<Vec3, Vec3>>>> HOOKED = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "hooked"),// The ID of your Attachment
            (builder) ->
                    builder.syncWith(StreamCodec.composite(UUIDUtil.STREAM_CODEC, Pair::getFirst,
                            StreamCodec.composite(Vec3.STREAM_CODEC, Pair::getFirst, Vec3.STREAM_CODEC, Pair::getSecond, Pair::new),
                            Pair::getSecond, (UUID uuid, Pair<Vec3, Vec3> pair) -> new Pair<>(uuid, pair)).apply(ByteBufCodecs.list()), AttachmentSyncPredicate.all())
    );

 */

    public static final AttachmentType<List<AnchorAttachment>> HOOKED = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "hooked"),// The ID of your Attachment
            (builder) -> builder.syncWith(AnchorAttachment.STREAM_CODEC.apply(ByteBufCodecs.list()), AttachmentSyncPredicate.all())
    );


    public static void registerModAttachments() {
        Prismium.LOGGER.info("Registering Mod Attachments for " + Prismium.MOD_ID);

    }
}
