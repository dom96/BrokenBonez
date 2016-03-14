package com.dragonfruitstudios.brokenbonez;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.util.Log;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Scenes.Scene;
import java.util.HashMap;

/**
 * This class implements a game scene manager, allowing using a single GameView object, to switch between
 * different "scenes" (or screens). To use
 * GameSceneManager gameSceneManager = new GameSceneManager(gameView, "SceneName", SceneObject);
 *
 * To add a new scene, use addScene(newSceneName, newGameObject, boolean setScene)
 * The final parameter is optional and if set to true, it will also run setScene with the new scene.
 * By default, it is false.
 *
 * To switch scene, use gameSceneManager.setScene("AwesomeScene")
 *
 * To get the current scene, you can use .getCurrentSceneObject() or .getCurrentSceneString()
 *
 * You can get a scene object from its name by using getGameSceneByName("SceneName)
 *
 * To update, use gameSceneManager.update(lastUpdate)   (Should be handled by GameLoop)
 * You can also pass in a string or object to draw that specific object
 *
 * To draw, use gameSceneManager.draw()     (Should be handled by GameLoop)
 * You can also pass in a string or object to draw that specific object
 *
 * To update current object size, use updateSize(width, length)
 * For a different object by name, use updateSize(sceneName, width, length
 */

public class GameSceneManager {
    public GameView gameView;
    public Activity activity;
    private HashMap<String, Scene> gameScenes=new HashMap<String, Scene>();
    private String currentScene = null;


    public GameSceneManager(GameView gameView, String SceneName,  Scene newGameObject){
        this.gameView = gameView;
        gameScenes.put(SceneName, newGameObject);
        this.currentScene = SceneName;
        this.activity = (Activity) gameView.getContext();
    }

    public GameSceneManager(GameView gameView){
        this.gameView = gameView;
    }

    public void addScene(String SceneName,  Scene newGameObject){
        //Adds new scene to the gameScenes hashmap
        gameScenes.put(SceneName, newGameObject);
    }

    public void addScene(String SceneName,  Scene newGameObject, boolean setScene){
        //Adds new scene to the gameScenes hashmap and if setScene = true, will also set currentScene to that scene
        gameScenes.put(SceneName, newGameObject);
        if (setScene){
            this.setScene(SceneName);
        }
    }

    public void draw(){
        // Draw the scene. Should only be called by GameLoop
        this.getGameSceneByName(currentScene).draw(this.gameView);
    }

    public void draw(Scene GameScene){
        // Draw the scene. Can be called from anywhere. Can pass in a custom GameScene object
        GameScene.draw(this.gameView);
    }

    public void draw(String SceneName){
        // Draw the scene. Can be called from anywhere. Can pass in a custom GameScene name
        this.getGameSceneByName(SceneName).draw(this.gameView);
    }

    public void update(float lastUpdate){
        // Update the scene. Should only be called by GameLoop
        this.getGameSceneByName(this.currentScene).update(lastUpdate);
    }

    public void update(Scene GameScene, float lastUpdate){
        // Update the scene. Can be called from anywhere. Can pass in a custom GameScene object
        GameScene.update(lastUpdate);
    }

    public void update(String SceneName, float lastUpdate){
        // Update the scene. Can be called from anywhere. Can pass in a custom GameScene name
        this.getGameSceneByName(SceneName).update(lastUpdate);
    }

    public void setScene(String currentScene){
        // Deactivate previous scene
        if (this.currentScene != null) {
            getCurrentSceneObject().deactivate();
        }
        // Set currentScene to the passed in name of another scene
        this.currentScene = currentScene;
        // After changing the scene call `updateSize` to let the scene know what the size of
        // the GameView is -DP.
        if (gameView.getWidth() != 0 && gameView.getHeight() != 0) {
            updateSize(gameView.getWidth(), gameView.getHeight());
        }
        else {
            Log.d("GameSceneManager", "Could not update size because GameView doesn't know it.");
        }
        // Run the activate method of the new scene
        this.getCurrentSceneObject().activate();

    }

    public Scene getCurrentSceneObject(){
        // Get current scene object being drawn/updated
        return this.getGameSceneByName(this.currentScene);
    }

    public String getCurrentSceneString(){
        // Get current scene name being drawn/updated
        return this.currentScene;
    }

    public void updateSize(int width, int height){
        // Update size of current scene
        this.getGameSceneByName(this.currentScene).updateSize(width, height);
    }

    public void updateSize(String SceneName, int width, int height){
        // Update size of a specified object by scene name
        this.getGameSceneByName(SceneName).updateSize(width, height);
    }

    public Scene getGameSceneByName(String SceneName) {
        // Get a scene object from passing in the string name of it
        Scene gs;
        if (this.gameScenes.containsKey(SceneName)) {
            gs = this.gameScenes.get(SceneName);
        } else {
            gs = null;
            Log.e("GameScenesLoader", "Unable to get gameScene with name " + SceneName);
            throw new RuntimeException("Unable to select gameScene as gameScene does not exist with that name.");
        }
        return gs;
    }

    // Called when the user minimizes the game.
    // or when the 'P' key is pressed (when debugging in an emulator).
    public void pause() {
        this.getCurrentSceneObject().pause();
    }

    // Called when the user resumes the game from the android menu.
    public void resume() {
        this.getCurrentSceneObject().resume();
    }

    public void onSensorChanged(SensorEvent event) {
        this.getCurrentSceneObject().onSensorChanged(event);}
}