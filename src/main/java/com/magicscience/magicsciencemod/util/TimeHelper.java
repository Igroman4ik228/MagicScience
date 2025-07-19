package com.magicscience.magicsciencemod.util;

public class TimeHelper {

    public static final int TICKS_PER_SECOND = 20;
    public static final int TICKS_PER_MINUTE = 60 * TICKS_PER_SECOND;

    public static int seconds(int seconds) {
        return seconds * TICKS_PER_SECOND;
    }

    public static int minutes(int minutes) {
        return minutes * TICKS_PER_MINUTE;
    }

    public static int seconds(double seconds) {
        return (int) (seconds * TICKS_PER_SECOND);
    }

    public static int minutes(double minutes) {
        return (int) (minutes * TICKS_PER_MINUTE);
    }
}