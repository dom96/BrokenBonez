package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.GameScene;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class MenuState implements GameObject {
    ImageButton startGame;
    ImageButton hiScore;
    ImageButton credits;
    ImageButton settings;
    AssetLoader assetLoader;
    Bitmap background;
    boolean noiseOn;
    boolean noiseWait;
    float waitTime = 0;
    Bitmap noise;
    VectorF pos;
    float rotation;
    GameSceneManager gameSceneManager;
    Settings savedSettings;

    public MenuState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        startGame = new ImageButton("menu/start.png", assetLoader, getScreenWidth() / 2 - getScreenWidth() / 4, getScreenHeight() / 4 + 60, 930, 270);
        hiScore = new ImageButton("menu/highscore.png", assetLoader, getScreenWidth() / 2 - getScreenWidth() / 4, getScreenHeight() / 4 * 2 + 120, 425, 120);
        credits = new ImageButton("menu/credits.png", assetLoader,  getScreenWidth() / 4 * 2 + 15, getScreenHeight() / 4 * 2 + 120, 425, 120);
        settings = new ImageButton("menu/settings.png", assetLoader, (getScreenWidth() / 10) * 9 + 70, (getScreenHeight() / 10) * 9 + 15, 120, 100);
        this.assetLoader.AddAssets(new String[]{"menu/tv.png", "menu/tvnoise.png", "staticnoise.mp3"});
        noise = assetLoader.getBitmapByName("menu/tvnoise.png");
        background = assetLoader.getBitmapByName("menu/tv.png");
        pos = new VectorF(0, 0);
        rotation = 0;
        this.savedSettings = new Settings(gameSceneManager);
    }

    @Override
    public void update(float lastUpdate) {
        if(getNoiseWait() == true){
            waitTime += lastUpdate;
                if(waitTime > 1000) {
                    noiseOn = false;
                    noiseWait = false;
                    waitTime = 0;
                    this.assetLoader.getSoundByName("staticnoise.mp3").stop();
                    startBikeSelectionScreen();
            }
        }
    }

    @Override
    public void updateSize(int width, int height) {
        if (width == 0 || height == 0) {
            return;
        }
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void draw(GameView view) {
        view.drawImage(background, new Rect(0, 0, 1200, 920), new RectF(0, 0, Graphics.getScreenWidth(), Graphics.getScreenHeight()), 0);
        startGame.draw(view);
        hiScore.draw(view);
        credits.draw(view);
        settings.draw(view);
        if(getNoiseOn() == true){
            view.drawImage(noise, new Rect(0, 0, 1200, 920), new RectF(0, 0, Graphics.getScreenWidth(), Graphics.getScreenHeight()), 0);
        }
    }

    /**TODO:
     * @param event
     * Create a timer so the white noise appears for a short duration before transitioning to the
     * screen linked to the button the user pressed(this could be our loading screen here.)
     * Add a way to launch the game-play screen. I can launch it through GameLoop by
     * uncommenting line 36 but unsure how to get it implemented to launch here. Andrew
     * I need a quick explanation on that.
     */
    public void onTouchEvent(MotionEvent event) {
        startGame.onTouchEvent(event);
        hiScore.onTouchEvent(event);
        credits.onTouchEvent(event);
        settings.onTouchEvent(event);

        if(startGame.onTouchEvent(event) == true){
            if (savedSettings.isBoolSoundEnabled()) {
                assetLoader.getSoundByName("staticnoise.mp3").play(false);
            }
            setNoiseOn();
            setNoiseWait();
        }
        if(hiScore.onTouchEvent(event) == true){
            Log.d("HISCORE", "isTouched");
            gameSceneManager.setScene("highScoreScene");
        }
        if(credits.onTouchEvent(event) == true){
            gameSceneManager.setScene("creditsScene");
        }
        if(settings.onTouchEvent(event) == true){
            gameSceneManager.setScene("settingsScene");
        }

    }

    public void startBikeSelectionScreen(){this.gameSceneManager.setScene("bikeSelectionScene");}
    public boolean getNoiseOn(){
        return noiseOn;
    }
    public boolean getNoiseWait(){
        return noiseWait;
    }
    public void setNoiseOn() {
        noiseOn = true;
    }
    public void setNoiseWait(){
        noiseWait = true;
    }
}