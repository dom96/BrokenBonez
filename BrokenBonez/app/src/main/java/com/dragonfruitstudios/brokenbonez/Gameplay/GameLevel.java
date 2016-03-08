package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Game.Level;
import com.dragonfruitstudios.brokenbonez.Game.LevelInfo;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Circle;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Intersector;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Line;
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
public class GameLevel extends Level {
    GameState gameState; // Used to grab assets, and physics simulator.

    VectorF startPoint; // Holds the coordinates which determine where the bike starts.

    LevelInfo info; // Holds information about the current level.

    VectorF bikePos;

    public GameLevel(GameState state) {
        gameState = state;

        startPoint = new VectorF(0, 0); // Just a reasonable default.
        bikePos = new VectorF(0, 0);

        // Create a default level info object (TODO: This should be loaded from LevelInfo text file).
        info = new LevelInfo("level1", "surface.png", "ground.png");
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

        //info.solids.add(LevelInfo.SolidLayer.createRect(new VectorF(0, 410), 200, 1000,
        //        info.getSurfaceKey(), info.getTransparentKey(), info.getTransparentKey(),
        //        info.getTransparentKey(), info.getGroundKey()));

        info.loadSVG(state.getAssetLoader(), "level_dino.svg", new VectorF(0, 410));

        /*ArrayList<Line> lines = new ArrayList<Line>();
        ArrayList<String> keys = new ArrayList<String>();
        lines.add(new Line(200, 410, 215, 440));
        keys.add(info.getSurfaceKey());
        lines.add(new Line(215, 440, 250, 460));
        keys.add(info.getSurfaceKey());
        lines.add(new Line(250, 460, 295, 475));
        keys.add(info.getSurfaceKey());
        lines.add(new Line(295, 475, 300, 480));
        keys.add(info.getSurfaceKey());
        info.solids.add(LevelInfo.SolidLayer.createPolygon(lines, keys, info.getGroundKey()));*/

        // Load bitmaps defined in LevelInfo.
        info.loadAssets(state.getAssetLoader());

        Simulator physicsSimulator = gameState.getPhysicsSimulator();
        for (LevelInfo.SolidLayer sl : info.solids) {
            physicsSimulator.createStaticBody(sl);
        }

        /*
        // TODO: Hardcoded for now.
        // Define some test Polygons.
        for (int i = 0; i < 20; i++) {
            float height = info.calcGroundHeight(getAssetLoader(), 720);
            Rect rect = new Rect(new VectorF(i*2800, height), 2600, 50);
            physicsSimulator.createStaticBody(rect);
            Triangle triangle = new Triangle(new VectorF(i*2600, height-100), -200, 100);
            Triangle triangle2 = new Triangle(new VectorF(i*2800, height-100), 200, 100);
            physicsSimulator.createStaticBody(triangle);
            physicsSimulator.createStaticBody(triangle2);
        }
        // TODO: Change 2 to 1000 for a perf test, fix the slowdown.
        for (int i = 0; i < 2; i++) {
            Rect rect = new Rect(new VectorF(20*3000, info.calcGroundHeight(getAssetLoader(), 720)- 200), 200, 700);
            physicsSimulator.createStaticBody(rect);
        }
        */
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


        // Draw solid layers.
        gameView.enableCamera();
        for (LevelInfo.SolidLayer sl : info.solids) {
            drawSolidLayer(sl, gameView);
        }
        gameView.disableCamera();

        // Draw debug info.
        String debugInfo = String.format("Level[grndY: %.1f, totalY: %d]",
                currHeight, gameView.getHeight());
        gameView.drawText(debugInfo, 100, 30, Color.WHITE);

    }

    private void drawSolidLayer(LevelInfo.SolidLayer sl, GameView gameView) {
        for (int i = 0; i < sl.getLines().size(); i++) {
            String assetKey = sl.getAssetKey(i);
            // Don't draw if key is transparent.
            if (!assetKey.equals(info.getTransparentKey())) {
                Bitmap asset = getAssetLoader().getBitmapByName(assetKey);
                Line line = sl.getLines().get(i);
                Graphics.drawRepeated(gameView, asset, line.getStart(), (int)line.getSize().x,
                        line.calcRotation());
            }
        }
        //Log.w("GameLeve", "----");

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
