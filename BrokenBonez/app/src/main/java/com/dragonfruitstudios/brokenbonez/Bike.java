package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

public class Bike {
    PointF pos; // The x,y coords of the bottom left of the bike.
    PointF startPos;

    boolean back;
    public Bike(PointF startPos) {
        // TODO: Can't do `new PointF(startPos)`?
        pos = new PointF(startPos.x, startPos.y);
        this.startPos = new PointF(startPos.x, startPos.y);
    }

    public void draw(GameView gameView) {
        gameView.drawRect(pos.x, pos.y - 30, pos.x + 60, pos.y, Color.parseColor("#87000B"));
    }

    public void update() {
        if (pos.x >= 380) {
            back = true;
        }

        if (pos.x <= startPos.x) {
            back = false;
        }

        if (back)
        {
            pos.x -= 2;
        }
        else {
            pos.x += 2;
        }


    }

}
