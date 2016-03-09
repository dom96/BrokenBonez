package com.dragonfruitstudios.brokenbonez.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.util.Log;
import android.view.View;

import com.dragonfruitstudios.brokenbonez.Math.Collisions.Line;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Polygon;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.security.InvalidParameterException;

/**
 *  This class implements a View which supports drawing. Currently implemented as a custom View,
 *  but the API has been designed to support other backends.
 *  It may for example utilise a GlSurfaceView backend in the future depending on performance.
 */
public class GameView extends View {
    boolean ready;
    Canvas canvas;
    Paint paint;
    Camera camera;
    boolean cameraEnabled;

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
        if (w == 0 || h == 0) {
            Log.d("GameView", "Size changed to 0, skipping event.");
            return;
        }
        Log.d("GameView", String.format("Size changed. W: %s, H: %s, oldW: %s, oldH: %s",
                w, h, oldw, oldh));
        callbacks.onSizeChanged(this, w, h, oldw, oldh);

        // Update camera size.
        camera.updateSize(w, h);
    }

    public GameView(Context context) {
        super(context);
        paint = new Paint();

        // Set a default camera.
        camera = new Camera(0, 0);
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

    /**
     * Draws a section of the image at the specified `dest` rectangle.
     * @param image
     * @param src The section of the image to draw.
     * @param dest Where to draw the image.
     * @param rotation The rotation of the image about its top-left corner.
     */
    public void drawImage(Bitmap image, Rect src, RectF dest, float rotation) {
        checkCanvas();
        if (dest.top > dest.bottom || dest.left > dest.right || src.top > src.bottom ||
                src.left > src.right) {
            throw new InvalidParameterException("Invalid rectangle.");
        }
        canvas.save();
        canvas.rotate((float) Math.toDegrees(rotation), dest.left, dest.top);
        canvas.drawBitmap(image, src, dest, paint);
        canvas.restore();
    }

    public void fillPolygon(Bitmap image, Polygon polygon) {
        checkCanvas();
        // Code below adapted from: http://stackoverflow.com/a/3721521/492186
        // Customised it a great deal, pretty much only used it to learn how to draw an image filled
        // Polygon in Android.
        Path path = new Path();
        for (int i = 0; i < polygon.getLines().size(); i++) {
            Line line = polygon.getLines().get(i);
            if (i == 0) {
                path.moveTo(line.getStart().x, line.getStart().y);
                path.lineTo(line.getFinish().x, line.getFinish().y);
            }
            else {
                path.lineTo(line.getFinish().x, line.getFinish().y);
            }
        }
        path.close();

        PathShape shape = new PathShape(path, canvas.getWidth(), canvas.getHeight());
        BitmapShader bs = new BitmapShader(image, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        ShapeDrawable sd = new ShapeDrawable(shape);
        sd.getPaint().setShader(bs);
        sd.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

        sd.draw(canvas);

    }
}