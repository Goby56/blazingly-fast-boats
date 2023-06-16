package com.goby56.blazinglyfastboats.data

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.ItemModelGenerator

class ModModelGenerator : FabricModelProvider {
    constructor(output: FabricDataOutput) : super(output)

    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator?) {
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator?) {
    }
}