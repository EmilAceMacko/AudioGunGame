package Game;

import processing.core.PVector;
import java.util.ArrayList;

public class Entity extends GameObject implements Constants {
    // Variables:
    public PVector vel, push;
    public boolean moving;
    public GameObject[] collisions;
    public PVector collisionSide;
    // Constants:
    public static final float ENTITY_PUSH_FACTOR = 0.05f;
    // Constructor:
    public Entity() {
        super();
        // Default values:
        vel = new PVector(0, 0); // The current velocity of this entity.
        push = new PVector(0, 0); // How much this entity is being pushed away from another entity.
        moving = false; // Whether this entity is currently moving.
        collisions = new GameObject[0]; // Holds a list of all the objects that this entity has collided with after moving!
        collisionSide = new PVector(0, 0); // Holds which side a collision has occurred. (X: -1 = Left, 1 = Right. Y: -1 = Up, 1 = Down.)
    }

    public void update() {
        super.update();
        // Determine whether we're moving:
        moving = (vel.x != 0.0f || vel.y != 0.0f);
        // Move and store the list of collisions (if there are any):
        collisions = move();
    }

    // Returns an array of the objects that were collided with.
    public GameObject[] move() {
        ArrayList<GameObject> collided = new ArrayList<>(); // Stores all the objects that have been collided with.
        push.set(0, 0); // The pushing force that pushes this entity away from other entities.
        vel.limit(SPEED_LIMIT); // Limit the velocity.
        collisionSide.set(0, 0); // Reset side-collision indicator.

        // Check for collision against other Entities first:
        collided.addAll(entityCollision(Game.roomObjects[Game.camera.roomX][Game.camera.roomY]));
        collided.addAll(entityCollision(Game.globalObjects));
        push.limit(SPEED_LIMIT); // Limit the push force.

        // Check for collision against non-entity objects lastly (one axis at a time):
        GameObject colObj; // For a non-entity object that we may collide with.

        pos.x += vel.x + push.x; // Move on the X axis.
        colObj = obstacleCollision(); // Check against other objects on the X axis.
        if (colObj != null) { // Solve collision on the X axis:
            collisionSide.x = Math.signum(vel.x + push.x);
            pos.x = (colObj.pos.x + colObj.width / 2.0f) - collisionSide.x * (width + colObj.width) / 2.0f - width / 2.0f;
            vel.x = 0.0f; // Stop horizontal velocity.
            push.x = 0.0f; // Stop horizontal push.
        }
        collided.add(colObj); // Add object to collision list.

        pos.y += vel.y + push.y; // Move on the Y axis.
        colObj = obstacleCollision(); // Check against other objects on the Y axis.
        if (colObj != null) { // Solve collision on the Y axis:
            collisionSide.y = Math.signum(vel.y + push.y);
            pos.y = (colObj.pos.y + colObj.height / 2.0f) - collisionSide.y * (height + colObj.height) / 2.0f - height / 2.0f;
            vel.y = 0.0f; // Stop vertical velocity.
            push.y = 0.0f; // Stop vertical push.
        }
        collided.add(colObj); // Add object to collision list.

        return collided.toArray(new GameObject[0]); // Return the collision list as an array.
    }

    // Check for collision against Entity objects:
    public ArrayList<GameObject> entityCollision(ArrayList<GameObject> objectList) {
        ArrayList<GameObject> ents = new ArrayList<>(); // Create a list to store collided entities in.
        for (GameObject obj : objectList) {
            if (obj != this && obj instanceof Entity && obj.solid) {
                // Create circular hitboxes for self and other (x/y = center coordinate, z = radius):
                PVector selfC = new PVector( pos.x + width/2.0f, pos.y + height/2.0f, Math.min(width, height)/2.0f);
                PVector otherC = new PVector(obj.pos.x + obj.width/2.0f, obj.pos.y + obj.height/2.0f, Math.min(obj.width, obj.height)/2.0f);
                // Distance calculation:
                double distSqr = Math.pow(otherC.x - selfC.x, 2) + Math.pow(otherC.y - selfC.y, 2); // The (square) distance between the entities.
                double angle = Math.atan2(selfC.y - otherC.y, selfC.x - otherC.x); // The angle between the entities.
                double overlap = Math.sqrt(Math.pow(otherC.z + selfC.z, 2) - distSqr); // The total overlap distance between the entities.
                if (overlap > 0.0f) {
                    push.add((float) (Math.cos(angle) * overlap * ENTITY_PUSH_FACTOR), (float) (Math.sin(angle) * overlap * ENTITY_PUSH_FACTOR));
                }
                ents.add(obj);
            }
        }
        return ents;
    }
    // Check for collision against a non-Entity object:
    public GameObject obstacleCollision() {
        for (GameObject obj : Game.roomObjects[Game.camera.roomX][Game.camera.roomY]) {
            if (!(obj instanceof Entity) && obj.solid) {
                if (collisionObjectObject(this, obj)) {
                    return obj;
                }
            }
        }
        for (GameObject obj : Game.globalObjects) {
            if (!(obj instanceof Entity) && obj.solid) {
                if (collisionObjectObject(this, obj)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public void display() {
        super.display();
    }

    // Basic object-on-object collision function(s):
    public boolean collisionObjectObject(GameObject self, GameObject other) { // Given two objects:
        return collisionObjectObject(self, 0, 0, other);
    }
    public boolean collisionObjectObject(GameObject self, PVector off, GameObject other) { // Given two objects and an offset for the first object (as a PVector):
        return collisionObjectObject(self, off.x, off.y, other);
    }
    public boolean collisionObjectObject(GameObject self, float xOff, float yOff, GameObject other) { // Given two objects and an offset for the first object (as floats):
        return Game.areaInArea(xOff + self.pos.x, yOff + self.pos.y, self.width, self.height, other.pos.x, other.pos.y, other.width, other.height);
    }

    // Ray-on-object collision function(s): (Returns null if no intersection is found, otherwise the intersection point is returned)
    public PVector collisionRayObject(float rayAngle, PVector ray, GameObject obj) { // Given ray angle, ray start (as a PVector), and an object:
        // Calculate ray vectors:
        PVector rayVec = new PVector((float) Math.cos(rayAngle) * RAY_LENGTH, (float) Math.sin(rayAngle) * RAY_LENGTH); // The vector between the start and end of the ray.
        PVector rayEnd = PVector.add(ray, rayVec); // The end-point of the ray.
        // Get object corners:
        PVector c1 = obj.pos; // Upper left corner of the object.
        PVector c2 = PVector.add(c1, new PVector(obj.width, obj.height)); // Lower right corner of the object.
        // Perform segment-on-segment intersections on all four edges of the object:
        PVector[] edges = new PVector[4];
        edges[UP] = collisionSegmentSegment(ray.x, ray.y, rayEnd.x, rayEnd.y, c1.x, c1.y, c2.x, c1.y); // Top edge.
        edges[LEFT] = collisionSegmentSegment(ray.x, ray.y, rayEnd.x, rayEnd.y, c1.x, c1.y, c1.x, c2.y); // Left edge.
        edges[DOWN] = collisionSegmentSegment(ray.x, ray.y, rayEnd.x, rayEnd.y, c1.x, c2.y, c2.x, c2.y); // Bottom edge.
        edges[RIGHT] = collisionSegmentSegment(ray.x, ray.y, rayEnd.x, rayEnd.y, c2.x, c1.y, c2.x, c2.y); // Right edge.
        // Find the closest intersection point:
        float closestDist = RAY_LENGTH;
        int closestEdge = -1;
        for (int i = 0; i < edges.length; i++) {
            if (edges[i] != null) { // If edge intersected with ray:
                float dist = PVector.sub(edges[i], ray).mag(); // Get distance between the ray start point and the edge intersection point.
                if (dist < closestDist) { // If intersection is closer than previous:
                    closestDist = dist; // Save new closest intersection distance.
                    closestEdge = i; // Save the edge with the new closest intersection.
                }
            }
        }
        // Return:
        if (closestEdge > -1) return edges[closestEdge]; // Return closest intersection.
        else return null; // No intersections found.
    }

    // Segment-on-segment intersection function: (Returns null if no intersection is found)
    public PVector collisionSegmentSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) { // Given two pairs of 2D coordinates (four coordinates).
        // Algorithm: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection#Given_two_points_on_each_line_segment
        float div = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4); // The common divisor of "t" and "u":
        float t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / div; // The intersection point t along the segment going from x1,y1 to x2,y2 (between 0 and 1).
        float u = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / div; // The intersection point u along the segment going from x3,y3 to x4,y4 (between 0 and 1).
        if (0f <= t && t <= 1f && 0f <= u && u <= 1f) { // If both t and u lie between 0 and 1, then there is an intersection on both segments:
            return new PVector(x1 + t * (x2 - x1), y1 + t * (y2 - y1)); // Return the intersection coordinate.
        } else { // either/both t and u lie outside the range 0 to 1.
            return null; // No valid intersection coordinate found.
        }
    }
}
