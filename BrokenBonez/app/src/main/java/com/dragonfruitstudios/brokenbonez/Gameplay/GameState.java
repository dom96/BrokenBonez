package com.dragonfruitstudios.brokenbonez.Gameplay;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Button;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;

public class GameState implements GameObject {
    Level currentLevel;
    Bike bike;

    private AssetLoader assetLoader;
    private Simulator physicsSimulator;

    // Nate's menu test
    // TODO: Please move this into your own class.
    Button button;

    public GameState(AssetLoader assetLoader) {
        // Create a new physics simulator.
        this.physicsSimulator = new Simulator();

        currentLevel = new Level(this);
        bike = new Bike(currentLevel);

        // Load assets.
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png"});

        // TODO: Nate's button testing
        button = new Button();
    }

    public void update(float lastUpdate) {
        bike.update(lastUpdate);
        physicsSimulator.update(lastUpdate);
    }

    public void updateSize(int w, int h) {
        currentLevel.updateSize(w, h);
        bike.updateSize(w, h);
    }

    public void draw(GameView view) {
        currentLevel.draw(view);
        bike.draw(view);
        physicsSimulator.draw(view);
    }

    public void setBikeAcceleration(float strength) {
        bike.setTorque(strength);
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
    public Simulator getPhysicsSimulator() {
        return physicsSimulator;
    }
}

