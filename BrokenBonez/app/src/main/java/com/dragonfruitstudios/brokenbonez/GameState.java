package com.dragonfruitstudios.brokenbonez;

public class GameState {
    Level currentLevel;
    Bike bike;

    GameView gameView;

    public GameState(GameView gameView) {
        currentLevel = new Level(gameView);
        bike = new Bike(currentLevel.getStartPoint());
        this.gameView = gameView;
    }

    public void update() {
        bike.update();
    }

    public void updateSize(int w, int h) {
        bike.updateStartPos(currentLevel.getStartPoint(w, h));
    }

    public void draw() {
        currentLevel.draw();
        bike.draw(gameView);
    }

}
