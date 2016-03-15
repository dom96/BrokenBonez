package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.util.Log;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Input.Accelerometer;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;
import com.dragonfruitstudios.brokenbonez.Menu.Settings;
import com.dragonfruitstudios.brokenbonez.Menu.SettingsState;

import java.io.IOException;

/**
 * Implements the game scene in which the bulk of the gameplay occurs.
 */
public class GameScene extends Scene {
    Settings settings;
    GameState state;

    public GameScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        this.state = new GameState(assetLoader, this.gameSceneManager);
        newGame(Bike.CharacterType.Leslie, Bike.BodyType.Bike, Color.BLUE);
    }

    public void newGame(Bike.CharacterType characterType, Bike.BodyType bikeBodyType, int bikeColor) {
        this.state.newGame(characterType, bikeBodyType, bikeColor);
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
        if(settings.boolSoundEnabled == true) {
            state.getAssetLoader().getSoundByName("bikeEngine.mp3").play(true);
            state.getAssetLoader().getSoundByName("bikeEngine.mp3").setVolume(0.5f);
            state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").setVolume(1f);
            state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").play(true);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (settings.boolAccelEnabled == true) {
            state.setBikeTilt(Accelerometer.calculateTiltStrength(event));
        }
    }
}