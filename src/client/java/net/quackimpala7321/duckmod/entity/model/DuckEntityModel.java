package net.quackimpala7321.duckmod.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.quackimpala7321.duckmod.entity.DuckEntity;

// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class DuckEntityModel<T extends DuckEntity> extends AnimalModel<T> {
	private final ModelPart head;
	private final ModelPart beak;
	private final ModelPart neck;
	private final ModelPart body;
	private final ModelPart leftWing;
	private final ModelPart rightWing;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	public DuckEntityModel(ModelPart root) {
		this.head = root.getChild(EntityModelPartNames.HEAD);
		this.beak = root.getChild(EntityModelPartNames.BEAK);
		this.neck = root.getChild(EntityModelPartNames.NECK);
		this.body = root.getChild(EntityModelPartNames.BODY);
		this.leftWing = root.getChild(EntityModelPartNames.LEFT_WING);
		this.rightWing = root.getChild(EntityModelPartNames.RIGHT_WING);
		this.leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
		this.rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(22, 12).cuboid(-2.0F, -2.25F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.25F, -5.0F));

		ModelPartData beak = modelPartData.addChild(EntityModelPartNames.BEAK, ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -0.5F, -4.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 13.5F, -5.0F));

		ModelPartData neck = modelPartData.addChild(EntityModelPartNames.NECK, ModelPartBuilder.create().uv(0, 3).cuboid(-1.0F, -2.0F, -0.75F, 2.0F, 2.0F, 1.5F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -4.25F));

		ModelPartData body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(18, 22).cuboid(-3.0F, -3.5F, -3.0F, 6.0F, 1.0F, 7.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-3.5F, -2.5F, -4.75F, 7.0F, 3.0F, 9.0F, new Dilation(0.0F))
				.uv(0, 12).cuboid(-3.5F, 0.5F, -3.75F, 7.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 18.5F, 0.25F));

		ModelPartData leftWing = modelPartData.addChild(EntityModelPartNames.LEFT_WING, ModelPartBuilder.create().uv(0, 21).mirrored().cuboid(-1.0F, 0.0F, -4.0F, 1.0F, 3.0F, 7.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-3.5F, 16.0F, 1.0F));

		ModelPartData rightWing = modelPartData.addChild(EntityModelPartNames.RIGHT_WING, ModelPartBuilder.create().uv(0, 21).cuboid(0.0F, 0.0F, -4.0F, 1.0F, 3.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 16.0F, 1.0F));

		ModelPartData leftLeg = modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(4, 12).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(23, 0).cuboid(-1.5F, 3.0F, -2.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.5F, 20.0F, 1.5F));

		ModelPartData rightLeg = modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 12).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
				.uv(13, 21).cuboid(-1.5F, 3.0F, -2.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, 20.0F, 1.5F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		beak.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		neck.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leftWing.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		rightWing.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		leftLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		rightLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	public void animateModel(Pose pose) {
		if(pose == Pose.SWIMMING) {
			this.leftLeg.hidden = true;
			this.rightLeg.hidden = true;
		} else if(pose == Pose.SITTING || pose == Pose.ON_HEAD) {
			this.body.pivotY = 20.5f;
			this.body.pitch = (float)Math.toRadians(-25);

			this.leftLeg.hidden = false;
			this.rightLeg.hidden = false;
		} else if (pose == Pose.STANDING) {
			this.body.pivotY = 18.5f;
			this.body.pitch = (float)Math.toRadians(0);

			this.leftLeg.hidden = false;
			this.rightLeg.hidden = false;
		}

		this.neck.pivotZ = this.body.pivotZ - 3.0f;
		this.neck.pivotY = this.body.pivotY - 2.0f;

		this.head.pivotY = this.neck.pivotY - 2.75f;
		this.head.pivotZ = this.neck.pivotZ - 0.75f;

		this.beak.pivotY = this.head.pivotY + 0.25f;
		this.beak.pivotZ = this.head.pivotZ;

		this.leftWing.pivotY = this.body.pivotY - 2.5f;
		this.rightWing.pivotY = this.body.pivotY - 2.5f;

	}

	@Override
	public void animateModel(T duckEntity, float limbAngle, float limbDistance, float tickDelta) {
		animateModel(getPose(duckEntity));
	}

	public enum Pose {
		SITTING,
		ON_HEAD,
		STANDING,
		SWIMMING
	}

	Pose getPose(DuckEntity duckEntity) {
		if(duckEntity.isTouchingWater()) {
			return Pose.SWIMMING;
		}
		if(duckEntity.isInSittingPose()) {
			return Pose.SITTING;
		}
		if(duckEntity.isOnOwner()) {
			return Pose.ON_HEAD;
		}
		return Pose.STANDING;
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of(this.head, this.beak);
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(this.body, this.leftLeg, this.rightLeg, this.leftWing, this.rightWing);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.head.pitch = headPitch * ((float)Math.PI / 180);
		this.head.yaw = headYaw * ((float)Math.PI / 180);
		this.head.pivotY = this.neck.pivotY - 2.75f;
		this.head.pivotZ = this.neck.pivotZ - 0.75f;

		this.beak.pivotY = this.head.pivotY + 0.25f;
		this.beak.pivotZ = this.head.pivotZ;
		this.beak.pitch = this.head.pitch;
		this.beak.yaw = this.head.yaw;

		this.rightLeg.pitch = MathHelper.cos(limbAngle * 0.6662f) * 1.4f * limbDistance;
		this.leftLeg.pitch = MathHelper.cos(limbAngle * 0.6662f + (float)Math.PI) * 1.4f * limbDistance;

		this.leftWing.pitch = this.body.pitch;
		this.rightWing.pitch = this.body.pitch;

		this.leftWing.roll = animationProgress / 2;
		this.rightWing.roll = -animationProgress / 2;

		this.neck.pitch = this.body.pitch + (float) Math.toRadians(30);
	}
}