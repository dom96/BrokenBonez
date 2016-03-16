package com.dragonfruitstudios.brokenbonez;


import com.dragonfruitstudios.brokenbonez.Game.Levels.LevelInfo;
import com.dragonfruitstudios.brokenbonez.Game.Levels.SolidObject;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class SVGParserTests {
    @Test
    public void parse_isCorrect() {
        LevelInfo levelInfo = new LevelInfo(LevelInfo.LevelID.Level1, "", "", "");

        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\"\n" +
                "              \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n" +
                "\n" +
                "<svg xmlns=\"http://www.w3.org/2000/svg\"\n" +
                "     width=\"83.3333in\" height=\"10.6667in\"\n" +
                "     viewBox=\"0 0 6000 768\">\n" +
                "<path id=\"booster1\" class=\"booster\"\n" +
                "        fill=\"none\" stroke=\"black\" stroke-width=\"1\"\n" +
                "        d=\"M 3280,470.00\" />\n" +
                "</svg>\n";
        InputStream is = new ByteArrayInputStream(data.getBytes());
        levelInfo.loadSVG(is, new VectorF(0, 0));

        SolidObject booster = levelInfo.getSolidObject("booster");
        assertEquals(3280, booster.pos.x, 0.001);
        assertEquals(470, booster.pos.y, 0.001);
    }
}
