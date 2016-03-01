package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.Bike;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;
import com.dragonfruitstudios.brokenbonez.R;
import com.plattysoft.leonids.ParticleSystem;

/**
 * Implements the game scene in which the bulk of the gameplay occurs.
 */
public class GameScene extends Scene {
    GameState state;

    public GameScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        this.state = new GameState(assetLoader, this.gameSceneManager);
        newGame(Bike.BodyType.Bike, Color.BLUE);
    }

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
        TouchHandler.ControlIsActive action = TouchHandler.determineAction(event, getScreenWidth() / 2);
        Log.d("GameActivity/Touch", action.toString());
        switch (action) {
            case ACTION_NONE:
                state.setBikeAcceleration(0.0f);
                break;
            case ACTION_GAS_SLOWEST:
                state.setBikeAcceleration(0.1f);
                state.getAssetLoader().getSoundByName("bikeEngineRev.mp3").play(false);   //Nearly ready, still little more test
                break;
            case ACTION_GAS_SLOW:
                state.setBikeAcceleration(0.2f);
                state.getAssetLoader().getSoundByName("bikeEngineRev.mp3").play(false);   //Nearly ready, still little more test
                break;
            case ACTION_GAS_AVG:
                state.setBikeAcceleration(0.3f);
                state.getAssetLoader().getSoundByName("bikeEngineRev.mp3").play(false);   //Nearly ready, still little more test
                break;
            case ACTION_GAS_FAST:
                state.setBikeAcceleration(0.4f);
                state.getAssetLoader().getSoundByName("bikeEngineRev.mp3").play(false);   //Nearly ready, still little more test
                break;
            case ACTION_GAS_FASTEST:
                state.setBikeAcceleration(0.5f);
                state.getAssetLoader().getSoundByName("bikeEngineRev.mp3").play(false);   //Nearly ready, still little more test
                break;
            case ACTION_GAS_UP:
                state.setBikeAcceleration(0f);
                break;
            case ACTION_BRAKE_DOWN:
                // TODO: This should brake not move the wheel backward.
                state.setBikeAcceleration(-0.5f);
                break;
            case ACTION_BRAKE_UP:
                state.setBikeAcceleration(0f);
                break;
        }
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @Override
    public void activate() {
        state.getAssetLoader().getSoundByName("bikeEngine.mp3").play(true);
        state.getAssetLoader().getSoundByName("bikeEngine.mp3").setVolume(0.5f);
        state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").setVolume(1f);
        state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").play(true);
    }
}
