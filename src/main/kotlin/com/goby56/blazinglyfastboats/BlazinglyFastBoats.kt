package com.goby56.blazinglyfastboats

import com.goby56.blazinglyfastboats.item.ModItems
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BlazinglyFastBoats : ModInitializer {
	val MOD_ID: String = "blazingly-fast-boats";
    val logger: Logger = LoggerFactory.getLogger(MOD_ID);

	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		logger.info("Hello Fabric world!")
		ModItems.initialize();
	}
}