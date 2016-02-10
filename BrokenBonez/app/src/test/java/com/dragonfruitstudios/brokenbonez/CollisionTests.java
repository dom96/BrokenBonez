package com.dragonfruitstudios.brokenbonez;

import com.dragonfruitstudios.brokenbonez.Math.Collisions.Line;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Polygon;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Rect;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollisionTests {

    @Test
    public void distance_isCorrect() {
        Rect r = new Rect(10, 200, 300, 250);
        Rect r2 = new Rect(0, 410, 3000, 460);

        float distSq = r.distanceSquared(new VectorF(30, 30));
        float distSq2 = r2.distanceSquared(new VectorF(30, 30));

        assertEquals(28900, distSq, 0.0001f);
        assertEquals(144400, distSq2, 0.0001f);
    }

    @Test
    public void lineCollision_isCorrect() {
        Line l = new Line(new VectorF(5, 5), new VectorF(50, 5));
        Line l2 = new Line(new VectorF(5, 5), new VectorF(50, 50));

        assertTrue(l.collidesWith(new VectorF(10, 5)));
        assertFalse(l.collidesWith(new VectorF(10, 6)));
        assertFalse(l.collidesWith(new VectorF(60, 5)));
        assertTrue(l.collidesWith(new VectorF(5, 5)));

        assertTrue(l2.collidesWith(new VectorF(5, 5)));
        assertTrue(l2.collidesWith(new VectorF(50, 50)));
        assertFalse(l2.collidesWith(new VectorF(0, 5)));
    }

    @Test
    public void polygonCollision_isCorrect() {
        Line triangleLeft = new Line(200, 300, 200, 150);
        Line triangleBottom = new Line(200, 300, 300, 300);
        Line triangleMiddle = new Line(200, 150, 300, 300);

        Polygon triangle = new Polygon(new Line[] {triangleLeft, triangleBottom, triangleMiddle});
        assertTrue(triangle.collidesWith(new VectorF(140, 200)));
        assertTrue(triangle.collidesWith(new VectorF(300, 300)));
        assertFalse(triangle.collidesWith(new VectorF(210, 170)));
    }

}
