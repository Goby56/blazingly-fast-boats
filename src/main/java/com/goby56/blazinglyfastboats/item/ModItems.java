package com.goby56.blazinglyfastboats.item;

import com.goby56.blazinglyfastboats.BlazinglyFastBoats;
import com.goby56.blazinglyfastboats.item.custom.MotorboatItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item MOTORBOAT = registerItem("motorboat",
            new MotorboatItem(false, BoatEntity.Type.ACACIA, new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(BlazinglyFastBoats.MOD_ID, name), item);
    }

    public static void register() {
        BlazinglyFastBoats.LOGGER.info("Registering modded items for " + BlazinglyFastBoats.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
            entries.add(MOTORBOAT);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(MOTORBOAT);
        });
    }
}
