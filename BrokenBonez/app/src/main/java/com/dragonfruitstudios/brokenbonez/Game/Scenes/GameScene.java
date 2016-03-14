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

    // TODO: Put this into Graphics.
    public int getScreenWidth() {return Resources.getSystem().getDisplayMetrics().widthPixels;}

    public void newGame(Bike.BodyType bikeBodyType, int bikeColor) {
        this.state.newGame(bikeBodyType, bikeColor);
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
        // Determine what action the user performed.
        // TODO: Clean this up.
        TouchHandler.ControlIsActive action = TouchHandler.determineAction(event, getScreenWidth() / 2);
        Log.d("GameActivity/Touch", action.toString());
        switch (action) {
            case ACTION_NONE:
                state.setBikeAcceleration(TouchHandler.getAccel());
                break;
            case ACTION_GAS_DOWN:
                state.setBikeAcceleration(TouchHandler.getAccel());
                state.getAssetLoader().getSoundByName("bikeEngineRev.mp3").play(false);   //Nearly ready, still little more test
                break;
            case ACTION_GAS_UP:
                state.setBikeAcceleration(TouchHandler.getAccel());
                break;
            case ACTION_BRAKE_DOWN:
                // TODO: This should brake not move the wheel backward.
                state.setBikeAcceleration(TouchHandler.getAccel());
                break;
            case ACTION_BRAKE_UP:
                state.setBikeAcceleration(TouchHandler.getAccel());
                break;
        }

        state.onTouchEvent(event);
    }

    @Override
    public void activate() {
        state.getAssetLoader().getSoundByName("bikeEngine.mp3").play(true);
        state.getAssetLoader().getSoundByName("bikeEngine.mp3").setVolume(0.5f);
        state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").setVolume(1f);
        state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").play(true);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
            accelerometer.onSensorChanged(event);
            //Log.d("RETURN VALUE", "" + Accelerometer.getReturnValue());
            // TODO: Test this when bike method is created.
            Accelerometer.getReturnValue();
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