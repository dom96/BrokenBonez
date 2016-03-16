package com.dragonfruitstudios.brokenbonez.Game.Levels;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Line;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Polygon;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import junit.framework.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class specifies information about a specific level.
 */
public class LevelInfo {
    /**
     * Specifies one of the levels in the game. Used to select different levels.
     */
    public enum LevelID {
        Unknown, Level1, Level2, Level3, Level4
    }

    public enum AssetType {Surface, Fill, Transparent}

    private LevelID levelID; // This level's ID.
    private ArrayList<Layer> layers;
    private String surfacePath; // Path to the image which is drawn for the bike to ride on.
    private String groundPath; // Path to the image which can be drawn below surface.
    private String finishPath; // Path to the image which signifies the finish line.
    // A list of Polygon's describing solid ground.
    private ArrayList<SolidLayer> solids;
    // A mapping between a class name and a list of Solid objects with that class specified in SVG.
    private HashMap<String,ArrayList<SolidObject>> objects;
    // Specifies the assets to use for a specific solidLayer class.
    private HashMap<String,SolidLayer.Info> slAssets;

    /**
     * Creates a new LevelInfo object with the specified parameters.
     * @param levelID The LevelID of this info.
     * @param surfacePath The path to the image representing the surface of this level.
     * @param groundPath The path to the image representing the ground of this level.
     * @param finishPath The path to the image representing the finish line of this level.
     */
    public LevelInfo(LevelID levelID, String surfacePath, String groundPath, String finishPath) {
        this.levelID = levelID;
        this.surfacePath = surfacePath;
        this.groundPath = groundPath;
        this.finishPath = finishPath;
        this.layers = new ArrayList<Layer>();
        this.solids = new ArrayList<SolidLayer>();
        this.objects = new HashMap<String, ArrayList<SolidObject>>();
        this.slAssets = new HashMap<String,SolidLayer.Info>();
    }

    public void addLayer(Layer layer) {
        layers.add(layer);
    }

    /**
     * Loads the specified asset at `path` by adding it to the AssetLoader, then adds it into
     * the specified HashMap so that it can be scaled to fit the screen at a later time.
     */
    private void loadAsset(AssetLoader loader, HashMap<String, Bitmap> scaled, String path) {
        loader.AddAssets(new String[] {path});
        scaled.put(path, loader.getBitmapByName(path));
    }

    /**
     * Adds the solids defined by this LevelInfo into the specified Physics simulator instance.
     */
    public void loadSolids(Simulator sim) {
        for (SolidLayer sl : solids) {
            sim.addStaticShape(sl);
        }
    }

    /**
     * Loads the assets needed by this level. Returns a mapping from asset key to Bitmap, these
     * should be used to hold a scaled version of the images.
     */
    public HashMap<String, Bitmap> loadAssets(AssetLoader loader) {
        HashMap<String, Bitmap> result = new HashMap<>();
        loadAsset(loader, result, getSurfaceKey());
        loadAsset(loader, result, getGroundKey());
        loadAsset(loader, result, getFinishLineKey());
        // Add the background layer's assets.
        for (Layer l : layers) {
            loadAsset(loader, result, getLayerKey(l));
        }
        // Add the Solid layers' assets.
        for (SolidLayer sl : solids) {
            for (String key : sl.getAssetKeys()) {
                loadAsset(loader, result, key);
            }
        }
        return result;
    }

    /**
     * Retrieves a HashMap containing a mapping from class to SolidLayer.Info.
     */
    public HashMap<String,SolidLayer.Info> getSolidLayerAssets() {
        return slAssets;
    }

    /**
     * Creates and adds a new SolidLayer.Info object into this LevelInfo's asset list.
     * @param theClass The class of the SolidLayer (as specified in SVG) that this info references.
     * @param surfaceKey The surface asset key to use for drawing the surface.
     * @param fillKey The fill asset key to use for drawing the fill of the SolidLayer.
     */
    public void addInfo(String theClass, String surfaceKey, String fillKey) {
        slAssets.put(theClass, new SolidLayer.Info(theClass, surfaceKey, fillKey));
    }

    public void addInfo(String theClass, String surfaceKey, String fillKey,
                        VectorF surfaceOffset) {
        addInfo(theClass, surfaceKey, fillKey);
        slAssets.get(theClass).surfaceOffset = surfaceOffset;
    }

    /**
     * Same as above but can specify that the surface is in foreground. Only works for
     * closed Polygons!
     */
    public void addInfo(String theClass, String surfaceKey, String fillKey,
                        VectorF surfaceOffset, boolean surfaceInForeground) {
        addInfo(theClass, surfaceKey, fillKey, surfaceOffset);
        slAssets.get(theClass).surfaceInForeground = surfaceInForeground;
    }

    /**
     * Retrieves the SolidLayer.Info for the specified SolidLayer. This is just a little shortcut.
     */
    public SolidLayer.Info getSolidLayerInfo(SolidLayer sl) {
        return sl.getInfo();
    }

    /**
     * Gets the first SolidObject with the specified class. If none such exists then an exception
     * is raised.
     */
    public SolidObject getSolidObject(String theClass) {
        if (!objects.containsKey(theClass)) {
            throw new RuntimeException("Could not find solid object: " + theClass);
        }

        return objects.get(theClass).get(0);
    }

    /**
     * Returns a list of the SolidObjects with the specified class. Returns an empty list if
     * there is no SolidObjects with that class.
     */
    public ArrayList<SolidObject> findSolidObjects(String theClass) {
        if (!objects.containsKey(theClass)) {
            return new ArrayList<>();
        }
        return objects.get(theClass);
    }

    public ArrayList<SolidLayer> getSolids() {
        return solids;
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    // <editor-fold desc="Custom SVG parser.">

    /**
     * Takes a String containing `<path>` data. Parses that String into a list of Lines. The
     * `pos` parameter specifies where the lines will begin.
     * For example:
     *
     * M 0,0
     * C 0.00,2.00 17.00,16.00 17.00,16.00
         17.00,16.00 28.00,24.00 29.00,25.00
     */
    private ArrayList<Line> parsePath(String path, VectorF pos) {
        // Spec for 'C' here: https://www.w3.org/TR/SVG/paths.html#PathDataCubicBezierCommands
        // Only `curveto` is supported as that's all that GIMP generates.
        // Yep, I know I probably could have found some library to parse the SVG for me, but
        // parsing manually is more fun.
        ArrayList<Line> result = new ArrayList<>();
        int i = 0;

        boolean mStarted = false;
        boolean pathStarted = false;

        // C Params are `x1,y1 x2,y2 x,y`, we only care about the last param.
        int currentParam = 0;
        boolean currentCoordIsY = false;
        VectorF currentPoint = pos.copy();
        String[] coords = new String[] {"", ""};
        while (i < path.length()) {
            switch (path.charAt(i)) {
                case 'M':
                    mStarted = true;
                    if (path.charAt(i+1) == ' ') {
                        i++; // Skip whitespace.
                    }
                    break;
                case ' ':
                case '\n':
                    if (pathStarted) {
                        if (currentParam == 2) {
                            VectorF end = new VectorF(Float.valueOf(coords[0]) + pos.x,
                                    Float.valueOf(coords[1]) + pos.y);
                            Assert.assertTrue("Found line which starts and ends in the same place.",
                                    end.subtracted(currentPoint).magnitude() != 0);
                            result.add(new Line(currentPoint.copy(), end));
                            currentPoint.set(end.x, end.y);
                            currentParam = 0;
                        }
                        else if (coords[0].length() > 0) {
                            currentParam++;
                            Assert.assertTrue(currentParam <= 2);
                        }
                        currentCoordIsY = false;
                        coords = new String[]{"", ""};
                    }

                    if (mStarted) {
                        currentPoint = new VectorF(Float.valueOf(coords[0]) + pos.x,
                                Float.valueOf(coords[1]) + pos.y);
                        mStarted = false;
                        currentCoordIsY = false;
                        coords = new String[]{"", ""};
                    }
                    break;
                case 'C':
                    pathStarted = true;
                    mStarted = false;
                    if (path.charAt(i+1) == ' ') {
                        i++; // Skip whitespace.
                    }
                    break;
                case '-':
                case '.':
                    if (pathStarted || mStarted) {
                        if (currentCoordIsY) {
                            coords[1] += path.charAt(i);
                        }
                        else {
                            coords[0] += path.charAt(i);
                        }
                    }
                    break;
                case ',':
                    if (pathStarted || mStarted) {
                        Assert.assertTrue(!currentCoordIsY);
                        currentCoordIsY = true;
                    }
                    break;
                default:
                    Assert.assertTrue("Unknown char: " + path.charAt(i),
                            Character.isDigit(path.charAt(i)));
                    if (pathStarted || mStarted) {
                        // We have a number.
                        if (currentCoordIsY) {
                            coords[1] += path.charAt(i);
                        }
                        else {
                            coords[0] += path.charAt(i);
                        }
                    }
                    break;
            }
            i++;
        }

        // Add the last line.
        if (coords[0].length() > 0) {
            VectorF end = new VectorF(Float.valueOf(coords[0]) + pos.x,
                    Float.valueOf(coords[1]) + pos.y);
            if (!mStarted) {
                Assert.assertTrue("Found line which starts and ends in the same place.",
                        end.subtracted(currentPoint).magnitude() != 0);
                result.add(new Line(currentPoint.copy(), end));
            }
            else {
                // Create a Point line (line which starts and ends at the same point).
                // This type of line is used to designate the position of Level objects, finish
                // flag and more.
                result.add(new Line(end.copy(), end));
            }
        }
        return result;
    }

    private SolidLayer generateSolidLayer(ArrayList<Line> lines, String theClass) {
        // Assume all the lines in path are surface paths.
        // Create an AssetKey which specifies that the lines in Path should be drawn as the surface.
        SolidLayer.AssetKey surfaceKey = new SolidLayer.AssetKey(
                AssetType.Surface, 0, lines.size()-1);

        // Define a variable to hold the array of AssetKeys.
        SolidLayer.AssetKey[] keys;
        // Define a variable which determines whether this path is closed (and does not need
        // to be closed automatically).
        boolean selfClosed = false;

        // Check if the lines form a closed Polygon.
        VectorF firstPoint = lines.get(0).getStart();
        VectorF lastPoint = lines.get(lines.size()-1).getFinish();
        float distSq = firstPoint.distSquared(lastPoint);
        // If the distance between the first lines start point and the last lines finish point is
        // close enough then we assume that the Path forms a closed Polygon.
        if (distSq < Math.pow(5, 2)) {
            keys = new SolidLayer.AssetKey[] {
                    surfaceKey
            };
            selfClosed = true;
        }
        else {
            // Otherwise we quickly close the Polygon.
            Line left = new Line(lines.get(0).getStart().copy(),
                    lines.get(0).getStart().added(new VectorF(0, 400)));
            Line right = new Line(lines.get(lines.size() - 1).getFinish().copy(),
                    lines.get(lines.size() - 1).getFinish().added(new VectorF(0, 400)));
            Line bottom = new Line(left.getFinish().copy(), right.getFinish().copy());
            lines.add(right);
            lines.add(bottom);
            lines.add(left);
            keys = new SolidLayer.AssetKey[]{
                    surfaceKey,
                    new SolidLayer.AssetKey(AssetType.Transparent,
                            surfaceKey.indexEnd + 1, surfaceKey.indexEnd + 4)
            };
        }
        // Create a new SolidLayer using the specified `lines` and asset keys `keys`.
        SolidLayer newLayer = SolidLayer.createPolygon(this, lines, keys, theClass, selfClosed);
        return newLayer;
    }

    private SolidObject generateSolidObject(ArrayList<Line> lines, String theClass) {
        return new SolidObject(lines.get(0).getStart(), theClass);
    }

    private void addSolidObject(SolidObject so) {
        if (objects.containsKey(so.theClass)) {
            objects.get(so.theClass).add(so);
        }
        else {
            ArrayList<SolidObject> classObjects = new ArrayList<SolidObject>();
            classObjects.add(so);
            objects.put(so.theClass, classObjects);
        }
    }

    /**
     * Parses the SVG file at `levels/<path>` and adds the paths defined in it into
     * this LevelInfo's solid layer's list. The `pos` parameter specifies the starting position
     * of the paths.
     */
    public void loadSVG(AssetLoader loader, String path, VectorF pos) {
        AssetManager assetManager = loader.getAssetManager();

        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(assetManager.open("levels/" + path));

            Element doc = dom.getDocumentElement();
            NodeList elements = doc.getElementsByTagName("path");
            for (int i = 0; i < elements.getLength(); i++) {
                NamedNodeMap attrs = elements.item(i).getAttributes();
                Node d = attrs.getNamedItem("d");
                ArrayList<Line> lines = parsePath(d.getNodeValue(), pos);
                if (lines.size() > 0) {
                    // TODO: Error checking for `class`.
                    String theClass = attrs.getNamedItem("class").getNodeValue();
                    if (lines.get(0).isPoint()) {
                        SolidObject newObject = generateSolidObject(lines, theClass);
                        addSolidObject(newObject);
                    }
                    else {
                        SolidLayer newLayer = generateSolidLayer(lines, theClass);
                        solids.add(newLayer);
                    }
                }
            }
        }
        catch (ParserConfigurationException | IOException | SAXException exc) {
            Log.e("LevelInfo", "Error parsing SVG: " + exc.toString());
            // TODO: Error system, show user a dialog box when an error occurs at root.
        }
    }

    // </editor-fold>

    // <editor-fold desc="Getters for asset keys.">

    private String getLevelPath() {
        // Currently only support 1 level tileset, so this is simply hardcoded in.
        return "levels/level1/";
    }

    public String getImagePath(String name) {
        return getLevelPath() + name;
    }

    public String getLayerKey(Layer layer) {
        return getLevelPath() + layer.path;
    }

    public String getSurfaceKey() {
        return getLevelPath() + surfacePath;
    }

    public String getGroundKey() {
        return getLevelPath() + groundPath;
    }

    public String getFinishLineKey() {
        return getLevelPath() + finishPath;
    }

    public static String getTransparentKey() {
        return "";
    }

    // </editor-fold>

    public static String getLevelName(LevelID levelID) {
        switch (levelID) {
            case Level1:
                return "level1";
            case Level2:
                return "level2";
            case Level3:
                return "level3";
            case Level4:
                return "level4";
            case Unknown:
                throw new RuntimeException("Unable to get level path of Unknown LevelID.");
        }
        return "";
    }

    public static String getLevelPath(LevelID levelID) {
        switch (levelID) {
            case Level1:
                return "level1.svg";
            case Level2:
                return "level2.svg";
            case Level3:
                return "level3.svg";
            case Level4:
                return "level4.svg";
            case Unknown:
                throw new RuntimeException("Unable to get level path of Unknown LevelID.");
        }
        return "";
    }

    /**
     * Returns the level after `levelID`.
     */
    public static LevelID getNextLevel(LevelID levelID) {
        switch (levelID) {
            case Level1:
                return LevelID.Level2;
            case Level2:
                return LevelID.Level3;
            case Level3:
                return LevelID.Level4;
            case Level4:
                return LevelID.Level1;
        }
        return LevelID.Level1;
    }


}
