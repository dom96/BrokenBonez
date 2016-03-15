package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.Camera;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
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
    VectorF pos;
    float rotation;

    public LevelSelectionState(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        this.gameSceneManager = gameSceneManager;
        this.assetLoader = assetLoader;
        this.physicsSimulator = new Simulator();
        camera = new Camera(0, 0);
        this.assetLoader.AddAssets(new String[]{"selection/levelText.png"});
        background = assetLoader.getBitmapByName("selection/levelText.png");
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
        view.drawImage(background, new Rect(0,0, 1200, 920), new RectF(0,0, Graphics.getScreenWidth(), Graphics.getScreenHeight()), 0);
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

        if(level1.onTouchEvent(event) == true){
            this.gameSceneManager.setScene("gameScene");
        }
        if(level2.onTouchEvent(event) == true){
            this.gameSceneManager.setScene("gameScene");
        }
        if(level3.onTouchEvent(event) == true){
            this.gameSceneManager.setScene("gameScene");
        }
        if(level4.onTouchEvent(event) == true){
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