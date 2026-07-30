package net.s0larj.prismium.entity.layers;

import com.google.common.collect.Collections2;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Optionull;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.attachment.AnchorAttachment;
import net.s0larj.prismium.attachment.AnchorDebugData;
import net.s0larj.prismium.entity.ModCustomEntityClient;
import net.s0larj.prismium.mixin.ModelPartAccessorMixin;

import java.util.*;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public abstract class ProjectileEntityLayer<M extends LivingEntityRenderState, S> extends RenderLayer<M, EntityModel<EntityRenderState>> {

    private static final Set<UUID> DEBUG_PRINTED = new HashSet<>();

    //Model that will be rendered on the entity.
    private final Model<S> model;
    // Render state used by the anchor model.
    private final S modelState;
    // Texture used by the anchor model.
    private final Identifier texture;
    //Currently unused, but kept in case different placement behavior is added later.
    private final PlacementStyle placementStyle;

    public ProjectileEntityLayer(LivingEntityRenderer<?, M, EntityModel<EntityRenderState>> renderer, Model<S> model, S modelState,
                                 Identifier texture, PlacementStyle placementStyle) {
        super(renderer);
        this.model = model;
        this.modelState = modelState;
        this.texture = texture;
        this.placementStyle = placementStyle;
    }

    // The subclass provides the list of anchors currently attached to the rendered entity.
    protected abstract List<AnchorAttachment> numStuck(LivingEntityRenderState state);

    double anchorPivotOffset = 0.20D; // Aligns the anchor’s visible tip with the hit point instead of its model origin


    public void submit(
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            LivingEntityRenderState state,
            float yRot,
            float xRot
    ) {
        List<AnchorAttachment> projectiles = this.numStuck(state);

        if (projectiles.isEmpty()) return;

        for (AnchorAttachment projectile : projectiles) {
            poseStack.pushPose();

            AnchorDebugData server = projectile.debug();
            Vec3 localPosition = projectile.pos();

            double hitHeight = localPosition.y * state.boundingBoxHeight;

            double modelOriginY = -EntityModel.MODEL_Y_OFFSET;

            double renderX = localPosition.x;
            double renderY = modelOriginY - hitHeight - anchorPivotOffset;
            double renderZ = -localPosition.z;

            float directionX = (float) -localPosition.x;
            float directionZ = (float) localPosition.z;

            float hookYaw = (float) Math.toDegrees(Math.atan2(directionX, directionZ));

            float finalYaw = hookYaw + 180.0F;
            float finalPitch = projectile.xRot();
            float finalRoll = 0.0F;

            if (DEBUG_PRINTED.add(projectile.uuid())) {
                System.out.printf("""
            
                        ============================ ANCHOR DEBUG ============================
                        
                        IDENTITY
                        Entity type:       %s
                        Entity class:      %s
                        Entity UUID:       %s
                        Entity numeric ID: %d
                        
                        ANCHOR
                        Anchor UUID:   %s
                        Attachment count:  %d
                        Render-state class:%s
                        
                        STATUS
                        Alive:             %s
                        Invulnerable:      %s
                        Owner distance:    %.4f
                        
                        ENTITY SIZE
                        Server width:      %.4f
                        Server height:     %.4f
                        Client height:     %.4f
                        Eye height:        %.4f
                        
                        %-25s | %12s | %12s | %12s
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        
                        BOUNDING BOX
                        %-25s | %12s | %12s | %12s
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        
                        HEIGHT CALCULATION
                        Raw hit height:             %.4f
                        Saved hit percentage:       %.4f
                        Client calculated height:   %.4f
                        Model origin Y:             %.4f
                        Anchor pivot offset:        %.4f
                        Horizontal center distance: %.4f
                        
                        %-25s | %12s | %12s | %12s
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        
                        ROTATION
                        %-25s | %12s | %12s | %12s
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        %-25s | %12.4f | %12.4f | %12.4f
                        
                        DIRECTION CALCULATION
                        Direction X:    %.4f
                        Direction Z:    %.4f
                        Anchor Yaw:     %.4f
                        
                        ======================================================================
                        %n""",

                        server.entityType(),
                        server.entityClass(),
                        server.entityUuid(),
                        server.entityId(),
                        projectile.uuid(),
                        projectiles.size(),
                        state.getClass().getName(),

                        server.entityAlive(),
                        server.entityInvulnerable(),
                        server.ownerDistance(),

                        server.entityWidth(),
                        server.entityHeight(),
                        state.boundingBoxHeight,
                        server.eyeHeight(),

                        "POSITION", "X", "Y", "Z",

                        "Entity world",
                        server.entityPosition().x,
                        server.entityPosition().y,
                        server.entityPosition().z,

                        "Anchor World",
                        server.worldHitPosition().x,
                        server.worldHitPosition().y,
                        server.worldHitPosition().z,

                        "Offset",
                        server.worldOffset().x,
                        server.worldOffset().y,
                        server.worldOffset().z,

                        "Server saved local",             // The hit position rotated into the entity’s local coordinate system.
                        server.localHitPosition().x,      // The local position received from the server; this should match
                        server.localHitPosition().y,
                        server.localHitPosition().z,

                        "Client received local",
                        localPosition.x,
                        localPosition.y,
                        localPosition.z,

                        "Projectile world",               // Projectile’s world position when the information was recorded.
                        server.projectilePosition().x,
                        server.projectilePosition().y,
                        server.projectilePosition().z,

                        "Final model render",             // Local position where the anchor model is actually drawn.
                        renderX,
                        renderY,
                        renderZ,

                        "Bounding box center",
                        (server.boundingBoxMin().x + server.boundingBoxMax().x) / 2.0D,
                        (server.boundingBoxMin().y + server.boundingBoxMax().y) / 2.0D,
                        (server.boundingBoxMin().z + server.boundingBoxMax().z) / 2.0D,

                        "BOUNDING BOX", "X", "Y", "Z",

                        "Minimum",
                        server.boundingBoxMin().x,
                        server.boundingBoxMin().y,
                        server.boundingBoxMin().z,

                        "Maximum",
                        server.boundingBoxMax().x,
                        server.boundingBoxMax().y,
                        server.boundingBoxMax().z,

                        "Dimensions",
                        server.boundingBoxMax().x - server.boundingBoxMin().x,
                        server.boundingBoxMax().y - server.boundingBoxMin().y,
                        server.boundingBoxMax().z - server.boundingBoxMin().z,

                        server.worldOffset().y,                     // Hit’s height above the entity’s feet.
                        server.hitHeightPercent(),                  // Hit height divided by the entity’s total height.
                        hitHeight,                                  //
                        modelOriginY,                               // Vertical origin used when rendering models.
                        anchorPivotOffset,                          // Correction for the origin point inside the anchor model
                        server.horizontalDistanceFromCenter(),      // Horizontal distance from the hit to the entity’s center.

                        "VELOCITY", "X", "Y", "Z",

                        "Entity velocity",
                        server.entityVelocity().x,
                        server.entityVelocity().y,
                        server.entityVelocity().z,

                        "Projectile velocity",
                        server.projectileVelocity().x,
                        server.projectileVelocity().y,
                        server.projectileVelocity().z,

                        "Direction toward center",
                        directionX,
                        0.0F,
                        directionZ,

                        "Render position",
                        renderX,
                        renderY,
                        renderZ,

                        "Saved local position",
                        localPosition.x,
                        localPosition.y,
                        localPosition.z,

                        "ROTATION", "Yaw", "Pitch", "Roll",

                        "Entity",
                        server.entityYaw(),
                        server.entityPitch(),
                        0.0F,

                        "Body / head",
                        server.bodyYaw(),
                        0.0F,
                        server.headYaw(),

                        "Projectile",
                        server.projectileYaw(),
                        server.projectilePitch(),
                        0.0F,

                        "Client final",        // Final rotation applied to the rendered anchor model.
                        finalYaw,
                        finalPitch,
                        finalRoll,

                        directionX,            // The X component pointing from the hit toward the entity’s center.
                        directionZ,            // The z component pointing from the hit toward the entity’s center.
                        hookYaw                // The horizontal angle calculated from the direction values.
                );
            }

            poseStack.translate(renderX, renderY, renderZ);

            poseStack.mulPose(Axis.YP.rotationDegrees(finalYaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(finalPitch));
            poseStack.mulPose(Axis.ZP.rotationDegrees(finalRoll));

            submitNodeCollector.submitModel(
                    this.model,
                    this.modelState,
                    poseStack,
                    this.texture,
                    lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    state.outlineColor,
                    null
            );

            poseStack.popPose();
        }
    }


    @Environment(EnvType.CLIENT)
    public enum PlacementStyle {
        IN_CUBE,
        ON_SURFACE
    }
}