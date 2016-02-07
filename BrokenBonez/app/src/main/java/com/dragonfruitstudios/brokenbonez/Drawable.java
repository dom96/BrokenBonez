package com.dragonfruitstudios.brokenbonez;

/**
 * Defines a class which can be drawn on a GameView.
 */
public interface Drawable {
    /**
     * The method which will be called whenever the class should be redrawn.
     * @param view The view on which the GameObject should be drawn on.
     */
    void draw(GameView view);

}
