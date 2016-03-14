package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameLoop;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.ParticleSystem.ParticleSystem;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;

public class GameState {
    GameLevel currentLevel;
    Bike bike;
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private Simulator physicsSimulator;

    private Bitmap[] smokeParticles;
    private ParticleSystem particleSystem;
    public HighScore score;

    private Camera camera;

    private DeathOverlay deathOverlay;
    private boolean slowMotion;
    private int i = 0;
    private int j = 2;

    public GameState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.gameSceneManager = gameSceneManager;

        // Load assets.
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png",
                "bike/body_two.png", "particlesystem/smoke1.png", "particlesystem/smoke2.png", "particlesystem/smoke3.png", "particlesystem/smoke4.png"});
        this.assetLoader.AddAssets(new String[]{"bikeEngine.mp3", "bikeEngineRev.mp3",
                "brokenboneztheme.ogg"});

        // Create a new physics simulator.
        this.physicsSimulator = new Simulator();
        this.smokeParticles = new Bitmap[]{assetLoader.getBitmapByName("particlesystem/smoke1.png"),
                assetLoader.getBitmapByName("particlesystem/smoke2.png"),
                assetLoader.getBitmapByName("particlesystem/smoke3.png"),
                assetLoader.getBitmapByName("particlesystem/smoke4.png"),};
        camera = new Camera(0, 0);
        currentLevel = new GameLevel(this);
        bike = new Bike(currentLevel, Bike.BodyType.Bike);

        slowMotion = false;

        this.score = new HighScore(gameSceneManager.gameView);
    }

    public void newGame(Bike.BodyType bikeBodyType, int bikeColor) {
        bike.setColor(bikeColor);
        bike.setBodyType(bikeBodyType);
        bike.reset();
        setSlowMotion(false);

        // Ensure that deathOverlay has been created.
        if (deathOverlay != null) {
            deathOverlay.disable();
        }
    }

    public void update(float lastUpdate) {
        bike.update(lastUpdate);
        physicsSimulator.update(lastUpdate);
        currentLevel.update(lastUpdate, bike.getPos());
        camera.centerHorizontally(bike.getPos().x);
        if(TouchHandler.cIA == TouchHandler.ControlIsActive.ACTION_GAS_DOWN){
            i = 2;
            i++;
            if(i > 3){
                i = 2;
            }
        } else {
            i++;
            if(i > 1){
                i = 0;
            }
            j = 4;
        }
        this.particleSystem = new ParticleSystem((int) bike.getPos().y - 30, 1020, 100, 10, 100, smokeParticles[i], j);
        particleSystem.updatePhysics((int) lastUpdate);
        score.changeTimeBy(lastUpdate);
    }
    public void updateSize(int w, int h) {
        currentLevel.updateSize(w, h);
        bike.updateSize(w, h);
        camera.updateSize(w, h);

        // Create the DeathOverlay once the size of the GameView is known.
        deathOverlay = new DeathOverlay(assetLoader, w, h);
    }

    public void draw(GameView view) {
        view.setCamera(camera);
        currentLevel.draw(view);
        bike.draw(view);
        physicsSimulator.draw(view);
        particleSystem.doDraw(view);
        deathOverlay.draw(view);
        score.draw(view);
    }

    public void onTouchEvent(MotionEvent event) {
        deathOverlay.onTouchEvent(event);
    }

    public void setBikeAcceleration(float strength) {
        bike.setTorque(strength);
    }

    /**
     * Sets the bike's tilting force. Value should be between -1 and 1. Negative values mean
     * tilt to the left (left wheel down, right wheel up), positive mean tilt to the right (left
     * wheel up, right wheel down).
     */
    public void setBikeTilt(float value) {
        bike.setTilt(value);
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
    public Simulator getPhysicsSimulator() {
        return physicsSimulator;
    }

    public void setSlowMotion(boolean value) {
        slowMotion = value;
        if (slowMotion) {
            Simulator.setUpdateRate(200);
        }
        else {
            Simulator.setUpdateRate(GameLoop.targetFPS);
        }
    }

    public void endGame() {
        deathOverlay.enable();
        setSlowMotion(true);
    }
}

