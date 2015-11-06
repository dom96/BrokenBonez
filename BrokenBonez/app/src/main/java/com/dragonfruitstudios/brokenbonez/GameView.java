package com.dragonfruitstudios.brokenbonez;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 *  This class implements a View which supports drawing. Currently implemented as a SurfaceView,
 *  but the API has been designed to support other backends.
 *  It may for example utilise a GlSurfaceView backend in the future depending on performance.
 *
 *  The `lockCanvas` method must be called before any draw method is called. After the drawing ends
 *  you should call the complementary `unlockCanvas` method.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder holder; // Underlying holder for this SurfaceView

    Canvas lockedCanvas; // Currently locked canvas to perform drawing on.
    boolean ready; // Determines whether the SurfaceView has been created.

    int width, height; // The width and height of this SurfaceView.

    public GameView(Context context, int width, int height) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);
        holder.setFixedSize(width, height);
        this.width = width;
        this.height = height;
    }

    public void lockCanvas() {
        if (lockedCanvas != null) {
            // The canvas is currently locked, cannot lock again.
            throw new RuntimeException("Locking of canvas failed: canvas already locked.");
        }
        lockedCanvas = holder.lockCanvas();
        if (lockedCanvas == null) {
            // This happens if the surface has not been created yet.
            // http://developer.android.com/reference/android/view/SurfaceHolder.html#lockCanvas%28%29
            throw new RuntimeException("Locking of canvas failed: lockCanvas returned null.");
        }
    }

    public void unlockCanvas() {
        holder.unlockCanvasAndPost(lockedCanvas);
        lockedCanvas = null;
    }

    private void checkCanvas() {
        if (lockedCanvas == null) {
            throw new RuntimeException("Canvas has not been locked.");
        }
    }

    /**
     * Clear the GameView with the specified color.
     * @param color
     */
    public void clear(int color) {
        checkCanvas();
        Paint p = new Paint();
        p.setColor(color);
        lockedCanvas.drawRect(0, 0, width, height, p);
    }

    /**
     * Draw the specified text at the specified x,y coords with the specified color.
     * @param text
     * @param x
     * @param y
     * @param color
     */
    public void drawText(String text, float x, float y, int color) {
        checkCanvas();
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(15);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        lockedCanvas.drawText(text, x, y, paint);
    }

    public void drawRect(float left, float top, float right, float bottom, int color) {
        checkCanvas();
        Paint paint = new Paint();
        paint.setColor(color);

        lockedCanvas.drawRect(left, top, right, bottom, paint);
    }

    /**
     * Determines whether this GameView can be drawn to.
     */
    public boolean isReady() {
        return ready;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ready = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        ready = false;
    }


}
