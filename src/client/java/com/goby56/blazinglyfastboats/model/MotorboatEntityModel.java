package com.goby56.blazinglyfastboats.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class MotorboatEntityModel extends EntityModel<Entity> {
    private final ModelPart hull;
    private final ModelPart engine;

    public MotorboatEntityModel(ModelPart root, ModelPart back, ModelPart sides, ModelPart floor) {
        this.hull = root.getChild("hull");
        this.engine = root.getChild("engine");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData hull = modelPartData.addChild("hull", ModelPartBuilder.create().uv(0, 27).cuboid(-8.0F, -7.0F, -11.0F, 16.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 22.0F, 5.5F, 0.0F, 3.1416F, 0.0F));

        ModelPartData back = hull.addChild("back", ModelPartBuilder.create().uv(0, 19).cuboid(6.0F, -9.0F, 23.5F, 3.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 23).cuboid(-6.0F, -5.0F, 23.5F, 12.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(13, 19).cuboid(-9.0F, -9.0F, 23.5F, 3.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, -5.5F));

        ModelPartData sides = hull.addChild("sides", ModelPartBuilder.create().uv(0, 35).cuboid(0.5F, -31.0F, -7.0F, 28.0F, 6.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 43).cuboid(1.5F, -31.0F, -25.0F, 28.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-15.0F, 24.0F, -10.5F, 0.0F, -1.5708F, 0.0F));

        ModelPartData floor = hull.addChild("floor", ModelPartBuilder.create().uv(0, 0).cuboid(-13.5F, -8.0F, -2.0F, 28.0F, 16.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 4.5F, 1.5708F, 1.5708F, 0.0F));

        ModelPartData engine = modelPartData.addChild("engine", ModelPartBuilder.create().uv(62, 0).cuboid(-5.0F, -15.0F, -20.0F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F))
                .uv(62, 20).cuboid(-2.0F, -5.0F, -19.0F, 4.0F, 2.0F, 4.0F, new Dilation(0.0F))
                .uv(78, 20).cuboid(-2.0F, -3.0F, -18.0F, 4.0F, 4.0F, 3.0F, new Dilation(0.0F))
                .uv(92, 20).cuboid(-1.0F, -2.0F, -20.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(100, 20).cuboid(-2.0F, -3.0F, -21.0F, 4.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 64);
    }
    @Override
    public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        hull.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        engine.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
