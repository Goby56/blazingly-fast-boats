package com.goby56.blazinglyfastboats;

import com.goby56.blazinglyfastboats.entity.ModEntities;
import com.goby56.blazinglyfastboats.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlazinglyFastBoats implements ModInitializer {
	public static final String MOD_ID = "blazingly-fast-boats";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.register();
		ModEntities.register();
	}
}