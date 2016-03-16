package com.dragonfruitstudios.brokenbonez.Game.Levels;

import com.dragonfruitstudios.brokenbonez.Game.GameView;

public class Layer {
    public String path; // Relative to level image dir.
    public float yPos; // Resolution independent factors used to place this layer along the vertical.
    public float scrollFactor; // Factor used to scroll this layer when the bike moves.
    public GameView.ImageOrigin origin;

    public Layer(String path, float yPos, float scrollFactor, GameView.ImageOrigin origin) {
        this.path = path;
        this.yPos = yPos;
        this.scrollFactor = scrollFactor;
        this.origin = origin;
    }
}