package com.dragonfruitstudios.brokenbonez.Input;

import android.graphics.PointF;
import android.view.MotionEvent;

/** Touch-Handler**/
public class TouchHandler {
    static float accel;
    public enum ControlIsActive {ACTION_GAS_DOWN, ACTION_GAS_UP, ACTION_BRAKE_DOWN, ACTION_BRAKE_UP, ACTION_NONE}
    static ControlIsActive cIA = ControlIsActive.ACTION_NONE;
    static boolean gasIsDown;
    static boolean gasIsUp;
    static boolean brakeIsDown;
    static boolean brakeIsUp;

    public TouchHandler() {
        accel = 0.0f;
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
                        setAccel(-0.5f);
                    }
                }
                if(f.x > midPoint && f.x < midPoint + quarterMidPoint * 4){
                    gasIsDown = true;
                    if(f.x > midPoint && f.x < midPoint + quarterMidPoint / 2) {
                        cIA = ControlIsActive.ACTION_GAS_DOWN;
                        setAccel(0.1f);
                    }
                    if(f.x > midPoint + quarterMidPoint / 2 && f.x < midPoint + quarterMidPoint) {
                        cIA = ControlIsActive.ACTION_GAS_DOWN;
                        setAccel(0.2f);
                    }
                    if(f.x > midPoint + quarterMidPoint && f.x < midPoint + quarterMidPoint + quarterMidPoint / 2) {
                        cIA = ControlIsActive.ACTION_GAS_DOWN;
                        setAccel(0.3f);
                    }
                    if(f.x > midPoint + quarterMidPoint + quarterMidPoint / 2 && f.x < midPoint + quarterMidPoint + quarterMidPoint) {
                        cIA = ControlIsActive.ACTION_GAS_DOWN;
                        setAccel(0.4f);
                    }
                    if(f.x >midPoint + quarterMidPoint + quarterMidPoint && f.x < midPoint + quarterMidPoint + quarterMidPoint + quarterMidPoint / 2) {
                        cIA = ControlIsActive.ACTION_GAS_DOWN;
                        setAccel(0.5f);
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
                        setAccel(0.0f);
                    }
                }
                if(f.x > midPoint){
                    gasIsUp = true;
                    if(gasIsUp = true) {
                        brakeIsUp = false;
                        cIA = ControlIsActive.ACTION_GAS_UP;
                        setAccel(0.0f);
                    }
                }
                break;
            }
            default:
                cIA = ControlIsActive.ACTION_NONE;
                setAccel(0.0f);
                break;
        }
        return cIA;
    }
    public static void setAccel(float newAccel){
        accel = newAccel;
    }
    public static float getAccel(){
        return accel;
    }
}