package com.dragonfruitstudios.brokenbonez.Gameplay;

import android.util.Log;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.GameLoop;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;


public class GameState {
    GameLevel currentLevel;
    Bike bike;
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private Simulator physicsSimulator;
    public HighScore score;

    private Camera camera;

    private FinishOverlay finishOverlay;
    private boolean slowMotion;

    public GameState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.gameSceneManager = gameSceneManager;

        // Load assets.
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[] {"bike/wheel_basic.png", "bike/body_one.png",
                "bike/body_two.png"});
        this.assetLoader.AddAssets(new String[]{"bikeEngine.mp3", "bikeEngineRev.mp3",
                "brokenboneztheme.ogg"});

        // Create a new physics simulator.
        this.physicsSimulator = new Simulator();

        camera = new Camera(0, 0);

        currentLevel = new GameLevel(this);
        bike = new Bike(currentLevel, Bike.BodyType.Bike);

        slowMotion = false;
        finishOverlay = new FinishOverlay(assetLoader);
        this.score = new HighScore(gameSceneManager.gameView);
    }

    public void newGame(Bike.BodyType bikeBodyType, int bikeColor) {
        bike.setColor(bikeColor);
        bike.setBodyType(bikeBodyType);
        bike.reset();
        setSlowMotion(false);
        finishOverlay.disable();
        score.reset();
    }

    public void update(float lastUpdate) {
        bike.update(lastUpdate);
        physicsSimulator.update(lastUpdate);
        currentLevel.update(lastUpdate, bike.getPos());
        camera.centerHorizontally(bike.getPos().x);
        if (!finishOverlay.isEnabled()) {
            score.changeTimeBy(lastUpdate);
        }
    }

    public void updateSize(int w, int h) {
        currentLevel.updateSize(w, h);
        bike.updateSize(w, h);
        camera.updateSize(w, h);
    }

    public void draw(GameView view) {
        view.setCamera(camera);
        currentLevel.draw(view);
        bike.draw(view);
        physicsSimulator.draw(view);
        finishOverlay.draw(view);
        score.draw(view);
    }

    public void onTouchEvent(MotionEvent event) {
        if (!finishOverlay.isEnabled()) {
            // Determine what action the user performed.
            TouchHandler.ControlIsActive action = TouchHandler.determineAction(event,
                    Graphics.getScreenWidth() / 2);
            switch (action) {
                case ACTION_GAS_UP:
                case ACTION_BRAKE_UP:
                case ACTION_NONE:
                    setBikeAcceleration(0);
                    break;
                case ACTION_GAS_DOWN:
                case ACTION_BRAKE_DOWN:
                    setBikeAcceleration(TouchHandler.getAccel());
                    getAssetLoader().getSoundByName("bikeEngineRev.mp3").play(false);   //Nearly ready, still little more test
                    break;
            }
        }

        FinishOverlay.OverlayResult result = finishOverlay.onTouchEvent(event);
        Log.d("GS", "FinishOverlay wants: " + result.toString());
        switch (result) {
            case Continue:
                score.setCallbacks(new HighScore.HighScoreCallbacks() {
                    @Override
                    public void onNameEntered(boolean enteredName) {
                        // TODO: Choose next level.
                        newGame(bike.getBodyType(), bike.getColor());
                    }
                });
                score.askName(true);
                break;
            case RestartLevel:
                newGame(bike.getBodyType(), bike.getColor());
                break;
            case ShowMainMenu:
                gameSceneManager.setScene("menuScene");
                break;
            case None:
                break;
        }
    }

    private void setBikeAcceleration(float strength) {
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

    public void endGame(boolean crashed) {
        finishOverlay.enable(crashed);
        setSlowMotion(true);
    }
}

