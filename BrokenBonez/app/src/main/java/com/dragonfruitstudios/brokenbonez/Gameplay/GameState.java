package com.dragonfruitstudios.brokenbonez.Gameplay;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Button;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class GameState implements GameObject {
    Level currentLevel;
    Bike bike;

    private AssetLoader assetLoader;
    private Simulator physicsSimulator;

    private Camera camera;

    // Nate's menu test
    // TODO: Please move this into your own class.
    Button button;

    public GameState(AssetLoader assetLoader) {
        // Create a new physics simulator.
        this.physicsSimulator = new Simulator();

        currentLevel = new Level(this);
        bike = new Bike(currentLevel);

        camera = new Camera(0, 0);

        // Load assets.
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png"});

        // TODO: Nate's button testing
        button = new Button();
    }

    public void update(float lastUpdate) {
        bike.update(lastUpdate);
        physicsSimulator.update(lastUpdate);
        camera.centerHorizontally(bike.getPos().x);
    }

    public void updateSize(int w, int h) {
        currentLevel.updateSize(w, h);
        bike.updateSize(w, h);
        camera.updateSize(w, h);
    }

    public void draw(GameView view) {
        camera.applyCamera(view);
        currentLevel.draw(view);
        //view.translate(0, 0);
        bike.draw(view);
        //view.translate(-cameraPos.x, -cameraPos.y);
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

