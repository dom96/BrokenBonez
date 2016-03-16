package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.view.MotionEvent;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Menu.CreditsState;
import com.dragonfruitstudios.brokenbonez.Menu.Settings;

public class CreditsScene extends Scene {
    CreditsState state;
    Settings settings;

    public CreditsScene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        super(assetLoader, gameSceneManager);
        state = new CreditsState(assetLoader, gameSceneManager);
    }

    public void activate(){
        this.settings = new Settings(gameSceneManager);
        if(settings.isBoolSoundEnabled()) {
            state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").setVolume(0.6f);
            state.getAssetLoader().getSoundByName("brokenboneztheme.ogg").play(true);
        }
    }

    public void draw(GameView view) {
        state.draw(view);
    }
    public void update(float lastUpdate) {
        state.update(lastUpdate);
    }
    public void updateSize(int width, int height) {state.updateSize(width, height);}
    public void onTouchEvent(MotionEvent event) {
        state.onTouchEvent(event);
    }
}
