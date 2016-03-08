package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;

public class LevelSelectionState implements GameObject {
    LevelSelectionLevel levelSelectionlevel;
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private Simulator physicsSimulator;
    private Camera camera;

    public LevelSelectionState(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        this.gameSceneManager = gameSceneManager;
        this.assetLoader = assetLoader;
        this.physicsSimulator = new Simulator();
        camera = new Camera(0, 0);
        levelSelectionlevel = new LevelSelectionLevel(this);
    }

    public int getScreenWidth() {return Resources.getSystem().getDisplayMetrics().widthPixels;}
    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void update(float lastUpdate) {
        physicsSimulator.update(lastUpdate);
        levelSelectionlevel.update(lastUpdate);
        levelSelectionlevel.getPhysicsSimulator().update(lastUpdate);
    }

    @Override
    public void updateSize(int w, int h) {
        levelSelectionlevel.updateSize(w, h);
        camera.updateSize(w, h);
    }

    @Override
    public void draw(GameView view) {
        view.setCamera(camera);
        levelSelectionlevel.draw(view);
        physicsSimulator.draw(view);
        levelSelectionlevel.draw(view);

    }

    public void onTouchEvent(MotionEvent event) {

    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    public Simulator getPhysicsSimulator() {
        return physicsSimulator;
    }
}