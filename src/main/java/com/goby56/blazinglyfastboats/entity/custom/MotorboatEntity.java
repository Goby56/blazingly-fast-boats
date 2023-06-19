package com.goby56.blazinglyfastboats.entity.custom;

import com.goby56.blazinglyfastboats.entity.ModEntities;
import com.goby56.blazinglyfastboats.item.ModItems;
import com.goby56.blazinglyfastboats.utils.EasingFunction;
import com.mojang.datafixers.util.Either;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MotorboatEntity extends BoatEntity {
    public static final float MAX_FORWARD_SPEED = 1.2f;
    public static final float MAX_ROLL_DEGREES = 15f;
    public static final float MAX_PITCH_DEGREES = 20f;
    public static final float MAX_PLANING_HEIGHT = 0.25f;
    public static final float VELOCITY_DECAY = 0.93f; // 0.9 regular boat

    public static final float MOTOR_POWER = 2;

    public int direction = 0; // -1, 0, 1 : backwards, stationary, forwards
    public boolean isAccelerating = false;

    public VelocityPhase currentVelocityPhase = VelocityPhase.STATIONARY;

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

    public double getVelocityFactor() {
        if (this.currentVelocityPhase == VelocityPhase.STATIONARY) {
            return 0;
        }
        float maxVelocity = Math.max(this.currentVelocityPhase.fromVelocity, this.currentVelocityPhase.toVelocity);
        return this.getVelocity().horizontalLength() / maxVelocity;
    }

    private void applyVelocity(double magnitude, float yaw) {
        this.setVelocity(this.getVelocity().add(Vec3d.fromPolar(0, this.getYaw() + yaw).multiply(magnitude)));
    }

    private void handleInput() {
        double velocity = this.getVelocity().horizontalLength();

        if (this.pressingLeft) {
            this.yawVelocity -= 1 - this.getVelocityFactor();
        }
        if (this.pressingRight) {
            this.yawVelocity += 1 - this.getVelocityFactor();
        }
        this.setYaw(this.getYaw() + this.yawVelocity);

        this.isAccelerating = false;
        this.direction = 0;
        if (this.pressingForward) {
            this.isAccelerating = true;
            this.direction = 1;
        }
        if (this.pressingBack) {
            this.isAccelerating = true;
            this.direction = -1;
        }

        this.currentVelocityPhase.nextVelocity(velocity, false)
                .ifLeft(newVelocity -> this.applyVelocity(newVelocity, this.getYaw()))
                .ifRight(phase -> this.currentVelocityPhase = phase );

        if (this.pressingLeft ^ this.pressingRight) {
            int rollDirection = this.pressingLeft ? 1 : -1;
            this.roll += rollDirection * Math.abs(this.yawVelocity);
        }
        this.roll = MathHelper.clamp(this.roll, -MAX_ROLL_DEGREES, MAX_ROLL_DEGREES);
    }

    @Override
    protected void updatePaddles() {
        if (!this.hasPassengers()) {
            return;
        }
        this.handleInput();
    }

    @Override
    protected void updateVelocity() {
        double d = -0.04f;
        double e = this.hasNoGravity() ? 0.0 : (double)-0.04f;
        double f = 0.0;
        this.velocityDecay = 0.05f;
        // TODO move some of this code to @tick
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
            this.setVelocity(vec3d.x, vec3d.y + e, vec3d.z);
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

    public enum VelocityPhase {
        STATIONARY(0, 0, 0, 0, false),

        FORWARD_ACCELERATION(60, 0f, 0.5f, 1, true),
        PLANING_ACCELERATION(80, 0.5f, 2f, 1, true, true),
        PLANING_DRIFT(40, 2f, 1.25f, 1, false),
        FORWARD_DRIFT(30, 0.5f, 0f, 1, false),

        REVERSING_DRIFT(10, 0.1f, 0f, -1, false),
        REVERSING_ACCELERATION(20, 0f, 0.1f, -1, true, true);

        private final int tickDuration;
        private final float fromVelocity;
        private final float toVelocity;

        private final int direction;
        private final boolean isAccelerating;

        private final boolean isIncremental;
        private final EasingFunction velocityFunction;

        private final boolean isMax;

        public Either<Double, VelocityPhase> nextVelocity(double currentVelocity, boolean isBoosting) {
            if (!withinBounds(currentVelocity)) {
                if (currentVelocity > this.toVelocity == this.isIncremental && this.isMax) {
                    return Either.left((double) this.toVelocity);
                }
                return Either.right(getPhase(currentVelocity, this.direction, this.isAccelerating));
            }
            int tickProgress = this.velocityFunction.inverse(currentVelocity);
            tickProgress += isBoosting ? 2 : 1;
            return Either.left(this.velocityFunction.compute(tickProgress));
        }

        public static VelocityPhase getPhase(double velocity, int direction, boolean isAccelerating) {
            for (VelocityPhase phase : VelocityPhase.values()) {
                if (phase.withinBounds(velocity) && phase.direction == direction && phase.isAccelerating == isAccelerating) {
                    return phase;
                }
            }
            if (direction == 1) {
                return PLANING_ACCELERATION;
            }
            if (direction == -1) {
                return REVERSING_ACCELERATION;
            }
            return STATIONARY;
        }

        private boolean withinBounds(double velocity) {
            return velocity < fromVelocity != this.isIncremental &&
                    velocity > fromVelocity != this.isIncremental;
        }

        VelocityPhase(int tickDuration, float fromVelocity, float toVelocity, int direction, boolean isAccelerating) {
            this.tickDuration = tickDuration;
            this.fromVelocity = fromVelocity;
            this.toVelocity = toVelocity;
            this.direction = direction;
            this.isAccelerating = isAccelerating;

            this.isMax = false;

            this.isIncremental = toVelocity - fromVelocity > 0;
            this.velocityFunction = new EasingFunction(this.tickDuration, this.fromVelocity, this.toVelocity);
        }

        VelocityPhase(int tickDuration, float fromVelocity, float toVelocity, int direction, boolean isAccelerating, boolean isMax) {
            this.tickDuration = tickDuration;
            this.fromVelocity = fromVelocity;
            this.toVelocity = toVelocity;
            this.direction = direction;
            this.isAccelerating = isAccelerating;

            this.isMax = isMax;

            this.isIncremental = toVelocity - fromVelocity > 0;
            this.velocityFunction = new EasingFunction(this.tickDuration, this.fromVelocity, this.toVelocity);
        }
    }
}


