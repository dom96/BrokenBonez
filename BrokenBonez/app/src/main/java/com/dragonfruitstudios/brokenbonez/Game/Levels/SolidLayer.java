package com.dragonfruitstudios.brokenbonez.Game.Levels;

import com.dragonfruitstudios.brokenbonez.Math.Collisions.Line;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Polygon;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a layer that the Bike will collide with (and ride over). Essentially this represents
 * the solid ground.
 */
public class SolidLayer extends Polygon {
    // This maps directly to Polygon.lines and determines what image should be drawn over each
    // line.
    private ArrayList<AssetKey> assetKeys;
    // The class to look up for assets.
    private String theClass;
    private boolean selfClosed; // Whether this Polygon was closed in the SVG.

    private LevelInfo owner; // The LevelInfo that this SolidLayer is a part of.

    private SolidLayer(Polygon polygon) {
        super(polygon.getLines());
    }

    /**
     * Creates a new SolidLayer with the specified list of Lines representing it. The asset keys
     * describe what assets will be used to draw each line in this SolidLayer, they are packed
     * for efficiency.
     */
    public static SolidLayer createPolygon(LevelInfo owner, ArrayList<Line> lines,
                                           AssetKey[] assetKeys, String theClass,
                                           boolean selfClosed) {
        SolidLayer result = new SolidLayer(new Polygon(lines));
        result.assetKeys = new ArrayList<AssetKey>();
        result.owner = owner;
        result.theClass = theClass;
        result.selfClosed = selfClosed;
        for (AssetKey ak : assetKeys) {
            for (int i = ak.indexStart; i <= ak.indexEnd; i++) {
                result.assetKeys.add(ak);
            }
        }
        return result;
    }

    ArrayList<String> getAssetKeys() {
        ArrayList<String> result = new ArrayList<>();
        for (SolidLayer.AssetKey ak : assetKeys) {
            String key = getAssetKey(ak.assetType);
            // Make sure the key is not transparent.
            if (!key.equals(LevelInfo.getTransparentKey())) {
                result.add(key);
            }
        }

        String fillKey = getAssetKey(LevelInfo.AssetType.Fill);
        if (!fillKey.equals(LevelInfo.getTransparentKey())) {
            result.add(fillKey);
        }
        return result;
    }

    public String getAssetKey(LevelInfo.AssetType assetType) {
        // Grab the SolidLayer assets HashMap
        HashMap<String,SolidLayer.Info> slAssets = owner.getSolidLayerAssets();

        if (!slAssets.containsKey(this.theClass)) {
            throw new RuntimeException("Couldn't find SL's class in LevelInfo's assets: " +
                    this.theClass);
        }
        switch (assetType) {
            case Fill:
                return slAssets.get(this.theClass).fillKey;
            case Surface:
                return slAssets.get(this.theClass).surfaceKey;
            case Transparent:
                return LevelInfo.getTransparentKey();
            default:
                throw new InvalidParameterException("AssetType not implemented: " + assetType);
        }
    }

    public LevelInfo.AssetType getAssetType(int i) {
        return assetKeys.get(i).assetType;
    }

    public Info getInfo() {
        return owner.getSolidLayerAssets().get(theClass);
    }

    /**
     * Determines whether this SolidLayer's Fill image should be drawn using `fillPolygon`.
     *
     * Currently `true` for Polygons which have been automatically closed by the SVG path
     * parser.
     */
    public boolean usesFillPolygon() {
        return !selfClosed;
    }

    /**
     * A packed data structure which holds information about the asset type to use for drawing
     * of specific lines.
     */
    static class AssetKey {
        LevelInfo.AssetType assetType;
        int indexStart;
        int indexEnd;

        AssetKey(LevelInfo.AssetType assetType, int indexStart, int indexEnd) {
            this.assetType = assetType;
            this.indexStart = indexStart;
            this.indexEnd = indexEnd;
        }
    }

    /**
     * Stores information about a specific SolidLayer class' asset keys.
     *
     * Note: This is just a simple class that stores some information about asset keys,
     * hence its fields are public.
     */
    public static class Info {
        public String theClass; // The SolidLayer class this info describes.
        public String surfaceKey; // The asset key to use for the surface of this SolidLayer.
        public String fillKey; // The asset key to use for the fill of this SolidLayer.

        public VectorF surfaceOffset; // How much to offset the drawn image by.

        public boolean surfaceInForeground; // Whether to draw this SolidLayer in the foreground.

        Info(String theClass, String surfaceKey, String fillKey) {
            this.theClass = theClass;
            this.surfaceKey = surfaceKey;
            this.fillKey = fillKey;
            surfaceOffset = new VectorF(0, 0);
        }
    }
}