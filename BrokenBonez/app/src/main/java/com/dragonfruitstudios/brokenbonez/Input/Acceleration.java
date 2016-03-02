package com.dragonfruitstudios.brokenbonez.Input;

public class Acceleration {
    static float accel;

    public Acceleration(){
            accel = 0.0f;
    }
    public static void setAccel(float newAccel){
        accel = newAccel;
    }
    public static float getAccel(){
        return accel;
    }
}
