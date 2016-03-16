package com.dragonfruitstudios.brokenbonez.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
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
 *
 *  To use this view, create a new instance of it and then use `setContentView` in an Activity.
 *  You will then need to set the GameView's callbacks using the `setCallbacks` method. The
 *  callbacks are called whenever the view wants to be drawn or when its size changes.
 *
 *  When wanting to draw on the GameView, call one of the many public drawing methods defined here.
 */
public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();

    private boolean ready; // Determines whether the Canvas is ready to be drawn on.
    private Canvas canvas;
    private Paint paint; // Contains drawing options.
    private Camera camera; // The camera which can be used for drawing.
    private boolean cameraEnabled;

    /**
     * Stores two GameView callbacks.
     */
    public interface GVCallbacks {
        /**
         * Called when the GameView wants to be drawn.
         */
        void performDraw(GameView gameView);

        /**
         * Called when the GameView's size changed.
         */
        void onSizeChanged(GameView gameView, int w, int h, int oldw, int oldh);
    }

    private GVCallbacks callbacks;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Prepare the canvas.
        ready = true;
        this.canvas = canvas;
        // Call user's callback to perform drawing.
        callbacks.performDraw(this);
        // Dispose canvas.
        ready = false;
        this.canvas = null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Ignore this event when it reports that width and height are 0.
        if (w == 0 || h == 0) {
            Log.d(TAG, "Size changed to 0, skipping event.");
            return;
        }
        Log.i(TAG, String.format("Size changed. W: %s, H: %s, oldW: %s, oldH: %s",
                w, h, oldw, oldh));
        callbacks.onSizeChanged(this, w, h, oldw, oldh);

        // Update the GameView camera's size.
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

    /**
     * Assigns the specified camera to this GameView.
     */
    public void setCamera(Camera camera) {
        if (this.cameraEnabled) {
            throw new RuntimeException("Disable the current camera before changing it.");
        }
        this.camera = camera;
        this.cameraEnabled = false;
    }

    /**
     * Gets the GameView's Camera.
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Activates the GameView's camera. This causes all draws to be translated by a certain amount
     * of pixels specified by the camera.
     */
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

    /**
     * Deactivates the GameView's camera. All calls after a call to this won't be translated.
     */
    public void disableCamera() {
        if (!this.cameraEnabled) {
            throw new RuntimeException("Camera has not been enabled.");
        }
        this.camera.disable(this);
        this.cameraEnabled = false;
    }

    /**
     * A little method to ensure that the Canvas is ready.
     */
    private void checkCanvas() {
        if (!ready) {
            throw new IllegalArgumentException("Cannot draw right now, Canvas not ready.");
        }
    }

    /**
     * Clear the GameView with the specified color.
     */
    public void clear(int color) {
        checkCanvas();
        paint.setColor(color);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);
    }

    /**
     * Draw the specified text at the specified x,y coords with the specified color.
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
     * Draws the specified text with the specified text size.
     */
    public void drawText(String text, float x, float y, int color, int textSize) {
        checkCanvas();
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, x, y, paint);
    }

    /**
     * Draws the specified text in with a center text alignment.
     */
    public void drawTextCenter(String text, float x, float y, int color, int textSize) {
        paint.setTextAlign(Paint.Align.CENTER);
        drawText(text, x, y, color, textSize);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    /**
     * Draws the specified text in with a center text alignment and specified rotation.
     */
    public void drawTextCenter(String text, float x, float y, int color, int textSize,
                               float rotation) {
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.save();
        canvas.rotate((float) Math.toDegrees(rotation), x, y);
        drawText(text, x, y, color, textSize);
        canvas.restore();
        paint.setTextAlign(Paint.Align.LEFT);
    }


    /**
     * Draws a rectangle at the specified coordinates and with the specified color.
     * The rectangle is drawn with its contents filled.
     */
    public void drawRect(float left, float top, float right, float bottom, int color) {
        checkCanvas();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, paint);
        paint.reset();
    }

    /**
     * Draws a filled circle with a center at position (cx, cy) and radius specified.
     */
    public void drawCircle(float cx, float cy, float radius, int color) {
        checkCanvas();

        paint.setColor(color);
        canvas.drawCircle(cx, cy, radius, paint);
    }

    /**
     * Draws a circle with the specified paint style.
     */
    public void drawCircle(float cx, float cy, float radius, int color, Paint.Style style) {
        checkCanvas();

        paint.setStyle(style);
        drawCircle(cx, cy, radius, color);
        paint.reset();
    }

    /**
     * Draws a line between two vectors with the specified color.
     */
    public void drawLine(VectorF start, VectorF finish, int color) {
        checkCanvas();
        paint.setColor(color);
        canvas.drawLine(start.getX(), start.getY(), finish.getX(), finish.getY(), paint);
    }

    /**
     * Determines the relative location that an object will be drawn.
     */
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
        canvas.restore();
    }

    /**
     * Draws a section of the image at the specified `dest` rectangle.
     * @param image The image to draw.
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

    /**
     * Draws the specified Polygon with its inside filled with the specified image.
     */
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