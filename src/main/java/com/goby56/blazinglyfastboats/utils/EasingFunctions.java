package com.goby56.blazinglyfastboats.utils;

public class EasingFunctions {
    public static double easeOutBack(double x) {
        // https://easings.net/#easeOutBack

        float c1 = 1.70158f;
        float c3 = c1 + 1;

        return 1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2);
    }

    public static double upsideDownParabola(double x) {
        return -4 * Math.pow(x, 2) + 4 * x;
    }

    public static double easeOutQuad(double x) {
        return 1 - (1 - x) * (1 - x);
    }
}
