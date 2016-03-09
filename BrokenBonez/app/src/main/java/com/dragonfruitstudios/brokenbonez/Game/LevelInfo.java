package com.dragonfruitstudios.brokenbonez.Game;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Line;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Polygon;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import junit.framework.Assert;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class specifies information about a specific level.
 */
public class LevelInfo {
    public String name;
    public ArrayList<Layer> layers;
    public String surfacePath; // Path to the image which is drawn for the bike to ride on.
    public String groundPath; // Path to the image which can be drawn below surface.
    public ArrayList<SolidLayer> solids;

    /**
     * This is a layer that the Bike will collide with (and ride over).
     */
    public static class SolidLayer extends Polygon {
        // This maps directly to Polygon.lines and determines what image should be drawn over each
        // line.
        private ArrayList<AssetKey> assetKeys;
        private String fillKey; // The key of the image used to fill the layer.

        private SolidLayer(Polygon polygon) {
            super(polygon.getLines());
        }

        public static SolidLayer createRect(VectorF pos, float width, float height,
                                            String top, String left, String right, String bottom,
                                            String fill) {
            SolidLayer result = new SolidLayer(new Rect(pos, width, height));
            result.assetKeys = new ArrayList<AssetKey>();
            result.assetKeys.add(new AssetKey(top, 0, 0));
            result.assetKeys.add(new AssetKey(left, 3, 3));
            result.assetKeys.add(new AssetKey(right, 1, 1));
            result.assetKeys.add(new AssetKey(bottom, 2, 2));
            result.fillKey = fill;
            return result;
        }

        public static SolidLayer createPolygon(ArrayList<Line> lines, AssetKey[] assetKeys,
                                               String fillKey) {
            SolidLayer result = new SolidLayer(new Polygon(lines));
            result.assetKeys = new ArrayList<AssetKey>();
            for (AssetKey ak : assetKeys) {
                for (int i = ak.indexStart; i <= ak.indexEnd; i++) {
                    result.assetKeys.add(ak);
                }
            }
            result.fillKey = fillKey;
            return result;
        }

        public String getAssetKey(int i) {
            return assetKeys.get(i).key;
        }

        public String getFillKey() {
            return fillKey;
        }

        static class AssetKey {
            String key;
            int indexStart;
            int indexEnd;

            AssetKey(String key, int indexStart, int indexEnd) {
                this.key = key;
                this.indexStart = indexStart;
                this.indexEnd = indexEnd;
            }
        }
    }

    public static class Layer {
        public String path; // Relative to level image dir.
        public float yPos; // Resolution independent factors used to place this layer along the vertical.
        public float scrollFactor; // Factor used to scroll this layer when the bike moves.
        public float yMargin; // How much to move this layer down after its
                              // position has been calculated based on the `yFactor`.
        public GameView.ImageOrigin origin;

        public Layer(String path, float yPos, float scrollFactor, float yMargin, GameView.ImageOrigin origin) {
            this.path = path;
            this.yPos = yPos;
            this.scrollFactor = scrollFactor;
            this.yMargin = yMargin;
            this.origin = origin;
        }
    }

    /**
     * This layer is extended by the color specified if the image is too small. Used for the
     * sky layer.
     */
    public static class ColorLayer extends Layer {
        public float colorHeight;
        public int colorTop; // Color specifying the color to put above the image.
        public int colorBottom; // Color specifying the color to put below the image.

        public ColorLayer(String path, float yPos, float scrollFactor, float yMargin,
                          GameView.ImageOrigin origin, float colorHeight,
                          String colorTop, String colorBottom) {
            super(path, yPos, scrollFactor, yMargin, origin);
            this.colorHeight = colorHeight;
            this.colorTop = Color.parseColor(colorTop);
            this.colorBottom = Color.parseColor(colorBottom);
        }

        public ColorLayer(String path, float yPos, float scrollFactor, float yMargin,
                          GameView.ImageOrigin origin, float colorHeight,
                          int colorTop, int colorBottom) {
            super(path, yPos, scrollFactor, yMargin, origin);
            this.colorTop = colorTop;
            this.colorBottom = colorBottom;
        }
    }

    public LevelInfo(String name, String surfacePath, String groundPath) {
        this.name = name;
        this.surfacePath = surfacePath;
        this.groundPath = groundPath;
        this.layers = new ArrayList<Layer>();
        this.solids = new ArrayList<SolidLayer>();
    }

    public void loadAssets(AssetLoader loader) {
        loader.AddAssets(new String[] {getSurfaceKey()});
        loader.AddAssets(new String[] {getGroundKey()});
        // Add the background layer's assets.
        for (Layer l : layers) {
            loader.AddAssets(new String[] {getLayerKey(l)});
        }
        // Add the Solid layers' assets.
        for (SolidLayer sl : solids) {
            // TODO: UGH. An `addAsset` method is desperately needed.
            for (SolidLayer.AssetKey ak : sl.assetKeys) {
                if (!ak.key.equals(getTransparentKey())) {
                    loader.AddAssets(new String[]{ak.key});
                }
            }
        }
    }

    private String getLevelPath() {
        return "levels/" + name + "/";
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

    public String getTransparentKey() {
        return "";
    }

    public float calcGroundHeight(AssetLoader loader, int height) {
        // For now we assume that the last layer is the ground.
        Layer groundLayer = layers.get(layers.size()-1);
        Bitmap groundBitmap = loader.getBitmapByName(getLayerKey(groundLayer));
        switch (groundLayer.origin) {
            case BottomLeft:
                return height*groundLayer.yPos - groundBitmap.getHeight();
            default:
                return 410;
                //throw new RuntimeException("Not implemented TODO");
        }
    }

    /**
     * Takes a String containing `<path>` data. Parses that String into a list of Lines.
     * For example:
     *
     * M 0,0
     * C 0.00,2.00 17.00,16.00 17.00,16.00
         17.00,16.00 28.00,24.00 29.00,25.00
     */
    private ArrayList<Line> parsePath(String path, VectorF pos) {
        // Spec for 'C' here: https://www.w3.org/TR/SVG/paths.html#PathDataCubicBezierCommands
        // Only `curveto` is supported as that's all that GIMP generates.
        // Yep, I wrote a custom parser for this.
        ArrayList<Line> result = new ArrayList<>();
        int i = 0;

        boolean pathStarted = false;

        // Params are `x1,y1 x2,y2 x,y`, we only care about the last param.
        int currentParam = 0;
        boolean currentCoordIsY = false;
        VectorF currentPoint = pos.copy();
        String[] coords = new String[] {"", ""};
        while (i < path.length()) {
            switch (path.charAt(i)) {
                case 'M':
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
                    break;
                case 'C':
                    pathStarted = true;
                    if (path.charAt(i+1) == ' ') {
                        i++; // Skip whitespace.
                    }
                    break;
                case '.':
                    if (pathStarted) {
                        if (currentCoordIsY) {
                            coords[1] += '.';
                        }
                        else {
                            coords[0] += '.';
                        }
                    }
                    break;
                case ',':
                    if (pathStarted) {
                        Assert.assertTrue(!currentCoordIsY);
                        currentCoordIsY = true;
                    }
                    break;
                default:
                    Assert.assertTrue("Unknown char: " + path.charAt(i),
                            Character.isDigit(path.charAt(i)));
                    if (pathStarted) {
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

        return result;
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
                Node d = elements.item(i).getAttributes().getNamedItem("d");
                ArrayList<Line> lines = parsePath(d.getNodeValue(), pos);
                // Assume all the lines in path are surface paths.
                SolidLayer.AssetKey surfaceKey = new SolidLayer.AssetKey(getSurfaceKey(), 0,
                        lines.size()-1);

                // TODO: Quick way to close the Polygon.
                Line left = new Line(lines.get(0).getStart().copy(),
                        lines.get(0).getStart().added(new VectorF(0, 400)));
                Line right = new Line(lines.get(lines.size()-1).getFinish().copy(),
                        lines.get(lines.size()-1).getFinish().added(new VectorF(0, 400)));
                Line bottom = new Line(left.getFinish().copy(), right.getFinish().copy());
                lines.add(left);
                lines.add(right);
                lines.add(bottom);
                SolidLayer.AssetKey[] keys = new SolidLayer.AssetKey[] {
                        surfaceKey,
                        new SolidLayer.AssetKey(getTransparentKey(), surfaceKey.indexEnd+1,
                                surfaceKey.indexEnd+4)
                };
                solids.add(SolidLayer.createPolygon(lines, keys, getGroundKey()));
            }
        }
        catch (ParserConfigurationException | IOException | SAXException exc) {
            Log.e("LevelInfo", "Error parsing SVG: " + exc.toString());
            // TODO: Error system, show user a dialog box when an error occurs at root.
        }
    }


}
