package com.dragonfruitstudios.brokenbonez;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;

public class GameState {
    Level currentLevel;
    Bike bike;

    AssetLoader assetLoader;

    boolean paused;
    float debugStep;

    public GameState(AssetLoader assetLoader) {
        currentLevel = new Level(this);
        bike = new Bike(currentLevel);

        debugStep = -1;
        paused = true;

        // Load assets
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png"});
    }

    /**
     * Separate method so that it can be used when debugging.
     * @param ms
     */
    public void step(float ms) {
        bike.update(ms);
    }

    public void update(float lastUpdate) {
        if (!paused) {
            step(lastUpdate);
        }

        if (debugStep > 0) {
            step(debugStep);
        }
    }

    public void updateSize(int w, int h) {
        currentLevel.updateSize(w, h);
        bike.updateSize(w, h);
    }

    public void draw(GameView view) {
        currentLevel.draw(view);
        bike.draw(view);
    }

    public void setBikeAcceleration(float strength) {
        bike.setTorque(strength);
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }

    // The following are used when debugging.

    public void pause() {
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public void resume() {
        paused = false;
    }

    public void setDebugStep(float debugStep) {
        this.debugStep = debugStep;
    }

    public float getDebugStep() {
        return debugStep;
    }
}

