package com.dragonfruitstudios.brokenbonez.Game.Levels;

import android.graphics.Color;

import com.dragonfruitstudios.brokenbonez.Game.GameView;

/**
 * This layer is extended by the color specified if the image is too small. Used for the
 * sky layer.
 */
public class ColorLayer extends Layer {
    public float colorHeight; // How much of the colorTop/Bottom to draw above/below image.
    public int colorTop; // Color specifying the color to put above the image.
    public int colorBottom; // Color specifying the color to put below the image.

    public ColorLayer(String path, float yPos, float scrollFactor,
                      GameView.ImageOrigin origin, float colorHeight,
                      String colorTop, String colorBottom) {
        super(path, yPos, scrollFactor, origin);
        this.colorHeight = colorHeight;
        this.colorTop = Color.parseColor(colorTop);
        this.colorBottom = Color.parseColor(colorBottom);
    }

    public ColorLayer(String path, float yPos, float scrollFactor,
                      GameView.ImageOrigin origin, float colorHeight,
                      int colorTop, int colorBottom) {
        super(path, yPos, scrollFactor, origin);
        this.colorHeight = colorHeight;
        this.colorTop = colorTop;
        this.colorBottom = colorBottom;
    }
}