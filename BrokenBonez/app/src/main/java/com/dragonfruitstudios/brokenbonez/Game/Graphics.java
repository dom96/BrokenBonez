package com.dragonfruitstudios.brokenbonez.Game;

import android.graphics.Bitmap;

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
}
