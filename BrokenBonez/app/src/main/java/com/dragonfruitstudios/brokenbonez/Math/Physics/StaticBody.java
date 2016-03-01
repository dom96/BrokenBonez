package com.dragonfruitstudios.brokenbonez.Math.Physics;

import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Circle;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Intersector;
import com.dragonfruitstudios.brokenbonez.Math.Collisions.Manifold;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;

/**
 * Implements a static body. That is, a body which has infinite mass and does not participate in
 * the physics simulation.
 */
public class StaticBody extends Body {
    Intersector boundingShape;

    /**
     * Creates a new static shape based on the specified shape template. The specified shape
     * is copied and so can be used to create multiple bodies.
     * @param shape
     */
    public StaticBody(Intersector shape) {
        boundingShape = shape.copy();
        this.mass = 0;
        this.inverseMass = 0;
        this.restitution = 0.1f;
    }

    void draw(GameView view) {
        boundingShape.draw(view);
    }

    Manifold collisionTest(DynamicBody dBody) {
        Manifold result = dBody.getBoundingShape().collisionTest(boundingShape);
        result.setFirstBody(this);
        result.setSecondBody(dBody);
        return result;
    }

    public VectorF getPos() {
        return boundingShape.getPos();
    }

    public VectorF getSize() {
        return boundingShape.getSize();
    }

}
