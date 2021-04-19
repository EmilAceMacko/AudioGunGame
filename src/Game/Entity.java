package Game;

import processing.core.PVector;
import java.util.ArrayList;

public class Entity extends GameObject implements Constants {

    public PVector vel, push;
    public boolean moving;
    public GameObject[] collisions;

    public Entity() {
        super();
        // Default values:
        vel = new PVector(0, 0); // The current velocity of this entity.
        push = new PVector(0, 0); // How much this entity is being pushed away from another entity.
        moving = false; // Whether this entity is currently moving.
        collisions = new GameObject[0]; // Holds a list of all the objects that this entity has collided with after moving!
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

        // Check for collision against other Entities first:
        collided.addAll(entityCollision(Game.roomObjects[(int) Game.camera.roomPos.x][(int) Game.camera.roomPos.y]));
        collided.addAll(entityCollision(Game.globalObjects));
        push.limit(SPEED_LIMIT); // Limit the push force.

        // Check for collision against non-entity objects lastly (one axis at a time):
        GameObject colObj; // For a non-entity object that we may collide with.

        pos.x += vel.x + push.x; // Move on the X axis.
        colObj = obstacleCollision(); // Check against other objects on the X axis.
        if (colObj != null) { // Solve collision on the X axis:
            pos.x = (colObj.pos.x + colObj.width / 2.0f) - Math.signum(vel.x + push.x) * (width + colObj.width) / 2.0f - width / 2.0f;
            vel.x = 0.0f; // Stop horizontal velocity.
            push.x = 0.0f;
        }
        collided.add(colObj);

        pos.y += vel.y + push.y; // Move on the Y axis.
        colObj = obstacleCollision(); // Check against other objects on the Y axis.
        if (colObj != null) { // Solve collision on the Y axis:
            pos.y = (colObj.pos.y + colObj.height / 2.0f) - Math.signum(vel.y + push.y) * (height + colObj.height) / 2.0f - height / 2.0f;
            vel.y = 0.0f; // Stop vertical velocity.
            push.y = 0.0f;
        }
        collided.add(colObj);

        return collided.toArray(new GameObject[0]);
    }

    // Check for collision against Entity objects:
    public ArrayList<GameObject> entityCollision(ArrayList<GameObject> objectList) {
        ArrayList<GameObject> ents = new ArrayList<>();
        for (GameObject obj : objectList) {
            if (obj != this && obj instanceof Entity && obj.solid) {
                // Create circular hitboxes for self and other (x/y = center coordinate, z = radius):
                PVector selfC = new PVector(pos.x + width/2.0f, pos.y + height/2.0f, Math.min(width, height)/2.0f);
                PVector otherC = new PVector(obj.pos.x + obj.width/2.0f, obj.pos.y + obj.height/2.0f, Math.min(obj.width, obj.height)/2.0f);

                double distSqr = Math.pow(otherC.x - selfC.x, 2) + Math.pow(otherC.y - selfC.y, 2); // The distance between the entities (squared)
                double angle = Math.atan2(selfC.y - otherC.y, selfC.x - otherC.x); // The angle between the objects.
                double overlap = Math.sqrt(Math.pow(otherC.z + selfC.z, 2) - distSqr); // The total overlap distance between the entities.
                double pushFactor = 0.05;
                if (overlap > 0.0f) {
                    push.add((float)(Math.cos(angle) * overlap * pushFactor), (float)(Math.sin(angle) * overlap * pushFactor));
                }
                ents.add(obj);
            }
        }
        return ents;
    }
    // Check for collision against a non-Entity object:
    public GameObject obstacleCollision() {
        for (GameObject obj : Game.roomObjects[(int) Game.camera.roomPos.x][(int) Game.camera.roomPos.y]) {
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
    public boolean collisionObjectObject(GameObject self, PVector off, GameObject other) { // Given two objects and an offset for the first object (as PVector):
        return collisionObjectObject(self, off.x, off.y, other);
    }
    public boolean collisionObjectObject(GameObject self, float xOff, float yOff, GameObject other) { // Given two objects and an offset for the first object (as floats):
        return Game.areaInArea(xOff + self.pos.x, yOff + self.pos.y, self.width, self.height, other.pos.x, other.pos.y, other.width, other.height);
    }
    // Ray-on-object collision function(s):
    public boolean collisionRayObject(float rayAngle, PVector ray, GameObject other) { // Given ray angle, ray start (as PVector), and an object:
        // Algorithm: https://stackoverflow.com/questions/99353/how-to-test-if-a-line-segment-intersects-an-axis-aligned-rectange-in-2d

        // Calculate ray vectors:
        PVector rayVec = new PVector((float) Math.cos(rayAngle) * RAY_LENGTH, (float) Math.sin(rayAngle) * RAY_LENGTH); // The vector between the start and end of the ray.
        PVector rayEnd = PVector.add(ray, rayVec); // The end-point of the ray:
        // Get object corners:
        PVector c1 = other.pos; // Upper left corner of the object.
        PVector c2 = new PVector(other.pos.x + other.width, other.pos.y + other.height); // Lower right corner of the object.
        if(DEBUG) {
            float roomOffX = Game.camera.roomPos.x * ROOM_WIDTH;
            float roomOffY = Game.camera.roomPos.y * ROOM_HEIGHT;
            Sketch.processing.stroke(255, 0, 255);
            Sketch.processing.line(ray.x-roomOffX, ray.y-roomOffY, rayEnd.x-roomOffX, rayEnd.y-roomOffY);
        }
        float rayCross = cross(ray, rayEnd);
        float UL = rayVec.y * c1.x - rayVec.x * c1.y + rayCross; // Upper Left:
        float UR = rayVec.y * c2.x - rayVec.x * c1.y + rayCross; // Upper Right:
        float LL = rayVec.y * c1.x - rayVec.x * c2.y + rayCross; // Lower Left:
        float LR = rayVec.y * c2.x - rayVec.x * c2.y + rayCross; // Lower Right:

        if((UL > 0.0f && UR > 0.0f && LL > 0.0f && LR > 0.0f) || (UL < 0.0f && UR < 0.0f && LL < 0.0f && LR < 0.0f)) { // If all corners are either above or below the ray:
            return false; // No intersection.
        } else {
            return Game.areaInArea(Math.min(ray.x, rayEnd.x), Math.min(ray.y, rayEnd.y), Math.abs(rayVec.x), Math.abs(rayVec.y), c1.x, c1.y, c2.x-c1.x, c2.y-c1.y);
        }
    }
    public boolean collisionRayObject(float rayAngle, float rayX, float rayY, GameObject other) { // Given ray angle, ray start (as floats), and an object:
        return collisionRayObject(rayAngle, new PVector(rayX, rayY), other);
    }
    // 2D Vector cross product:
    public float cross(PVector a, PVector b) {
        return b.x * a.y - a.x * b.y;
    }
}
