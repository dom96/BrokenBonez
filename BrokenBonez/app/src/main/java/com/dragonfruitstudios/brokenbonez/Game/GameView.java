package com.dragonfruitstudios.brokenbonez.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

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
    Paint paint;

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
        paint = new Paint();
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
     *
     * @param color
     */
    public void clear(int color) {
        checkCanvas();
        paint.setColor(color);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
    }

    /**
     * Draw the specified text at the specified x,y coords with the specified color.
     *
     * @param text
     * @param x
     * @param y
     * @param color
     */
    public void drawText(String text, float x, float y, int color) {
        checkCanvas();
        paint.setColor(color);
        paint.setTextSize(15);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawText(text, x, y, paint);
    }

    /**
     * Draws a rectangle at the specified coordinates and with the specified color.
     * The rectangle is drawn with its contents filled.
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param color
     */

    public void drawRect(float left, float top, float right, float bottom, int color) {
        checkCanvas();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, paint);
        paint.reset();
    }

    public void drawRectFrame(float left, float top, float right, float bottom, int color) {
        // TODO: Just merge this with `drawRect`.
        checkCanvas();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left, top, right, bottom, paint);
        paint.reset();
    }

    public void drawCircle(float cx, float cy, float radius, int color) {
        checkCanvas();

        paint.setColor(color);
        canvas.drawCircle(cx, cy, radius, paint);
    }

    public void drawCircle(float cx, float cy, float radius, int color, Paint.Style style) {
        checkCanvas();

        paint.setStyle(style);
        drawCircle(cx, cy, radius, color);
        paint.reset();
    }

    public void drawCircleWithLine(float cx, float cy, float radius, int color, int lineColor) {
        drawCircle(cx, cy, radius, color);

        paint.setColor(lineColor);
        canvas.drawLine(cx, cy, cx + radius, cy, paint);
    }

    public void drawLine(VectorF start, VectorF finish, int color) {
        checkCanvas();
        paint.setColor(color);
        canvas.drawLine(start.getX(), start.getY(), finish.getX(), finish.getY(), paint);
    }

    public enum ImageOrigin {
        TopLeft, Middle
    }


    /**
     * Draws an image at the specified position. The origin determines how the `pos` is
     * interpreted.
     */
    public void drawImage(Bitmap image, VectorF pos, float rotation, ImageOrigin origin) {
        checkCanvas();
        canvas.save();
        VectorF transformedPos = pos.clone();
        //Log.d("Image", image.getWidth() + "");
        switch (origin) {
            case Middle:
                transformedPos.sub(new VectorF(image.getWidth() / 2, image.getHeight() / 2));
                break;
        }

        canvas.rotate((float) Math.toDegrees(rotation), transformedPos.x + (image.getWidth() / 2),
                transformedPos.y + (image.getHeight() / 2));
        canvas.drawBitmap(image, transformedPos.getX(), transformedPos.getY(), paint);
        //paint.setColor(Color.parseColor("#7d0aa9"));
        //canvas.drawLine(transformedPos.getX(), transformedPos.getY(), transformedPos.getX(), transformedPos.getY() + 30, paint);
        //canvas.drawLine(pos.getX(), pos.getY(), pos.getX(), pos.getY() + 30, paint);
        canvas.restore();
    }
}