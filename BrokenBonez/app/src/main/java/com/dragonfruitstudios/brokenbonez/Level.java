package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.BoundingShapes.Circle;
import com.dragonfruitstudios.brokenbonez.BoundingShapes.Rect;

import java.util.ArrayList;

// Currently just a simple class to draw the level.
// TODO: Load level design from file.
// TODO: Scroll the level based on camera position
public class Level implements Drawable {
    VectorF startPoint; // Holds the coordinates which determine where the bike starts.
    ArrayList<Rect> groundRectangles;

    public Level() {
        startPoint = new VectorF(0, 0); // Just a reasonable default.
        groundRectangles = new ArrayList<Rect>();
        // TODO: Hardcoded for now.
        groundRectangles.add(new Rect(0, calcGroundHeight(), 3000, calcGroundHeight()+50));

        groundRectangles.add(new Rect(10, 200, 500, 260));
    }

    public void updateSize(int w, int h) {
        Log.d("UpdateSize", "Updating size in Level: " + w + " " + h);
        startPoint = calcStartPoint(w, h);
    }

    public void draw(GameView gameView) {
        float currHeight = calcGroundHeight();
        // Draw the sky
        gameView.drawRect(0, 0, gameView.getWidth(), currHeight,
                Color.parseColor("#06A1D3"));

        // Draw debug info.
        String debugInfo = String.format("Level[grndY: %.1f, colY: %.1f, totalY: %d]",
                currHeight, groundRectangles.get(0).getTop(), gameView.getHeight());
        gameView.drawText(debugInfo, 100, 30, Color.WHITE);

        // Draw the grass.
        gameView.drawRect(0, currHeight, gameView.getWidth(),
                currHeight + 20, Color.parseColor("#069418"));
        currHeight += 20;
        // Draw the ground.
        gameView.drawRect(0, currHeight, gameView.getWidth(),
                gameView.getHeight(), Color.parseColor("#976600"));

        // More debug info drawing.
        for (Rect r : groundRectangles) {
            r.draw(gameView);
        }

    }

    public void update(float lastUpdate) {
        // TODO
    }

    /**
     * Returns the Bike's starting point.
     */
    public VectorF getStartPoint() {
        Log.d("StartPoint", startPoint.toString());
        return startPoint;
    }

    /**
     * Calculates the bike's starting point based on the screen width and height.
     *
     * @param w The width of the screen.
     * @param h The height of the screen.
     */
    private VectorF calcStartPoint(int w, int h) {
        return new VectorF(5, 40);
    }

    private float calcGroundHeight() {
        return 410.0f; // TODO
    }

    // <editor-fold desc="Collision detection">

    /**
     * Returns the nearest solid object that the VectorF could collide with (or is currently
     * colliding with).
     */
    public Rect getNearestSolid(VectorF point) {
        // TODO: Make this more efficient.
        // Go through each collision rectangle and check if it's the closest rectangle.
        float closest = -1;
        Rect result = new Rect(-1, -1, -1, -1);
        for (Rect r : groundRectangles) {
            float temp = r.distanceSquared(point);
            if (closest > temp || closest == -1) {
                closest = temp;
                result = r;
            }
        }
        if (closest == -1) {
            throw new RuntimeException("Could not get nearest solid.");
        }
        return result;
    }

    public boolean intersectsGround(Circle c) {
        // TODO: Make this more efficient.
        for (Rect r : groundRectangles) {
            if (c.collidesWith(r)) {
                return true;
            }
        }
        return false;
    }

    // </editor-fold>

}
