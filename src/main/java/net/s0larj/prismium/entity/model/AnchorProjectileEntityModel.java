package net.s0larj.prismium.entity.model;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.Identifier;
import net.s0larj.prismium.Prismium;
import net.s0larj.prismium.entity.state.AnchorProjectileEntityRenderState;

public class AnchorProjectileEntityModel extends EntityModel<AnchorProjectileEntityRenderState> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Prismium.MOD_ID, "textures/entity/anchor_projectile.png"), "main");
	private final ModelPart bb_main;

	public AnchorProjectileEntityModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -2.0F));

		PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 3).addBox(-1.0F, -4.8717F, -1.025F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
				.texOffs(12, 9).addBox(-1.0F, -3.8717F, -1.025F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.4F))
				.texOffs(0, 0).addBox(-2.05F, -2.8727F, -0.975F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0773F, 7.025F, -1.5708F, 0.0F, -3.1416F));

		PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 13).addBox(2.8717F, 0.0F, -1.025F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.401F))
				.texOffs(12, 12).addBox(2.8717F, -1.0F, -1.025F, 2.0F, 1.0F, 2.0F, new CubeDeformation(-0.401F)), PartPose.offsetAndRotation(0.0F, -1.0773F, 7.025F, 0.0F, -1.5708F, 1.5708F));

		PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(14, 6).addBox(-2.0251F, 4.1386F, -1.025F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0773F, 5.025F, -1.5708F, 0.7418F, 3.1416F));

		PartDefinition cube_r4 = bb_main.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(12, 0).addBox(0.0251F, 4.1386F, -1.025F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0773F, 5.025F, -1.5708F, -0.7418F, -3.1416F));

		PartDefinition cube_r5 = bb_main.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(4, 6).addBox(-1.2389F, 3.8201F, -1.025F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -1.0773F, 5.025F, -1.5708F, -0.3927F, -3.1416F));

		PartDefinition cube_r6 = bb_main.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(4, 3).addBox(-1.7611F, 3.8201F, -1.025F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -1.0773F, 5.025F, -1.5708F, 0.3927F, 3.1416F));

		PartDefinition cube_r7 = bb_main.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(4, 9).addBox(2.0913F, 2.0913F, -1.025F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, -1.0773F, 5.025F, -1.5708F, 0.7854F, 3.1416F));

		PartDefinition cube_r8 = bb_main.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 3).addBox(-0.5F, -5.4727F, -0.525F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0773F, 5.025F, -1.5708F, 0.0F, -3.1416F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

}