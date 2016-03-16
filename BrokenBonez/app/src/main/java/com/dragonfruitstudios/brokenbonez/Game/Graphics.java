package com.dragonfruitstudios.brokenbonez.Game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Line;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Polygon;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class Graphics {
    // Determines whether to draw debug information on the screen.
    // Change to true to see some interesting overlays!
    public static final boolean drawDebugInfo = false;

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
     * Turns the image black and white.
     */
    public static void ghostify(Bitmap img) {
        int[] pixels = new int[img.getWidth()*img.getHeight()];
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        for (int i = 0; i < pixels.length; i++) {
            float avg = Color.red(pixels[i]) + Color.green(pixels[i]) + Color.blue(pixels[i]);
            avg /= 3;
            pixels[i] = Color.argb(Color.alpha(pixels[i]), (int)avg, (int)avg, (int)avg);
        }

        img.setPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
    }

    /**
     * Draws the specified Bitmap, but only draws portion specified by `width`. If the Bitmap
     * is smaller than `width` then it is repeated, if it is bigger then it is cropped.
     *
     * The `offset` parameter specifies at which point in the image the drawing should start.
     * For convenience the `offset` will wrap around.
     */
    public static void drawRepeated(GameView view, Bitmap img, int offset, VectorF pos,
                                    float width, float rotation) {
        int drawnWidth = 0;
        int wrappedOffset = offset;
        // Wrap the offset around if it's larger than image width.
        if (wrappedOffset >= img.getWidth()) {
            wrappedOffset = img.getWidth() - wrappedOffset;
        }
        // Keep drawing until the specified width is drawn.
        while (drawnWidth < Math.floor(width)) {
            // Calculate the maximum width that can be drawn
            int maxWidth = Math.min((int)Math.floor(width-drawnWidth), img.getWidth()-wrappedOffset);
            RectF dest = new RectF(pos.x + drawnWidth, pos.y,
                    pos.x + drawnWidth + maxWidth, pos.y + img.getHeight());
            view.drawImage(img, new Rect(wrappedOffset, 0, wrappedOffset+maxWidth, img.getHeight()),
                    dest, rotation);
            // Increment `drawnWidth` by the width that was drawn.
            drawnWidth += maxWidth;
        }
    }

    /**
     * Returns the device's screen width.
     *
     * This is a convenience method, using GameView.getWidth might be more reliable.
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * Returns the device's screen height.
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    // <editor-fold desc="Methods for scaling to screen resolution">

    /**
     * Assuming an original resolution of 1280x768, this method will scale `x` to fit the
     * specified screen width `w`.
     */
    public static float scaleX(float x, int w) {
        return (x / 1280) * w;
    }

    /**
     * Assuming an original resolution of 1280x768, this method will scale `y` to fit the
     * specified screen width `h`.
     */
    public static float scaleY(float y, int h) {
        return (y / 768) * h;
    }

    public static void scalePos(VectorF pos, int w, int h) {
        pos.div(new VectorF(1280, 768));
        pos.mult(new VectorF(w, h));
    }

    /**
     * Scales the specified line to the current phone's resolution.
     */
    public static void scaleLine(Line line, int w, int h) {
        // The levels have been designed for a 768x1280 screen.
        scalePos(line.getStart(), w, h);
        scalePos(line.getFinish(), w, h);
    }

    /**
     * Scales the specified polygon to the current phone's resolution. Assuming the Polygon has
     * been created for a resolution of 768x1280.
     */
    public static void scalePolygon(Polygon polygon, int w, int h) {
        for (Line l : polygon.getLines()) {
            scaleLine(l, w, h);
        }
        polygon.recalculateBounds();
    }

    // </editor-fold>

}
