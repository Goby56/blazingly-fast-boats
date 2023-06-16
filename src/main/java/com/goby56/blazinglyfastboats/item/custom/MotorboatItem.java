package com.goby56.blazinglyfastboats.item.custom;

import com.goby56.blazinglyfastboats.entity.custom.MotorboatEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.ChestBoatEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class MotorboatItem extends BoatItem {
    public MotorboatItem(boolean chest, BoatEntity.Type type, Settings settings) {
        super(chest, type, settings);
    }

    @Override
    protected BoatEntity createEntity(World world, HitResult hitResult) {
        return new MotorboatEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
    }
}
