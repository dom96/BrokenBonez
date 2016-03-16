package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.graphics.Color;
import android.hardware.SensorEvent;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.LevelInfo;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Input.Accelerometer;
import com.dragonfruitstudios.brokenbonez.Menu.Settings;
import com.dragonfruitstudios.brokenbonez.Menu.SettingsState;

/**
 * Implements the game scene in which the bulk of the gameplay occurs.
 */
public class GameScene extends Scene {
    Settings settings;
    GameState state;

    public GameScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        // Create a default GameState instance.
        this.state = new GameState(this, assetLoader, this.gameSceneManager,
                LevelInfo.LevelID.Level1, Bike.CharacterType.Leslie,
                Bike.BodyType.Bike, Color.BLUE);
    }

    public void newGame(LevelInfo.LevelID levelID, Bike.CharacterType characterType,
                        Bike.BodyType bikeBodyType, int bikeColor) {
        // The easiest way to create a new game is to recreate the GameState. Resetting the state
        // manually is a very difficult to do reliably.
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
        this.settings = new Settings(gameSceneManager);
        if(settings.isBoolSoundEnabled()) {
            state.getAssetLoader().getSoundByName("bikeEngine.mp3").play(true);
            state.getAssetLoader().getSoundByName("bikeEngine.mp3").setVolume(0.5f);
            state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").setVolume(0.6f);
            state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").play(true);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (settings.isBoolAccelEnabled()) {
            state.setBikeTilt(Accelerometer.calculateTiltStrength(event));
        }
    }
}