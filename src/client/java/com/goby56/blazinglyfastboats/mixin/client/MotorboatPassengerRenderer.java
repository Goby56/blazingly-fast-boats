package com.goby56.blazinglyfastboats.mixin.client;

import com.goby56.blazinglyfastboats.entity.custom.MotorboatEntity;
import com.goby56.blazinglyfastboats.render.MotorboatEntityRenderer;
import com.goby56.blazinglyfastboats.utils.EasingFunctions;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class MotorboatPassengerRenderer {
	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"))
	private void renderPassenger(LivingEntity livingEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (livingEntity.hasVehicle()) {
			if (livingEntity.getVehicle() instanceof MotorboatEntity motorboat) {
				double velocityFactor = motorboat.getVelocity().horizontalLength() / MotorboatEntity.MAX_VELOCITY;
				double pitch = MotorboatEntityRenderer.maxHullPitch * EasingFunctions.upsideDownParabola(velocityFactor);

				RotationAxis rollAxis = RotationAxis.of(Vec3d.fromPolar((float) pitch, motorboat.getYaw()).toVector3f());
				RotationAxis pitchAxis = RotationAxis.of(Vec3d.fromPolar((float) pitch, motorboat.getYaw() + 90f).toVector3f());

				matrixStack.multiply(rollAxis.rotationDegrees(-motorboat.roll));
				matrixStack.multiply(pitchAxis.rotationDegrees((float) (MotorboatEntityRenderer.maxHullPitch * EasingFunctions.upsideDownParabola(velocityFactor))));
				matrixStack.translate(0, MotorboatEntityRenderer.planingHeight * EasingFunctions.easeOutBack(velocityFactor), 0);
			}
		}
	}
}