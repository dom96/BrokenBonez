package com.dragonfruitstudios.brokenbonez;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder holder;

    Canvas lockedCanvas;
    boolean ready;

    public GameView(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);
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

    public void clear(int color) {
        checkCanvas();
        lockedCanvas.drawColor(color);
        lockedCanvas.drawRect(0, 0, 480, 320, new Paint());
    }

    public void drawText(String text, float x, float y, int color) {
        checkCanvas();
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(60);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        lockedCanvas.drawText(text, x, y, paint);
    }

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
