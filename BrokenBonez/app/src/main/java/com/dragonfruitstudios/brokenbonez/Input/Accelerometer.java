package com.dragonfruitstudios.brokenbonez.Input;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

public class Accelerometer {
    long lastUpdate = System.currentTimeMillis();
    static float returnValue = 0;
    enum phoneSide {LEFT, RIGHT, FLAT, DOWN}
    static phoneSide mPhoneSide = null;

    public static float x = 0;
    public static float y = 0;
    public static float z = 0;

    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float currX, currY, currZ;

            currX = event.values[0];
            currY = event.values[1];
            currZ = event.values[2];

            x = currX;
            y = currY;
            z = currZ;

            long currentTime = System.currentTimeMillis();
            if((currentTime - lastUpdate) > 100){
                long extraTime = (currentTime - lastUpdate);
                lastUpdate = currentTime;
                
                Log.d("X", "" + currX);
                Log.d("Y", "" + currY);
                //Log.d("Z", "" + currZ);

                if((currY > 3.0)){
                    mPhoneSide = phoneSide.LEFT;
                    //Log.d("tilted on", "" + mPhoneSide);
                    switch ((int) currX){
                        case 3: returnValue = (float) 0.6;
                            break;
                        case 4: returnValue = (float) 0.5;
                            break;
                        case 5: returnValue = (float) 0.4;
                            break;
                        case 6: returnValue = (float) 0.3;
                            break;
                        case 7: returnValue = (float) 0.2;
                            break;
                        case 8: returnValue = (float) 0.1;
                            break;
                        default:break;
                    }
                }

                else if((currY < -3.0)){
                    mPhoneSide = phoneSide.RIGHT;
                    //Log.d("tilted on", "" + mPhoneSide);
                    switch ((int) currX){
                        case 3: returnValue = (float) 0.1;
                            break;
                        case 4: returnValue = (float) 0.2;
                            break;
                        case 5: returnValue = (float) 0.3;
                            break;
                        case 6: returnValue = (float) 0.4;
                            break;
                        case 7: returnValue = (float) 0.5;
                            break;
                        case 8: returnValue = (float) 0.6;
                            break;
                        default:break;
                    }
                }
                else if(currZ < 1.0){
                    mPhoneSide = phoneSide.DOWN;
                }
                else if(currY < -1.0){
                    mPhoneSide = phoneSide.FLAT;
                }
            }
        }
    }

    public static float getReturnValue(){
        if (Accelerometer.mPhoneSide != null) {
            return returnValue;
        } else {
            return returnValue;
        }
    }

    public static boolean isLeft() {
        return mPhoneSide == phoneSide.LEFT;
    }
    public static boolean isFlat() {return mPhoneSide == phoneSide.FLAT; }
    public static boolean isDown() {return mPhoneSide == phoneSide.DOWN; }
    public static float bikeStill() {return (float) 0.0;}
}