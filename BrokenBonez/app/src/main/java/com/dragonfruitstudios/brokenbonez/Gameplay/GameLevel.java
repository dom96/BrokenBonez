package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Game.Levels.ColorLayer;
import com.dragonfruitstudios.brokenbonez.Game.Levels.Layer;
import com.dragonfruitstudios.brokenbonez.Game.Levels.Level;
import com.dragonfruitstudios.brokenbonez.Game.Levels.LevelInfo;
import com.dragonfruitstudios.brokenbonez.Game.Levels.SolidLayer;
import com.dragonfruitstudios.brokenbonez.Game.Levels.SolidObject;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;
import com.dragonfruitstudios.brokenbonez.LevelObjects.LevelBoost;
import com.dragonfruitstudios.brokenbonez.LevelObjects.LevelCoin;
import com.dragonfruitstudios.brokenbonez.LevelObjects.LevelObject;
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
    LevelInfo.LevelID currentLevelID;

    boolean layersScaled = false;
    boolean bitmapsScaled = false;
    HashMap<String,Bitmap> scaledBitmaps;

    ArrayList<LevelObject> levelObjects;

    public GameLevel(GameState state, LevelInfo.LevelID levelID) {
        gameState = state;

        startPoint = new VectorF(0, 0); // Just a reasonable default.
        bikePos = new VectorF(0, 0);
        layersScaled = false;
        bitmapsScaled = false;
        currentLevelID = levelID;
        String levelPath = LevelInfo.getLevelPath(levelID);

        // Create a default level info object (TODO: This should be loaded from LevelInfo text file).
        info = new LevelInfo(levelID, "surface.png", "ground.png", "finish.png");
        // Parameters to constructors:
        // Y position (based on 768 high screen), scroll factor, image origin
        // (For ColorLayer):
        //     color height, color top, color bottom.
        info.layers.add(new ColorLayer("sky.png", 154f, 0.01f,
                GameView.ImageOrigin.MiddleLeft, 100f, "#1e3973", "#466ab9"));
        info.layers.add(new Layer("buildings1.png", 454f, 0.09f,
                GameView.ImageOrigin.BottomLeft));
        info.layers.add(new ColorLayer("buildings2.png", 479f, 0.15f,
                GameView.ImageOrigin.BottomLeft, 20f, Color.TRANSPARENT, Color.BLACK));
        info.layers.add(new Layer("bushes.png", 518f, 0.8f,
                GameView.ImageOrigin.BottomLeft));

        // Load the SVG file which defines the level's geometry.
        info.loadSVG(gameState.getAssetLoader(), levelPath, new VectorF(-600, 0));

        // Initialise the SolidLayer class asset keys.
        info.addInfo("plain", info.getSurfaceKey(), info.getImagePath("ground_base.png"),
                new VectorF(0, 0));
        info.addInfo("little_ramp", info.getTransparentKey(), info.getImagePath("little_ramp.png"),
                new VectorF(0, 0));
        info.addInfo("building1", info.getTransparentKey(), info.getImagePath("building1.png"),
                new VectorF(0, 0));
        info.addInfo("building2", info.getTransparentKey(), info.getImagePath("building2.png"),
                new VectorF(0, 0));
        info.addInfo("little_ramp2", info.getTransparentKey(), info.getImagePath("little_ramp2.png"),
                new VectorF(0, 0));
        info.addInfo("bridge", info.getImagePath("bridge_rail.png"), info.getImagePath("bridge.png"),
                new VectorF(-10, -39), true);

        // Load bitmaps defined in LevelInfo.
        scaledBitmaps = info.loadAssets(gameState.getAssetLoader());

        Simulator physicsSimulator = gameState.getPhysicsSimulator();
        for (SolidLayer sl : info.solids) {
            physicsSimulator.addStaticShape(sl);
        }

        // Initialise the LevelObjects based on the ones specified in LevelInfo.
        levelObjects = new ArrayList<>();
        ArrayList<SolidObject> coins = info.findSolidObjects("coin");
        ArrayList<SolidObject> boosters = info.findSolidObjects("booster");
        for (SolidObject coin : coins) {
            VectorF pos = coin.pos.copy();
            Graphics.scalePos(pos, Graphics.getScreenWidth(), Graphics.getScreenHeight());
            levelObjects.add(new LevelCoin(getAssetLoader(), pos.x, pos.y, 0));
        }
        for (SolidObject booster : boosters) {
            VectorF pos = booster.pos.copy();
            Graphics.scalePos(pos, Graphics.getScreenWidth(), Graphics.getScreenHeight());
            levelObjects.add(new LevelBoost(getAssetLoader(), pos.x, pos.y, 0));
        }
    }

    public void updateSize(int w, int h) {
        Log.d("UpdateSize", "Updating size in Level: " + w + " " + h);
        startPoint = calcStartPoint(w, h);

        if (!layersScaled) {
            // Scale each SolidLayer's coordinates to the current phone's resolution.
            for (SolidLayer sl : info.solids) {
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
        // Draw the different layers.
        for (Layer l : info.layers) {
            VectorF pos = bikePos.copy();
            pos.mult(new VectorF(-l.scrollFactor, 0));
            Bitmap img = scaledBitmaps.get(info.getLayerKey(l));

            if (l instanceof ColorLayer) {
                ColorLayer cLayer = ((ColorLayer) l);

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


        gameView.enableCamera();
        // Draw solid layers.
        for (SolidLayer sl : info.solids) {
            drawSolidLayer(sl, gameView);
        }

        // Draw the finish line.
        // The finish line's class is "finish".
        drawFinishLine(info.getSolidObject("finish"), gameView);

        // Draw the level objects.
        for (LevelObject obj : levelObjects) {
            obj.draw(gameView);
        }

        gameView.disableCamera();

        if (Graphics.drawDebugInfo) {
            // Draw debug info.
            String debugInfo = String.format("Level[Name: %s]", currentLevelID);
            gameView.drawText(debugInfo, 100, 30, Color.WHITE);
        }
    }

    public void drawForeground(GameView view) {
        view.enableCamera();
        for (SolidLayer sl : info.solids) {
            drawSolidLayerForeground(sl, view);
        }
        view.disableCamera();
    }

    /**
     * Calculates the angle to draw gap between solid layers.
     */
    private float calcFillAngle(SolidLayer sl, int index) {
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
    private float calcAngleDiff(SolidLayer sl, int index) {
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

    private void drawSolidLayer(SolidLayer sl, GameView gameView) {
        // Draw the SolidLayer's fill image.
        String fillKey = sl.getAssetKey(LevelInfo.AssetType.Fill);
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
            SolidLayer.Info classInfo = info.getSolidLayerInfo(sl);
            LevelInfo.AssetType assetType = sl.getAssetType(i);
            String assetKey = sl.getAssetKey(assetType);
            // Don't draw if key is transparent and is to be drawn behind the bike.
            if (!assetKey.equals(info.getTransparentKey()) && !classInfo.surfaceInForeground) {
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

    /**
     * Draws a solid layer's surface in the foreground. This is primarily used for the drawing
     * of the bridge railings.
     */
    private void drawSolidLayerForeground(SolidLayer sl, GameView view) {
        // Grab the Info object for this specific SolidLayer.
        SolidLayer.Info classInfo = info.getSolidLayerInfo(sl);
        // Only draw when the surface key isn't transparent, and the SolidLayer wasn't filled
        // automatically by the SolidLayer parser.
        if (!classInfo.surfaceKey.equals(info.getTransparentKey()) && !sl.usesFillPolygon()) {
            // Grab the AssetLoader key for this SolidLayer's Surface.
            String surfaceKey = sl.getAssetKey(LevelInfo.AssetType.Surface);
            // Grab the image for this SolidLayer's surface key.
            Bitmap surfaceImage = scaledBitmaps.get(surfaceKey);

            // Create a source rectangle for this image.
            Rect src = new Rect(0, 0, surfaceImage.getWidth(), surfaceImage.getHeight());
            // Compute a rectangle to draw the image into.
            RectF dest = new RectF(sl.getRect());
            dest.offset(classInfo.surfaceOffset.x, classInfo.surfaceOffset.y);
            // TODO: This is a bit rough and is bridge railing specific.
            dest.bottom = dest.top + (surfaceImage.getHeight()-40);

            // Draw the image.
            view.drawImage(surfaceImage, src, dest, 0);
        }
    }

    private void drawFinishLine(SolidObject so, GameView view) {
        String finishLineKey = info.getFinishLineKey();
        Bitmap finishLine = scaledBitmaps.get(finishLineKey);
        VectorF pos = so.pos.copy();
        Graphics.scalePos(pos, view.getWidth(), view.getHeight());
        view.drawImage(finishLine, pos, 0, GameView.ImageOrigin.TopLeft);
    }

    public void update(float lastUpdate, Bike bike, HighScore score) {
        this.bikePos = bike.getPos();

        // Determine if Bike passed the finish line.
        SolidObject finishLine = info.getSolidObject("finish");
        VectorF pos = finishLine.pos.copy();
        Graphics.scalePos(pos, Graphics.getScreenWidth(), Graphics.getScreenWidth());
        if (bikePos.x >= pos.x) {
            gameState.endGame(false);
        }

        // Update the level objects.
        for (LevelObject obj : levelObjects) {
            obj.update(lastUpdate, bike, score);
        }
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
        return new VectorF(20, 300);
    }

    @Override
    public void onBikeCrash() {
        gameState.endGame(true);
    }

    public LevelInfo.LevelID getLevelID() {
        return currentLevelID;
    }
}
