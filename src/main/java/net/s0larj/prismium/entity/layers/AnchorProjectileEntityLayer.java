package net.s0larj.prismium.entity.layers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.model.AnchorProjectileEntityModel;
import net.s0larj.prismium.entity.model.ModEntityModelLayers;
import net.s0larj.prismium.entity.state.AnchorProjectileEntityRenderState;

@Environment(EnvType.CLIENT)
public class AnchorProjectileEntityLayer extends ProjectileEntityLayer<AnchorProjectileEntityRenderState> {

    public AnchorProjectileEntityLayer(final LivingEntityRenderer<?, LivingEntityRenderState, EntityModel<EntityRenderState>> renderer, final EntityRendererProvider.Context context) {
        super(renderer, new AnchorProjectileEntityModel(context.bakeLayer(ModEntityModelLayers.ANCHOR_PROJECTILE)), new AnchorProjectileEntityRenderState(), Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "anchor_projectile.png"), ProjectileEntityLayer.PlacementStyle.IN_CUBE);
    }

    @Override
    protected int numStuck(LivingEntityRenderState state) {
        return 0;
    }
}
