package com.dragonfruitstudios.brokenbonez;


/**
 * A interface defining a class that is a visible object within the game.
 */
public interface GameObject {
    /**
     * The method which will be called whenever the class should be redrawn.
     * @param view The view on which the GameObject should be drawn on.
     */
    void draw(GameView view);

    /**
     * The method which will be called whenever the class should be updated.
     *
     * You may for example change the position of a sprite drawn on the screen.
     * @param lastUpdate The amount of milliseconds since `update` was last called.
     */
    void update(float lastUpdate);

    /**
     * The method which will be called whenever the screen size has updated.
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    void updateSize(int width, int height);
}
