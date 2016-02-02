package com.dragonfruitstudios.brokenbonez;

import com.dragonfruitstudios.brokenbonez.BoundingShapes.Rect;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CollisionTests {

    @Test
    public void rotate_isCorrect() {
        Rect r = new Rect(10, 200, 300, 250);
        Rect r2 = new Rect(0, 410, 3000, 460);

        float distSq = r.distanceSquared(new VectorF(30, 30));
        float distSq2 = r2.distanceSquared(new VectorF(30, 30));

        assertEquals(28900, distSq, 0.0001f);
        assertEquals(144400, distSq2, 0.0001f);
    }

}
