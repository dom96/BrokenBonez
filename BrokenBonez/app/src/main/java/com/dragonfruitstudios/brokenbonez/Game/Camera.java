package com.dragonfruitstudios.brokenbonez.Game;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

/**
 * Implements a simple 2D camera. It is used for drawing specific sections of a level or scene.
 * It provides some simple methods for controlling the camera like `centerHorizontally`.
 */
public class Camera {
    private VectorF pos; // The world position of this camera.
    private float width;
    private float height;

    public Camera(float width, float height) {
        pos = new VectorF(0, 0);
        this.width = width;
        this.height = height;
    }

    /**
     * Center the camera horizontally on the specified x coordinate.
     */
    public void centerHorizontally(float x) {
        pos.set(x - (width/2), 0);
    }

    /**
     * Scrolls the camera vertically.
     */
    public void scrollY(float y) {
        pos.add(new VectorF(0, y));
    }

    /**
     * Activate this camera on the specified view.
     *
     * Note: You should use GameView.enableCamera instead.
     */
    public void enable(GameView view) {
        view.translate(-pos.x, -pos.y);
    }

    /**
     * Deactivate this camera on the specified view.
     *
     * Note: You should use GameView.disableCamera instead.
     */
    public void disable(GameView view) {
        view.translate(pos.x, pos.y);
    }

    /**
     * Used to update the cameras width and height.
     */
    public void updateSize(int w, int h) {
        width = w;
        height = h;
    }

    /**
     * Returns the Camera's position.
     */
    public VectorF getPos() {
        return pos;
    }

}
