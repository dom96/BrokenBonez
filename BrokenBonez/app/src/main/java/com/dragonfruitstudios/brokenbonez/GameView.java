package com.dragonfruitstudios.brokenbonez;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


/**
 *  This class implements a View which supports drawing. Currently implemented as a SurfaceView,
 *  but the API has been designed to support other backends.
 *  It may for example utilise a GlSurfaceView backend in the future depending on performance.
 *
 *  The `lockCanvas` method must be called before any draw method is called. After the drawing ends
 *  you should call the complementary `unlockCanvas` method.
 */
public class GameView extends View {
    boolean ready;
    Canvas canvas;

    public interface GVCallbacks {
        void performDraw(GameView gameView);
        void onSizeChanged(GameView gameView, int w, int h, int oldw, int oldh);
    }

    GVCallbacks callbacks;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ready = true;
        this.canvas = canvas;
        callbacks.performDraw(this);
        ready = false;
        this.canvas = null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("GameView", String.format("Size changed. W: %s, H: %s, oldW: %s, oldH: %s",
                w, h, oldw, oldh));
        callbacks.onSizeChanged(this, w, h, oldw, oldh);

    }

    public GameView(Context context) {
        super(context);
    }

    public void setCallbacks(GVCallbacks drawingFunction) {
        this.callbacks = drawingFunction;
    }

    private void checkCanvas() {
        if (!ready) {
            throw new IllegalArgumentException("Cannot draw right now, Canvas not ready.");
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
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p);
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
        canvas.drawText(text, x, y, paint);
    }

    public void drawRect(float left, float top, float right, float bottom, int color) {
        checkCanvas();

        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(left, top, right, bottom, paint);
    }




}
