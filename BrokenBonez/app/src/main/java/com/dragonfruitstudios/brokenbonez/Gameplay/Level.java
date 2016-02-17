package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.LevelInfo;
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
public class Level {
    GameState gameState; // Used to grab assets, and physics simulator.

    VectorF startPoint; // Holds the coordinates which determine where the bike starts.

    LevelInfo info; // Holds information about the current level.

    VectorF bikePos;

    public Level(GameState state) {
        gameState = state;

        startPoint = new VectorF(0, 0); // Just a reasonable default.
        bikePos = new VectorF(0, 0);

        // Create a default level info object (TODO: This should be loaded from LevelInfo text file).
        info = new LevelInfo("level1");
        info.layers.add(new LevelInfo.ColorLayer("sky.png", 0.2f, 0.01f, 0f,
                GameView.ImageOrigin.MiddleLeft, 40f, "#1e3973", "#466ab9"));
        info.layers.add(new LevelInfo.Layer("buildings1.png", 0.5f, 0.09f, 0f,
                GameView.ImageOrigin.BottomLeft));
        info.layers.add(new LevelInfo.ColorLayer("buildings2.png", 0.5f, 0.15f, 25f,
                GameView.ImageOrigin.BottomLeft, 20f, Color.TRANSPARENT, Color.BLACK));
        info.layers.add(new LevelInfo.Layer("bushes.png", 1f, 0.8f, -250f,
                GameView.ImageOrigin.BottomLeft));
        info.layers.add(new LevelInfo.Layer("ground.png", 1f, 1f, 0f,
                GameView.ImageOrigin.BottomLeft));

        // Load bitmaps defined in LevelInfo.
        info.loadAssets(state.getAssetLoader());

        Simulator physicsSimulator = gameState.getPhysicsSimulator();
        // TODO: Hardcoded for now.
        for (int i = 0; i < 20; i++) {
            Rect rect = new Rect(new VectorF(i*3000, info.calcGroundHeight(getAssetLoader(), 720)+i*10), 3000, 50);
            physicsSimulator.createStaticBody(rect);
        }
        // TODO: Change 2 to 1000 for a perf test, fix the slowdown.
        for (int i = 0; i < 2; i++) {
            Rect rect = new Rect(new VectorF(20*3000, info.calcGroundHeight(getAssetLoader(), 720)- 200), 200, 700);
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

    private void drawScrolled(GameView view, Bitmap img, float scrollFactor,
                              VectorF pos, GameView.ImageOrigin origin) {
        float gameWidth = view.getWidth();
        // Calculate the amount of images that need to
        // be drawn in order for them to fill the screen.
        int imgCount = (int)Math.ceil(gameWidth / img.getWidth());
        // Get the world position of the left side of the screen.
        float screenLeft = view.getCamera().getPos().x;
        // Calculate at which image index we need to start drawing at (the others
        // are off screen).
        int startAt = (int)Math.floor(screenLeft*scrollFactor / img.getWidth());
        for (int i = startAt; i <= imgCount+startAt; i++) {
            // Draw the image at the position given by `i` multiplied by `img.getWidth()`.
            VectorF start = new VectorF(i*img.getWidth(), 0);
            view.drawImage(img, pos.added(start), 0, origin);
        }
    }

    public void draw(GameView gameView) {
        AssetLoader assetLoader = gameState.getAssetLoader();
        float currHeight = info.calcGroundHeight(assetLoader, gameView.getHeight());
        // Draw the different layers.
        for (LevelInfo.Layer l : info.layers) {
            VectorF pos = bikePos.copy();
            pos.mult(new VectorF(-l.scrollFactor, 0));
            Bitmap img = assetLoader.getBitmapByName(info.getLayerKey(l));
            if (l instanceof LevelInfo.ColorLayer) {
                LevelInfo.ColorLayer cLayer = ((LevelInfo.ColorLayer) l);

                // Draw the image
                pos.add(0, gameView.getHeight() * l.yPos + l.yMargin);
                drawScrolled(gameView, img, l.scrollFactor, pos, l.origin);

                float imgTop = 0;
                float imgBottom = 0;
                switch (l.origin) {
                    case MiddleLeft:
                    case Middle:
                        imgTop = pos.y - (img.getHeight()/2);
                        imgBottom = pos.y + (img.getHeight()/2);
                        gameView.drawRect(0, imgTop - cLayer.colorHeight,
                                gameView.getWidth(), imgTop, cLayer.colorTop);
                        gameView.drawRect(0, imgBottom,
                                gameView.getWidth(), imgBottom + cLayer.colorHeight, cLayer.colorBottom);
                        break;
                    case TopLeft:
                        break;
                    case BottomLeft:
                        imgTop = pos.y - (img.getHeight());
                        imgBottom = pos.y;
                        gameView.drawRect(0, imgTop - cLayer.colorHeight,
                                gameView.getWidth(), imgTop, cLayer.colorTop);
                        gameView.drawRect(0, imgBottom,
                                gameView.getWidth(), imgBottom + cLayer.colorHeight, cLayer.colorBottom);
                        break;
                }
            }
            else {
                pos.add(0, gameView.getHeight() * l.yPos + l.yMargin);
                drawScrolled(gameView, img, l.scrollFactor, pos, l.origin);
            }
        }

        // Draw debug info.
        String debugInfo = String.format("Level[grndY: %.1f, totalY: %d]",
                currHeight, gameView.getHeight());
        gameView.drawText(debugInfo, 100, 30, Color.WHITE);

    }

    public void update(float lastUpdate, VectorF bikePos) {
        this.bikePos = bikePos;
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
}
