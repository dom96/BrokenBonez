package com.dragonfruitstudios.brokenbonez.AssetLoading;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    /*
    Initialise new AssetLoader by passing in the activity and an array of string which are the name of the asset (ignoring the /img/ or /sound/ prefix).
    You can use .mp3 files for sounds/music and .png files for images. The assetloader supports mixing and matching of images/sounds/music.
    For the difference between sounds/music, SoundPool is used for sound shorter than 2 seconds (2000ms), while MediaPlayer is used for longer sounds, like music.
    E.g.
    String[] s = {"bob.png", "jim.mp3", "sam.png"};
    AssetLoader a = new AssetLoader(this, s);
    If you want to add a new asset later, use

    To later get an image, you can use a.getBitmapByName("bob.png")
    To play a sound, you can use a.getSoundByName("jim.mp3").play()
    For sounds, the Sound object also has some other methods available including .pause, .resume

     */
    public AssetLoader(Activity activity, String[] assets){
        this.activity = activity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
            this.soundPool = new SoundPool.Builder().setMaxStreams(5).setAudioAttributes(audioAttributes).build();
        }
        else
        {
            this.soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }

        this.assetManager = this.activity.getAssets();
        AddAssets(assets);
    }


    HashMap<String, Bitmap> images=new HashMap<String, Bitmap>();
    HashMap<String, Sound> sounds=new HashMap<String, Sound>();
    Activity activity;
    SoundPool soundPool;
    AssetManager assetManager;

    /*
    To add an asset later  to the asset manager. Make sure to pass in an array of strings which are the name of the asset (ignoring the /img/ or /sound/ prefix).
    Returns the new asset loader.
    E.g.
    String[] s = {"bob.png", "jim.mp3", "sam.png"};
    a.AddAssets(s);
     */
    @SuppressWarnings("deprecation")
    public AssetLoader AddAssets(String[] assets){
        for (int count = 0; count < assets.length; count++) {
            long length = 0;
            try {
                if (isSound(assets[count])) {
                    AssetFileDescriptor df = assetManager.openFd("sound/" + assets[count]);
                    length = df.getLength();
                }
            if ((isSound(assets[count])) && (length < 1000000)) {
                this.sounds.put(assets[count], loadInSoundEffect(assetManager, "sound/" + assets[count]));
            } else if (isSound(assets[count])){
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

    private boolean isSound(String filePath){
        // Returns true if the provided string is a sound (uses .mp3 or .ogg)
        if (filePath.substring(filePath.length() - 3).equals("mp3") || filePath.substring(filePath.length() - 3).equals("ogg")){
            return true;
        }
        else
        {
            return false;
        }
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
            Log.e("AssetLoader", "Unable to load asset with name " + key);
            throw new RuntimeException("Unable to select asset as asset does not exist with that name.");
        }
        return b;
    }

    public Sound getSoundByName(String key){
        /*
        Get a sound based on its name (including extension). Doesn't matter if is a sound effect or music.
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

        Log.d("AssetLoader", "Loading music from " + asset);
        Music music = new Music(this.soundPool, assetM, asset);
        return music;
    }

    private Sound loadInSoundEffect(AssetManager assetM, String asset) {
        Log.d("AssetLoader", "Loading sound effect from " + asset);
        SoundEffect sound = new SoundEffect(this.soundPool, assetM, asset);
        return sound;
    }

    private Bitmap loadInBitmap(AssetManager assetM, String asset) {
        Bitmap image = null;
        InputStream inputStream = null;
        Log.d("AssetLoader", "Loading bitmap " + asset);
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
        Log.d("AssetLoader", "Bitmap loaded " + asset);
        return image;
    }

    public void pause(){
        this.soundPool.autoPause();
        for(Map.Entry<String, Sound> entry : sounds.entrySet()) {
            entry.getValue().pause();
        }
    }

    public void resume(){
        this.soundPool.autoResume();
        for(Map.Entry<String, Sound> entry : sounds.entrySet()) {
            entry.getValue().resume();
        }
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

}
