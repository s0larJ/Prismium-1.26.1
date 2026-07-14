package net.s0larj.prismium.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;

@Environment(EnvType.CLIENT)
public abstract class ProjectileEntityLayer<S> extends RenderLayer<LivingEntityRenderState, EntityModel<EntityRenderState>> {
    private final Model<S> model;
    private final S modelState;
    private final Identifier texture;
    private final ProjectileEntityLayer.PlacementStyle placementStyle;

    public ProjectileEntityLayer(final LivingEntityRenderer<?, LivingEntityRenderState, EntityModel<EntityRenderState>> renderer, final Model<S> model, final S modelState, final Identifier texture, final ProjectileEntityLayer.PlacementStyle placementStyle) {
        super(renderer);
        this.model = model;
        this.modelState = modelState;
        this.texture = texture;
        this.placementStyle = placementStyle;
    }

    protected abstract int numStuck(final LivingEntityRenderState state);

    private void submitStuckItem(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final float directionX, final float directionY, final float directionZ, final int outlineColor) {
        float directionXZ = Mth.sqrt(directionX * directionX + directionZ * directionZ);
        float yRot = (float)(Math.atan2((double)directionX, (double)directionZ) * (double)(180F / (float)Math.PI));
        float xRot = (float)(Math.atan2((double)directionY, (double)directionXZ) * (double)(180F / (float)Math.PI));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
        submitNodeCollector.submitModel(this.model, this.modelState, poseStack, this.texture, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, (ModelFeatureRenderer.CrumblingOverlay)null);
    }

    public void submit(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final LivingEntityRenderState state, final float yRot, final float xRot) {
        int count = this.numStuck(state);
        if (count > 0) {
            RandomSource random = RandomSource.createThreadLocalInstance();

            for(int i = 0; i < count; ++i) {
                poseStack.pushPose();
                ModelPart modelPart = Util.getRandom(this.getParentModel().allParts(), random);
                ModelPart.Cube cube = modelPart.getRandomCube(random);
                modelPart.translateAndRotate(poseStack);
                float midX = random.nextFloat();
                float midY = random.nextFloat();
                float midZ = random.nextFloat();
                if (this.placementStyle == ProjectileEntityLayer.PlacementStyle.ON_SURFACE) {
                    int plane = random.nextInt(3);
                    switch (plane) {
                        case 0 -> midX = snapToFace(midX);
                        case 1 -> midY = snapToFace(midY);
                        default -> midZ = snapToFace(midZ);
                    }
                }

                poseStack.translate(Mth.lerp(midX, cube.minX, cube.maxX) / 16.0F, Mth.lerp(midY, cube.minY, cube.maxY) / 16.0F, Mth.lerp(midZ, cube.minZ, cube.maxZ) / 16.0F);
                this.submitStuckItem(poseStack, submitNodeCollector, lightCoords, -(midX * 2.0F - 1.0F), -(midY * 2.0F - 1.0F), -(midZ * 2.0F - 1.0F), state.outlineColor);
                poseStack.popPose();
            }

        }
    }

    private static float snapToFace(final float value) {
        return value > 0.5F ? 1.0F : 0.5F;
    }

    @Environment(EnvType.CLIENT)
    public static enum PlacementStyle {
        IN_CUBE,
        ON_SURFACE;
    }
}
