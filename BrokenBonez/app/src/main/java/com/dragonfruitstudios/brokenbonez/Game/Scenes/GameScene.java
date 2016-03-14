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

/**
 * Implements the game scene in which the bulk of the gameplay occurs.
 */
public class GameScene extends Scene {
    GameState state;
    Accelerometer accelerometer;

    public GameScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        accelerometer = new Accelerometer();
        this.state = new GameState(assetLoader, this.gameSceneManager);
        newGame(Bike.BodyType.Bike, Color.BLUE);
    }

    public void newGame(Bike.BodyType bikeBodyType, int bikeColor) {
        this.state.newGame(Bike.CharacterType.Leslie, bikeBodyType, bikeColor);
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
        state.getAssetLoader().getSoundByName("bikeEngine.mp3").play(true);
        state.getAssetLoader().getSoundByName("bikeEngine.mp3").setVolume(0.5f);
        state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").setVolume(1f);
        state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").play(true);
        state.score.reset();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            accelerometer.onSensorChanged(event);

            if (Accelerometer.isLeft()) {
                state.setBikeTilt(Accelerometer.getReturnValue());
            }
            else {
                state.setBikeTilt(-Accelerometer.getReturnValue());
            }
            if(Accelerometer.isFlat()){
                state.setBikeTilt(Accelerometer.bikeStill());
            }
            if(Accelerometer.isDown()){
                state.setBikeTilt(Accelerometer.bikeStill());
            }
            if(Accelerometer.getReturnValue() == 0){
                state.setBikeTilt(Accelerometer.bikeStill());
            }
    }
}