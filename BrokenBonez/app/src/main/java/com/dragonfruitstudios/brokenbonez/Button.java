package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.view.MotionEvent;

public class Button {

    public Button() {

    }

    public void draw(GameView gameView) {
        gameView.drawRect(30, 200, 200, 260, Color.parseColor("#000000"));
    }

    public void onTouchEvent(MotionEvent event) {

            }
}
