package com.dragonfruitstudios.brokenbonez.Game;

/**
 * Defines a class which can be drawn on a GameView.
 *
 * Primarily used for objects which can be drawn for debugging purposes, like bounding boxes for
 * collision detection.
 */
public interface Drawable {
    /**
     * The method which will be called whenever the class should be redrawn.
     * @param view The view on which the GameObject should be drawn on.
     */
    void draw(GameView view);

}
