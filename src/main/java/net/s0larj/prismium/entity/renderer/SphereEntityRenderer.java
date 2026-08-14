package net.s0larj.prismium.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Optionull;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.s0larj.prismium.entity.custom.SphereEntity;
import net.s0larj.prismium.entity.state.SphereEntityRenderState;

import java.util.function.Function;

public class SphereEntityRenderer extends EntityRenderer<SphereEntity, SphereEntityRenderState> {

    public SphereEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public SphereEntityRenderState createRenderState() {
        return new SphereEntityRenderState();
    }

    @Override
    public void extractRenderState(SphereEntity entity, SphereEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
    }

    @Override
    public void submit(SphereEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugFilledBox(), (pose, buffer) -> {
            //all calculations and code for sphere come from https://www.songho.ca/opengl/gl_sphere.html

            //radius of sphere
            int radius = 3;

            //vertex position, xy is the length of the radius along the xy axis, or the bottom of the triangle for spherical coords
            float x= 0;
            float y = 0;
            float z =0;
            float xy;

            //vertex position of last vertex
            float lx, ly, lz;

            //texture coordinates, to put a 2d texture on the shape
            float s, t;

            //how many sections
            int sectorCount = 28;
            int stackCount = 28;

            //divide by how many different sections you want
            float sectorStep = (float) (2 * Math.PI / sectorCount);
            float stackStep = (float) (Math.PI / stackCount);
            float sectorAngle, stackAngle;

            //stacks are north/south, aka latitude
            for(int latitude = 0; latitude <= stackCount; latitude++){
                stackAngle = (float) (Math.PI / 2 - latitude * stackStep);        // starting from pi/2 to -pi/2
                xy = radius * Mth.cos(stackAngle);             // r * cos(u)
                if( latitude != 0){
                    lz = z;
                    z = radius * Mth.sin(stackAngle);              // r * sin(u)
                }else{
                    z = radius * Mth.sin(stackAngle);              // r * sin(u)
                    lz = z;
                }


                // add (sectorCount+1) vertices per stack
                // first and last vertices have same position and normal, but different tex coords
                //sectors are east/west, aka longitude
                for(int longitude = 0; longitude <= sectorCount; longitude++) {
                    sectorAngle = longitude * sectorStep;           // starting from 0 to 2pi

                    // vertex position (x, y, z)

                    if( longitude != 0){
                        lx = x;
                        ly = y;
                        x = xy * Mth.cos(sectorAngle);             // r * cos(u) * cos(v)
                        y = xy * Mth.sin(sectorAngle);             // r * cos(u) * sin(v)
                    }else{
                        x = xy * Mth.cos(sectorAngle);             // r * cos(u) * cos(v)
                        y = xy * Mth.sin(sectorAngle);             // r * cos(u) * sin(v)
                        lx = x;
                        ly = y;
                    }

                    // 2 triangles per sector excluding first and last stacks
                    // k1 => k2 => k1+1
                    if(latitude != 0) {
                        vertex(buffer, pose, state.lightCoords, lx, ly, lz);
                        vertex(buffer, pose, state.lightCoords, lx, ly + y , lz + z);
                        vertex(buffer, pose, state.lightCoords, lx +x, ly+y, lz);
                    }

                    // k1+1 => k2 => k2+1
                    if(latitude != (stackCount-1)) {
                        vertex(buffer, pose, state.lightCoords, lx +x, ly +y, lz);
                        vertex(buffer, pose, state.lightCoords, lx, ly + y , lz + z);
                        vertex(buffer, pose, state.lightCoords, lx + x, ly + y, lz + z);
                    }

                    // vertex tex coord (s, t) range between [0, 1]
                    s = (float) longitude / sectorCount;
                    t = (float) latitude / stackCount;
                }
            }

        });
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private static void vertex(final VertexConsumer builder, final PoseStack.Pose pose, final int lightCoords, final float x, final float y, final float z) {
        builder.addVertex(pose.pose(), x, y, z)
                .setColor(-1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightCoords);
    }


}
