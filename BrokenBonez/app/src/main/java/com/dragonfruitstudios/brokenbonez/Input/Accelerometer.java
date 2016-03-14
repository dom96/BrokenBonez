package com.dragonfruitstudios.brokenbonez.Input;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class Accelerometer {
    /**
     * Returns a value between -1 and 1 determining the strength of the bike tilting.
     */
    public static float calculateTiltStrength(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float accelY = event.values[1];
            return accelY / 9.81f;
        }
        return 0f;
    }
}