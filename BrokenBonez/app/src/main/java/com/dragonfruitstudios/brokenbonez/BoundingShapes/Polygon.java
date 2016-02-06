package com.dragonfruitstudios.brokenbonez.BoundingShapes;

import android.graphics.Color;

import com.dragonfruitstudios.brokenbonez.Drawable;
import com.dragonfruitstudios.brokenbonez.GameView;
import com.dragonfruitstudios.brokenbonez.VectorF;

/**
 * This class implements an irregular shape composed of multiple line segments. It also implements
 * collision detection between this shape and other shapes.
 *
 * TODO: Polygon may not be the appropriate name here.
 */
public class Polygon implements Drawable, Intersector {
    Line[] lines;


    public Polygon(Line[] lines) {
        this.lines = lines;
    }

    public static Polygon createTriangle(VectorF a, VectorF b, VectorF c) {
        Line triangleLeft = new Line(a, b);
        Line triangleBottom = new Line(a, c);
        Line triangleMiddle = new Line(b, c);

        Polygon triangle = new Polygon(new Line[] {triangleLeft, triangleBottom, triangleMiddle});
        return triangle;
    }

    // TODO: isPointInside https://en.wikipedia.org/wiki/Even%E2%80%93odd_rule

    public Manifold collisionTest(VectorF point) {
        for (Line l : lines) {
            if (l.collidesWith(point)) {
                // TODO: Yes, I know this is inefficient.
                // Calculate the penetration depth and collision normal.

                // Get vector for line start to finish.
                VectorF startToFinish = l.getFinish().subtracted(l.getStart());
                VectorF normal = new VectorF(-startToFinish.getY(), startToFinish.getX());
                normal.normalise();

                // The depth will always be 0
                // TODO: Unless `collidesWith` can detect whether `point` is inside the polygon.
                float depth = 0f;
                return new Manifold(normal, depth, true);
            }
        }
        return new Manifold(null, -1, false);
    }

    /**
     * NB. This only checks whether `point` collides with the edges of this polygon, i.e.
     * if it's inside it `false` may be returned.
     * @param point
     * @return
     */
    public boolean collidesWith(VectorF point) {
        for (Line l : lines) {
            if (l.collidesWith(point)) {
                return true;
            }
        }

        return false;
    }

    public float distanceSquared(VectorF point) {
        float result = -1;
        for (Line l : lines) {
            float temp = l.distanceSquared(point);
            if (result == -1 || temp < result) {
                result = temp;
            }
        }
        return result;
    }

    public Line[] getLines() {
        return lines;
    }

    /**
     This method is just for debugging purposes!
     */
    public void draw(GameView view) {
        for (Line l : lines) {
            l.draw(view);
        }
    }
}
