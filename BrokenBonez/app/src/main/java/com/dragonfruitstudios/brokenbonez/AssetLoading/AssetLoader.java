package com.dragonfruitstudios.brokenbonez.AssetLoading;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.SoundEffectConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.dragonfruitstudios.brokenbonez.AssetLoading.SoundEffect;

public class AssetLoader {
    /*
    Initialise new AssetLoader by passing in the activty and an array of string which are the name of the asset (ignoring the /img/ prefix).
    E.g.
    String[] s = {"bob.png", "jim.png", "sam.png"};
    AssetLoader a = new AssetLoader(this, s);
     */
    public AssetLoader(Activity activity, String[] assets){
        this.activity = activity;
        AddAssets(activity, assets);
    }


    HashMap<String, Bitmap> images=new HashMap<String, Bitmap>();
    HashMap<String, Sound> sounds=new HashMap<String, Sound>();
    Activity activity;
    SoundPool soundPool;

    /*
    Add assets to the asset manager. Make sure to pass in an array of strings which are the name of the asset (ignoring the /img/ prefix).
    Returns the new asset loader.
    E.g.
    String[] s = {"bob.png", "jim.png", "sam.png"};
    a.AddAssets(this, s);
     */
    public AssetLoader AddAssets(Activity activity, String[] assets){
        AssetManager assetManager = activity.getAssets();
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        for (int count = 0; count < assets.length; count++) {
            try {
                MediaPlayer mp = new MediaPlayer();
                AssetFileDescriptor df = assetManager.openFd(assets[count]);
                mp.setDataSource(df.getFileDescriptor(), df.getStartOffset(), df.getLength());
                int length = mp.getDuration();

            if ((assets[count].substring(assets[count].length() - 3).equals("mp3")) && (length < 2000)) {
                this.sounds.put(assets[count], loadInSoundEffect(assetManager, "sound/" + assets[count]));
            } else if (assets[count].substring(assets[count].length() - 3).equals("mp3")){
                Sound x = loadInMusicFile(assetManager, "sound/" + assets[count]);
                this.sounds.put(assets[count], loadInMusicFile(assetManager, "sound/" + assets[count]));
            }
            else
            {
                this.images.put(assets[count], loadInBitmap(assetManager, "img/" + assets[count]));
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public Bitmap getBitmapByName(String key) {
        /*
        Get a bitmap based on its name (including extension)
        E.g.
        a.getBitmapByName("jim.png");
         */
        Bitmap b;
        if (this.images.containsKey(key)){
            b = this.images.get(key);
        } else{
            b = null;
            //Log.e("AssetLoader", "Unable to load asset with name " + key);
            throw new RuntimeException("Unable to select asset as asset does not exist with that name.");
        }
        return b;
    }

    public Sound getSoundByName(String key){
        /*
        Get a sound based on its name (including extension)
        E.g.
        a.getBitmapByName("jim.mp3");
         */
        Sound s;
        if (this.sounds.containsKey(key)){
            s = this.sounds.get(key);
        } else{
            s = null;
            throw new RuntimeException("Unable to select sound asset as asset does not exist with that name.");
        }
        return s;
    }

    private Sound loadInMusicFile(AssetManager assetM, String asset) {
        Log.e("AssetLoader", "Loading music from " + asset);
        Music music = new Music(this.soundPool, assetM, asset);
        return music;
    }

    private Sound loadInSoundEffect(AssetManager assetM, String asset) {
        Log.e("AssetLoader", "Loading sound effect from " + asset);
        SoundEffect sound = new SoundEffect(this.soundPool, assetM, asset);
        return sound;
    }

    private Bitmap loadInBitmap(AssetManager assetM, String asset) {
        Bitmap image = null;
        InputStream inputStream = null;
        Log.e("AssetLoader", "Loading bitmap " + asset);
        try {
            inputStream = assetM.open(asset);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            image = BitmapFactory.decodeStream(inputStream, null, options);

        } catch (IOException e) {
            Log.e("AssetLoader", "Error loading bitmap: " + e.getMessage());
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("Unable to close inputstream");}
        }
        Log.e("AssetLoader", "Bitmap loaded " + asset);
        return image;
    }




}
