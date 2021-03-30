package Objects;

import Game.Entity;
import Game.Game;
import Game.GameObject;
import processing.core.PVector;

public class DeeJay extends Entity {
    // DeeJay variables:
    public PVector aimPivot;
    public float aimAngle;
    public int gunSpriteID;
    public boolean controllable;
    public boolean shooting;
    public float moveSpeed;
    public NPC talkingPartner;
    public boolean talking;
    // DeeJay constants:
    public static final int NPC_TALK_DISTANCE = TILE * 3;
    public static final int ANIM_WALK_LENGTH = 3;
    public static final float ANIM_WALK_SPEED = 0.25f;

    public DeeJay() {
        super();
        animated = true;
        spriteID = SPR_DEEJAY_START;
        // Default values:
        aimPivot = new PVector(8 * SCALE, 8 * SCALE); // The point that the gun pivots around.
        aimAngle = 0.0f; // The angle (in radians) that DeeJay is currently aiming.
        gunSpriteID = SPR_DEEJAY_START + 4; // The sprite for the audio gun.
        controllable = true; // Whether DeeJay is influenced by player inputs.
        shooting = false; // Whether DeeJay is currently firing the gun.
        moveSpeed = 3.5f; // The maximum speed at which DeeJay can move.
        talkingPartner = null; // The NPC that DeeJay is closest to and is "talking" to.
        talking = false; // Whether DeeJay is currently "talking" with an NPC.
    }

    public void update() {
        // Horizontal Inputs:
        if(Game.getInput(LEFT, HOLD)) {
            vel.x = -moveSpeed; // Move left.
        } else if (Game.getInput (RIGHT, HOLD)) {
            vel.x = moveSpeed; // Move right.
        } else vel.x = 0; // Don't move.

        // Vertical Inputs:
        if(Game.getInput(B_UP, HOLD)) {
            vel.y = -moveSpeed; // Move up.
        } else if (Game.getInput(DOWN, HOLD)) {
            vel.y = moveSpeed; // Move down.
        } else vel.y = 0; // Don't move.

        super.update(); // Let Entity class handle movement.

        // Find nearest NPC and make them speak if we are close enough.
        nearestNPC();

        // Aim:
        aim();

        if (shooting) {
            //DeeJay.fireBullet();
        }

        // Lastly, animate DeeJay based on inputs and his current state.
        animate();
    }

    public void nearestNPC() {
        // Find the nearest NPC:
        NPC closestNPC = null;
        float closestDist = NPC_TALK_DISTANCE;
        // Check all objects in the current room:
        for(GameObject obj : Game.roomObjects[(int)Game.camera.roomPos.x][(int)Game.camera.roomPos.y]) {
            if(obj instanceof NPC) {
                float dist = (float)Game.distance(pos, obj.pos);
                if(dist < closestDist) {
                    // New closest NPC:
                    closestNPC = (NPC)obj;
                    closestDist = dist;
                }
            }
        }
        // Check all global objects:
        for(GameObject obj : Game.globalObjects) {
            if(obj instanceof NPC) {
                float dist = (float)Game.distance(pos, obj.pos);
                if(dist < closestDist) {
                    // New closest NPC:
                    closestNPC = (NPC)obj;
                    closestDist = dist;
                }
            }
        }

        // If there is an NPC close by    OR    we already have a talking partner:
        if(closestNPC != null || talkingPartner != null) {
            // If we are do not already have a talking partner:
            if (talkingPartner == null) {
                talkingPartner = closestNPC; // Make the NPC our new talking partner.
                startConvo(); // Start talking to the NPC.
            } else { // Otherwise, already have an NPC that we talk to:
                // Get the distance to the NPC we're talking to:
                float dist = (float) Game.distance(pos, talkingPartner.pos);
                // If we're too far away from the NPC    OR    if there's another NPC closer to us:
                if (dist > NPC_TALK_DISTANCE || closestNPC != talkingPartner) {
                    stopConvo(); // Stop talking to the current talking partner.
                }
            }
        }
    }

    public void startConvo() {
        if (!talking) {
            Game.dialogueBox.dialogue = (talkingPartner.affected ? talkingPartner.postDialogue : talkingPartner.preDialogue);
            Game.dialogueBox.start();
            talking = true;
        }
    }

    public void stopConvo() {
        if (talking) {
            // If the dialogue box is open, close it:
            if (Game.dialogueBox.open) {
                Game.dialogueBox.stop();
            }
            // When the dialogue box is fully closed, stop talking:
            if (!Game.dialogueBox.active) {
                talkingPartner = null;
                talking = false;
            }
        }
    }

    public void aim() {
        PVector localPos = Game.getLocalCoordinates(pos);
        aimAngle = (float)Math.atan2(Game.mouse.x - (localPos.x + aimPivot.x), Game.mouse.y - (localPos.y + aimPivot.y));
    }

    public void animate() {
        PVector localPos = Game.getLocalCoordinates(pos);
        int rightSprite = (Game.mouse.x >= localPos.x + aimPivot.x) ? 5 : 0; // DeeJay must face right if the cursor is on his right.
        // DeeJay sprite:
        if (moving) {
            // Animate walk:
            animTime += ANIM_WALK_SPEED;
            if (animTime >= (float)ANIM_WALK_LENGTH) { // If the animation time is out of frames
                animTime = 0.0f; // Reset animation so it loops.
            }
            // Set the sprite corresponding to the animation time:
            spriteID = SPR_DEEJAY_START + 1 + (int)animTime + rightSprite;
        } else { // Not moving:
            animTime = 0.0f;
            spriteID = SPR_DEEJAY_START + rightSprite;
        }
        // Gun sprite:
        gunSpriteID = SPR_DEEJAY_START + 4 + rightSprite;
    }

    public void display() {
        PVector localPos = Game.getLocalCoordinates(pos);
        // Draw DeeJay's sprites:
        Game.drawSprite(spriteID, localPos.x, localPos.y);
        // Draw the gun sprite:
        boolean right = (Game.mouse.x >= localPos.x + aimPivot.x);
        float gunX = localPos.x + (2 - (right ? 0 : 4)) * SCALE;
        float gunY = localPos.y + 6*SCALE;
        float pivX = (6 + (right ? 0 : 4)) * SCALE;
        float pivY = 2 * SCALE;
        float newAimAngle = aimAngle - (float)Math.PI / 2.0f + (right ? 0f : (float)-Math.PI);
        Game.drawSpriteRotated(gunSpriteID, gunX, gunY, newAimAngle, pivX, pivY);
    }
}
