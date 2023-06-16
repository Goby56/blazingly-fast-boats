package com.goby56.blazinglyfastboats.render;

import com.goby56.blazinglyfastboats.BlazinglyFastBoats;
import com.goby56.blazinglyfastboats.entity.custom.MotorboatEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class MotorboatEntityRenderer extends EntityRenderer<MotorboatEntity> {

    public MotorboatEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.shadowRadius = 0.8F;
    }

    @Override
    public Identifier getTexture(MotorboatEntity entity) {
        BlazinglyFastBoats.LOGGER.info("WHHEEERE IS TEXTURREWS");
        return new Identifier(BlazinglyFastBoats.MOD_ID, "textures/entity/motorboat.png");
    }
}
