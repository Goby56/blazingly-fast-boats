package com.goby56.blazinglyfastboats

import com.goby56.blazinglyfastboats.data.ModModelGenerator
import com.goby56.blazinglyfastboats.data.ModRecipeGenerator
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object BlazinglyFastBoatsDataGenerator : DataGeneratorEntrypoint {
	override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
		val pack: FabricDataGenerator.Pack = fabricDataGenerator.createPack();

		pack.addProvider { ModModelGenerator(it) }
	}
}