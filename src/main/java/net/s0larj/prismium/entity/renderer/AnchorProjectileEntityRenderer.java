package net.s0larj.prismium.entity.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.model.AnchorProjectileEntityModel;
import net.s0larj.prismium.entity.model.ModEntityModelLayers;
import net.s0larj.prismium.entity.state.AnchorProjectileEntityRenderState;

public class AnchorProjectileEntityRenderer extends EntityRenderState {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "textures/entity/mini_golem.png");

    public AnchorProjectileEntityRenderer(EntityRendererProvider.Context context) {
        super(); // 0.375 shadow radius
    }
}
