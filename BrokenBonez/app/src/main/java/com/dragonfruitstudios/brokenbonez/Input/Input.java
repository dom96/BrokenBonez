package com.dragonfruitstudios.brokenbonez.Input;

import android.view.KeyEvent;

import java.util.List;

/**
 * Created by rmsfan on 16/11/2015.
 */
public interface Input   {
    public static class KeyEvent{
        public static final int KEY_DOWN=0;
        public static final int KEY_UP=1;

        public int type;
        public int keyCode;
        public char keyChar;
    }

    public static class TouchEvent{
        public static final int TOUCH_DOWN=0;
        public static final int TOUCH_UP=1;
        public static final int TOUCH_DRAGGED=2;

        public int type;
        public int x,y; //relative to component's origin
        public int pointer;
    }

    public boolean isKeyPressed(int keyCode); //takes a keyCode and returns whether the corresponding key is currently
    //pressed or not.
    public boolean isTouchDown(int pointer); //return whether a given pointer is down, as well as its current x and y
    // coordinates. Note that the coordinates will be undefined if the corresponding pointer
    // is not actually touching the screen.

    public int getTouchX(int pointer);
    public int getTouchY(int pointer);

    //return the respective acceleration values of each accelerometer axis:
    public float getAccelY();
    public float getAccelX();
    public float getAccelZ();

    // used for event based handling:
    // return the KeyEvent and TouchEvent instances that got recorded since the last time we called these methods.
    public List<KeyEvent> getKeyEvents();
    public List<TouchEvent> getTouchEvents();
}
