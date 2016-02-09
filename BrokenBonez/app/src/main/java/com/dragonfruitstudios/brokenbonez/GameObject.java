package com.dragonfruitstudios.brokenbonez;

/**
 * An interface defining a class that receives events from the GameLoop.
 *
 * This interface extends the Drawable interface which defines a `draw` method.
 *
 * Primarily used for objects within the game. For example: the bike, or a collectible item
 * (powerup). These objects can be drawn, need to be updated over the course of the game, and
 * need to be notificed when the size of the GameView is changed.
 */
public interface GameObject extends Drawable {
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
