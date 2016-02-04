package com.dragonfruitstudios.brokenbonez;

public class GameState {
    Level currentLevel;
    Bike bike;

    public GameState() {
        currentLevel = new Level();
        bike = new Bike(currentLevel);
    }

    public void update(float lastUpdate) {
        bike.update(lastUpdate);
    }

    public void updateSize(int w, int h) {
        currentLevel.updateSize(w, h);
        bike.updateSize(w, h);
    }

    public void draw(GameView view) {
        currentLevel.draw(view);
        bike.draw(view);
    }

    public void setBikeAcceleration(float strength) {
        bike.setAcceleration(strength);
    }
}
