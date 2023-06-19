package com.goby56.blazinglyfastboats.entity.custom;

import com.goby56.blazinglyfastboats.entity.ModEntities;
import com.goby56.blazinglyfastboats.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MotorboatEntity extends BoatEntity {
    public static final float MAX_VELOCITY = 1.2f; // blocks per tick
    public static final float MAX_ROLL_DEGREES = 15f;
    public static final float MAX_PITCH_DEGREES = 20f;
    public static final float MAX_PLANING_HEIGHT = 0.25f;
    public static final float VELOCITY_DECAY = 0.93f; // 0.9 regular boat

    public static final float MOTOR_POWER = 2;
    public static final float MAX_ACCELERATION = ;
    public static final float TURNING_RADIUS = 5f;
    public static final float ACCELERATION_SCALE = 0.1f;
    public static final float DRAG_COEFFICIENT = 0.8f;

    public float roll = 0;
    public float pitch = 0;
    public float planingHeight = 0;

    public Vec3d acceleration = Vec3d.ZERO;

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

    private double getVelocityFactor() {
        return this.getVelocity().horizontalLength() / MAX_VELOCITY;
    }

    private void setAcceleration(Vec3d acceleration) {
        this.acceleration = acceleration;
    }

    private void setAcceleration(double x, double y, double z) {
        this.acceleration = new Vec3d(x, y, z);
    }

    private void applyAcceleration(Vec3d acceleration) {
        this.acceleration = this.acceleration.add(acceleration);
    }

    private void applyAcceleration(float magnitude, float yaw) {
        this.applyAcceleration(Vec3d.fromPolar(0, this.getYaw() + yaw).multiply(magnitude));
    }

    private void handleInput() {
        double vel = this.getVelocity().horizontalLength();
        System.out.print("Centripetal acceleration: ");
        System.out.print(ACCELERATION_SCALE * Math.pow(vel, 2) / TURNING_RADIUS);
        System.out.print("  Forward acceleration: ");
        System.out.print(ACCELERATION_SCALE);
        System.out.println();
        if (this.pressingLeft) {
            this.applyAcceleration((float) (Math.pow(vel, 2) / TURNING_RADIUS), -90);
        }
        if (this.pressingRight) {
            this.applyAcceleration((float) (Math.pow(vel, 2) / TURNING_RADIUS), 90);
        }
        if (this.pressingForward) {
            this.applyAcceleration(ACCELERATION_SCALE / 10, 0);
        }
        if (this.pressingBack) {
            this.applyAcceleration(ACCELERATION_SCALE / 40, 180);
        }

        if (this.acceleration.horizontalLength() > MAX_ACCELERATION) {
            this.setAcceleration(this.acceleration.normalize().multiply(MAX_ACCELERATION));
        }

        Vec3d velNorm = this.getVelocity().normalize();
        this.setYaw((float) ((float) (180f / Math.PI) * -Math.atan2(velNorm.x, velNorm.z)));
        this.applyAcceleration((float) (ACCELERATION_SCALE * DRAG_COEFFICIENT * this.getVelocityFactor()), 180);

        if (this.pressingLeft ^ this.pressingRight) {
            int rollDirection = this.pressingLeft ? 1 : -1;
            this.roll += rollDirection * Math.abs(this.acceleration.normalize().dotProduct(velNorm));
        }
        this.roll = MathHelper.clamp(this.roll, -MAX_ROLL_DEGREES, MAX_ROLL_DEGREES);

        this.setVelocity(this.getVelocity().add(acceleration));
    }

    @Override
    protected void updatePaddles() {
        if (!this.hasPassengers()) {
            return;
        }
        if (this.location == Location.IN_WATER && this.getVelocityFactor() > 1e-2) {
            this.handleInput();
            return;
        }
        float f = 0.0f;
        if (this.pressingLeft) {
            this.yawVelocity -= 1.0f;
        }
        if (this.pressingRight) {
            this.yawVelocity += 1.0f;
        }
        this.setYaw(this.getYaw() + this.yawVelocity);
        if (this.pressingForward) {
            f += 0.004f;
        }
        if (this.pressingBack) {
            f -= 0.0005f;
        }
        this.setVelocity(this.getVelocity().add(MathHelper.sin(-this.getYaw() * ((float)Math.PI / 180)) * f, 0.0, MathHelper.cos(this.getYaw() * ((float)Math.PI / 180)) * f));
    }

    @Override
    protected void updateVelocity() {
        double d = -0.04f;
        double e = this.hasNoGravity() ? 0.0 : (double)-0.04f;
        double f = 0.0;
        this.velocityDecay = 0.05f;

        Vec3d vel = this.getVelocity();
        if (Math.abs(vel.x) < 1e-2) {
            this.setAcceleration(0, 0, this.acceleration.z);
        }
        if (Math.abs(vel.z) < 1e-2) {
            this.setAcceleration(this.acceleration.x, 0, 0);
        }
        if (vel.horizontalLength() > MAX_VELOCITY) {
            this.setVelocity(vel.normalize().multiply(MAX_VELOCITY));
        }

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
                // maybe spawn particles of block on top of and break propeller
            }
            this.setVelocity(vel.x * (double)this.velocityDecay, vel.y + e, vel.z * (double)this.velocityDecay);
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
}


