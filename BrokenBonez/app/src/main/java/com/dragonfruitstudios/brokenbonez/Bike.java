package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.Vector;

public class Bike {
    PointF pos; // The x,y coords of the bottom left of the bike.
    PointF startPos;

    Vector velocity;

    public Bike(PointF startPos) {
        // TODO: Can't do `new PointF(startPos)`?
        pos = new PointF(startPos.x, startPos.y);
        Log.d("Bike", "Start pos: " + pos.toString());
        this.startPos = new PointF(startPos.x, startPos.y);

        // TODO: For testing.
        velocity = new Vector(5, 0, Math.PI / 2);
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
        PointF resolved = velocity.resolve();
        pos.x += resolved.x;
        pos.y += resolved.y;

        if (pos.x >= 380) {
            velocity = velocity.toReverse();
        }

        if (pos.x <= startPos.x) {
            velocity = velocity.toReverse();
        }
    }
}
