package com.dragonfruitstudios.brokenbonez.Input;

import android.content.Context;
import android.os.Vibrator;

/** Vibration.Vibrate(1000, gameSceneManager.gameView.getContext()); **/
public class Vibration {

    public Vibration(){

    }

    public static void Vibrate(float strength, Context context){
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate((long) strength);
    }
}