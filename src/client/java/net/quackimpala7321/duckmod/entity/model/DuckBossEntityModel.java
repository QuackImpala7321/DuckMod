package net.quackimpala7321.duckmod.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.quackimpala7321.duckmod.entity.DuckBossEntity;

// Made with Blockbench 4.8.1
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class DuckBossEntityModel<T extends DuckBossEntity> extends AnimalModel<T> {
	private static final String CROWN = "crown";

	private final ModelPart crown;
	private final ModelPart head;
	private final ModelPart beak;
	private final ModelPart neck;
	private final ModelPart body;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final ModelPart leftWing;
	private final ModelPart rightWing;
	public DuckBossEntityModel(ModelPart root) {
		this.crown = root.getChild(CROWN);
		this.head = root.getChild(EntityModelPartNames.HEAD);
		this.beak = root.getChild(EntityModelPartNames.BEAK);
		this.neck = root.getChild(EntityModelPartNames.NECK);
		this.body = root.getChild(EntityModelPartNames.BODY);
		this.leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
		this.rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
		this.leftWing = root.getChild(EntityModelPartNames.LEFT_WING);
		this.rightWing = root.getChild(EntityModelPartNames.RIGHT_WING);
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData crown = modelPartData.addChild(CROWN, ModelPartBuilder.create().uv(36, 0).cuboid(1.0F, -15.0F, -7.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(36, 0).cuboid(-2.0F, -15.0F, -7.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(36, 0).cuboid(-2.0F, -15.0F, -4.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(36, 0).cuboid(1.0F, -15.0F, -4.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(36, 3).cuboid(-1.0F, -14.0F, -4.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(36, 3).cuboid(-1.0F, -14.0F, -7.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(36, 5).cuboid(1.0F, -14.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(36, 5).cuboid(-2.0F, -14.0F, -6.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(22, 12).cuboid(-2.0F, -2.25F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.25F, -5.0F));

		ModelPartData beak = modelPartData.addChild(EntityModelPartNames.BEAK, ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.5F, -5.0F));

		ModelPartData neck = modelPartData.addChild(EntityModelPartNames.NECK, ModelPartBuilder.create().uv(0, 3).cuboid(-1.0F, -2.0F, -0.75F, 2.0F, 2.0F, 1.5F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -4.25F));

		ModelPartData body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(18, 22).cuboid(-3.0F, -3.5F, -3.0F, 6.0F, 1.0F, 7.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-3.5F, -2.5F, -4.75F, 7.0F, 3.0F, 9.0F, new Dilation(0.0F))
		.uv(0, 12).cuboid(-3.5F, 0.5F, -3.75F, 7.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 18.5F, 0.25F));

		ModelPartData leftLeg = modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(4, 12).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(23, 0).cuboid(-1.5F, 3.0F, -2.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 20.0F, 1.5F));

		ModelPartData rightLeg = modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 12).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(13, 21).cuboid(-1.5F, 3.0F, -2.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, 20.0F, 1.5F));

		ModelPartData leftWing = modelPartData.addChild(EntityModelPartNames.LEFT_WING, ModelPartBuilder.create().uv(0, 21).mirrored().cuboid(-1.0F, 0.0F, -4.0F, 1.0F, 3.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.5F, 16.0F, 1.0F));

		ModelPartData rightWing = modelPartData.addChild(EntityModelPartNames.RIGHT_WING, ModelPartBuilder.create().uv(0, 21).cuboid(0.0F, 0.0F, -4.0F, 1.0F, 3.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 16.0F, 1.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(DuckBossEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		crown.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		beak.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		neck.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leftLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		rightLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leftWing.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		rightWing.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(this.head, this.beak, this.crown);
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.body, this.leftLeg, this.rightLeg, this.leftWing, this.rightWing);
	}
}