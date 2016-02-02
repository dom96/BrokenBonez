package com.dragonfruitstudios.brokenbonez.Input;

import android.graphics.PointF;
import android.view.MotionEvent;

/** Touch-Handler**/
public class TouchHandler {
    public enum ControlIsActive {ACTION_BRAKE_DOWN, ACTION_BRAKE_UP, ACTION_GAS_DOWN, ACTION_GAS_UP, ACTION_NONE}
    static ControlIsActive cIA = ControlIsActive.ACTION_NONE;
    static boolean gasIsDown;
    static boolean gasIsUp;
    static boolean brakeIsDown;
    static boolean brakeIsUp;

    public TouchHandler() {

    }

    public static ControlIsActive determineAction(MotionEvent event, float midPoint) {
        int pointerIndex = event.getActionIndex();
        int maskedAction = event.getActionMasked();
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
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
                if(f.x > midPoint){
                    gasIsDown = true;
                    if(gasIsDown = true) {
                        brakeIsDown = false;
                        cIA = ControlIsActive.ACTION_GAS_DOWN;
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