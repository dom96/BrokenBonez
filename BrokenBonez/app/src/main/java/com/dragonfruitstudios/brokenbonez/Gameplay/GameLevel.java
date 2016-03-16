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

/**
 * Used to draw a Game level as defined by a LevelInfo.
 */
public class GameLevel extends Level {
    private GameState gameState; // Used to grab assets, and physics simulator.

    private VectorF startPoint; // Holds the coordinates which determine where the bike starts.

    private LevelInfo info; // Holds information about the current level.

    private VectorF bikePos; // The current bike position.
    private LevelInfo.LevelID currentLevelID;

    private boolean layersScaled = false; // Have the layers been scaled?
    private boolean bitmapsScaled = false; // Have the bitmaps been scaled?
    private HashMap<String,Bitmap> scaledBitmaps; // A mapping from asset key to scaled bitmap.

    private ArrayList<LevelObject> levelObjects; // A list of objects to draw on the level.

    public GameLevel(GameState state, LevelInfo.LevelID levelID) {
        // Initialise field values.
        gameState = state;

        startPoint = new VectorF(0, 0); // Just a reasonable default.
        bikePos = new VectorF(0, 0);
        layersScaled = false;
        bitmapsScaled = false;
        currentLevelID = levelID;

        String levelPath = LevelInfo.getLevelPath(levelID);

        // Create a default level info object.
        // TODO: Was planning on reading this information from an ini file but ran out of time.
        // TODO: The support for different images is baked in and the LevelInfo class is very
        // TODO: customisable.
        info = new LevelInfo(levelID, "surface.png", "ground.png", "finish.png");
        // Create a number of layers for the level.
        // Parameters to Layer constructors:
        //   Y position (based on 768 high screen), scroll factor, image origin
        //   (For ColorLayer):
        //     color height, color top, color bottom.
        info.addLayer(new ColorLayer("sky.png", 154f, 0.01f,
                GameView.ImageOrigin.MiddleLeft, 100f, "#1e3973", "#466ab9"));
        info.addLayer(new Layer("buildings1.png", 454f, 0.09f,
                GameView.ImageOrigin.BottomLeft));
        info.addLayer(new ColorLayer("buildings2.png", 479f, 0.15f,
                GameView.ImageOrigin.BottomLeft, 20f, Color.TRANSPARENT, Color.BLACK));
        info.addLayer(new Layer("bushes.png", 518f, 0.8f,
                GameView.ImageOrigin.BottomLeft));

        // Load the SVG file which defines the level's geometry.
        info.loadSVG(gameState.getAssetLoader(), levelPath, new VectorF(-600, 0));

        // Initialise the SolidLayer class asset keys.
        // Create a class for the default ground.
        info.addInfo("plain", info.getSurfaceKey(), info.getImagePath("ground_base.png"),
                new VectorF(0, 0));
        // Create a class for the first ramp kind.
        info.addInfo("little_ramp", LevelInfo.getTransparentKey(),
                info.getImagePath("little_ramp.png"), new VectorF(0, 0));
        // Create a class for the first building kind.
        info.addInfo("building1", LevelInfo.getTransparentKey(),
                info.getImagePath("building1.png"), new VectorF(0, 0));
        // Create a class for the second building kind.
        info.addInfo("building2", LevelInfo.getTransparentKey(),
                info.getImagePath("building2.png"), new VectorF(0, 0));
        // Create a class for the second ramp kind.
        info.addInfo("little_ramp2", LevelInfo.getTransparentKey(),
                info.getImagePath("little_ramp2.png"), new VectorF(0, 0));
        // Create a class for the bridge.
        info.addInfo("bridge", info.getImagePath("bridge_rail.png"),
                info.getImagePath("bridge.png"), new VectorF(-10, -39), true);

        // Load bitmaps defined in LevelInfo.
        scaledBitmaps = info.loadAssets(gameState.getAssetLoader());

        // Load the solid layers into the physics simulator, so that the bike can collide with them.
        Simulator physicsSimulator = gameState.getPhysicsSimulator();
        info.loadSolids(physicsSimulator);

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
        // Recalculate bike start point.
        startPoint = calcStartPoint(w, h);

        if (!layersScaled) {
            // Scale each SolidLayer's coordinates to the current phone's resolution.
            for (SolidLayer sl : info.getSolids()) {
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
        for (Layer l : info.getLayers()) {
            drawLayer(gameView, l);
        }

        gameView.enableCamera();
        // Draw solid layers.
        for (SolidLayer sl : info.getSolids()) {
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
        for (SolidLayer sl : info.getSolids()) {
            drawSolidLayerForeground(sl, view);
        }
        view.disableCamera();
    }

    private void drawLayer(GameView gameView, Layer l) {
        // Copy the bike position so that we can make changes to it.
        VectorF pos = bikePos.copy();
        // Multiply the position by the scrollFactor so that it moves left appropriate distance.
        pos.mult(new VectorF(-l.scrollFactor, 0));
        Bitmap img = scaledBitmaps.get(info.getLayerKey(l));

        // Check what type the Layer actually is.
        if (l instanceof ColorLayer) {
            ColorLayer cLayer = ((ColorLayer) l);

            // Draw the image
            pos.add(0, Graphics.scaleY(l.yPos, gameView.getHeight()));
            drawScrolled(gameView, img, l.scrollFactor, pos, l.origin);

            // Draw the color above and below the image.
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
            // Simply draw the image.
            pos.add(0, Graphics.scaleY(l.yPos, gameView.getHeight()));
            drawScrolled(gameView, img, l.scrollFactor, pos, l.origin);
        }
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
        if (!fillKey.equals(LevelInfo.getTransparentKey())) {
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

        int slOffsetX = 0; // Where to start at in the next surface image.
        // Draw the image beneath each line.
        for (int i = 0; i < sl.getLines().size(); i++) {
            // Grab the asset info for this SolidLayer's class.
            SolidLayer.Info classInfo = info.getSolidLayerInfo(sl);
            // Find the asset type to draw for this SolidLayer line.
            LevelInfo.AssetType assetType = sl.getAssetType(i);
            // Get the asset key for that asset type.
            String assetKey = sl.getAssetKey(assetType);
            // Don't draw if key is transparent or is to be drawn behind the bike.
            if (!assetKey.equals(LevelInfo.getTransparentKey()) && !classInfo.surfaceInForeground) {
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
        if (!classInfo.surfaceKey.equals(LevelInfo.getTransparentKey()) && !sl.usesFillPolygon()) {
            // Grab the AssetLoader key for this SolidLayer's Surface.
            String surfaceKey = sl.getAssetKey(LevelInfo.AssetType.Surface);
            // Grab the image for this SolidLayer's surface key.
            Bitmap surfaceImage = scaledBitmaps.get(surfaceKey);

            // Create a source rectangle for this image.
            Rect src = new Rect(0, 0, surfaceImage.getWidth(), surfaceImage.getHeight());
            // Compute a rectangle to draw the image into.
            RectF dest = new RectF(sl.getRect());
            dest.offset(classInfo.surfaceOffset.x, classInfo.surfaceOffset.y);
            // TODO: This is a bit rough and is bridge railing specific and likely also
            // TODO: resolution specific.
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
        // Set the bike position.
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
