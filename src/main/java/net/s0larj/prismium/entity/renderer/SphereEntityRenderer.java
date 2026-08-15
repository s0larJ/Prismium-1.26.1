package net.s0larj.prismium.entity.renderer;

import com.mojang.blaze3d.Blaze3D;
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
import net.minecraft.world.phys.Vec3;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.custom.SphereEntity;
import net.s0larj.prismium.entity.state.SphereEntityRenderState;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
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
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.debugQuads(), (pose, buffer) -> {
            //all calculations and code for sphere come from https://www.songho.ca/opengl/gl_sphere.html

            //radius of sphere
            float radius = 5;

            //vertex position, xy is the length of the radius along the xy axis, or the bottom of the triangle for spherical coords
            float x= 0;
            float y = 0;
            float z =0;
            float xy;

            //position of last vertex
            float lx, ly, lz;

            //position of vertex from stack above
            float ix, iy;

            //position of last vertex from stack above
            float ilx, ily;

            //list of current stack vertices
            List<Pair<Float, Float>> vertices = new ArrayList<>(List.of());

            //list of previous stack vertices
            List<Pair<Float, Float>> lVertices = new ArrayList<>(List.of());

            //texture coordinates, to put a 2d texture on the shape
            float s, t;

            //how many sections
            int sectorCount = 100;
            int stackCount = 100;

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
                    //makes all x and y for one z value then goes to next z or stack
                    sectorAngle = longitude * sectorStep;           // starting from 0 to 2pi


                    if( longitude != 0 && latitude != 0){
                        lx = x;
                        ly = y;
                        x = xy * Mth.cos(sectorAngle);             // r * cos(u) * cos(v)
                        y = xy * Mth.sin(sectorAngle);             // r * cos(u) * sin(v)
                        vertices.add(longitude, new Pair<>(x, y));
                        ilx = lVertices.get(longitude - 1).getA();
                        ily = lVertices.get(longitude - 1).getB();
                        ix = lVertices.get(longitude).getA();
                        iy = lVertices.get(longitude).getB();

                        //make a quad by connecting the points, k1 -> k2 -> k2 + 1 -> k1 + 1
                        vertex(buffer, pose, state.lightCoords, ilx, ily, lz);
                        vertex(buffer, pose, state.lightCoords, lx, ly, z);
                        vertex(buffer, pose, state.lightCoords, x, y, z);
                        vertex(buffer, pose, state.lightCoords, ix, iy, lz);


                    }else{
                        x = xy * Mth.cos(sectorAngle);             // r * cos(u) * cos(v)
                        y = xy * Mth.sin(sectorAngle);             // r * cos(u) * sin(v)6
                        vertices.add(longitude, new Pair<>(x, y));
                    }

                    // vertex tex coord (s, t) range between [0, 1]
                    s = (float) longitude / sectorCount;
                    t = (float) latitude / stackCount;
                }
                lVertices.clear();
                lVertices.addAll(vertices);
                vertices.clear();
            }


        });
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private static void vertex(final VertexConsumer builder, final PoseStack.Pose pose, final int lightCoords, final float x, final float y, final float z) {
        builder.addVertex(pose.pose(), x, y, z)
                .setColor(0, 0, 255, 255)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightCoords);
    }


}
