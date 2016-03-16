package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.hardware.SensorEvent;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameObject;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;

/**
 * An interface defining a class that implements a scene.
 *
 * This interface extends the `GameObject` interface, which then extends the `Drawable` interface.
 * As such, it defines a `draw`, `update` and `updateSize` method. See the relevant interfaces for
 * a description of each of them.
 *
 * Used for different game scenes, for example: GameScene (where the actual gameplay takes place),
 * MainMenuScene (where the main menu is drawn and the user can touch a "Start Game" button to start
 * the game, etc).
 */
public abstract class Scene implements GameObject {
    GameSceneManager gameSceneManager;
    public AssetLoader assetLoader;

    public Scene(AssetLoader assetLoader, GameSceneManager gameSceneManager) {
        this.assetLoader = assetLoader;
        this.gameSceneManager = gameSceneManager;
    }

    /**
     * The method which will be called whenever the screen has been touched.
     * @param event Information about the event.
     */
    public abstract void onTouchEvent(MotionEvent event);

    // These methods are optional but can be overridden to receive notifications about the events
    // they represent.

    /**
     * Called when the game is paused.
     */
    public void pause() {}

    /**
     * Called when the game is resumed.
     */
    public void resume() {}

    /**
     * Called when this Scene is shown to the user.
     */
    public void activate() {}

    /**
     * Called when this Scene is hidden from the user and another Scene takes its place.
     */
    public void deactivate() {}

    /**
     * Called when one of the device's sensors changes values.
     */
    public void onSensorChanged(SensorEvent event) {}
}
