package com.dragonfruitstudios.brokenbonez.ParticleSystem;

import android.graphics.Bitmap;

import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Gameplay.GameState;
import com.dragonfruitstudios.brokenbonez.Input.TouchHandler;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;
import com.dragonfruitstudios.brokenbonez.Menu.Settings;
import com.dragonfruitstudios.brokenbonez.Menu.SettingsState;

public class ParticleManager {
    private AssetLoader assetLoader;
    private GameSceneManager gameSceneManager;
    private GameState state;
    Settings settings;
    private Bitmap[] smokeParticles;
    private Bitmap mudParticles;
    private ParticleSystem smokeParticleSystem;
    private ParticleSystem mudParticleSystem;
    private boolean mudBoolean;

    private int i = 0;
    private int j = 2;

    public ParticleManager(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        this.gameSceneManager = gameSceneManager;

        // Load assets.
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[]{"particlesystem/smoke1.png", "particlesystem/smoke2.png", "particlesystem/smoke3.png", "particlesystem/smoke4.png",
                "particlesystem/fire.png", "particlesystem/mud.png", "particlesystem/nomud.png"});

        this.smokeParticles = new Bitmap[]{assetLoader.getBitmapByName("particlesystem/smoke1.png"),
                assetLoader.getBitmapByName("particlesystem/smoke2.png"),
                assetLoader.getBitmapByName("particlesystem/smoke3.png"),
                assetLoader.getBitmapByName("particlesystem/smoke4.png"),
                assetLoader.getBitmapByName("particlesystem/fire.png")};

        this.mudParticles = assetLoader.getBitmapByName("particlesystem/mud.png");
        settings = SettingsState.load(gameSceneManager.gameView.getContext());


    }

    public void update(float lastUpdate, VectorF bikePos){
        if(TouchHandler.cIA == TouchHandler.ControlIsActive.ACTION_GAS_DOWN){
            i = 1;
            i++;
            if(i > 3){
                i = 0;
            }
            j = 4;
        } else {
            i = 0;
            i++;
            if(i > 1){
                i = 0;
            }
            j = 4;
        }
        this.smokeParticleSystem = new ParticleSystem((int) bikePos.y - 25, 1030, 100, 10, 100, smokeParticles[i], j, gameSceneManager);
        smokeParticleSystem.updatePhysics((int) lastUpdate);
        this.mudParticleSystem = new ParticleSystem(720, 1280, 100, 10, 100, mudParticles, 4, gameSceneManager);
        mudParticleSystem.updatePhysics((int) lastUpdate);
    }

    public void draw(GameView view){
        if(settings.boolParticlesEnabled == true) {
            smokeParticleSystem.doDraw(view);
            mudParticleSystem.doDraw(view);
        }
    }
}