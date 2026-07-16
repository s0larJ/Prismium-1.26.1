package net.s0larj.prismium.entity.layers;

import com.google.common.collect.Collections2;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.attachment.AnchorAttachment;
import net.s0larj.prismium.entity.ModCustomEntityClient;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public abstract class ProjectileEntityLayer<M extends LivingEntityRenderState, S> extends RenderLayer<M, EntityModel<EntityRenderState>> {
    private final Model<S> model;
    private final S modelState;
    private final Identifier texture;
    private final ProjectileEntityLayer.PlacementStyle placementStyle;

    public ProjectileEntityLayer(final LivingEntityRenderer<?, M, EntityModel<EntityRenderState>> renderer, final Model<S> model, final S modelState, final Identifier texture, final ProjectileEntityLayer.PlacementStyle placementStyle) {
        super(renderer);
        this.model = model;
        this.modelState = modelState;
        this.texture = texture;
        this.placementStyle = placementStyle;
    }

    protected abstract List<AnchorAttachment> numStuck(final LivingEntityRenderState state);

    private void submitStuckItem(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final float directionX, final float directionY, final float directionZ, final int outlineColor) {
        float directionXZ = Mth.sqrt(directionX * directionX + directionZ * directionZ);
        float yRot = (float)(Math.atan2((double)directionX, (double)directionZ) * (double)(180F / (float)Math.PI));
        float xRot = (float)(Math.atan2((double)directionY, (double)directionXZ) * (double)(180F / (float)Math.PI));
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(xRot));
        submitNodeCollector.submitModel(this.model, this.modelState, poseStack, this.texture, lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, (ModelFeatureRenderer.CrumblingOverlay)null);
    }

    public void submit(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final LivingEntityRenderState state, final float yRot, final float xRot) {
        List<AnchorAttachment> projectiles = this.numStuck(state);
        if (!projectiles.isEmpty()) {

            RandomSource random = RandomSource.createThreadLocalInstance(Objects.requireNonNull(state.getData(ModCustomEntityClient.ENTITY_ID)));
            for (var projectile:projectiles) {
                poseStack.pushPose();
                //ModelPart modelPart = Util.getRandom(List.copyOf(Collections2.filter(this.getParentModel().allParts(), part -> !part.isEmpty())), random);
                //ModelPart.Cube cube = modelPart.getRandomCube(random);
                //modelPart.translateAndRotate(poseStack);
                /*
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
                 */

                //poseStack.translate(Mth.lerp(midX, (float) projectile.x() - state.x,  (float) projectile.x() - state.x) / 16.0F, Mth.lerp(midY, (float) projectile.y() - state.y, (float) projectile.y() - state.y) / 16.0F, Mth.lerp(midZ, (float) projectile.z() - state.z, (float) projectile.z() - state.z) / 16.0F);
                poseStack.translate(projectile.pos().x, -(projectile.pos().y-1.6), -projectile.pos().z);
                //Vector3f dir = projectile.ang().toVector3f();
                this.submitStuckItem(poseStack, submitNodeCollector, lightCoords, 0, 0, 0, state.outlineColor);
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
