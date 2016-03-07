package com.dragonfruitstudios.brokenbonez.Input;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class Accelerometer {
    float lastX, lastY;
    boolean isInitialized = false;

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        final float THRESHOLD = (float) 0.4; // value changes the accelerometer sensitivity

        if (!isInitialized) {
            lastX = x;
            lastY = y;
            Log.d("ACCELEROMETER:", "0.0");
            isInitialized = true;
        } else {
            lastX = x;
            lastY = y;

            //Log.d("X", "x-axis" + Float.toString(lastX));
            //Log.d("Y", "y-axis" + Float.toString(lastY));

            if(lastX < THRESHOLD + 9.0 && lastX > THRESHOLD + 4.5 && lastY < THRESHOLD + 6.5 && lastY > THRESHOLD) {
                Log.d("LEFT", "left");
            }
            if (lastX < THRESHOLD + 9.0 && lastX > THRESHOLD + 4.5 && lastY > -THRESHOLD - 6.5 && lastY < -THRESHOLD) {
                Log.d("RIGHT", "right");
            }
            }
    }
}