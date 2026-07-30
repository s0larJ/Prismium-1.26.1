package net.s0larj.prismium.attachment;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.Prismium;

import java.util.List;

public class ModAttachments {

    public static final AttachmentType<List<AnchorAttachment>> HOOKED = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "hooked"),// The ID of your Attachment
            (builder) -> builder.syncWith(AnchorAttachment.STREAM_CODEC.apply(ByteBufCodecs.list()), AttachmentSyncPredicate.all())
    );


    public static void registerModAttachments() {
        Prismium.LOGGER.info("Registering Mod Attachments for " + Prismium.MOD_ID);

    }
}
