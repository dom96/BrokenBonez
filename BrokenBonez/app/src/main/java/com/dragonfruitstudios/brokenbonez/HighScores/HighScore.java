package com.dragonfruitstudios.brokenbonez.HighScores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.EditText;

import com.dragonfruitstudios.brokenbonez.Game.GameView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * High score class
 * This class allows you to set up a high score in the game, get the users name (using askName()) and save the high score (using addHighScoreData())
 * The score can be edited using setScore() (and can be grabbed using getScore())
 * While name can be set using setName() (and can be grabbed using getName())
 *
 * Example
 * To add a new high score to the system
 *  HighScore highScore = new HighScore(gameView);
 *  highScore.setCurrentScore(4);
 *  highScore.askName(true); //The true is to make sure it saves the score when then user selects submit.
 *
 */

public class HighScore {

    int currentScore = 0;
    GameView gameView;
    String name;

    public  final class PrefData {
        public String key;
        public int value;
    }

    public HighScore(GameView gameView){
        this.gameView = gameView;
    }

    /**
     * Basically calls addHighScore() using the class variables (name and currentScore)
     */
    public void addHighScoreData(){
        AddHighScore(this.getCurrentScore(), this.getName());
    }

    /**
     *
     * Adds a new high score to the sharedpreferences file.
     * Checks first if 5 scores exist, if not it will just add the score no matter what.
     * If 5 scores exist, it is a little more picky, only adding it if it is higher than the lowest score
     * The score is saved as     "name-day-month-year-score"
     *
     * @param newHighScore high score passed into the function to be saved
     * @param name         name of the user being passed in
     */
    private void AddHighScore(int newHighScore, String name){
        SharedPreferences gamePrefs = gameView.getContext().getSharedPreferences("BrokenScores", Context.MODE_PRIVATE);
        DateFormat dateForm = new SimpleDateFormat("dd-MM-yyyy-kk-mm-ss");
        String highScore = name + "-" + dateForm.format(new Date());
        if (gamePrefs.getAll().size() > 4){
            int lowestScore = 0;
            String lowestID = "";
            List<PrefData> highScoresList = buildArrayListOfSharedPreferences(gamePrefs.getAll());

            for (int count = 0; count < highScoresList.size(); count++) {

                if (highScoresList.get(count).value == newHighScore){
                    lowestScore = 1000;
                    break;
                }
                if (lowestScore == 0){
                    lowestScore = highScoresList.get(count).value;
                    lowestID = highScoresList.get(count).key;
                } else {
                    if (highScoresList.get(count).value < lowestScore) {
                        lowestScore = highScoresList.get(count).value;
                        lowestID = highScoresList.get(count).key;
                    }
                }
            }
            if (lowestScore < newHighScore){
                SharedPreferences.Editor editor = gamePrefs.edit();
                editor.remove(lowestID);
                editor.putInt(highScore, newHighScore);
                editor.commit();
            }
        }else{
            SharedPreferences.Editor editor = gamePrefs.edit();
            editor.putInt(highScore, newHighScore);
            editor.commit();
        }
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  List<PrefData> buildArrayListOfSharedPreferences(Map<String,?> highscores){
        List<PrefData> highScoresList = new ArrayList<>();
        for(Map.Entry<String,?> singleHighscore : highscores.entrySet()) {
            PrefData prefData = new PrefData();
            prefData.key = singleHighscore.getKey();
            prefData.value = Integer.parseInt(singleHighscore.getValue().toString());
            highScoresList.add(prefData);
        }
        return highScoresList;
    }

    /**
     * Generates a sorted list of the scores, sorting by the score
     * @return sorted list, sorted by score
     */
    public  List<PrefData> getSortedList(){
        SharedPreferences gamePrefs = this.gameView.getContext().getSharedPreferences("BrokenScores", Context.MODE_PRIVATE);
        List<PrefData> list = buildArrayListOfSharedPreferences(gamePrefs.getAll());
        list = sortList(list);
        return list;
    }

    private  List<PrefData> sortList(List<PrefData> list){
        Collections.sort(list, new Comparator<PrefData>() {
            @Override
            public int compare(PrefData z1, PrefData z2) {
                if (z1.value > z2.value)
                    return 1;
                if (z1.value < z2.value)
                    return -1;
                return 0;
            }
        });
        return list;
    }

    /**
     * Clears all stored high scores
     */
    public void clearAllHighScores(){
        SharedPreferences gamePrefs = this.gameView.getContext().getSharedPreferences("BrokenScores", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = gamePrefs.edit();
        editor.clear();
        editor.commit();
    }


    /**
     * Asks the user for a name to use with their score using AlertDialog. If no value is entered.
     * If using saveScore=true, then no score will be saved.
     * If a valid name is entered, then name is set to the provided value.
     *
     * @param saveScore Boolean value for if the score should be saved automatically after
     */
    public void askName(final boolean saveScore){
        final AlertDialog.Builder nameAlert = new AlertDialog.Builder(this.gameView.getContext());
        nameAlert.setTitle("Enter your name");
        nameAlert.setMessage("If you want to save this high score, please enter your name.");
        final EditText nameInputBox = new EditText(this.gameView.getContext());
        nameAlert.setView(nameInputBox);
        nameAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setName(nameInputBox.getText().toString());
                if (saveScore){
                    addHighScoreData();
                }
            }
        });
        nameAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setName("");
            }
        });
        AlertDialog nameAlertDialog = nameAlert.create();
        nameAlertDialog.show();
    }

    public void uploadWeb(){
        // TODO Add ability to upload stats
    }

    public void draw(GameView gameView) {
        gameView.drawText(String.valueOf(this.getCurrentScore()), gameView.getWidth() - 200, 100, Color.WHITE, 40);

    }

}
