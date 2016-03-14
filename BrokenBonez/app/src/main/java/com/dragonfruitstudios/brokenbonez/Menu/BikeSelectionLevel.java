package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Color;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Level;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameLevel;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class  BikeSelectionLevel extends Level {

    BikeSelectionState bikeSelectionState;
    VectorF startPoint;
    VectorF bikePos;

    public BikeSelectionLevel(BikeSelectionState state) {
        bikeSelectionState = state;
        startPoint = new VectorF(getScreenWidth() / 10 * 7 + 20, getScreenHeight() / 4 - 20);
        bikePos = new VectorF(getScreenWidth() / 2 - 60, getScreenHeight() / 4 + 60);

        Simulator physicsSimulator = bikeSelectionState.getPhysicsSimulator();

        for (int i = 0; i < 1; i++) {
            float height = 320;
            Rect rect = new Rect(new VectorF(getScreenWidth() / 10 * 6, getScreenHeight() / 10 * 4), 600, 30);
            physicsSimulator.createStaticBody(rect);
        }

        for(int i = 0; i < 1; i++) {
            float height = 300;
            Rect rect = new Rect(new VectorF(getScreenWidth() / 10 * 6, getScreenHeight() / 10 * 3), 30, 140);
            physicsSimulator.createStaticBody(rect);
        }

        for(int i = 0; i < 1; i++) {
            float height = 300;
            Rect rect = new Rect(new VectorF(getScreenWidth() / 10 * 9, getScreenHeight() / 10 * 3), 30, 140);
            physicsSimulator.createStaticBody(rect);
        }
    }

    public void updateSize(int w, int h) {

    }

    public void draw(GameView gameView) {
    }

    public void update(float lastUpdate) {

    }

    public AssetLoader getAssetLoader() {
        return bikeSelectionState.getAssetLoader();
    }

    public Simulator getPhysicsSimulator() {
        return bikeSelectionState.getPhysicsSimulator();
    }

    public VectorF getStartPoint() {
        return startPoint;
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}