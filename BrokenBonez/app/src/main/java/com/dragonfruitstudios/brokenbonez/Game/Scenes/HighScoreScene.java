package com.dragonfruitstudios.brokenbonez.Game.Scenes;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.MotionEvent;
import com.dragonfruitstudios.brokenbonez.AssetLoading.AssetLoader;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.GameSceneManager;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScore;
import com.dragonfruitstudios.brokenbonez.HighScores.HighScoreData;
import com.dragonfruitstudios.brokenbonez.Menu.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class HighScoreScene extends Scene {
    private GameView gameView;
    protected ArrayList<HighScoreData> currentHighScores = new ArrayList<HighScoreData>();
    private HighScore highScore;
    private ImageButton clearHighScore;

    public HighScoreScene(AssetLoader assetLoader, GameSceneManager gameSceneManager){
        super(assetLoader, gameSceneManager);
        this.gameView = gameSceneManager.gameView;
        this.highScore = new HighScore(this.gameView);
        clearHighScore = new ImageButton("clear.png", assetLoader, 120, 840, 340, 76);
    }

    public void updateCurrentHighScoresList(){
        List<HighScore.PrefData> highScoreList = highScore.getSortedList();
        currentHighScores.clear();
        for (int count = 0; count < highScoreList.size(); count++) {
            HighScoreData h = new HighScoreData(highScoreList.get(count).key, highScoreList.get(count).value);
            this.currentHighScores.add(h);
        }
    }

    public int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        clearHighScore.onTouchEvent(event);
        if(clearHighScore.onTouchEvent(event) == true){
                currentHighScores.clear();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void activate() {
        updateCurrentHighScoresList();

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void update(float lastUpdate) {

    }

    @Override
    public void updateSize(int width, int height) {

    }

    @Override
    public void draw(GameView view) {
        clearHighScore.draw(view);
        HighScoreData highScore;
        gameView.drawText("High Scores", getScreenWidth() / 2, 100, Color.WHITE, 80);
        gameView.drawText("Name", (getScreenWidth() / 6) * 2, (210), Color.WHITE, 74);
        gameView.drawText("Time(Sec)", (getScreenWidth() / 6) * 3, (210), Color.WHITE, 74);
        gameView.drawText("Score", (getScreenWidth() / 6) * 4, (210), Color.WHITE, 74);
        for (int i = this.currentHighScores.size() -1; i >= 0; i--) {
            highScore = currentHighScores.get(i);
            gameView.drawText(highScore.name, (getScreenWidth() / 6) * 2, (300 + (i * 70)), Color.WHITE, 66);
            gameView.drawText("" + (highScore.time/1000), (getScreenWidth() / 6) * 3, (300 + (i * 70)), Color.WHITE, 66);
            gameView.drawText("" + highScore.score, (getScreenWidth() / 6) * 4, (300 + (i * 70)), Color.WHITE, 66);
        }
    }
}
