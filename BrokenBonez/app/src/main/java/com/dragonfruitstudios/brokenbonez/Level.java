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
public class Level {
    GameView gameView;

    PointF startPoint; // Holds the coordinates which determine where the bike starts.
    ArrayList<Rect> groundRectangles;

    public Level(GameView gameView) {
        this.gameView = gameView;
        startPoint = getStartPoint();
        groundRectangles = new ArrayList<Rect>();
        // TODO: Hardcoded for now.
        groundRectangles.add(new Rect(0, 410, 3000, 420));

        groundRectangles.add(new Rect(10, 200, 500, 260));
    }

    public void updateSize(int w, int h) {
        Log.d("UpdateSize", "Updating size in Level: " + w + " " + h);
        startPoint = getStartPoint(w, h);

        // Update collision boxes.
        // Currently the ground is drawn based on the start point's y coordinate.
        groundRectangles.get(0).setHorizontal(startPoint.y, startPoint.y + 40);
    }

    public void draw() {
        float currHeight = startPoint.y;
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

    public PointF getStartPoint() {
        return getStartPoint(gameView.getWidth(), gameView.getHeight());
    }

    /**
     * An overloaded `getStartPoint` which accepts a width and height.
     * @param w
     * @param h
     * @return
     */
    public PointF getStartPoint(int w, int h) {
        // Calculate the surface level.
        float surfaceLevel = h / 2 + 50;
        return new PointF(5, surfaceLevel);
    }

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
}
