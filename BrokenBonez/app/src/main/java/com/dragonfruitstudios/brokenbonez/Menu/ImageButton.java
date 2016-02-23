package com.dragonfruitstudios.brokenbonez.Menu;


import android.graphics.Bitmap;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;

public class ImageButton {
    Bitmap startGame;
    AssetLoader assetLoader;

    public ImageButton(AssetLoader assetLoader){
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[]{"start_game.png"});
        startGame = assetLoader.getBitmapByName("start_game.png");
    }
}
