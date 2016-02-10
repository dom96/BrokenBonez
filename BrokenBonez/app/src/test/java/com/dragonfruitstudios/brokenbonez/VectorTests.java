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
        vec1.rotate((float)Math.toRadians(180));
        assertEquals(-5, vec1.x, 0.0001f);

    }
}