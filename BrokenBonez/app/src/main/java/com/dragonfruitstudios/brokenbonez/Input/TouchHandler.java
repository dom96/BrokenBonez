package com.dragonfruitstudios.brokenbonez.Input;

import android.app.Activity;
import android.graphics.PointF;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;

/** Single & Multi-Touch input Handler.
 *
 *  Initialise the TouchHandler by passing in the activity and two target int counters
 *
 *  E.G.
 *  TouchHandler touchHandler = new TouchHandler(this, 222, 222);
 *
 *  Then create a boolean method with a MotionEvent argument passed in.
 *  And then execute methods from the API on your object within the method using your MotionEvent argument.
 *  Don't forget to add onTouchOff as this will track when you take your finger off the screen and a silent
 *  error will occur in the background if it isn't present.
 *
 *  E.G.
 *  public boolean onTouchEvent(MotionEvent event){
 *  touchHandler.onTouchDown(event);
 *  touchHandler.onTouchOff(event);
 *  }
 *
 *  TODO:
 *  Multi-touch within this class does work, it has something to do with the action
 *  pointer down & the action down. I will work on this as soon as I can feel free to
 *  experiment with this code as there is still much more which can be added for greater overall
 *  functionality. Check out this page for more info:
 *  https://developer.android.com/reference/android/view/MotionEvent.html
 *
 * **/

public class TouchHandler {
    float counterX;
    float counterY;
    public TouchHandler(Activity activity, int counterX, int counterY) {
        this.counterX = counterX;
        this.counterY = counterY;

    }

    public boolean onTouchDown(MotionEvent event) {
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
                Log.d("FINGER TOUCHED SCREEN", "A finger has touched the screen!");
                if (f.x < counterX && f.y > counterY) {
                    Log.d("FINGER TOUCHED SCREEN", "FINGER TOUCHED SCREEN ON A SPECIFIC AREA");
                }
                break;
            }
        }
        return true;
    }

    public boolean onMove(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        SparseArray<PointF> mActivePointers;
        switch (maskedAction) {
            case MotionEvent.ACTION_MOVE: {
                mActivePointers = new SparseArray<PointF>();
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                Log.d("FINGER IS MOVING", "A finger is moving on the screen!");
                if (f.x < counterX && f.y > counterY) {
                    Log.d("FINGER MOVED", "FINGER MOVED ON A SPECIFIC AREA");
                }
                break;
            }
        }
        return true;
    }

    public boolean onTouchOff(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        SparseArray<PointF> mActivePointers;
        switch (maskedAction) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers = new SparseArray<PointF>();
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);
                Log.d("FINGER WENT OFF", "A finger has went off the screen!");
                if (f.x < counterX && f.y > counterY) {
                    Log.d("FINGER WENT OFF", "FINGER WENT OFF A SPECIFIC AREA");
                }
                break;
            }
        }
        return true;
    }
}