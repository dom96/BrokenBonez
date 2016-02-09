package com.dragonfruitstudios.brokenbonez;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;

public class GameState implements GameObject {
    Level currentLevel;
    Bike bike;

    AssetLoader assetLoader;

    // Nate's menu test
    // TODO: Please move this into your own class.
    Button button;

    public GameState(AssetLoader assetLoader) {
        currentLevel = new Level(this);
        bike = new Bike(currentLevel);

        // Load assets
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png"});

        // TODO: Nate's button testing
        button = new Button();
    }

    public void update(float lastUpdate) {
        bike.update(lastUpdate);
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
}

