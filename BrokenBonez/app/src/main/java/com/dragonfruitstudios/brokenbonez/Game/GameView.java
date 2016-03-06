package com.dragonfruitstudios.brokenbonez.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.dragonfruitstudios.brokenbonez.GameLoop;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.concurrent.locks.ReentrantLock;

/**
 *  This class implements a View which supports drawing. Currently implemented as a custom View,
 *  but the API has been designed to support other backends.
 *  It may for example utilise a GlSurfaceView backend in the future depending on performance.
 */
public class GameView extends TextureView implements TextureView.SurfaceTextureListener {
    boolean ready;
    boolean locked;
    Canvas canvas;
    Surface surface;

    Thread gameLoopThread;

    Paint paint;
    Camera camera;
    boolean cameraEnabled;

    public interface GVCallbacks {
        void performDraw(GameView gameView);

        void onSizeChanged(GameView gameView, int w, int h, int oldw, int oldh);

        void onSurfaceAvailable(GameView gameView);

        void onSurfaceDestroyed(GameView gameView);
    }

    GVCallbacks callbacks;

    /*@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //ready = true;
        //this.canvas = canvas;
        //callbacks.performDraw(this);
        //ready = false;
        //this.canvas = null;
        synchronized (canvasBitmap) {
            canvas.drawBitmap(canvasBitmap, 0, 0, null);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }*/

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.surface = new Surface(surface);

        ready = true;
        callbacks.onSurfaceAvailable(this);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int w, int h) {
        if (w == 0 || h == 0) {
            Log.d("GameView", "Size changed to 0, skipping event.");
            return;
        }
        Log.w("GameView", String.format("Size changed. W: %s, H: %s, oldW: %s, oldH: %s",
                w, h, 0, 0));
        callbacks.onSizeChanged(this, w, h, 0, 0);

        // Update camera size.
        camera.updateSize(w, h);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.w("GameView", "onSurfaceTextureDestroyed");

        ready = false;

        callbacks.onSurfaceDestroyed(this);

        surface.release();
        surface = null;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public GameView(Context context) {
        super(context);
        paint = new Paint();

        // Set a default camera.
        camera = new Camera(0, 0);

        setSurfaceTextureListener(this);
    }

    public void setCallbacks(GVCallbacks drawingFunction) {
        this.callbacks = drawingFunction;
    }

    /**
     * Sets the translation vector which will be applied to every draw call.
     *
     * To reset you can simply call `translate(0, 0)`.
     * @param x Amount to translate on x-axis.
     * @param y Amount to translate on y-axis.
     */
    public void translate(float x, float y) {
        checkCanvas();
        canvas.translate(x, y);
    }

    public void setCamera(Camera camera) {
        if (this.cameraEnabled) {
            throw new RuntimeException("Disable the current camera before changing it.");
        }
        this.camera = camera;
        this.cameraEnabled = false;
    }

    public Camera getCamera() {
        return camera;
    }

    public void enableCamera() {
        if (this.camera == null) {
            throw new RuntimeException("You need to set a camera first.");
        }
        if (this.cameraEnabled) {
            throw new RuntimeException("Camera has already been enabled.");
        }
        this.camera.enable(this);
        this.cameraEnabled = true;
    }

    public void disableCamera() {
        if (!this.cameraEnabled) {
            throw new RuntimeException("Camera has not been enabled.");
        }
        this.camera.disable(this);
        this.cameraEnabled = false;
    }

    public void captureCanvas() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canvas = surface.lockHardwareCanvas();
        }
        else {
            canvas = surface.lockCanvas(null);
        }
        locked = true;
    }

    public void releaseCanvas() {
        surface.unlockCanvasAndPost(canvas);
        canvas = null;
        locked = false;
    }

    private void checkCanvas() {
        if (!locked) {
            throw new IllegalArgumentException("Cannot draw right now, Canvas not captured.");
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

    public void drawText(String text, float x, float y, int color, int textSize) {
        checkCanvas();
        paint.setColor(color);
        paint.setTextSize(textSize);
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
        TopLeft, Middle, BottomLeft,
        MiddleLeft
    }


    /**
     * Draws an image at the specified position. The origin determines how the `pos` is
     * interpreted.
     */
    public void drawImage(Bitmap image, VectorF pos, float rotation, ImageOrigin origin) {
        checkCanvas();
        canvas.save();
        VectorF transformedPos = pos.copy();
        //Log.d("Image", image.getWidth() + "");
        switch (origin) {
            case Middle:
                transformedPos.sub(new VectorF(image.getWidth() / 2, image.getHeight() / 2));
                break;
            case MiddleLeft:
                transformedPos.sub(new VectorF(0, image.getHeight()/2));
                break;
            case BottomLeft:
                transformedPos.sub(new VectorF(0, image.getHeight()));
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

    public void drawImage(Bitmap image, float x, float y, float rotation, ImageOrigin origin) {
        checkCanvas();
        canvas.save();

        canvas.rotate((float) Math.toDegrees(rotation), x + (image.getWidth() / 2),
                y + (image.getHeight() / 2));
        canvas.drawBitmap(image, x, y, paint);
        //paint.setColor(Color.parseColor("#7d0aa9"));
        //canvas.drawLine(transformedPos.getX(), transformedPos.getY(), transformedPos.getX(), transformedPos.getY() + 30, paint);
        //canvas.drawLine(pos.getX(), pos.getY(), pos.getX(), pos.getY() + 30, paint);
        canvas.restore();
    }
}