package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Circle;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Intersector;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Manifold;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Triangle;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;

// Currently just a simple class to draw the level.
// TODO: Load level design from file.
// TODO: Scroll the level based on camera position
public class Level implements GameObject {
    GameState gameState; // Used to grab assets, and physics simulator.

    VectorF startPoint; // Holds the coordinates which determine where the bike starts.

    public Level(GameState state) {
        gameState = state;

        startPoint = new VectorF(0, 0); // Just a reasonable default.

        Simulator physicsSimulator = gameState.getPhysicsSimulator();
        // TODO: Hardcoded for now.
        for (int i = 0; i < 20; i++) {
            Rect rect = new Rect(new VectorF(i*3000, calcGroundHeight()+i*10), 3000, 50);
            physicsSimulator.createStaticBody(rect);
        }
        // TODO: Change 2 to 1000 for a perf test, fix the slowdown.
        for (int i = 0; i < 2; i++) {
            Rect rect = new Rect(new VectorF(20*3000, calcGroundHeight()- 200), 200, 700);
            physicsSimulator.createStaticBody(rect);
        }

        physicsSimulator.createStaticBody(new Rect(new VectorF(10, 200), 190, 60));

        // Add a triangle
        Triangle triangle = new Triangle(new VectorF(200, 190), 300, 110);
        Triangle triangle2 = new Triangle(new VectorF(900, 150), -400, 150);
        physicsSimulator.createStaticBody(new Rect(new VectorF(900, 140), 200, 260));

        physicsSimulator.createStaticBody(triangle);
        physicsSimulator.createStaticBody(triangle2);
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
        String debugInfo = String.format("Level[grndY: %.1f, totalY: %d]",
                currHeight, gameView.getHeight());
        gameView.drawText(debugInfo, 100, 30, Color.WHITE);

        // Draw the grass.
        gameView.drawRect(0, currHeight, gameView.getWidth(),
                currHeight + 20, Color.parseColor("#069418"));
        currHeight += 20;
        // Draw the ground.
        gameView.drawRect(0, currHeight, gameView.getWidth(),
                gameView.getHeight(), Color.parseColor("#976600"));
    }

    public void update(float lastUpdate) {
        // TODO
    }

    public AssetLoader getAssetLoader() {
        return gameState.getAssetLoader();
    }

    public Simulator getPhysicsSimulator() {
        return gameState.getPhysicsSimulator();
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

}
