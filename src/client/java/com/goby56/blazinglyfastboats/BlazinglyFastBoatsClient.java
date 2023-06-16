package com.goby56.blazinglyfastboats;

import com.goby56.blazinglyfastboats.entity.ModEntities;
import com.goby56.blazinglyfastboats.model.MotorboatEntityModel;
import com.goby56.blazinglyfastboats.render.MotorboatEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class BlazinglyFastBoatsClient implements ClientModInitializer {
	public static final EntityModelLayer MOTORBOAT_LAYER = new EntityModelLayer(new Identifier(BlazinglyFastBoats.MOD_ID, "motorboat_entity"), "main");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BlazinglyFastBoats.LOGGER.info("Registering client specific stuff for " + BlazinglyFastBoats.MOD_ID);
		EntityRendererRegistry.register(ModEntities.MOTORBOAT, MotorboatEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(MOTORBOAT_LAYER, MotorboatEntityModel::getTexturedModelData);

	}
}