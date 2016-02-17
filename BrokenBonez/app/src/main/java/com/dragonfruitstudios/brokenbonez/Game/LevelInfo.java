package com.dragonfruitstudios.brokenbonez.Game;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;

import java.util.ArrayList;

/**
 * This class specifies information about a specific level.
 */
public class LevelInfo {
    public String name;
    public ArrayList<Layer> layers;

    public static class Layer {
        public String path; // Relative to level image dir.
        public float yPos; // Resolution independent factors used to place this layer along the vertical.
        public float scrollFactor; // Factor used to scroll this layer when the bike moves.
        public float yMargin; // How much to move this layer down after its
                              // position has been calculated based on the `yFactor`.
        public GameView.ImageOrigin origin;

        public Layer(String path, float yPos, float scrollFactor, float yMargin, GameView.ImageOrigin origin) {
            this.path = path;
            this.yPos = yPos;
            this.scrollFactor = scrollFactor;
            this.yMargin = yMargin;
            this.origin = origin;
        }
    }

    /**
     * This layer is extended by the color specified if the image is too small. Used for the
     * sky layer.
     */
    public static class ColorLayer extends Layer {
        public float colorHeight;
        public int colorTop; // Color specifying the color to put above the image.
        public int colorBottom; // Color specifying the color to put below the image.

        public ColorLayer(String path, float yPos, float scrollFactor, float yMargin,
                          GameView.ImageOrigin origin, float colorHeight,
                          String colorTop, String colorBottom) {
            super(path, yPos, scrollFactor, yMargin, origin);
            this.colorHeight = colorHeight;
            this.colorTop = Color.parseColor(colorTop);
            this.colorBottom = Color.parseColor(colorBottom);
        }

        public ColorLayer(String path, float yPos, float scrollFactor, float yMargin,
                          GameView.ImageOrigin origin, float colorHeight,
                          int colorTop, int colorBottom) {
            super(path, yPos, scrollFactor, yMargin, origin);
            this.colorTop = colorTop;
            this.colorBottom = colorBottom;
        }
    }

    public LevelInfo(String name) {
        this.name = name;
        this.layers = new ArrayList<Layer>();
    }

    public void loadAssets(AssetLoader loader) {
        for (Layer l : layers) {
            loader.AddAssets(new String[] {"levels/" + name + "/" + l.path});
        }
    }

    public String getLayerKey(Layer layer) {
        return "levels/" + name + "/" + layer.path;
    }

    public float calcGroundHeight(AssetLoader loader, int height) {
        // For now we assume that the last layer is the ground.
        Layer groundLayer = layers.get(layers.size()-1);
        Bitmap groundBitmap = loader.getBitmapByName(getLayerKey(groundLayer));
        switch (groundLayer.origin) {
            case BottomLeft:
                return height*groundLayer.yPos - groundBitmap.getHeight();
            default:
                return 410;
                //throw new RuntimeException("Not implemented TODO");
        }
    }


}
