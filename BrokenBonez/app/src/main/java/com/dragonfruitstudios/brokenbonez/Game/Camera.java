package com.dragonfruitstudios.brokenbonez.Game;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class Camera {
    private VectorF pos; // The world position of this camera.
    private float width;
    private float height;

    public Camera(float width, float height) {
        pos = new VectorF(0, 0);
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
        return pos;
    }

}
