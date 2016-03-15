package com.dragonfruitstudios.brokenbonez.Math.Collisions;

import android.graphics.*;
import android.graphics.Rect;

import com.dragonfruitstudios.brokenbonez.Game.Drawable;
import com.dragonfruitstudios.brokenbonez.Game.GameView;
import com.dragonfruitstudios.brokenbonez.Game.Graphics;
import com.dragonfruitstudios.brokenbonez.Math.VectorF;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class implements an irregular shape composed of multiple line segments. It also implements
 * collision detection between this shape and other shapes.
 */
public class Polygon extends Intersector implements Drawable {
    private ArrayList<Line> lines;

    private VectorF size;
    private RectF rect;

    private android.graphics.RectF calcRect() {
        // TODO: This code sucks. Clean it up. But make sure tests pass.
        float minX = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = -Float.MAX_VALUE;
        for (Line l : lines) {
            minX = Math.min(minX, l.getStart().x);
            maxX = Math.max(maxX, l.getStart().x);
            minY = Math.min(minY, l.getStart().y);
            maxY = Math.max(maxY, l.getStart().y);

            minX = Math.min(minX, l.getFinish().x);
            maxX = Math.max(maxX, l.getFinish().x);
            minY = Math.min(minY, l.getFinish().y);
            maxY = Math.max(maxY, l.getFinish().y);
        }
        return new RectF(minX, minY, maxX, maxY);
    }

    private VectorF calcSize() {
        return new VectorF(rect.right - rect.left, rect.bottom - rect.top);
    }

    protected Polygon() {
        this.lines = new ArrayList<Line>();
    }

    public Polygon(Line[] lines) {
        this.lines = new ArrayList<Line>(Arrays.asList(lines));
        rect = calcRect();
        size = calcSize();
    }

    public Polygon(ArrayList<Line> lines) {
        this.lines = lines;
        rect = calcRect();
        size = calcSize();
    }

    protected void addVertices(VectorF[] vertices) {
        for (int i = 0; i < vertices.length-1; i++) {
            lines.add(new Line(vertices[i].copy(), vertices[i+1].copy()));
        }
        lines.add(new Line(vertices[vertices.length - 1], vertices[0].copy()));
        recalculateBounds();
    }

    /**
     * Checks if the specified shape collides with this Polygon.
     * @return A Manifold containing information about the collision.
     */
    @Override
    public Manifold.Collection collisionTest(Intersector shape) {
        if (shape instanceof Circle) {
            return ((Circle)shape).collisionTest(this);
        }
        else if (shape instanceof Polygon) {
            return ((Polygon)shape).collisionTestWithPolygon(this);
        }

        return collisionNotImplemented(shape);
    }

    private Manifold.Collection collisionTestWithLine(Line line) {
        Manifold.Collection result = this.collisionTest(line.getStart());
        result.addAll(this.collisionTest(line.getFinish()));
        return result;
    }

    private Manifold.Collection collisionTestWithPolygon(Polygon shape) {
        // To determine whether two Polygon's intersect we simply check each vertex inside each
        // Polygon and see if it is inside the other Polygon using `collisionTest`.
        // TODO: There is likely a more efficient way of testing whether two Polygons intersect.

        // TODO: Test this method!
        Manifold.Collection result = new Manifold.Collection();
        for (Line l : shape.lines) {
            result.addAll(this.collisionTestWithLine(l));
        }

        for (Line l : this.lines) {
            result.addAll(shape.collisionTestWithLine(l));
        }
        return result;
    }

    /**
     * Checks whether `point` collides with this Polygon (when `point` is inside the Polygon then
     * True is also returned).
     *
     * Implementation details:
     *
     * This implementation uses two methods to check whether `point` collides with this
     * polygon. The first is a simple check to determine if `point` lies on any of the
     * Polygon's edges. The second is the use of the Even-odd rule, which allows us to check
     * whether `point` is inside the Polygon. It does so by drawing a ray from `point` to
     * infinity in an arbitrary direction and counting the number of times the ray crosses with
     * the Polygon's edges. If the number is odd then the point is inside.
     */
    public Manifold.Collection collisionTest(VectorF point) {
        // TODO: This implementation may be too slow when more complex polygons are involved.
        Manifold.Collection result = new Manifold.Collection();
        boolean odd = false;
        for (Line l : lines) {
            // Check if `point` is on the line segment `l`.
            Manifold res = l.collisionTest(point);
            if (res.hasCollided()) {
                result.add(res);
            }

            // Code carefully translated from the C code available here:
            // https://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
            // I reused the for loop for efficiency and it seems to be working well.
            // The following code draws a ray from `point` to infinity and checks if it
            // collides with `l`.
            if (l.getStart().y > point.y != l.getFinish().y > point.y) {
                if (point.x < (l.getFinish().x - l.getStart().x) *
                        (point.y - l.getStart().y) /
                        (l.getFinish().y - l.getStart().y) + l.getStart().x) {
                    odd = !odd;
                }
            }
        }
        // Check if ray from `point` to infinity collided with an odd number of lines.
        // If so, this suggests that the point is inside the Polygon (See Even-odd rule).
        if (odd && !result.hasCollisions()) {
            // Need to find the normal and penetration depth.
            // Do this by finding the line closest to `point`.
            Line closestLine = lines.get(0);
            float closestDist = lines.get(0).distanceSquared(point);
            for (int i = 1; i < lines.size(); i++) {
                float dist = lines.get(i).distanceSquared(point);
                if (dist < closestDist) {
                    closestLine = lines.get(i);
                    closestDist = dist;
                }
            }
            // TODO: Calculate normal correctly. The following approximation works rather
            // TODO: well though.
            result.add(new Manifold(new VectorF(0, 1), (float)Math.sqrt(closestDist), true));
        }
        return result;
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

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public Intersector copy() {
        return new Polygon(new ArrayList<Line>(lines));
    }

    public VectorF getPos() {
        return lines.get(0).getPos();
    }

    public VectorF getSize() {
        return size;
    }

    public RectF getRect() {
        return rect;
    }

    public void recalculateBounds() {
        rect = calcRect();
        size = calcSize();
    }

    /**
     This method is used to show where the polygon is on the screen, for debugging purposes only.
     */
    public void draw(GameView view) {
        if (Graphics.drawDebugInfo) {
            for (Line l : lines) {
                l.draw(view);
            }

            // Draw the size of the polygon somewhere beside it.
            view.drawText(getSize().x + "x" + getSize().y, lines.get(0).getPos().x - 5,
                    lines.get(0).getPos().y - 10, Color.RED);
        }
    }
}
