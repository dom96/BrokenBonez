package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.util.Log;

/**Core game loop class which handles drawing and updating of the game.**/
public class GameLoop implements Runnable {
    long lastTime = System.nanoTime();
    final int targetFPS;
    final long targetTime;
    volatile boolean run = false;
    GameView gameView;
    GameState gameState;

    /**A method for taking the input fps i.e. fps entered when declaring a new game loop in GameActivity class or
     * the fps we want our game loop to constantly run at.
     */
    public GameLoop(int inputFPS, GameView gameView) {
        targetFPS = inputFPS;
        targetTime = 1000000000 / targetFPS;
        this.gameView = gameView;

        // TODO: The following would be so much simpler and nicer if Android Studio would support
        // Java 8 :(
        // Could we use retrolambda? https://github.com/evant/gradle-retrolambda
        // If so the code would just be something like:
        // this.gameView.setDrawingFunction(this::gameDraw);
        this.gameView.setCallbacks(new GameView.GVCallbacks() {
            @Override
            public void performDraw(GameView gameView) {
                gameDraw(gameView);
            }

            @Override
            public void onSizeChanged(GameView gameView, int w, int h, int oldw, int oldh) {
                gameUpdateSize(w, h);
            }
        });
        this.gameState = new GameState(gameView);
    }

    long lastFPSTime;
    int counter;

    // These flags are used to report the current FPS.
    long lastFPSReport = System.currentTimeMillis(); // The time that FPS was reported last.
    long currFrames = 0; // The current amount of frames rendered.
    long currFPS = 0; // The current FPS

    @Override
    public void run(){
        while (true) { //
            long presentTime = System.nanoTime(); // Sets the present time to the current system time.
            long sleepTime; // Variable for the amount of time the thread needs to sleep for.
            long updateTime = presentTime - lastTime; /**Setting the update time to the present time
                                    subtracted from the last time in which the game loop was run.**/
            lastTime = presentTime; // Setting the last time to the present time.
            lastTime += updateTime; // Adding the update time to the last time and setting the result to last time.
            counter++;

            /**if statement for checking if the last fps time was over 1 million.
             * If it was then set the lastFPSTime to 0**/
            if (lastFPSTime >= 1000000000) {
                lastFPSTime = 0;
                counter = 0;
            }

            gameUpdate();
            gameView.postInvalidate();
            currFrames++;


            lastTime = System.nanoTime(); // Sets the last time the loop was run to the present time.
            sleepTime = (targetTime + (lastTime - presentTime)); /** Sleep time will be equal to the last time subtracted
                                                                        from the current time added to the target time.
                                                                    If the sleep time is more than 0 it will try to sleep
                                                                        for the sleep time divided by 1 million in long type**/
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000L);
                }
                catch (InterruptedException exc) {
                    Log.d("Error", "Interrupted exception was caught."); // Catches errors in relation to Thread.sleep
                }
            }
            // Report the amount of frames that have been rendered.
            if (System.currentTimeMillis() - lastFPSReport >= 1000) {
                Log.d("FPS", "Current FPS: " + currFPS );
                currFPS = currFrames;
                currFrames = 0;
                lastFPSReport = System.currentTimeMillis();
            }
        }
    }

    // Update method
    protected void gameUpdate() {
        //Log.d("Loop", "Updating" + counter);
        gameState.update();
    }

    protected void gameUpdateSize(int w, int h) {
        gameState.updateSize(w, h);
    }

    protected void gameDraw(GameView gameView) {
        gameView.clear(Color.BLACK);

        gameState.draw();

        gameView.drawText("FPS: " + currFPS, 20, 30, Color.WHITE);
    }

    // Pause method for handling the game when it is paused i.e. when the user minimizes the game.
    public void pause(){
        run = false;
    }

    // Resume method for handling the game when the game is resumed i.e. when the user resumes the game from the android menu.
    public void resume(){
        run = true;
    }
}
