package com.goby56.blazinglyfastboats.entity.custom;

import com.goby56.blazinglyfastboats.entity.ModEntities;
import com.goby56.blazinglyfastboats.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class MotorboatEntity extends BoatEntity {
    public LastPlayerInput lastPlayerInput = LastPlayerInput.NONE;

    public MotorboatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    public MotorboatEntity(World world, double x, double y, double z) {
        this(ModEntities.MOTORBOAT, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public void updateLastPlayerInput() {
        if (this.pressingForward) {
           this.lastPlayerInput = LastPlayerInput.FORWARD;
        } else if (this.pressingBack) {
            this.lastPlayerInput = LastPlayerInput.BACK;
        } else if (this.pressingLeft) {
            this.lastPlayerInput = LastPlayerInput.LEFT;
        } else if (this.pressingRight) {
            this.lastPlayerInput = LastPlayerInput.RIGHT;
        } else {
            this.lastPlayerInput = LastPlayerInput.NONE;
        }
    }

    @Override
    public void tick() {
        this.prevYaw = this.getYaw();
        super.tick();
    }

    @Override
    public Item asItem() {
        return ModItems.MOTORBOAT;
    }

    public enum LastPlayerInput{
        FORWARD,
        BACK,
        LEFT,
        RIGHT,
        NONE;

        LastPlayerInput() {
        }
    }
}


