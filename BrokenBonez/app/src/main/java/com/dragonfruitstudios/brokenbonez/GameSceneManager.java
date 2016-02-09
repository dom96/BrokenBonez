package com.dragonfruitstudios.brokenbonez;

import android.util.Log;
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
 */

public class GameSceneManager {
    protected GameView gameView;
    private HashMap<String, GameObject> gameScenes=new HashMap<String, GameObject>();
    private String currentScene;


    public GameSceneManager(GameView gameView, String SceneName,  GameObject newGameObject){
        this.gameView = gameView;
        gameScenes.put(SceneName, newGameObject);
        this.currentScene = SceneName;
    }

    public void addScene(String SceneName,  GameObject newGameObject){
        //Adds new scene to the gameScenes hashmap
        gameScenes.put(SceneName, newGameObject);
    }

    public void addScene(String SceneName,  GameObject newGameObject, boolean setScene){
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

    public void draw(GameObject GameScene){
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

    public void update(GameObject GameScene, float lastUpdate){
        // Update the scene. Can be called from anywhere. Can pass in a custom GameScene object
        GameScene.update(lastUpdate);
    }

    public void update(String SceneName, float lastUpdate){
        // Update the scene. Can be called from anywhere. Can pass in a custom GameScene name
        this.getGameSceneByName(SceneName).update(lastUpdate);
    }

    public void setScene(String currentScene){
        // Set currentScene to the passed in name of another scene
        this.currentScene = currentScene;
    }

    public GameObject getCurrentSceneObject(){
        // Get current scene object being drawn/updated
        return this.getGameSceneByName(this.currentScene);
    }

    public String getCurrentSceneString(){
        // Get current scene name being drawn/updated
        return this.currentScene;
    }

    public GameObject getGameSceneByName(String SceneName) {
        // Get a scene object from passing in the string name of it
        GameObject gs;
        if (this.gameScenes.containsKey(SceneName)) {
            gs = this.gameScenes.get(SceneName);
        } else {
            gs = null;
            Log.e("GameScenesLoader", "Unable to get gameScene with name " + SceneName);
            throw new RuntimeException("Unable to select gameScene as gameScene does not exist with that name.");
        }
        return gs;
    }

}
