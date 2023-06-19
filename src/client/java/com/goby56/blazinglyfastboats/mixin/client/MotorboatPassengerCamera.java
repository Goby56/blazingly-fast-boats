package com.goby56.blazinglyfastboats.mixin.client;

import com.goby56.blazinglyfastboats.entity.custom.MotorboatEntity;
import com.goby56.blazinglyfastboats.render.MotorboatEntityRenderer;
import com.goby56.blazinglyfastboats.utils.EasingFunctions;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class MotorboatPassengerCamera {
    @Shadow private Entity focusedEntity;

    @Shadow private float cameraY;

    @Inject(method = "updateEyeHeight", at = @At("TAIL"))
    private void translateEyeHeight(CallbackInfo ci) {
        if (this.focusedEntity != null && this.focusedEntity.hasVehicle()) {
           if (this.focusedEntity.getVehicle() instanceof MotorboatEntity motorboat) {
               double velocityFactor = motorboat.getVelocity().horizontalLength() / MotorboatEntity.MAX_VELOCITY;
               this.cameraY += MotorboatEntityRenderer.planingHeight * EasingFunctions.easeOutBack(velocityFactor) / 2;
               // Implement camera tilt (roll) if camera tilt on turn is true in config
           }
        }
    }

}
