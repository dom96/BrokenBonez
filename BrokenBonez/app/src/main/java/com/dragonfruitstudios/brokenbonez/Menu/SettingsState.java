package com.dragonfruitstudios.brokenbonez.Menu;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// tick boxes
public class SettingsState implements GameObject {
    ImageButton soundEnabled;
    ImageButton soundDisabled;
    ImageButton accelerometerEnabled;
    ImageButton accelerometerDisabled;
    ImageButton particlesEnabled;
    ImageButton particlesDisabled;
    AssetLoader assetLoader;
    GameSceneManager gameSceneManager;
    Settings settings;

    public SettingsState(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
        soundEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4), 150, 150);
        soundDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4), 150, 150);
        accelerometerEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 2), 150, 150);
        accelerometerDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 2), 150, 150);
        particlesEnabled = new ImageButton("menu/checked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4 * 3), 150, 150);
        particlesDisabled = new ImageButton("menu/unchecked.png", assetLoader, (getScreenWidth() / 4), (getScreenHeight() / 4 * 3), 150, 150);
        settings = new Settings(true, true, true);
        settings = SettingsState.load(gameSceneManager.gameView.getContext());
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
                load = new Settings(true, true, true);
            }
            return load;
        }
        catch (Exception exc) {
            Log.e("Setting", "Exception in load: " + exc.toString());
        }
        return null;
    }
}