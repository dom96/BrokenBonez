package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.BikeSelectionScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.CreditsScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.GameScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.HighScoreScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.LevelSelectionScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.MenuScene;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.SettingsScene;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class SplashState implements GameObject {
    AssetLoader assetLoader;
    GameSceneManager gameSceneManager;
    Bitmap splashScreen;
    final Bitmap scaledSplashScreen;
    boolean splashOn = true;
    boolean splashWait = true;
    float waitTime = 0;
    VectorF pos;
    float rotation;
    boolean hasDrawn = false;
    boolean hasInitialized = false;


    public SplashState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        this.assetLoader.AddAssets(new String[]{"menu/dragonfruitstudiossplash.png"});
        splashScreen = assetLoader.getBitmapByName("menu/dragonfruitstudiossplash.png");
        scaledSplashScreen = splashScreen.createScaledBitmap(splashScreen, getScreenWidth(), getScreenHeight(), false);
        pos = new VectorF(0, 0);
        rotation = 0;
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    @Override
    public void update(float lastUpdate) {
        if(hasDrawn == true && hasInitialized == false){
            CreditsScene creditsScene = new CreditsScene(assetLoader, gameSceneManager);
            MenuScene menuScene = new MenuScene(assetLoader, gameSceneManager);   //Create the new MenuScene
            GameScene gameScene = new GameScene(assetLoader, gameSceneManager);   //Create the new GameScene
            HighScoreScene highScoreScene = new HighScoreScene(assetLoader, gameSceneManager);
            BikeSelectionScene bikeSelectionScene = new BikeSelectionScene(assetLoader, gameSceneManager);
            LevelSelectionScene levelSelectionScene = new LevelSelectionScene(assetLoader, gameSceneManager);
            SettingsScene settingsScene = new SettingsScene(assetLoader, gameSceneManager);
            this.gameSceneManager.addScene("menuScene", menuScene, false);  //Add the MenuScene just created to the GameSceneManager, then sets it as the active scene
            this.gameSceneManager.addScene("gameScene", gameScene, false); //Add the Gamescene just created to the GameSceneManager, then makes sure it isn't set as active
            this.gameSceneManager.addScene("bikeSelectionScene", bikeSelectionScene, false);
            this.gameSceneManager.addScene("levelSelectionScene", levelSelectionScene, false);
            this.gameSceneManager.addScene("highScoreScene", highScoreScene, false);
            this.gameSceneManager.addScene("settingsScene", settingsScene, false);
            this.gameSceneManager.addScene("creditsScene", creditsScene, false);
            hasInitialized = true;
        }
        if(getSplashWait() == true){
            waitTime += lastUpdate;
            if(waitTime > 5000) {
                splashOn = false;
                splashWait = false;
                waitTime = 0;
                this.gameSceneManager.setScene("menuScene");
            }
        }
    }

    @Override
    public void updateSize(int width, int height) {

    }

    @Override
    public void draw(GameView view) {
        view.drawImage(scaledSplashScreen, pos, rotation, GameView.ImageOrigin.TopLeft);
        hasDrawn = true;
    }

    public boolean getSplashWait(){
        return splashWait;
    }
}
