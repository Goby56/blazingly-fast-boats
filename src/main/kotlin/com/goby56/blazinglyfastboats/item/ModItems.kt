package com.goby56.blazinglyfastboats.item

import com.goby56.blazinglyfastboats.BlazinglyFastBoats
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object ModItems {
    val MOTORBOAT = register("motorboat", Item(FabricItemSettings()))

    private fun register(name: String, item: Item): Item {
        return Registry.register(Registries.ITEM, Identifier(BlazinglyFastBoats.MOD_ID, name), item);
    }

    private fun setItemGroup(item: Item, itemGroup: ItemGroup) {

    }

    fun initialize() {
        BlazinglyFastBoats.logger.debug("Registering mod items for " + BlazinglyFastBoats.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register{ entries -> {
                entries.add(MOTORBOAT)
            }
        };
    }
}