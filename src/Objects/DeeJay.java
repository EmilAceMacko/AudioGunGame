package Objects;

import Game.Sketch;
import Game.Entity;
import Game.Game;
import Game.GameObject;
import processing.core.PVector;

import java.util.ArrayList;

public class DeeJay extends Entity {
    // DeeJay variables:
    public PVector aimPivot;
    public float aimAngle;
    public int gunSpriteID;
    public boolean controllable;
    public float moveSpeed;
    public NPC talkingPartner;
    public boolean talking;
    public GameObject target;
    public boolean shooting;
    public float animShoot;
    // DeeJay constants:
    public static final int NPC_TALK_DISTANCE = TILE * 3;
    public static final int ANIM_WALK_LENGTH = 3;
    public static final float ANIM_WALK_SPEED = 0.25f;
    public static final int ANIM_SHOOT_LENGTH = 2;
    public static final float ANIM_SHOOT_SPEED = 0.25f;

    public DeeJay() {
        super();
        animated = true;
        spriteID = SPR_DEEJAY_START;
        // Default values:
        aimPivot = new PVector(8 * SCALE, 8 * SCALE); // The point that the gun pivots around.
        aimAngle = 0.0f; // The angle (in radians) that DeeJay is currently aiming.
        gunSpriteID = SPR_DEEJAY_START + 4; // The sprite for the audio gun.
        controllable = true; // Whether DeeJay is influenced by player inputs.
        moveSpeed = 3.5f; // The maximum speed at which DeeJay can move.
        talkingPartner = null; // The NPC that DeeJay is closest to and is "talking" to.
        talking = false; // Whether DeeJay is currently "talking" with an NPC.
        target = null; // The object that DeeJay is hitting with the audio gun.
        shooting = false; // Whether DeeJay is currently firing the gun.
        animShoot = 0.0f; // The animation value for the shooting animation.
    }

    public void update() {
        // Horizontal Inputs:
        if(Game.getInput(B_LEFT, HOLD)) {
            vel.x = -moveSpeed; // Move left.
        } else if (Game.getInput (B_RIGHT, HOLD)) {
            vel.x = moveSpeed; // Move right.
        } else vel.x = 0; // Don't move.

        // Vertical Inputs:
        if(Game.getInput(B_UP, HOLD)) {
            vel.y = -moveSpeed; // Move up.
        } else if (Game.getInput(B_DOWN, HOLD)) {
            vel.y = moveSpeed; // Move down.
        } else vel.y = 0; // Don't move.

        // Let Entity class handle movement:
        super.update();
        // Find nearest NPC and make them speak if we are close enough.
        nearestNPC();
        // Handle aiming:
        aim();
        // Handle shooting the audio gun:
        shoot();
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

    public void shoot() {
        // Shoot only if the left mouse button is pressed/held, and if the cursor is within the room area.
        boolean mouseInRoom = Game.pointInArea(Game.mouse, 0, 0, ROOM_WIDTH, ROOM_HEIGHT);
        if (Game.getInput(MB_LEFT, HOLD) && mouseInRoom) shooting = true;
        else if (Game.getInput(MB_LEFT, NONE) || !mouseInRoom) shooting = false;

        Game.mixer.playing = shooting;

        if (shooting) {
            ArrayList<GameObject> newTargets = new ArrayList<>(); // The objects that are hit by the audio gun ray.
            float aimReal = (float)Math.PI/2 - aimAngle;
            // Test for ray collision on all objects:
            for (GameObject obj : Game.roomObjects[(int)Game.camera.roomPos.x][(int)Game.camera.roomPos.y]) {
                if (collisionRayObject(aimReal, PVector.add(pos, aimPivot), obj)) {
                    newTargets.add(obj);
                }
            }
            for (GameObject obj : Game.globalObjects) {
                if (obj != this && collisionRayObject(aimReal, PVector.add(pos, aimPivot), obj)) {
                    newTargets.add(obj);
                }
            }
            // Pick the object that is closest to DeeJay:
            if(newTargets.size() > 0) {
                float closest = new PVector(ROOM_WIDTH, ROOM_HEIGHT).mag();
                GameObject closestObj = null;
                for(GameObject obj : newTargets) {
                    float newDist = PVector.sub(obj.pos, pos).mag();
                    if(newDist <= closest) {
                        closest = newDist;
                        closestObj = obj;
                    }
                }
                if(target != null) {
                    // Check if target is not the same object we're already hitting:
                    if (closestObj != target) {
                        target.influencedByAudio = false; // Stop affecting old target (if we had one).
                        target = closestObj; // Set new target.
                        target.influencedByAudio = true;
                    }
                } else {
                    if (closestObj != null) {
                        target = closestObj; // Set new target.
                        target.influencedByAudio = true;
                    }
                }
            } else { // No objects hit by ray.
                if(target != null) {
                    target.influencedByAudio = false; // Stop affecting old target (if we had one).
                    target = null;
                }
            }
            /*
            if(target != null) {
                // For now, only check if the waveform matches:
                if(Game.mixer.waveform == target.waveInfluence) target.influencedByAudio = true;
            }*/

        } else { // Not shooting:
            // If the audio gun was affecting a target:
            if(target != null) target.influencedByAudio = false; // Stop affecting old target.
            target = null;
        }
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
        // Gun shaking when shooting:
        if (shooting) {
            // Animate gun:
            animShoot += ANIM_SHOOT_SPEED;
            if(animShoot >= ANIM_SHOOT_LENGTH) animShoot = 0.0f;
        } else { // Not shooting:
            animShoot = 0.0f;
        }
        // Gun sprite (direction):
        gunSpriteID = SPR_DEEJAY_START + 4 + rightSprite;
    }

    public void display() {
        PVector localPos = Game.getLocalCoordinates(pos);
        // Draw DeeJay's sprites:
        Game.drawSprite(spriteID, localPos.x, localPos.y);
        // Determine the facing direction:
        boolean right = (Game.mouse.x >= localPos.x + aimPivot.x);
        // Gun positional/pivotal coordinates:
        float gunX = localPos.x + (2 - (right ? 0 : 4)) * SCALE;
        float gunY = localPos.y + 6 * SCALE;
        float pivX = (6 + (right ? 0 : 4)) * SCALE;
        float pivY = 2 * SCALE;
        // Add gun shake:
        gunX -= (float)Math.sin(aimAngle) * animShoot;
        gunY -= (float)Math.cos(aimAngle) * animShoot;
        // Mirror the aim angle depending on the facing direction:
        float newAimAngle = aimAngle - (float)Math.PI / 2.0f + (right ? 0f : (float)-Math.PI);
        // Draw the gun sprite:
        Game.drawSpriteRotated(gunSpriteID, gunX, gunY, newAimAngle, pivX, pivY);

        if(shooting) collisionRayObject(-aimAngle+(float)Math.PI/2, PVector.add(pos, aimPivot), Game.roomObjects[0][0].get(0));
    }
}
