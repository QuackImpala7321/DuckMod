package net.quackimpala7321.duckmod.entity.model.custom;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ChickenEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.Entity;
import net.quackimpala7321.duckmod.entity.custom.DuckEntity;

public class DuckEntityModel<T extends DuckEntity> extends ChickenEntityModel<T> {
    public static final String RED_THING = "red_thing";
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart beak;
    private final ModelPart wattle;

    public DuckEntityModel(ModelPart root) {
        super(root);
        this.head = root.getChild(EntityModelPartNames.HEAD);
        this.beak = root.getChild(EntityModelPartNames.BEAK);
        this.wattle = root.getChild(RED_THING);
        this.body = root.getChild(EntityModelPartNames.BODY);
        this.rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
        this.leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
        this.rightWing = root.getChild(EntityModelPartNames.RIGHT_WING);
        this.leftWing = root.getChild(EntityModelPartNames.LEFT_WING);
    }

    @Override
    public void animateModel(T duckEntity, float limbAngle, float limbDistance, float tickDelta) {
        super.animateModel(duckEntity, limbAngle, limbDistance, tickDelta);

        if(duckEntity.isInSittingPose()) {
            this.body.setPivot(0.0f, 19.0f, 0.0f);
            this.body.pitch = (float)Math.toRadians(70);

            this.head.setPivot(0.0f, 18.0f, -4.0f);
            this.beak.setPivot(0.0f, 18.0f, -4.0f);
            this.wattle.setPivot(0.0f, 18.0f, -4.0f);

            this.leftWing.setPivot(4.0f, 16.0f, 0.0f);
            this.rightWing.setPivot(-4.0f, 16.0f, 0.0f);
        } else {
            this.body.setPivot(0.0f, 16.0f, 0.0f);
            this.body.pitch = (float)Math.toRadians(90);

            this.head.setPivot(0.0f, 15.0f, -4.0f);
            this.beak.setPivot(0.0f, 15.0f, -4.0f);
            this.wattle.setPivot(0.0f, 15.0f, -4.0f);

            this.leftWing.setPivot(4.0f, 13.0f, 0.0f);
            this.rightWing.setPivot(-4.0f, 13.0f, 0.0f);

        }

        this.leftWing.pitch = this.body.pitch - (float)Math.toRadians(90);
        this.rightWing.pitch = this.body.pitch - (float)Math.toRadians(90);
    }
}
