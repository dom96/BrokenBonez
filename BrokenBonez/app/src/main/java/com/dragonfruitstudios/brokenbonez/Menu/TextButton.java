package com.dragonfruitstudios.brokenbonez.Menu;

import android.graphics.Color;
import android.graphics.PointF;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.Game.GameView;

public class TextButton {
    String buttonName;
    float left;
    float right;
    float bottom;
    float top;
    float x;
    float y;
    boolean isTouched;

    public TextButton(String buttonName, float left, float top, float right, float bottom, float x, float y) {
        this.buttonName = buttonName;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.x = x;
        this.y = y;
    }

    public void draw(GameView gameView) {
        gameView.drawRect(left, top, right, bottom, Color.parseColor("#999999"));
        gameView.drawText(buttonName, x, y, Color.BLACK);
    }

    public void onTouchEvent(MotionEvent event, int leftMargin, int rightMargin, int bottomMargin, int topMargin) {
        int pointerIndex = event.getActionIndex();
        int maskedAction = event.getActionMasked();
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                if (f.x < rightMargin) {
                    if (f.x > leftMargin) {
                        if (f.y > topMargin) {
                            if (f.y < bottomMargin) {
                                isTouched = true;
                            }
                        }
                    }
                }
                break;
            }
        }
    }

    public boolean isTouched(){
        return isTouched;
    }
}