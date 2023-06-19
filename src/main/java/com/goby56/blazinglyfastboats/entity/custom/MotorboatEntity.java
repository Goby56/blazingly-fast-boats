package com.goby56.blazinglyfastboats.entity.custom;

import com.goby56.blazinglyfastboats.entity.ModEntities;
import com.goby56.blazinglyfastboats.item.ModItems;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MotorboatEntity extends BoatEntity {
    public LastPlayerInput lastPlayerInput = LastPlayerInput.NONE;

    public static final float MAX_FORWARD_SPEED = 1.2f;
    public static final float MAX_ROLL_DEGREES = 15f;
    public static final float MAX_PITCH_DEGREES = 20f;
    public static final float MAX_PLANING_HEIGHT = 0.25f;
    public static final float VELOCITY_DECAY = 0.93f; // 0.9 regular boat

    public float roll = 0;
    public float pitch = 0;
    public float planingHeight = 0;

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


    private double getVelocityFactor() {
        return this.getVelocity().horizontalLength() / MAX_FORWARD_SPEED;
    }

    private void handleInput() {
        if (!this.hasPassengers()) {
            return;
        }
        double speedIncrement = MAX_FORWARD_SPEED * (1 - VELOCITY_DECAY);
        double acceleration = 0f;
        double turningIncrement = (1 - 0.8 * this.getVelocityFactor());
        if (this.pressingLeft) {
            this.yawVelocity -= turningIncrement;
        }
        if (this.pressingRight) {
            this.yawVelocity += turningIncrement;
        }
        this.setYaw(this.getYaw() + this.yawVelocity);
        if (this.pressingLeft ^ this.pressingRight) {
            int rollDirection = this.pressingLeft ? 1 : -1;
            this.roll += rollDirection * Math.abs(this.yawVelocity);
        }
        this.roll = MathHelper.clamp(this.roll, -MAX_ROLL_DEGREES, MAX_ROLL_DEGREES);
//        if (this.pressingRight != this.pressingLeft && !this.pressingForward && !this.pressingBack) {
//
//        }
//        Vec3d velocity = Vec3d.fromPolar(0, this.getYaw());
        if (this.pressingForward) {
            acceleration += speedIncrement;
        }
        if (this.pressingBack) {
            acceleration -= speedIncrement / 4;
        }
        Vec3d accelerationVec = Vec3d.fromPolar(0, this.getYaw()).multiply(acceleration);
        this.setVelocity(this.getVelocity().add(accelerationVec));
//        this.setVelocity(this.getVelocity().add(MathHelper.sin(-this.getYaw() * ((float)Math.PI / 180)) * acceleration, 0.0, MathHelper.cos(this.getYaw() * ((float)Math.PI / 180)) * acceleration));
    }

    @Override
    protected void updatePaddles() {
        this.handleInput();
    }

    @Override
    protected void updateVelocity() {
        double d = -0.04f;
        double e = this.hasNoGravity() ? 0.0 : (double)-0.04f;
        double f = 0.0;
        this.velocityDecay = 0.05f;
        if (this.lastLocation == Location.IN_AIR && this.location != Location.IN_AIR && this.location != Location.ON_LAND) {
            this.waterLevel = this.getBodyY(1.0);
            this.setPosition(this.getX(), (double)(this.getWaterHeightBelow() - this.getHeight()) + 0.101, this.getZ());
            this.setVelocity(this.getVelocity().multiply(1.0, 0.0, 1.0));
            this.fallVelocity = 0.0;
            this.location = Location.IN_WATER;
        } else {
            if (this.location == Location.IN_WATER) {
                // implement the ability to go in lava (new Location value)
                // lava should be a little bit slower
                // TODO make water somewhat slippery
                f = (this.waterLevel - this.getY()) / (double)this.getHeight();
                this.velocityDecay = VELOCITY_DECAY;
            } else if (this.location == Location.UNDER_FLOWING_WATER) {
                e = -7.0E-4;
                this.velocityDecay = 0.9f;
            } else if (this.location == Location.UNDER_WATER) {
                f = 0.01f;
                this.velocityDecay = 0.45f;
            } else if (this.location == Location.IN_AIR) {
                this.velocityDecay = 0.9f;
            } else if (this.location == Location.ON_LAND) {
                this.velocityDecay = 0.1f;
                // maybe spawn particles of block on top of
            }
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.x * (double)this.velocityDecay, vec3d.y + e, vec3d.z * (double)this.velocityDecay);
            this.yawVelocity *= this.velocityDecay;
            this.roll *= this.velocityDecay;
            if (f > 0.0) {
                Vec3d vec3d2 = this.getVelocity();
                this.setVelocity(vec3d2.x, (vec3d2.y + f * 0.06153846016296973) * 0.75, vec3d2.z);
            }
        }
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


