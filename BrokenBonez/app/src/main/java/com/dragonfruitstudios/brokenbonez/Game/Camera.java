package com.dragonfruitstudios.brokenbonez.Game;

import android.util.Log;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class Camera {
    private VectorF pos; // The world position of this camera.
    private VectorF target;
    private float width;
    private float height;

    public Camera(float width, float height) {
        pos = new VectorF(0, 0);
        target = new VectorF(0, 0);
        this.width = width;
        this.height = height;
    }

    public void centerHorizontally(float x) {
        pos.set(x - (width/2), 0);
    }

    public void enable(GameView view) {
        view.translate(-pos.x, -pos.y);
    }

    public void disable(GameView view) {
        view.translate(pos.x, pos.y);
    }

    public void updateSize(int w, int h) {
        width = w;
        height = h;
    }

    public VectorF getPos() {
        return target;
    }

    public void setPos(VectorF pos) {
        this.target = pos;
    }

    private float threshold = 0.5f;
    private float fraction = 0.7f;
    public void update(float lastUpdate) {
        if (Math.abs(target.x - pos.x) > threshold) {
            pos.setX((target.x - pos.x) * fraction);
        }
    }
}
