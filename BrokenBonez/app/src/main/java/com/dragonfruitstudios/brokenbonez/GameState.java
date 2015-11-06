package com.dragonfruitstudios.brokenbonez;

public class GameState {
    Level currentLevel;



    public GameState() {
        currentLevel = new Level();

    }

    public void update() {


    }

    public void draw(GameView gameView) {
        currentLevel.draw(gameView);

    }

}
