package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Game.Level;
import com.dragonfruitstudios.brokenbonez.Game.LevelInfo;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Line;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Currently just a simple class to draw the level.
// TODO: Load level design from file.
// TODO: Scroll the level based on camera position
public class GameLevel extends Level {
    GameState gameState; // Used to grab assets, and physics simulator.

    VectorF startPoint; // Holds the coordinates which determine where the bike starts.

    LevelInfo info; // Holds information about the current level.

    VectorF bikePos;

    boolean layersScaled = false;
    boolean bitmapsScaled = false;
    HashMap<String,Bitmap> scaledBitmaps;

    public GameLevel(GameState state) {
        gameState = state;

        startPoint = new VectorF(0, 0); // Just a reasonable default.
        bikePos = new VectorF(0, 0);

        // Create a default level info object (TODO: This should be loaded from LevelInfo text file).
        info = new LevelInfo("level1", "surface.png", "ground.png");
        // Parameters to constructors:
        // Y position (based on 768 high screen), scroll factor, image origin
        // (For ColorLayer):
        //     color height, color top, color bottom.
        info.layers.add(new LevelInfo.ColorLayer("sky.png", 154f, 0.01f,
                GameView.ImageOrigin.MiddleLeft, 100f, "#1e3973", "#466ab9"));
        info.layers.add(new LevelInfo.Layer("buildings1.png", 454f, 0.09f,
                GameView.ImageOrigin.BottomLeft));
        info.layers.add(new LevelInfo.ColorLayer("buildings2.png", 479f, 0.15f,
                GameView.ImageOrigin.BottomLeft, 20f, Color.TRANSPARENT, Color.BLACK));
        info.layers.add(new LevelInfo.Layer("bushes.png", 518f, 0.8f,
                GameView.ImageOrigin.BottomLeft));
        //info.layers.add(new LevelInfo.Layer("ground.png", 768f, 1f,
        //        GameView.ImageOrigin.BottomLeft));

        // Load the SVG file which defines the level's geometry.
        info.loadSVG(state.getAssetLoader(), "level_flat.svg", new VectorF(-600, 0));

        // Initialise the SolidLayer class asset keys.
        info.addInfo("plain", info.getSurfaceKey(), info.getImagePath("ground_base.png"),
                new VectorF(0, 0));
        info.addInfo("little_ramp", info.getTransparentKey(), info.getImagePath("little_ramp.png"),
                new VectorF(0, 0));
        info.addInfo("building1", info.getTransparentKey(), info.getImagePath("building1.png"),
                new VectorF(0, 0));
        info.addInfo("building2", info.getTransparentKey(), info.getImagePath("building2.png"),
                new VectorF(0, 0));

        // Load bitmaps defined in LevelInfo.
        scaledBitmaps = info.loadAssets(state.getAssetLoader());

        Simulator physicsSimulator = gameState.getPhysicsSimulator();
        for (LevelInfo.SolidLayer sl : info.solids) {
            physicsSimulator.addStaticShape(sl);
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

        if (!layersScaled) {
            // Scale each SolidLayer's coordinates to the current phone's resolution.
            for (LevelInfo.SolidLayer sl : info.solids) {
                Graphics.scalePolygon(sl, w, h);
            }
            layersScaled = true;
        }

        // Prepare scaled bitmaps for the specified screen width/height.
        if (!bitmapsScaled) {
            // Create scaled versions of each of the bitmaps stored in the HashMap.
            for (Map.Entry<String, Bitmap> item : scaledBitmaps.entrySet()) {
                Bitmap img = Bitmap.createScaledBitmap(item.getValue(),
                        (int)Graphics.scaleX(item.getValue().getWidth(), w),
                        (int)Graphics.scaleY(item.getValue().getHeight(), h), false);
                scaledBitmaps.put(item.getKey(), img);
            }
            bitmapsScaled = true;
        }
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
            Bitmap img = scaledBitmaps.get(info.getLayerKey(l));

            if (l instanceof LevelInfo.ColorLayer) {
                LevelInfo.ColorLayer cLayer = ((LevelInfo.ColorLayer) l);

                // Draw the image
                pos.add(0, Graphics.scaleY(l.yPos, gameView.getHeight()));
                drawScrolled(gameView, img, l.scrollFactor, pos, l.origin);

                float imgTop = 0;
                float imgBottom = 0;
                float scaledColorHeight = Graphics.scaleY(cLayer.colorHeight, gameView.getHeight());
                switch (l.origin) {
                    case MiddleLeft:
                    case Middle:
                        imgTop = pos.y - (img.getHeight()/2);
                        imgBottom = pos.y + (img.getHeight()/2);
                        gameView.drawRect(0, imgTop - scaledColorHeight,
                                gameView.getWidth(), imgTop, cLayer.colorTop);
                        gameView.drawRect(0, imgBottom,
                                gameView.getWidth(), imgBottom + scaledColorHeight,
                                cLayer.colorBottom);
                        break;
                    case TopLeft:
                        break;
                    case BottomLeft:
                        imgTop = pos.y - (img.getHeight());
                        imgBottom = pos.y;
                        gameView.drawRect(0, imgTop - scaledColorHeight,
                                gameView.getWidth(), imgTop, cLayer.colorTop);
                        gameView.drawRect(0, imgBottom,
                                gameView.getWidth(), imgBottom + scaledColorHeight,
                                cLayer.colorBottom);
                        break;
                }
            }
            else {
                pos.add(0, Graphics.scaleY(l.yPos, gameView.getHeight()));
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

    /**
     * Calculates the angle to draw gap between solid layers.
     */
    private float calcFillAngle(LevelInfo.SolidLayer sl, int index) {
        ArrayList<Line> lines = sl.getLines();
        Line currentLine = lines.get(index);
        float currentAngle = currentLine.calcRotation();
        if (index+1 < lines.size()) {
            Line nextLine = lines.get(index+1);
            float nextAngle = nextLine.calcRotation();
            // Calculate the midpoint between the two angles.
            float midpoint = (currentAngle - nextAngle) / 2;
            return currentAngle - midpoint;
        }
        else {
            return 0;
        }
    }

    /**
     * Calculates the difference in angle between the current solid layer and the next.
     * Can be used to determine whether there is a gap between the two layers.
     */
    private float calcAngleDiff(LevelInfo.SolidLayer sl, int index) {
        ArrayList<Line> lines = sl.getLines();
        Line currentLine = lines.get(index);
        float currentAngle = currentLine.calcRotation();
        if (index+1 < lines.size()) {
            Line nextLine = lines.get(index + 1);
            float nextAngle = nextLine.calcRotation();
            return currentAngle-nextAngle;
        }
        else {
            return 0;
        }
    }

    private void drawSolidLayer(LevelInfo.SolidLayer sl, GameView gameView) {
        // Draw the SolidLayer's fill image.
        String fillKey = info.getSolidLayerKey(sl, LevelInfo.AssetType.Fill);
        if (!fillKey.equals(info.getTransparentKey())) {
            Bitmap fillImage = scaledBitmaps.get(fillKey);
            if (sl.usesFillPolygon()) {
                gameView.fillPolygon(fillImage, sl);
            }
            else {
                // TODO: Rotation.
                Rect src = new Rect(0, 0, fillImage.getWidth(), fillImage.getHeight());
                gameView.drawImage(fillImage, src, sl.getRect(), 0);
            }
        }

        int slOffsetX = 0;
        // Draw the image beneath each line.
        for (int i = 0; i < sl.getLines().size(); i++) {
            LevelInfo.SolidLayer.Info classInfo = info.getSolidLayerInfo(sl);
            LevelInfo.AssetType assetType = sl.getAssetType(i);
            String assetKey = info.getSolidLayerKey(sl, assetType);
            // Don't draw if key is transparent.
            if (!assetKey.equals(info.getTransparentKey())) {
                Bitmap asset = scaledBitmaps.get(assetKey);
                Line line = sl.getLines().get(i).copy();

                if (assetType == LevelInfo.AssetType.Surface) {
                    // Scale the surface offset depending on the screen resolution.
                    VectorF surfaceOffsetY = classInfo.surfaceOffset.copy();
                    Graphics.scalePos(surfaceOffsetY, gameView.getWidth(), gameView.getHeight());
                    // Translate the SolidLayer Line by the surface offset.
                    line.getStart().add(surfaceOffsetY);
                    line.getFinish().add(surfaceOffsetY);
                }

                // Determine whether a gap needs to be filled between solid surface layers.
                float angleDiff = calcAngleDiff(sl, i);
                if (angleDiff > 0) {
                    // Calculate the angle to draw the fill layer at.
                    float fillAngle = calcFillAngle(sl, i);
                    // Calculate the width of the fill layer to draw.
                    float fillWidth = 40*angleDiff;
                    VectorF pos = line.getFinish().copy();
                    // Move the fill layer closer to the current solid layer.
                    pos.multAdd(new VectorF(fillAngle), -fillWidth/2);
                    // TODO: Change the fill layer's height a bit?
                    Graphics.drawRepeated(gameView, asset, slOffsetX,
                            pos, fillWidth, fillAngle);
                    slOffsetX += fillWidth;
                }

                // Use the length of the line as the solid layer's width.
                int width = (int)line.getSize().x;
                // Draw the solid layer.
                Graphics.drawRepeated(gameView, asset, slOffsetX, line.getStart(), width,
                        line.calcRotation());
                slOffsetX += width;
            }
        }
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
