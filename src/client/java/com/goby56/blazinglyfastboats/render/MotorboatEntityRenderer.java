package com.goby56.blazinglyfastboats.render;

import com.goby56.blazinglyfastboats.BlazinglyFastBoats;
import com.goby56.blazinglyfastboats.BlazinglyFastBoatsClient;
import com.goby56.blazinglyfastboats.entity.custom.MotorboatEntity;
import com.goby56.blazinglyfastboats.model.MotorboatEntityModel;
import com.goby56.blazinglyfastboats.utils.EasingFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.*;
import org.joml.Quaternionf;

public class MotorboatEntityRenderer extends EntityRenderer<MotorboatEntity> {
    private final Pair<Identifier, CompositeEntityModel<MotorboatEntity>> textureAndModel;

    private final float planingHeight = 0.25f;
    private final float maxHullPitch = 20f; // degrees
    private final float maxHullRoll = 10f; // degrees
    private final float rollStrength = 20f;
    private final float hullRollDecay = 0.9f;
    private final float maxYawOnTurn = 180f; // degrees, the amount of turn that produces the maximum amount of hull roll
    private final float maxVelocity = 0.4f;

    public MotorboatEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.shadowRadius = 0.8F;
        this.textureAndModel = new Pair<>(new Identifier(BlazinglyFastBoats.MOD_ID, "textures/entity/motorboat.png"), this.createModel(ctx));
    }

    private CompositeEntityModel<MotorboatEntity> createModel(EntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getPart(BlazinglyFastBoatsClient.MOTORBOAT);
        return new MotorboatEntityModel(modelPart);
    }

    @Override
    public void render(MotorboatEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrices.push();
        matrices.translate(0.0F, 0.375F, 0.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F - yaw)); // Was 180.0F before, had to change

        double velocityDot = entity.getVelocity().normalize().dotProduct(Vec3d.fromPolar(0, yaw));
        double velocityFactor = entity.getVelocity().horizontalLength() / maxVelocity;
        if (velocityFactor > 1e-4 && entity.hasControllingPassenger()) {
//            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(String.valueOf(entity.getVelocity().horizontalLength())));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) (-this.maxHullPitch * EasingFunctions.upsideDownParabola(velocityFactor))));

//            if (entity.pressingLeft ^ entity.pressingRight) {
//                int rollDirection = entity.pressingLeft ? 1 : -1;
//                entity.roll += rollDirection * rollStrength * EasingFunctions.upsideDownParabola(velocityFactor) * tickDelta;
//            }
//            entity.roll *= hullRollDecay;
//            if (entity.roll > maxHullRoll) {
//                entity.roll = maxHullRoll;
//            }
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(String.valueOf(1 - velocityDot)));
            int rollDirection = entity.pressingLeft ? 1 : entity.pressingRight ? -1 : 0;
            float roll = (float) (rollDirection * EasingFunctions.easeOutElastic(1 - velocityDot) * rollStrength * EasingFunctions.upsideDownParabola(velocityFactor));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(Math.min(roll, maxHullRoll)));

            matrices.translate(0, planingHeight * EasingFunctions.easeOutBack(velocityFactor), 0);
        }

        entity.updateLastPlayerInput();

        float h = (float)entity.getDamageWobbleTicks() - tickDelta;
        float j = entity.getDamageWobbleStrength() - tickDelta;
        if (j < 0.0F) {
            j = 0.0F;
        }

        if (h > 0.0F) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.sin(h) * h * j / 10.0F * (float)entity.getDamageWobbleSide()));
        }

        float k = entity.interpolateBubbleWobble(tickDelta);
        if (!MathHelper.approximatelyEquals(k, 0.0F)) {
            matrices.multiply((new Quaternionf()).setAngleAxis(entity.interpolateBubbleWobble(tickDelta) * 0.017453292F, 1.0F, 0.0F, 1.0F));
        }

        Pair<Identifier, CompositeEntityModel<MotorboatEntity>> pair = this.textureAndModel;
        Identifier identifier = pair.getLeft();

        CompositeEntityModel<MotorboatEntity> compositeEntityModel = pair.getRight();
        matrices.scale(-1.0F, -1.0F, 1.0F);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        compositeEntityModel.setAngles(entity, tickDelta, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(compositeEntityModel.getLayer(identifier));
        compositeEntityModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!entity.isSubmergedInWater()) {
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());
            if (compositeEntityModel instanceof ModelWithWaterPatch modelWithWaterPatch) {
                modelWithWaterPatch.getWaterPatch().render(matrices, vertexConsumer2, light, OverlayTexture.DEFAULT_UV);
            }
        }

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumerProvider, light);
    }

    @Override
    public Identifier getTexture(MotorboatEntity entity) {
        return this.textureAndModel.getLeft();
    }
}
