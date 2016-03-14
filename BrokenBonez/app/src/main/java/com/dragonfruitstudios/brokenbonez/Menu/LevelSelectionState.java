package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.Physics.Simulator;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class LevelSelectionState implements GameObject {
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private Simulator physicsSimulator;
    private Camera camera;
    Bitmap background;
    ImageButton level1;
    ImageButton level2;
    ImageButton level3;
    ImageButton level4;
    final Bitmap scaledBackground;
    VectorF pos;
    float rotation;

    public LevelSelectionState(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        this.gameSceneManager = gameSceneManager;
        this.assetLoader = assetLoader;
        this.physicsSimulator = new Simulator();
        camera = new Camera(0, 0);
        this.assetLoader.AddAssets(new String[]{"selection/levelText.png"});
        background = assetLoader.getBitmapByName("selection/levelText.png");
        scaledBackground = background.createScaledBitmap(background, getScreenWidth(), getScreenHeight(), false);
        level1 = new ImageButton("selection/level1.png", assetLoader, (getScreenWidth() / 4 - 320), (getScreenHeight() / 4 - 200), 620, 460);
        level2 = new ImageButton("selection/level2.png", assetLoader, (getScreenWidth() / 4) * 2 + 200, (getScreenHeight() / 4 - 200), 620, 460);
        level3 = new ImageButton("selection/level3.png", assetLoader, (getScreenWidth() / 4) - 320, (getScreenHeight() / 4) * 2 + 60, 620, 460);
        level4 = new ImageButton("selection/level4.png", assetLoader, (getScreenWidth() / 4) * 2 + 200, (getScreenHeight() / 4) * 2 + 60, 620, 460);
        pos = new VectorF(0, 0);
        rotation = 0;
    }

    public int getScreenWidth() {return Resources.getSystem().getDisplayMetrics().widthPixels;}

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    public void update(float lastUpdate) {
        physicsSimulator.update(lastUpdate);
    }

    @Override
    public void updateSize(int w, int h) {
        camera.updateSize(w, h);
    }

    @Override
    public void draw(GameView view) {
        view.drawImage(scaledBackground, pos, rotation, GameView.ImageOrigin.TopLeft);
        view.setCamera(camera);
        physicsSimulator.draw(view);
        level1.draw(view);
        level2.draw(view);
        level3.draw(view);
        level4.draw(view);
    }

    public void onTouchEvent(MotionEvent event) {
        level1.onTouchEvent(event);
        level2.onTouchEvent(event);
        level3.onTouchEvent(event);
        level4.onTouchEvent(event);

        if(level1.isTouched() == true){
            level1.isTouched = false;
            this.gameSceneManager.setScene("gameScene");
        }
        if(level2.isTouched() == true){
            level2.isTouched = false;
            this.gameSceneManager.setScene("gameScene");
        }
        if(level3.isTouched() == true){
            level3.isTouched = false;
            this.gameSceneManager.setScene("gameScene");
        }
        if(level4.isTouched() == true){
            level4.isTouched = false;
            this.gameSceneManager.setScene("gameScene");
        }
    }

    public AssetLoader getAssetLoader() {
        return assetLoader;
    }
    public Simulator getPhysicsSimulator() {
        return physicsSimulator;
    }
}