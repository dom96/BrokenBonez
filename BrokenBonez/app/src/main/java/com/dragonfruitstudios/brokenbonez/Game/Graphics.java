package com.dragonfruitstudios.brokenbonez.Game;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class Graphics {
    /**
     * Finds every pixel with the specified `toReplace` color and replaces it with the
     * specified `replaceWith` color.
     */
    public static void replaceColor(Bitmap img, int toReplace, int replaceWith) {
        // Use `getPixels` to grab all the pixels in the image. This allows efficient editing of
        // each individual pixel.

        int[] pixels = new int[img.getWidth()*img.getHeight()];
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == toReplace) {
                pixels[i] = replaceWith;
            }
        }

        img.setPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
    }

    /**
     * Draws the specified Bitmap, but only draws portion specified by `width`. If the Bitmap
     * is smaller than `width` then it is repeated, if it is bigger then it is cropped.
     */
    public static void drawRepeated(GameView view, Bitmap img, VectorF pos, int width,
                                    float rotation) {
        int drawnWidth = 0;
        while (drawnWidth < width) {
            int minWidth = Math.min(width-drawnWidth, img.getWidth());
            RectF dest = new RectF(pos.x + drawnWidth, pos.y,
                    pos.x + drawnWidth + minWidth, pos.y + img.getHeight());
            view.drawImage(img, new Rect(0, 0, minWidth, img.getHeight()), dest, rotation);
            drawnWidth += minWidth;
        }
    }

}
