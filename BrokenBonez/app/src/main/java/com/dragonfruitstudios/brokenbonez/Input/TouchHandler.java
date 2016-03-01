package com.dragonfruitstudios.brokenbonez.Input;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

/** Touch-Handler**/
public class TouchHandler {
    public enum ControlIsActive {ACTION_GAS_SLOWEST, ACTION_GAS_SLOW, ACTION_GAS_AVG, ACTION_GAS_FAST, ACTION_GAS_FASTEST, ACTION_BRAKE_DOWN, ACTION_BRAKE_UP, ACTION_GAS_UP, ACTION_NONE}
    static ControlIsActive cIA = ControlIsActive.ACTION_NONE;
    static boolean gasIsDown;
    static boolean gasIsUp;
    static boolean brakeIsDown;
    static boolean brakeIsUp;

    public TouchHandler() {

    }

    public static ControlIsActive determineAction(MotionEvent event, float midPoint) {
        float quarterMidPoint = midPoint / 2;
        int pointerIndex = event.getActionIndex();
        int maskedAction = event.getActionMasked();
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE: {
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                if(f.x < midPoint) {
                    brakeIsDown = true;

                    if(brakeIsDown = true) {
                        gasIsDown = false;
                        cIA = ControlIsActive.ACTION_BRAKE_DOWN;
                    }
                }
                if(f.x > midPoint && f.x < midPoint + quarterMidPoint * 4){
                    gasIsDown = true;
                    if(f.x > midPoint && f.x < midPoint + quarterMidPoint / 2) {
                        cIA = ControlIsActive.ACTION_GAS_SLOWEST;
                    }
                    if(f.x > midPoint + quarterMidPoint / 2 && f.x < midPoint + quarterMidPoint) {
                        cIA = ControlIsActive.ACTION_GAS_SLOW;
                    }
                    if(f.x > midPoint + quarterMidPoint && f.x < midPoint + quarterMidPoint + quarterMidPoint / 2) {
                        cIA = ControlIsActive.ACTION_GAS_AVG;
                    }
                    if(f.x > midPoint + quarterMidPoint + quarterMidPoint / 2 && f.x < midPoint + quarterMidPoint + quarterMidPoint) {
                        cIA = ControlIsActive.ACTION_GAS_FAST;
                    }
                    if(f.x >midPoint + quarterMidPoint + quarterMidPoint && f.x < midPoint + quarterMidPoint + quarterMidPoint + quarterMidPoint / 2) {
                        cIA = ControlIsActive.ACTION_GAS_FASTEST;
                    }
                    if(gasIsDown = true) {
                        brakeIsDown = false;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                if(f.x < midPoint){
                    brakeIsUp = true;
                    if(brakeIsUp = true) {
                        gasIsUp = false;
                        cIA = ControlIsActive.ACTION_BRAKE_UP;
                    }
                }
                if(f.x > midPoint){
                    gasIsUp = true;
                    if(gasIsUp = true) {
                        brakeIsUp = false;
                        cIA = ControlIsActive.ACTION_GAS_UP;
                    }
                }
                break;
            }
            default:
                cIA = ControlIsActive.ACTION_NONE;
                break;
        }
        return cIA;
    }
}