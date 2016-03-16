package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.graphics.Color;
import android.hardware.SensorEvent;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Levels.LevelInfo;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Input.Accelerometer;
import com.dragonfruitstudios.brokenbonez.Menu.Settings;
import com.dragonfruitstudios.brokenbonez.Menu.SettingsState;

/**
 * Implements the game scene in which the majority of the GamePlay occurs.
 *
 * This Scene has a method which starts a brand new game, based on a number of parameter which
 * customise the game. This method is called `newGame`.
 */
public class GameScene extends Scene {
    Settings settings;
    GameState state;

    public GameScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        // Call Scene's constructor which will store the asset loader and game scene manager
        // for us.
        super(assetLoader, gameSceneManager);
        // Create a default GameState instance.
        this.state = new GameState(this, assetLoader, this.gameSceneManager,
                LevelInfo.LevelID.Level1, Bike.CharacterType.Leslie,
                Bike.BodyType.Bike, Color.BLUE);
    }

    /**
     * Starts a new game with the specified options.
     * @param levelID The level to start the game on.
     * @param characterType The character to put on the bike.
     * @param bikeBodyType The bike body type the player will use in this game.
     * @param bikeColor The bike color the player will use in this game.
     */
    public void newGame(LevelInfo.LevelID levelID, Bike.CharacterType characterType,
                        Bike.BodyType bikeBodyType, int bikeColor) {
        // The easiest way to create a new game is to recreate the GameState. Resetting the state
        // manually is very difficult to do reliably.
        this.state = new GameState(this, assetLoader, this.gameSceneManager,
                levelID, characterType, bikeBodyType, bikeColor);
        // The state needs to receive at least one `updateSize` call to draw the Game properly.
        this.state.updateSize(gameSceneManager.gameView.getWidth(),
                gameSceneManager.gameView.getHeight());
    }

    public void draw(GameView view) {
        state.draw(view);
    }
    public void update(float lastUpdate) {
        state.update(lastUpdate);
    }
    public void updateSize(int w, int h) {
        state.updateSize(w, h);
    }
    public void onTouchEvent(MotionEvent event) {
        state.onTouchEvent(event);
    }

    @Override
    public void activate() {
        settings = SettingsState.load(gameSceneManager.gameView.getContext());
        if (settings.boolSoundEnabled) {
            state.getAssetLoader().getSoundByName("bikeEngine.mp3").play(true);
            state.getAssetLoader().getSoundByName("bikeEngine.mp3").setVolume(0.5f);
            state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").setVolume(1f);
            state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").play(true);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (settings.boolAccelEnabled) {
            state.setBikeTilt(Accelerometer.calculateTiltStrength(event));
        }
    }
}