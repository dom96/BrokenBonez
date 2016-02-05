package com.dragonfruitstudios.brokenbonez;


/**
 * A interface defining a class which can be drawn on a GameView.
 */
public interface Drawable {
    /**
     * The method which will be called whenever the class should be redrawn.
     * @param view The view on which the Drawable should be drawn on.
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
