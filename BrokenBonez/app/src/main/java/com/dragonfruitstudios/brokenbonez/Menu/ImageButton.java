package com.dragonfruitstudios.brokenbonez.Menu;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

public class ImageButton {
    Bitmap image;
    final Bitmap scaledImage;
    String imageName;
    AssetLoader assetLoader;
    VectorF pos;
    float rotation;
    boolean isTouched;
    Rect rectangle;

    public ImageButton(String imageName, AssetLoader assetLoader, float x, float y, float width, float height){
        this.imageName = imageName;
        this.rotation = 0;
        this.pos = new VectorF(x, y);
        this.rectangle = new Rect(this.pos, width, height);
        this.assetLoader = assetLoader;
        this.assetLoader.AddAssets(new String[]{"start.png", "hiscore.png", "credits.png", "next.png", "prev.png", "select.png"});
        this.image = assetLoader.getBitmapByName(imageName);
        this.scaledImage = image.createScaledBitmap(image, (int)width, (int)height, false);

        /**startGame = assetLoader.getBitmapByName("start.png");
        hiScore = assetLoader.getBitmapByName("hiscore.png");
        credits = assetLoader.getBitmapByName("credits.png");
        next = assetLoader.getBitmapByName("next.png");
        prev = assetLoader.getBitmapByName("prev.png");
        select = assetLoader.getBitmapByName("select.png");**/

    }

    public void draw(GameView view) {
        view.drawImage(scaledImage, pos, rotation, GameView.ImageOrigin.TopLeft);
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