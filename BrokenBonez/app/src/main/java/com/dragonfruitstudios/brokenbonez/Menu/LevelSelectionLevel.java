package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameLevel;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;

public class LevelSelectionLevel extends GameLevel {
    LevelSelectionState levelSelectionState;

    public LevelSelectionLevel(LevelSelectionState state){
        levelSelectionState = state;
        Simulator physicsSimulator = levelSelectionState.getPhysicsSimulator();
    }

    public void updateSize(int w, int h) {

    }

    public void draw(GameView gameView) {

    }

    public void update(float lastUpdate) {

    }

    public AssetLoader getAssetLoader() {
        return levelSelectionState.getAssetLoader();
    }

    public Simulator getPhysicsSimulator() {
        return levelSelectionState.getPhysicsSimulator();
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
