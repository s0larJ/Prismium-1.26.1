package net.s0larj.prismium.entity;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.attachment.AnchorAttachment;
import net.s0larj.prismium.entity.layers.AnchorProjectileEntityLayer;
import net.s0larj.prismium.entity.model.ModEntityModelLayers;
import net.s0larj.prismium.entity.renderer.AnchorProjectileEntityRenderer;
import com.mojang.datafixers.util.Pair;
import oshi.util.tuples.Triplet;

import java.util.List;
import java.util.UUID;

public class ModCustomEntityClient implements ClientModInitializer {
    public static RenderStateDataKey<List<AnchorAttachment>> ANCHOR_KEY = RenderStateDataKey.create();
    public static RenderStateDataKey<Integer> ENTITY_ID = RenderStateDataKey.create();

    @Override
    public void onInitializeClient() {
        ModEntityModelLayers.registerModelLayers();
        // #endregion register_client
        // #region register_renderer
        EntityRenderers.register(ModEntityTypes.ANCHOR_PROJECTILE, AnchorProjectileEntityRenderer::new);
        // #endregion register_renderer
        // #region register_client

        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            registrationHelper.register(new AnchorProjectileEntityLayer(entityRenderer, context));
        });

    }
}