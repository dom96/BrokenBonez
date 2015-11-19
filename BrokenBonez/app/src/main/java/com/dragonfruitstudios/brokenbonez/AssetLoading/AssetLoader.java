package com.dragonfruitstudios.brokenbonez.AssetLoading;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class AssetLoader {
    /*
    Initialise new AssetLoader by passing in the activty and an array of string which are the name of the asset (ignoring the /img/ prefix).
    E.g.
    String[] s = {"bob.png", "jim.png", "sam.png"};
    AssetLoader a = new AssetLoader(this, s);
     */
    public AssetLoader(Activity activity, String[] assets){
        AddAssets(activity, assets);
    }


    HashMap<String, Bitmap> assets=new HashMap<String, Bitmap>();
    /*
    Add assets to the asset manager. Make sure to pass in an array of strings which are the name of the asset (ignoring the /img/ prefix).
    Returns the new asset loader.
    E.g.
    String[] s = {"bob.png", "jim.png", "sam.png"};
    a.AddAssets(this, s);
     */
    public AssetLoader AddAssets(Activity activity, String[] assets){
        AssetManager assetManager = activity.getAssets();
        for (int count = 0; count < assets.length; count++) {
            this.assets.put(assets[count], loadInBitmap(assetManager, "img/" + assets[count]));
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
        if (this.assets.containsKey(key)){
            b = this.assets.get(key);
        } else{
            b = null;
            //Log.e("AssetLoader", "Unable to load asset with name " + key);
            throw new RuntimeException("Unable to select asset as asset does not exist with that name.");
        }
        return b;
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
