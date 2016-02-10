package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.util.Log;
import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;

/**
 * Implements the game scene in which the bulk of the gameplay occurs.
 */
public class GameScene implements Scene {
    GameState state;

    public GameScene(AssetLoader assetLoader) {
        this.state = new GameState(assetLoader);
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
        TouchHandler.ControlIsActive action = TouchHandler.determineAction(event, 320);
        Log.d("GameActivity/Touch", action.toString());
        switch (action) {
            case ACTION_GAS_DOWN:
                state.setBikeAcceleration(0.5f);
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
}
