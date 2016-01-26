package com.dragonfruitstudios.brokenbonez.Input;
import android.view.MotionEvent;

/**Handles touch, moving touch and multi-touch.
 * Could add some way to target the specific points on screen i.e. a certain area triggers a certain event.
 * UNCOMMENT @Override on use.
 */
public class TouchHandler {
    //@Override
    public boolean onTouchEvent(MotionEvent event){
        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();
        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);
        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        // specify methods in relation to motion events
        switch (maskedAction) {
            /**ACTION_POINTER_DOWN used for multi-touch support.**/
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                //Do something
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //Do something
                break;
            }
            /**ACTION_POINTER_UP used for multi-touch support.
               ACTION_CANCEL used if something else takes control of the touch event.**/
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                //Do something
                break;
            }
        }
        return true;
    }
}