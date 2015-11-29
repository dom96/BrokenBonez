package com.dragonfruitstudios.brokenbonez.Input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**Class responsible for handling sensor events in relation to the accelerometer**/
public class AccelerometerHandler implements SensorEventListener {
    float accelX; // Accelerometer axes X
    float accelY; // Accelerometer axes Y
    float accelZ; // Accelerometer axes Z

    public AccelerometerHandler(Context context) {
        // Setting up event listening
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        // Declares the sensor type as accelerometer
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            /**Checks to see if there really is an accelerometer installed in the device and if
             * the sensor finds none the handler will change the acceleration to zero automatically.**/
            Sensor accelerometer = manager.getSensorList(
                    Sensor.TYPE_ACCELEROMETER).get(0);
            manager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_GAME); // A rate suitable for games. Constant Value(1)
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){
        /** Called when sensor accuracy has changed, a void function.
         *Needed in order to use sensor manager.
         *https://developer.android.com/reference/android/hardware/SensorManager.html**/
    }

    // Fetches the appropriate accelerometer values from the sensor event listener
    public void onSensorChanged(SensorEvent event){
        accelX=event.values[0];
        accelY=event.values[1];
        accelZ=event.values[2];
    }

    // Returns the current state of acceleration for the x, y, z values
    public float getAccelX(){ //yaw
        return accelX;
    }
    public float getAccelY(){ //pitch

        return accelY;
    }
    public float getAccelZ(){ //roll
        return accelZ;
    }
}