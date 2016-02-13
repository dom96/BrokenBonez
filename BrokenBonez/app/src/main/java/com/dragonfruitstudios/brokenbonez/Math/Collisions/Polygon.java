package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class implements an irregular shape composed of multiple line segments. It also implements
 * collision detection between this shape and other shapes.
 */
public class Polygon extends Intersector implements Drawable {
    protected ArrayList<Line> lines;

    protected Polygon() {
        this.lines = new ArrayList<Line>();
    }

    public Polygon(Line[] lines) {
        this.lines = new ArrayList<Line>(Arrays.asList(lines));
    }

    public Polygon(ArrayList<Line> lines) {
        this.lines = lines;
    }

    protected void addVertices(VectorF[] vertices) {
        VectorF prev = vertices[vertices.length-1];
        for (int i = 0; i < vertices.length-1; i++) {
            lines.add(new Line(vertices[i], vertices[i+1]));
        }
        lines.add(new Line(vertices[vertices.length - 1], vertices[0]));
    }

    /**
     * Checks if the specified shape collides with this Polygon.
     * @return A Manifold containing information about the collision.
     */
    @Override
    public Manifold collisionTest(Intersector shape) {
        if (shape instanceof Circle) {
            return ((Circle)shape).collisionTest(this);
        }

        return collisionNotImplemented(shape);
    }

    // TODO: isPointInside https://en.wikipedia.org/wiki/Even%E2%80%93odd_rule

    public Manifold collisionTest(VectorF point) {
        // TODO: This implementation may be too slow when more complex polygons are involved.
        for (Line l : lines) {
            Manifold res = l.collisionTest(point);
            if (res.hasCollided()) {
                // TODO: May want to calculate the depth more accurately, currently it will
                // always be 0.
                return res;
            }
        }
        return Manifold.noCollision();
    }

    /**
     * Determines whether `point` collides with this polygon's edges.
     *
     * Warning: This only checks whether `point` collides with the edges of this polygon, i.e.
     * if it's inside it `false` may be returned.
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

    public ArrayList<Line> getLines() {
        return lines;
    }

    public Intersector copy() {
        return new Polygon(new ArrayList<Line>(lines));
    }

    /**
     This method is used to show where the polygon is on the screen, for debugging purposes only.
     */
    public void draw(GameView view) {
        for (Line l : lines) {
            l.draw(view);
        }
    }
}
