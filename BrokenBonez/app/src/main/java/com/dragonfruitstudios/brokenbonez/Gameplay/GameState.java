package com.dragonfruitstudios.brokenbonez.Gameplay;
import android.util.Log;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Game.Levels.LevelInfo;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.GameScene;
import com.dragonfruitstudios.brokenbonez.GameLoop;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;
import java.io.IOException;
import com.dragonfruitstudios.brokenbonez.Menu.Settings;
import com.dragonfruitstudios.brokenbonez.ParticleSystem.ParticleManager;

public class GameState {
    private GameScene gameScene;

    private GameLevel currentLevel;
    private Bike bike;
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private Simulator physicsSimulator;
    private Ghost ghost;
    private ParticleManager particleManager;
    private HighScore score;
    private Camera camera;
    private FinishOverlay finishOverlay;
    private Settings settings;

    private boolean askingForHighScore; // determines whether the `askName` dialog is shown.
    private boolean gameEnded; // determines whether the game ended.

    public GameState(GameScene gameScene, AssetLoader assetLoader, GameSceneManager gameSceneManager,
                     LevelInfo.LevelID levelID, Bike.CharacterType characterType,
                     Bike.BodyType bikeBodyType, int bikeColor) {
        this.gameScene = gameScene;
        this.gameSceneManager = gameSceneManager;

        // Load assets.
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[]{"bikeEngine.mp3", "bikeEngineRev.mp3",
                "brokenboneztheme.ogg", "crash.mp3", "win.mp3"});

        // Create a new physics simulator.
        this.physicsSimulator = new Simulator();

        camera = new Camera(0, 0);
        currentLevel = new GameLevel(this, levelID);
        bike = new Bike(assetLoader, currentLevel, Bike.BodyType.Bike, Bike.CharacterType.Leslie);

        finishOverlay = new FinishOverlay(assetLoader);
        this.score = new HighScore(gameSceneManager.gameView);

        // Create Ghost to show the player a Ghost bike of the last playthrough.
        ghost = new Ghost(gameSceneManager.activity.getApplicationContext(), assetLoader, currentLevel);

        this.particleManager = new ParticleManager(assetLoader, gameSceneManager);

        // Initialise the bike's customisable settings.
        bike.setCharacterType(characterType);
        if (bike.getColor() != bikeColor) {
            bike.setColor(bikeColor);
        }
        bike.setBodyType(bikeBodyType);

        this.settings = new Settings(gameSceneManager);
    }

    public void update(float lastUpdate) {
        bike.update(lastUpdate);
        physicsSimulator.update(lastUpdate);
        currentLevel.update(lastUpdate, bike, score);
        particleManager.update(lastUpdate, bike.getPos());
        camera.centerHorizontally(bike.getPos().x);
        if (!finishOverlay.isShown()) {
            score.changeTimeBy(lastUpdate);
        }
        ghost.createSlice(lastUpdate, bike.getLeftWheel().getPos(), bike.getRightWheel().getPos(),
                bike.getLeftWheel().getRotation(), bike.getRightWheel().getRotation());
    }

    public void updateSize(int w, int h) {
        currentLevel.updateSize(w, h);
        bike.updateSize(w, h);
        camera.updateSize(w, h);
    }

    public void draw(GameView view) {
        view.setCamera(camera);
        currentLevel.draw(view);
        ghost.draw(view);
        bike.draw(view);
        physicsSimulator.draw(view);
        score.draw(view);
        finishOverlay.draw(view);
        particleManager.draw(view);
        currentLevel.drawForeground(view);
    }

    private void onHighscoreNameSubmitted(boolean enteredName, String name) {
        askingForHighScore = false;
        // Save the ghost only if a name was entered.
        if (enteredName) {
            try {
                ghost.save(name);
            } catch (IOException e) {
                Log.e("GameState", "Error saving Ghost: " + e.toString());
                throw new RuntimeException(e.toString());
            }
        }

        // Choose the next level.
        LevelInfo.LevelID nextLevel = LevelInfo.getNextLevel(
                currentLevel.getLevelID());
        // Make sure to reset the slow motion.
        setSlowMotion(false);
        // Start a new game in the next level.
        gameScene.newGame(nextLevel, bike.getCharacterType(),
                bike.getBodyType(), bike.getColor());
    }

    public void onTouchEvent(MotionEvent event) {
        if (!finishOverlay.isShown()) {
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
                    // Play the sound only if is not already playing.
                    if (!getAssetLoader().getSoundByName("bikeEngineRev.mp3").isPlaying() &&
                            this.settings.isBoolSoundEnabled()) {
                        getAssetLoader().getSoundByName("bikeEngineRev.mp3").play(false);
                    }
                    break;
            }
        }

        FinishOverlay.OverlayResult result = finishOverlay.onTouchEvent(event);
        switch (result) {
            case Continue:
                if (!askingForHighScore) {
                    score.setCallbacks(new HighScore.HighScoreCallbacks() {
                        @Override
                        public void onNameEntered(boolean enteredName, String name) {
                            onHighscoreNameSubmitted(enteredName, name);
                        }
                    });
                    score.askName(true);
                    askingForHighScore = true;
                }
                break;
            case RestartLevel:
                setSlowMotion(false);
                gameScene.newGame(currentLevel.getLevelID(), bike.getCharacterType(),
                        bike.getBodyType(), bike.getColor());
                break;
            case ShowMainMenu:
                setSlowMotion(false);
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
        if (value) {
            Simulator.setUpdateRate(200);
        }
        else {
            Simulator.setUpdateRate(GameLoop.targetFPS);
        }
    }

    public void endGame(boolean crashed) {
        if (!gameEnded) {
            gameEnded = true;
            setSlowMotion(true);
            if (!crashed) {
                assetLoader.getSoundByName("win.mp3").play();
                if (!ghost.isFinished()) {
                    ghost.finish();
                }
                finishOverlay.show(false, ghost.getCurrentTime(), ghost.getTimeDiff());
            } else {
                finishOverlay.show(true, ghost.getCurrentTime(), -1);
                assetLoader.getSoundByName("crash.mp3").play();
            }
        }
    }
}

