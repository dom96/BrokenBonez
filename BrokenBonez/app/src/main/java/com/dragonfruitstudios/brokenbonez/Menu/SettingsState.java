package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SettingsState implements GameObject {
    ImageButton soundEnabled;
    ImageButton soundDisabled;
    ImageButton accelerometerEnabled;
    ImageButton accelerometerDisabled;
    ImageButton particlesEnabled;
    ImageButton particlesDisabled;
    Bitmap sound;
    Bitmap accelerometer;
    Bitmap particles;
    AssetLoader assetLoader;
    GameSceneManager gameSceneManager;
    Settings settings;
    VectorF soundPos;
    VectorF accelerometerPos;
    VectorF particlesPos;

    public SettingsState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        this.assetLoader.AddAssets(new String[]{"menu/accelerometer.png", "menu/sound.png", "menu/particles.png"});
        soundEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4), 150, 150);
        soundDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4), 150, 150);
        accelerometerEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 2), 150, 150);
        accelerometerDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 2), 150, 150);
        particlesEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4 * 3), 150, 150);
        particlesDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4 * 3), 150, 150);
        sound = assetLoader.getBitmapByName("menu/sound.png");
        accelerometer = assetLoader.getBitmapByName("menu/accelerometer.png");
        particles = assetLoader.getBitmapByName("menu/particles.png");
        createDefaultSettings();
        settings = SettingsState.load(gameSceneManager.gameView.getContext());
        soundPos = new VectorF(getScreenWidth() / 4 * 2, getScreenHeight() / 4);
        accelerometerPos = new VectorF(getScreenWidth() / 4 * 2, getScreenHeight() / 2);
        particlesPos = new VectorF(getScreenWidth() / 4 * 2, getScreenHeight() / 4 * 3);
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void update(float lastUpdate) {

    }

    public void updateSize(int width, int height) {

    }

    public void draw(GameView view) {
        view.drawImage(sound, soundPos, 0, GameView.ImageOrigin.TopLeft);
        view.drawImage(accelerometer, accelerometerPos, 0, GameView.ImageOrigin.TopLeft);
        view.drawImage(particles, particlesPos, 0, GameView.ImageOrigin.TopLeft);

        if(settings.boolSoundEnabled == true) {
            soundEnabled.draw(view);
        } else {
            soundDisabled.draw(view);
        }
        if(settings.boolAccelEnabled == true) {
            accelerometerEnabled.draw(view);
        } else {
            accelerometerDisabled.draw(view);
        }
        if(settings.boolParticlesEnabled == true){
            particlesEnabled.draw(view);
        } else {
            particlesDisabled.draw(view);
        }
    }

    public void onTouchEvent(MotionEvent event) {
        soundEnabled.onTouchEvent(event);
        accelerometerEnabled.onTouchEvent(event);
        particlesEnabled.onTouchEvent(event);

        if(soundEnabled.onTouchEvent(event) == true){
            if(settings.boolSoundEnabled == true){
                settings.boolSoundEnabled = false;
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                settings.boolSoundEnabled = true;
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(accelerometerEnabled.onTouchEvent(event) == true){
            if(settings.boolAccelEnabled == true){
                settings.boolAccelEnabled = false;
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                settings.boolAccelEnabled = true;
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(particlesEnabled.onTouchEvent(event) == true){
            if(settings.boolParticlesEnabled == true){
                settings.boolParticlesEnabled = false;
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                settings.boolParticlesEnabled = true;
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void save() throws IOException{
        String filename = "settings";
        FileOutputStream stream = gameSceneManager.gameView.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
        ObjectOutputStream serialiseStream = new ObjectOutputStream(stream);
        serialiseStream.writeObject(settings);
        serialiseStream.flush();
        serialiseStream.close();
    }

    public static Settings load(Context context){
        String filename = "settings";
        try {
            FileInputStream stream = context.openFileInput(filename);
            ObjectInputStream deserialiseStream = new ObjectInputStream(stream);
            Settings load = (Settings)deserialiseStream.readObject();
            if(load == null){
                load = createDefaultSettings();
            }
            return load;
        }
        catch (Exception exc) {
            Log.e("Setting", "Exception in load: " + exc.toString());
            return createDefaultSettings();
        }
    }

    public static Settings createDefaultSettings(){
        return new Settings(true, true, false);
    }
}