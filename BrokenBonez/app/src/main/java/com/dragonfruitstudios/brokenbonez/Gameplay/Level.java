package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Circle;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Intersector;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Manifold;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Polygon;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;

// Currently just a simple class to draw the level.
// TODO: Load level design from file.
// TODO: Scroll the level based on camera position
public class Level implements GameObject {
    GameState gameState; // Used to grab assets.

    VectorF startPoint; // Holds the coordinates which determine where the bike starts.
    ArrayList<Intersector> intersectors;

    public Level(GameState state) {
        gameState = state;

        startPoint = new VectorF(0, 0); // Just a reasonable default.
        intersectors = new ArrayList<Intersector>();
        // TODO: Hardcoded for now.
        intersectors.add(new Rect(0, calcGroundHeight(), 3000, calcGroundHeight() + 50));

        intersectors.add(new Rect(10, 200, 200, 260));

        // Add a triangle

        Polygon triangle = Polygon.createTriangle(new VectorF(200, 300), new VectorF(200, 190), new VectorF(500, 300));
        Polygon triangle2 = Polygon.createTriangle(new VectorF(900, 300), new VectorF(900, 150), new VectorF(500, 300));
        //intersectors.add(new Rect(500, 300, 700, 400));
        intersectors.add(new Rect(900, 140, 1100, 400));

        intersectors.add(triangle);
        intersectors.add(triangle2);
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
                currHeight, ((Rect)intersectors.get(0)).getTop(), gameView.getHeight());
        gameView.drawText(debugInfo, 100, 30, Color.WHITE);

        // Draw the grass.
        gameView.drawRect(0, currHeight, gameView.getWidth(),
                currHeight + 20, Color.parseColor("#069418"));
        currHeight += 20;
        // Draw the ground.
        gameView.drawRect(0, currHeight, gameView.getWidth(),
                gameView.getHeight(), Color.parseColor("#976600"));

        // More debug info drawing.
        for (Drawable r : intersectors) {
            r.draw(gameView);
        }

    }

    public void update(float lastUpdate) {
        // TODO
    }

    public AssetLoader getAssetLoader() {
        return gameState.getAssetLoader();
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
     * Returns the distance to the nearest solid object that the VectorF could
     * collide with (or is currently colliding with).
     */
    public float getNearestSolid(VectorF point) {
        // TODO: Make this more efficient.
        // Go through each collision rectangle and check if it's the closest rectangle.
        float closest = -1;
        for (Intersector r : intersectors) {
            float temp = r.distanceSquared(point);
            if (closest > temp || closest == -1) {
                closest = temp;
            }
        }
        if (closest == -1) {
            throw new RuntimeException("Could not get nearest solid.");
        }
        return (float)Math.sqrt(closest);
    }

    public boolean intersectsGround(Circle c) {
        // TODO: Make this more efficient.
        for (Intersector r : intersectors) {
            if (c.collidesWith(r)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Manifold> collisionTest(Circle c) {
        ArrayList<Manifold> result = new ArrayList<Manifold>();
        for (Intersector r : intersectors) {
            Manifold test = c.collisionTest(r);
            if (test.isCollided()) {
                result.add(test);
            }
        }
        return result;
    }

    // </editor-fold>

}
