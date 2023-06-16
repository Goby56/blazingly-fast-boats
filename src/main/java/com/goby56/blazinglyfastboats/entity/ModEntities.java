package com.goby56.blazinglyfastboats.entity;

import com.goby56.blazinglyfastboats.BlazinglyFastBoats;
import com.goby56.blazinglyfastboats.entity.custom.MotorboatEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<MotorboatEntity> MOTORBOAT = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(BlazinglyFastBoats.MOD_ID, "motorboat_entity"),
            FabricEntityTypeBuilder.<MotorboatEntity>create(SpawnGroup.MISC, MotorboatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375f, 0.5625f))
                    .build());

    public static void register() {
        BlazinglyFastBoats.LOGGER.info("Registering modded entities for " + BlazinglyFastBoats.MOD_ID);
    }
}
