package com.dragonfruitstudios.brokenbonez.Input;

import android.app.Activity;
import android.graphics.PointF;
import android.util.SparseArray;
import android.view.MotionEvent;

/** Touch-Handler**/

public class TouchHandler {
    enum ControlIsActive {ACTION_BRAKE_DOWN, ACTION_BRAKE_UP, ACTION_GAS_DOWN, ACTION_GAS_UP}
    static ControlIsActive cIA = null;
    static boolean TouchIsDown;
    static boolean TouchIsUp;

    public TouchHandler() {


    }

    public static ControlIsActive OnTouchDown(Activity activity, MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        SparseArray<PointF> mActivePointers;
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                mActivePointers = new SparseArray<PointF>();
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                TouchIsDown = true;
                if(TouchIsDown = true & f.x < f.x / 2){
                    cIA = ControlIsActive.ACTION_BRAKE_DOWN;
                }
                if(TouchIsDown = true & f.x > f.x /2){
                    cIA = ControlIsActive.ACTION_GAS_DOWN;
                }
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers = new SparseArray<PointF>();
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                TouchIsUp = false;
                if(TouchIsUp = false & f.x < f.x / 2){
                    cIA = ControlIsActive.ACTION_BRAKE_UP;
                }
                if(TouchIsUp = false & f.x > f.x /2){
                    cIA = ControlIsActive.ACTION_GAS_UP;
                }
            }
        }
        return cIA;
    }
}