package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameLevel;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class BikeSelectionLevel extends GameLevel {

    BikeSelectionState bikeSelectionState;
    VectorF startPoint;
    VectorF bikePos;

    public BikeSelectionLevel(BikeSelectionState state) {
        super();
        bikeSelectionState = state;
        startPoint = new VectorF(getScreenWidth() / 2 - 60, getScreenHeight() / 4 + 60);
        bikePos = new VectorF(getScreenWidth() / 2 - 60, getScreenHeight() / 4 + 60);
        Simulator physicsSimulator = bikeSelectionState.getPhysicsSimulator();
        for (int i = 0; i < 1; i++) {
            float height = 320;
            Rect rect = new Rect(new VectorF(getScreenWidth() / 2 - 60, height), 130, 20);
            physicsSimulator.createStaticBody(rect);
        }
        for(int i = 0; i < 1; i++) {
            float height = 300;
            Rect rect = new Rect(new VectorF(getScreenWidth() / 2, height), 20, 20);
            physicsSimulator.createStaticBody(rect);
        }
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void updateSize(int w, int h) {
        Log.d("UpdateSize", "Updating size in Level: " + w + " " + h);
        //startPoint = calcStartPoint(w, h);
    }

    public void draw(GameView gameView) {
        AssetLoader assetLoader = bikeSelectionState.getAssetLoader();
        float currHeight = 320;
        String debugInfo = String.format("Level[grndY: %.1f, totalY: %d]",
                currHeight, gameView.getHeight());
        gameView.drawText(debugInfo, 100, 30, Color.WHITE);
    }

    public void update(float lastUpdate) {
        this.bikePos = bikePos;
    }

    public AssetLoader getAssetLoader() {
        return bikeSelectionState.getAssetLoader();
    }

    public Simulator getPhysicsSimulator() {
        return bikeSelectionState.getPhysicsSimulator();
    }

    public VectorF getStartPoint() {
        Log.d("StartPoint", startPoint.toString());
        return startPoint;
    }
}