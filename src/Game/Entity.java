package Game;

import processing.core.PVector;

import java.util.ArrayList;

public class Entity extends GameObject implements Constants {

    public PVector vel, push;
    public boolean moving;

    public Entity() {
        // Default values:
        super();
        vel = new PVector(0, 0);
        push = new PVector(0, 0);
        moving = false;
    }

    public void update() {
        super.update();
        if(vel.x != 0.0f || vel.y != 0.0f) {
            moving = true;
        }
        else if (moving) moving = false;
        move();

    }

    // Returns an array of the objects that were collided with.
    public GameObject[] move() {
        ArrayList<GameObject> collided; // Stores all the objects that have been collided with.
        push.set(0, 0); // The pushing force that pushes this entity away from other entities.
        vel.limit(SPEED_LIMIT); // Limit the velocity.

        // Check for collision against other Entities first:
        collided = entityCollision();
        push.limit(SPEED_LIMIT); // Limit the push force.

        // Check for collision against non-entity objects lastly (one axis at a time):
        GameObject colObj; // For a non-entity object that we may collide with.

        pos.x += vel.x + push.x; // Move on the X axis.
        colObj = obstacleCollision(); // Check against other objects on the X axis.
        if (colObj != null) { // Solve collision on the X axis:
            pos.x = (colObj.pos.x + colObj.width / 2.0f) - Math.signum(vel.x) * (width + colObj.width) / 2.0f - width / 2.0f;
            vel.x = 0.0f; // Stop horizontal velocity.
        }
        collided.add(colObj);

        pos.y += vel.y + push.y; // Move on the Y axis.
        colObj = obstacleCollision(); // Check against other objects on the Y axis.
        if (colObj != null) { // Solve collision on the Y axis:
            pos.y = (colObj.pos.y + colObj.height / 2.0f) - Math.signum(vel.y) * (height + colObj.height) / 2.0f - height / 2.0f;
            vel.y = 0.0f; // Stop vertical velocity.
        }
        collided.add(colObj);

        return collided.toArray(new GameObject[0]);
    }

    // Check for collision against Entity objects:
    public ArrayList<GameObject> entityCollision() {
        ArrayList<GameObject> ents = new ArrayList<>();
        for (GameObject obj : Game.roomObjects[(int) Game.camera.roomPos.x][(int) Game.camera.roomPos.y]) {
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
        for (GameObject obj : Game.globalObjects) {
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
    public boolean collisionObjectObject(GameObject self, GameObject other) {
        return collisionObjectObject(self, 0, 0, other);
    }
    public boolean collisionObjectObject(GameObject self, PVector off, GameObject other) {
        return collisionObjectObject(self, off.x, off.y, other);
    }
    public boolean collisionObjectObject(GameObject self, float xOff, float yOff, GameObject other) {
        return (xOff + self.pos.x < other.pos.x + other.width &&
                xOff + self.pos.x + self.width > other.pos.x &&
                yOff + self.pos.y < other.pos.y + other.height &&
                yOff + self.pos.y + self.height > other.pos.y);
    }
}
