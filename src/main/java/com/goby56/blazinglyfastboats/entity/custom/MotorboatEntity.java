package com.goby56.blazinglyfastboats.entity.custom;

import com.goby56.blazinglyfastboats.entity.ModEntities;
import com.goby56.blazinglyfastboats.item.ModItems;
import com.goby56.blazinglyfastboats.utils.EasingFunction;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;

public class MotorboatEntity extends BoatEntity {
    public static final float MAX_VELOCITY = 2f;
    public static final float MAX_REVERSE_VELOCITY = 0.2f;
    public static final float MAX_ROLL_DEGREES = 15f;
    public static final float MAX_PITCH_DEGREES = 20f;
    public static final float MAX_PLANING_HEIGHT = 0.25f;

    private static final float MINIMUM_VELOCITY = 1e-4f;

    public static final float MOTOR_POWER = 2;

    public int direction = 0; // -1, 0, 1 : backwards, stationary, forwards
    public int action = 0; // -1, 0, 1 : reversing/braking, nothing/drifting, accelerating forwards
    public boolean isBoosting = false;

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
        double maxVelocity = Math.max(this.currentVelocityPhase.fromVelocity, this.currentVelocityPhase.toVelocity);
        return this.getVelocity().horizontalLength() / maxVelocity;
    }

    private void applyVelocity(double magnitude, float yaw) {
        this.setVelocity(this.getVelocity().add(Vec3d.fromPolar(0, this.getYaw() + yaw).multiply(magnitude)));
    }

    private void setVelocity(double magnitude, float yaw) {
        this.setVelocity(Vec3d.fromPolar(0, yaw).multiply(magnitude));
    }

    private void handleInput() {
        double velocity = this.getVelocity().horizontalLength();

        double turningAmount = Math.max(0.2, 1 - velocity / MAX_VELOCITY);
        if (this.pressingLeft) {
            this.yawVelocity -= turningAmount;
        }
        if (this.pressingRight) {
            this.yawVelocity += turningAmount;
        }
        this.setYaw(this.getYaw() + this.yawVelocity);

        this.action = 0;
        if (this.pressingForward) {
            this.action += 1;
        }
        if (this.pressingBack) {
            this.action -= 1;
        }
        if (velocity < MINIMUM_VELOCITY) {
            // TODO TEST LESS THAN AND EQUAL
            this.direction = this.action;
        } else {
            this.direction = (int) Math.signum(this.getVelocity().normalize().dotProduct(Vec3d.fromPolar(0, this.getYaw())));
        }
        // TODO IMPLEMENT SO THAT TURNING MAKES IT SO THAT YOU CANT START PLANING
        System.out.printf("direction: %d, action: %d, ", this.direction, this.action);

        this.currentVelocityPhase = VelocityPhase.getPhase(velocity, this.direction, this.action);
        double newVelocity = this.currentVelocityPhase.nextVelocity(velocity, false);

        if (this.pressingLeft ^ this.pressingRight) {
//            newVelocity *= 0.9;
            // TODO TURNING SHOULD SLOW THE BOAT DOWN
//            newVelocity -= 0.1 * MAX_VELOCITY * Math.abs(this.yawVelocity) / 10f;
            System.out.printf("yaw vel: %f, ", yawVelocity);
            int rollDirection = this.pressingLeft ^ this.direction == -1 ? 1 : -1;
            this.roll += rollDirection * Math.abs(this.yawVelocity);
        }
        this.roll = MathHelper.clamp(this.roll, -MAX_ROLL_DEGREES, MAX_ROLL_DEGREES);


        System.out.printf("new vel: %f, ", newVelocity);
        this.setVelocity(newVelocity * direction, this.getYaw());

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
                this.velocityDecay = 0.9f;
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
            Vec3d vel = this.getVelocity();
            if (!this.hasPassengers()) {
                this.setVelocity(vel.x * this.velocityDecay, vel.y + e, vel.z * this.velocityDecay);
            } else {
                this.setVelocity(vel.x, vel.y + e, vel.z);
            }
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
        STATIONARY(1, 0, MINIMUM_VELOCITY, 0, 0),

        FORWARD_ACCELERATION(60, MINIMUM_VELOCITY, 0.8f, 1, 1),
        PLANING_ACCELERATION(80, 0.8f, MAX_VELOCITY, 1, 1),
        PLANING_DRIFT(40, MAX_VELOCITY, 1.4f, 1, 0),
        FORWARD_DRIFT(30, 1.4f, 0f, 1, 0),
        FORWARD_BRAKING(20, 1.4f, 0f, 1, -1),
        PLANING_BRAKING(10, MAX_VELOCITY, 1.4f, 1, -1),

        REVERSING_ACCELERATION(20, MINIMUM_VELOCITY, MAX_REVERSE_VELOCITY, -1, -1),
        REVERSING_DRIFT(10, MAX_REVERSE_VELOCITY, 0f, -1, 0),
        REVERSE_BRAKING(5, MAX_REVERSE_VELOCITY, 0f, -1, 1);

        private final int tickDuration;
        private final double fromVelocity;
        private final double toVelocity;

        private final int direction;
        private final int action;

        private final boolean isIncremental;
        private final EasingFunction velocityFunction;

        public double nextVelocity(double currentVelocity, boolean isBoosting) {
            if ((currentVelocity >= this.toVelocity == this.isIncremental) && (this == PLANING_ACCELERATION || this == REVERSING_ACCELERATION)) {
                return this.toVelocity;
            }
            double tickProgress = this.velocityFunction.inverse(currentVelocity);
            tickProgress += isBoosting ? 2 : 1;

            System.out.printf("phase: %s, prev tickProg: %f, ", this, tickProgress / this.tickDuration);

            if (tickProgress >= this.tickDuration) {
                // CAN MAYBE REMOVE
//                if (this == PLANING_ACCELERATION || this == REVERSING_ACCELERATION) {
//                    System.out.printf("toVelocity: %f, ", this.toVelocity);
//                    return this.toVelocity;
//                }
                if (this == STATIONARY) {
                    return MINIMUM_VELOCITY * 2;
                }
                tickProgress = tickDuration;

                if (this.isIncremental) {
                    return this.velocityFunction.compute(tickProgress) + MINIMUM_VELOCITY;
                } else {
                    return this.velocityFunction.compute(tickProgress) - MINIMUM_VELOCITY;
                }
            }

            System.out.printf("new tickProg: %f, ", tickProgress / this.tickDuration);
            return this.velocityFunction.compute(tickProgress);
        }

        public static VelocityPhase getPhase(double velocity, int direction, int action) {
            ArrayList<VelocityPhase> possiblePhases = new ArrayList<>();
            for (VelocityPhase phase : VelocityPhase.values()) {
                if (phase.direction == direction && phase.action == action) {
                    possiblePhases.add(phase);
                }
            }
            if (possiblePhases.size() == 1) {
                return possiblePhases.get(0);
            }
            if (possiblePhases.size() > 1) {
                for (VelocityPhase phase : possiblePhases) {
                    if (phase.withinBounds(velocity)) {
                        return phase;
                    }
                    if (phase.isMaxPhase(velocity)) {
                        return phase;
                    }
                }
            }
            return STATIONARY;
        }

        private boolean withinBounds(double velocity) {
            if ((velocity >= toVelocity == this.isIncremental) && (this == PLANING_ACCELERATION || this == REVERSING_ACCELERATION)) {
                return true;
            }
            if (this.isIncremental) {
                return (velocity >= fromVelocity) && (velocity < toVelocity);
            }
            return (velocity <= fromVelocity) && (velocity > toVelocity);
//            return (velocity >= fromVelocity == this.isIncremental) &&
//                    (velocity < toVelocity == this.isIncremental);
        }

        private boolean isMaxPhase(double velocity) {
            return (velocity >= MAX_VELOCITY && (this.fromVelocity == MAX_VELOCITY || this.toVelocity == MAX_VELOCITY));
        }

        VelocityPhase(int tickDuration, double fromVelocity, double toVelocity, int direction, int action) {
            this.tickDuration = tickDuration;
            this.fromVelocity = fromVelocity;
            this.toVelocity = toVelocity;
            this.direction = direction;
            this.action = action;

            this.isIncremental = toVelocity - fromVelocity > 0;
            this.velocityFunction = new EasingFunction(this.tickDuration, this.fromVelocity, this.toVelocity);
        }

    }
}


