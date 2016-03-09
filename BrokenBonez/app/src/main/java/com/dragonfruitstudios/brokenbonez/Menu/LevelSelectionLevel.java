package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Level;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameLevel;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class LevelSelectionLevel extends Level {
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

    @Override
    public VectorF getStartPoint() {
        return new VectorF(0, 0);
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
