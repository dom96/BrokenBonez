package com.dragonfruitstudios.brokenbonez;

import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Keep this in mind when making tests:
 * http://stackoverflow.com/questions/34010251/android-pointf-constructor-not-working-in-junit-test
 */

public class VectorTests {
    VectorF vec1;
    VectorF vec2;
    VectorF vec3;

    @Before
    public void initVectors() {
        vec1 = new VectorF(5, 0);
        vec2 = new VectorF(5, 5);
        vec3 = new VectorF(5, 10);
    }


    @Test
    public void angle_isCorrect() throws Exception {
        // Angles are relative to x-axis.
        assertEquals(Math.toRadians(0), vec1.angle(), 0.0001f);
        assertEquals(Math.toRadians(45), vec2.angle(), 0.0001f);
        assertEquals(Math.toRadians(63.43), vec3.angle(), 0.01f);
    }

    @Test
    public void rotate_isCorrect() {
        vec1.rotate((float) Math.toRadians(180));
        assertEquals(-5, vec1.x, 0.0001f);
    }

    @Test
    public void magnitude_isCorrect() {
        assertEquals(5, vec1.magnitude(), 0.0001f);

        // Tests created based on in-game issues.
        VectorF start = new VectorF(35, 40);
        VectorF end = new VectorF(65, 40);
        assertEquals(30, end.subtracted(start).magnitude(), 0.0001f);
    }

    @Test
    public void add_isCorrect() {
        VectorF added = vec1.added(30);
        assertEquals(35, added.x, 0.0001);
        assertEquals(30, added.y, 0.0001);

        VectorF added2 = vec1.added(new VectorF(5, -5));
        assertEquals(10, added2.x, 0.0001);
        assertEquals(-5, added2.y, 0.0001);

        added2.add(5, 3);
        assertEquals(15, added2.x, 0.001);
        assertEquals(-2, added2.y, 0.001);
    }


    @Test
    public void dist_isCorrect() {
        assertEquals(5, vec1.dist(10, 0), 0.001);
        assertEquals(25, vec1.distSquared(new VectorF(10, 0)), 0.001);
    }

    @Test
    public void normalise_isCorrect() {
        assertEquals(1, vec1.normalised().x, 0.001);
    }

}