package com.dragonfruitstudios.brokenbonez;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

// Currently just a simple class to draw the level.
// TODO: Load level design from file.
// TODO: Scroll the level based on camera position
public class Level {
GameView gameView;

public Level(GameView gameView) {
this.gameView = gameView;
}

public void draw() {
float currHeight = getStartPoint().y;
// Draw the sky
gameView.drawRect(0, 0, gameView.getRealWidth(), currHeight,
        Color.parseColor("#06A1D3"));
// Draw the grass.
gameView.drawRect(0, currHeight, gameView.getRealWidth(),
        currHeight + 20, Color.parseColor("#069418"));
currHeight += 20;
// Draw the ground.
gameView.drawRect(0, currHeight, gameView.getRealWidth(),
        gameView.getRealHeight(), Color.parseColor("#976600"));
}

public PointF getStartPoint() {
// Calculate the surface level.
float surfaceLevel = gameView.getRealHeight() / 2 + 50;
return new PointF(5, surfaceLevel);
}

}
