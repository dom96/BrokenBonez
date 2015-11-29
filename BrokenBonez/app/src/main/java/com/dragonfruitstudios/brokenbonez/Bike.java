package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

public class Bike {
    PointF pos; // The x,y coords of the bottom left of the bike.
    PointF startPos;

    boolean accel;
    boolean brake;
    public Bike(PointF startPos) {
        // TODO: Can't do `new PointF(startPos)`?
        pos = new PointF(startPos.x, startPos.y);
        Log.d("Bike", "Start pos: " + pos.toString());
        this.startPos = new PointF(startPos.x, startPos.y);
    }

    public void draw(GameView gameView) {
        gameView.drawRect(pos.x, pos.y - 30, pos.x + 60, pos.y, Color.parseColor("#87000B"));
    }

    public void updateStartPos(PointF startPos) {
        // This is necessary because when the Bike is initialised the GameView may not have
        // initialised properly yet, and so its height has not been calculated yet. This causes
        // the start position to be incorrect.
        Log.d("Bike", "Updated start pos: " + pos.toString());
        pos = new PointF(startPos.x, startPos.y);
        this.startPos = new PointF(startPos.x, startPos.y);
    }

    public void update() {

    	if (brake) {
           pos.x -= 2;
        }

    	if (accel) {
           pos.x += 4;
        }

        if (pos.x >= 380) {
           brake = true;
           accel = false;
        }

        if (pos.x <= startPos.x) {
           accel = true;
           brake = false;
        }
    }
}
