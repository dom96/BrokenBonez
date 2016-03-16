package com.dragonfruitstudios.brokenbonez.Game.Levels;

import com.dragonfruitstudios.brokenbonez.Game.GameView;

/**
 *
 */
public class Layer {
    public String path; // Relative to level image dir.
    public float yPos; // The vertical position to place this layer at, out of 768.
    public float scrollFactor; // Factor used to scroll this layer when the bike moves.
    public GameView.ImageOrigin origin;

    /**
     * Creates a new scrolling layer for a LevelInfo.
     * @param path The path to the image to draw for the layer (this should be repeatable).
     * @param yPos The vertical position to place this layer at, out of 768.
     * @param scrollFactor The factor to scroll this layer at, between 0 and 1.
     * @param origin How to align the image.
     */
    public Layer(String path, float yPos, float scrollFactor, GameView.ImageOrigin origin) {
        this.path = path;
        this.yPos = yPos;
        this.scrollFactor = scrollFactor;
        this.origin = origin;
    }
}