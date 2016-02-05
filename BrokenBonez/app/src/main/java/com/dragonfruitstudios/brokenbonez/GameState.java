package com.dragonfruitstudios.brokenbonez;

public class GameState {
    Level currentLevel;
    Bike bike;
    GameView gameView;
    Button button;

    public GameState(GameView gameView) {
        currentLevel = new Level(gameView);
        bike = new Bike(currentLevel.getStartPoint());
        button = new Button();
        this.gameView = gameView;
    }

    public void update(float lastUpdate) {
        bike.update(lastUpdate, currentLevel);
    }

    public void updateSize(int w, int h) {
        bike.updateStartPos(currentLevel.getStartPoint(w, h));
        currentLevel.updateSize(w, h);
    }

    public void draw() {
        currentLevel.draw();
        bike.draw(gameView);
        button.draw(gameView);
    }

    public void setBikeAcceleration(float strength) {
        bike.setAcceleration(strength);
    }
}
