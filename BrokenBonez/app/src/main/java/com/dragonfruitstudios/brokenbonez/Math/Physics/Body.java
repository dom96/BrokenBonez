package com.dragonfruitstudios.brokenbonez.Math.Physics;

public abstract class Body {
    float mass;
    float inverseMass;
    float restitution;

    float getRestitution() {
        return restitution;
    }

    float getInverseMass() {
        return inverseMass;
    }
}
