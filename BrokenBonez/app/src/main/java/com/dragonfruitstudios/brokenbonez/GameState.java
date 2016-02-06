package com.dragonfruitstudios.brokenbonez;

public class GameState {
    Level currentLevel;
    Bike bike;

    boolean paused;
    float debugStep;

    public GameState() {
        currentLevel = new Level();
        bike = new Bike(currentLevel);

        debugStep = -1;
        paused = true;
    }

    /**
     * Separate method so that it can be used when debugging.
     * @param ms
     */
    public void step(float ms) {
        bike.update(ms);
    }

    public void update(float lastUpdate) {
        if (!paused) {
            step(lastUpdate);
        }

        if (debugStep > 0) {
            step(debugStep);
        }
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

    // The following are used when debugging.

    public void pause() {
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public void resume() {
        paused = false;
    }

    public void setDebugStep(float debugStep) {
        this.debugStep = debugStep;
    }

    public float getDebugStep() {
        return debugStep;
    }
}

