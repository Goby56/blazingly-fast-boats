package com.goby56.blazinglyfastboats.utils;

public class EasingFunction {
    public double l;
    public double u;
    public double t;

    public EasingFunction(int tickDuration, double fromVelocity, double toVelocity) {
        this.l = fromVelocity;
        this.u = toVelocity;
        this.t = tickDuration;
    }

    public double compute(double x) {
        return (l - u) / (t * t) * x * x + 2 * (u - l) / t * x + l;
    }

    public double inverse(double y) {
        return (t - t * Math.sqrt(1 + (y - l) / (l - u)));
    }

    public static double easeOutBack(double x) {
        // https://easings.net/#easeOutBack

        float c1 = 1.70158f;
        float c3 = c1 + 1;

        return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
    }

    public static double upsideDownParabola(double x) {
        return -4 * x * (x - 1);
    }

    public static double easeOutElastic(double x) {
        // https://easings.net/#easeOutElastic

        double c4 = (2 * Math.PI) / 3;

        return x == 0 ? 0 : x == 1 ? 1 : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * c4) + 1;
    }


    public static double easeOutQuad(double x) {
        // https://easings.net/#easeOutQuad

        return 1 - (1 - x) * (1 - x);
    }
}
