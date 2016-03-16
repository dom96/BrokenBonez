package com.dragonfruitstudios.brokenbonez.ParticleSystem;

import android.graphics.Bitmap;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;
import com.dragonfruitstudios.brokenbonez.Menu.Settings;
import com.dragonfruitstudios.brokenbonez.Menu.SettingsState;

public class ParticleManager implements GameObject{
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    Settings settings;
    private Bitmap[] smokeParticles;
    private ParticleSystem smokeParticleSystem;

    private int i = 0;
    private int j = 2;

    public ParticleManager(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        this.gameSceneManager = gameSceneManager;

        // Load assets.
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[]{"particlesystem/smoke1.png", "particlesystem/smoke2.png", "particlesystem/smoke3.png", "particlesystem/smoke4.png"});

        this.smokeParticles = new Bitmap[]{assetLoader.getBitmapByName("particlesystem/smoke1.png"),
                assetLoader.getBitmapByName("particlesystem/smoke2.png"),
                assetLoader.getBitmapByName("particlesystem/smoke3.png"),
                assetLoader.getBitmapByName("particlesystem/smoke4.png")};

        settings = new Settings(gameSceneManager);
    }

    public void draw(GameView view){
        if(settings.isBoolParticlesEnabled() && ! ( smokeParticleSystem == null)) {
            smokeParticleSystem.doDraw(view);
        }
    }

    public void update(float lastUpdate, VectorF bikePos) {
        if(TouchHandler.cIA == TouchHandler.ControlIsActive.ACTION_GAS_DOWN){
            i = 1;
            i++;
            if(i > 3){
                i = 0;
            }
            j = 1;
        } else {
            i = 0;
            i++;
            if(i > 1){
                i = 0;
            }
            j = 1;
        }
        this.smokeParticleSystem = new ParticleSystem((int) bikePos.y - 25, 790, 100, 1, 10, smokeParticles[i], j, gameSceneManager);

    }
    public void update(float lastUpdate) {
        smokeParticleSystem.updatePhysics((int) lastUpdate);
    }

    public void updateSize(int width, int height) {

    }
}