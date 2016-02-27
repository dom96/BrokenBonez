package com.dragonfruitstudios.brokenbonez.Menu;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class ImageButton {
    Bitmap startGame;
    Bitmap hiScore;
    Bitmap credits;
    final Bitmap scaledStartGame;
    final Bitmap scaledHiScore;
    final Bitmap scaledCredits;
    AssetLoader assetLoader;
    VectorF pos;
    float rotation;
    boolean isTouched;
    Rect rectangle;

    public ImageButton(AssetLoader assetLoader, float x, float y, float width, float height){
        this.rotation = 0;
        this.pos = new VectorF(x, y);
        this.rectangle = new Rect(this.pos, width, height);
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[]{"start.png", "hiscore.png", "credits.png"});
        startGame = assetLoader.getBitmapByName("start.png");
        hiScore = assetLoader.getBitmapByName("hiscore.png");
        credits = assetLoader.getBitmapByName("credits.png");
        this.scaledStartGame = startGame.createScaledBitmap(startGame, 612, 180, false);
        this.scaledHiScore = hiScore.createScaledBitmap(hiScore, 270, 60, false);
        this.scaledCredits = credits.createScaledBitmap(credits, 270, 60, false);
    }

    public void onTouchEvent(MotionEvent event, float x, float y, float width, float height) {
        int pointerIndex = event.getActionIndex();
        int maskedAction = event.getActionMasked();
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                VectorF posCollide = new VectorF(x, y);
                if(rectangle.collidesWith(posCollide)) {
                    if(f.x > x && f.x < width){
                        if(f.y > y && f.y < height) {
                            isTouched = true;
                        }
                    }
                }
                break;
            }
        }
    }
        public boolean isTouched(){
            return isTouched;
        }
    }