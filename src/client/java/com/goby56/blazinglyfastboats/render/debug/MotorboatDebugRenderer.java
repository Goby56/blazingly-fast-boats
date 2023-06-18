package com.goby56.blazinglyfastboats.render.debug;

import com.goby56.blazinglyfastboats.entity.custom.MotorboatEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MotorboatDebugRenderer {

    public static void renderVelocityVector(MotorboatEntity entity, VertexConsumer vertices, MatrixStack matrices) {
        Vec3d vel = entity.getVelocity();
        Matrix4f posMat = matrices.peek().getPositionMatrix();
        Matrix3f normMat = matrices.peek().getNormalMatrix();
        vertices.vertex(posMat, 0.0F, entity.getStandingEyeHeight(), 0.0F).color(255, 255, 0, 255).normal(normMat, (float)vel.x, (float)vel.y, (float)vel.z).next();
        vertices.vertex(posMat, (float)(vel.x * 4.0D), (float)((double)entity.getStandingEyeHeight() + vel.y * 4.0D), (float)(vel.z * 4.0D)).color(255, 255, 0, 255).normal(normMat, (float)vel.x, (float)vel.y, (float)vel.z).next();
    }

//    public static void renderAccelerationVector(MotorboatEntity entity, VertexConsumer vertices, MatrixStack matrices) {
//        Vec3d acc = entity.acceleration;
//        Matrix4f posMat = matrices.peek().getPositionMatrix();
//        Matrix3f normMat = matrices.peek().getNormalMatrix();
//        vertices.vertex(posMat, 0.0F, entity.getStandingEyeHeight(), 0.0F).color(0, 255, 255, 255).normal(normMat, (float)acc.x, (float)acc.y, (float)acc.z).next();
//        vertices.vertex(posMat, (float)(acc.x * 4.0D), (float)((double)entity.getStandingEyeHeight() + acc.y * 4.0D), (float)(acc.z * 4.0D)).color(0, 255, 255, 255).normal(normMat, (float)acc.x, (float)acc.y, (float)acc.z).next();
//    }
}
