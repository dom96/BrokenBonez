package com.dragonfruitstudios.brokenbonez.Input;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class Accelerometer extends Activity implements SensorEventListener {
    float lastX, lastY, lastZ;
    boolean isInitialized;
    SensorManager sensorManager;
    Sensor thisAccelerometer;
    final float NOISE_BALANCE = (float) 2.0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isInitialized = false;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        thisAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, thisAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onResume(){
        super.onResume();
        sensorManager.registerListener(this, thisAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];

        float y = event.values[1];

        float z = event.values[2];

        if (!isInitialized) {

            lastX = x;

            lastY = y;

            lastZ = z;

            Log.d("0.0", "0.0");

            isInitialized = true;

        } else {
            float deltaX = Math.abs(lastX - x);

            float deltaY = Math.abs(lastY - y);

            float deltaZ = Math.abs(lastZ - z);

            if (deltaX < NOISE_BALANCE) deltaX = (float) 0.0;

            if (deltaY < NOISE_BALANCE) deltaY = (float) 0.0;

            if (deltaZ < NOISE_BALANCE) deltaZ = (float) 0.0;

            lastX = x;

            lastY = y;

            lastZ = z;

            Log.d("X", "xxxxxx" + Float.toString(deltaX));
            Log.d("Y", "yyyyyy" + Float.toString(deltaY));
            Log.d("Z", "zzzzzz" + Float.toString(deltaZ));

            if (deltaX > deltaY) {


            } else if (deltaY > deltaX) {


            } else {

            }
        }
    }
}
