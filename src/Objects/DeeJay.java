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
    public boolean talking, talkingAffected;
    public GameObject target;
    public PVector targetCoord;
    public boolean shooting;
    public float animShoot;
    public int shootCooldown;
    public int stepTimer;
    // DeeJay constants:
    public static final int NPC_TALK_DISTANCE = TILE * 3;
    public static final int ANIM_WALK_LENGTH = 3;
    public static final float ANIM_WALK_SPEED = 0.25f;
    public static final int ANIM_SHOOT_LENGTH = 2;
    public static final float ANIM_SHOOT_SPEED = 0.25f;
    public static final int SHOOT_COOLDOWN_MAX = 4;
    // Constructor:
    public DeeJay() {
        super();
        // Default values:
        animated = true;
        spriteID = SPR_DEEJAY_START;
        aimPivot = new PVector(8 * SCALE, 8 * SCALE); // The point that the gun pivots around.
        aimAngle = 0.0f; // The angle (in radians) that DeeJay is currently aiming.
        gunSpriteID = SPR_DEEJAY_START + 4; // The sprite for the audio gun.
        controllable = true; // Whether DeeJay is influenced by player inputs.
        moveSpeed = 3.5f; // The maximum speed at which DeeJay can move.
        talkingPartner = null; // The NPC that DeeJay is closest to and is "talking" to.
        talking = false; // Whether DeeJay is currently "talking" with an NPC.
        talkingAffected = false; // Whether the NPC has been affected.
        target = null; // The object that DeeJay is hitting with the audio gun.
        targetCoord = new PVector(0, 0); // The point that DeeJay is hitting with the audio gun (relative to DeeJay).
        shooting = false; // Whether DeeJay is currently firing the gun.
        animShoot = 0.0f; // The animation value for the shooting animation.
        shootCooldown = 7;
    }

    public void update() {
        // Horizontal Inputs:
        if (Game.getInput(B_LEFT, HOLD)) {
            vel.x = -moveSpeed; // Move left.
        } else if (Game.getInput(B_RIGHT, HOLD)) {
            vel.x = moveSpeed; // Move right.
        } else vel.x = 0; // Don't move.

        // Vertical Inputs:
        if (Game.getInput(B_UP, HOLD)) {
            vel.y = -moveSpeed; // Move up.
        } else if (Game.getInput(B_DOWN, HOLD)) {
            vel.y = moveSpeed; // Move down.
        } else vel.y = 0; // Don't move.

        super.update(); // Let forwardengine.Entity class handle movement.
        if (SOUNDFUL) footStep();
        nearestNPC(); // Find nearest NPC and make them speak if we are close enough.
        aim(); // Handle aiming.
        shoot(); // Handle shooting the audio gun.
        surfWeb(); // Handle interaction with the Computer in the final room.
        animate(); // Lastly, animate DeeJay based on inputs and his current state.
    }

    public void footStep() {
        if (moving) {
            stepTimer--;
            if (stepTimer <= 0) {
                Game.playSound(SND_FOOTSTEP);
                stepTimer = (int) (ANIM_WALK_LENGTH / ANIM_WALK_SPEED);
            }
        } else {
            stepTimer = 0;
        }
    }

    public void nearestNPC() {
        // Find the nearest NPC:
        NPC closestNPC = null;
        float closestDist = NPC_TALK_DISTANCE;
        // Check all objects in the current room:
        for (GameObject obj : Game.roomObjects[Game.camera.roomX][Game.camera.roomY]) {
            if (obj instanceof NPC) {
                float dist = (float)Game.distance(pos, obj.pos);
                if (dist < closestDist) {
                    // New closest NPC:
                    closestNPC = (NPC)obj;
                    closestDist = dist;
                }
            }
        }
        // Check all global objects:
        for (GameObject obj : Game.globalObjects) {
            if (obj instanceof NPC) {
                float dist = (float)Game.distance(pos, obj.pos);
                if (dist < closestDist) {
                    // New closest NPC:
                    closestNPC = (NPC)obj;
                    closestDist = dist;
                }
            }
        }

        // If there is an NPC close by    OR    we already have a talking partner:
        if (closestNPC != null || talkingPartner != null) {
            // If we are do not already have a talking partner:
            if (talkingPartner == null) {
                talkingPartner = closestNPC; // Make the NPC our new talking partner.
                talkingAffected = talkingPartner.affected;
                startConvo(); // Start talking to the NPC.
            } else { // Otherwise, already have an NPC that we talk to:
                // Get the distance to the NPC we're talking to:
                float dist = (float) Game.distance(pos, talkingPartner.pos);
                // If we're too far away from the NPC, or...
                // If there's another NPC closer to us, or...
                // If the NPC has been affected while talking:
                if (dist > NPC_TALK_DISTANCE || closestNPC != talkingPartner || talkingAffected != talkingPartner.affected) {
                    stopConvo(); // Stop talking to the current talking partner.
                }
            }
        }
    }

    public void startConvo() {
        if (!talking && talkingPartner.hasDialogue) {
            Game.dialogueBox.dialogue = (talkingPartner.affected ? talkingPartner.postDialogue : talkingPartner.preDialogue);
            Game.dialogueBox.start();
            Game.dialogueBox.upper = (talkingPartner.pos.y - Game.camera.roomY * ROOM_HEIGHT > ROOM_HEIGHT / 3f);
            talking = true;
            talkingPartner.talking = true;
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
                talkingPartner.talking = false;
                talkingPartner = null;
                talkingAffected = false;
                talking = false;
            }
        }
    }

    public void aim() {
        PVector localPos = Game.getLocalCoordinates(pos);
        aimAngle = (float)Math.atan2(Game.mouse.x - (localPos.x + aimPivot.x), Game.mouse.y - (localPos.y + aimPivot.y));
    }

    public void shoot() {
        // Decrement shoot-cooldown:
        if(shootCooldown > 0) shootCooldown--;

        // Shoot only if the left mouse button is pressed/held, and if the cursor is within the room area.
        boolean mouseInRoom = Game.pointInArea(Game.mouse, 0, 0, ROOM_WIDTH, ROOM_HEIGHT);
        shooting = false;
        if (Game.getInput(MB_LEFT, PRESS) && mouseInRoom && shootCooldown <= 0)
        {
            shooting = true;
            shootCooldown = SHOOT_COOLDOWN_MAX;

            float overtonePitch = 1f;
            boolean targetAffectedByAudio = false;

            PVector shootPoint = PVector.add(pos, aimPivot); // The point that the audio gun ray is shot from.
            ArrayList<GameObject> newTargets = new ArrayList<>(); // The objects that are hit by the audio gun ray.
            float aimReal = (float) Math.PI/2 - aimAngle; // The corrected aim angle.
            // Test for ray collision on all objects:
            for (GameObject obj : Game.roomObjects[Game.camera.roomX][Game.camera.roomY]) {
                if (obj.solid && collisionRayObject(aimReal, shootPoint, obj) != null) newTargets.add(obj);
            }
            for (GameObject obj : Game.globalObjects) {
                if (obj != this && obj.solid && collisionRayObject(aimReal, shootPoint, obj) != null) newTargets.add(obj);
            }
            // Pick the object that is closest to DeeJay:
            if (newTargets.size() > 0) {
                float closest = new PVector(ROOM_WIDTH, ROOM_HEIGHT).mag();
                GameObject closestObj = null;
                for (GameObject obj : newTargets) {
                    float newDist = PVector.sub(obj.pos, pos).mag();
                    if (newDist <= closest) {
                        closest = newDist;
                        closestObj = obj;
                    }
                }
                if (target != null) {
                    // Check if target is not the same object we're already hitting:
                    if (closestObj != target) {
                        // Stop affecting old target (if we had one):
                        target.isHitByAudio = false;
                        // Set new target:
                        target = closestObj;
                    }
                    // Affect target:
                    targetAffectedByAudio = target.audioHit(Game.mixer.waveform, Game.mixer.getFrequencyScale());
                } else {
                    if (closestObj != null) {
                        // Set new target:
                        target = closestObj;
                        // Affect new target:
                        targetAffectedByAudio = target.audioHit(Game.mixer.waveform, Game.mixer.getFrequencyScale());
                    }
                }

                if (target != null) {
                    // Do ray collision again to get the ray intersection coordinate (relative to the shoot point):
                    targetCoord = collisionRayObject(aimReal, shootPoint, target).sub(shootPoint);

                    // Get target audioPersistence data:
                    overtonePitch = (float) target.audioPersistence / (float) target.audioPersistenceThreshold;
                    if(overtonePitch > 1.0f) overtonePitch = 1.0f;
                    else if(overtonePitch < 0.0f) overtonePitch = 0.0f;
                }

            } else { // No objects hit by ray.
                target = null; // Unset target.
                // Set target coordinate to maximum ray distance:
                targetCoord.set((float) Math.cos(aimReal) * RAY_LENGTH, (float) Math.sin(aimReal) * RAY_LENGTH);
            }
            // Play audio:
            Game.mixer.playOscillators(targetCoord.mag(), targetAffectedByAudio, overtonePitch);
        } else { // Not shooting:
            target = null; // Unset target.
            targetCoord.set(0, 0); // Set target coordinate to zero.
        }
    }

    public void surfWeb() {
        if(Game.getInput(B_SPACE, PRESS)) {
            ArrayList<Computer> list = Game.listRoomObjects(Computer.class);
            if(!list.isEmpty()) {
                Computer computer = list.get(0);
                // Check if DeeJay is touching any of the computer's four sides:
                if (collisionObjectObject(this, 0, -SCALE, computer) ||  // Check DeeJay's top side.
                    collisionObjectObject(this, 0, SCALE, computer) ||   // Check DeeJay's bottom side.
                    collisionObjectObject(this, -SCALE, 0, computer) ||  // Check DeeJay's left side.
                    collisionObjectObject(this, SCALE, 0, computer)) {   // Check DeeJay's right side.
                    computer.openBrowser(); // Open the player's browser.
                }
            }
        }
    }

    public void animate() {
        PVector localPos = Game.getLocalCoordinates(pos);
        int rightSprite = (Game.mouse.x >= localPos.x + aimPivot.x) ? 5 : 0; // DeeJay must face right if the cursor is on his right.
        // DeeJay sprite:
        if (moving) {
            // Animate walk:
            animTime += ANIM_WALK_SPEED;
            if (animTime >= (float) ANIM_WALK_LENGTH) { // If the animation time is out of frames
                animTime = 0.0f; // Reset animation so it loops.
            }
            // Set the sprite corresponding to the animation time:
            spriteID = SPR_DEEJAY_START + 1 + (int) animTime + rightSprite;
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
        gunX -= (float) Math.sin(aimAngle) * animShoot;
        gunY -= (float) Math.cos(aimAngle) * animShoot;
        // Mirror the aim angle depending on the facing direction:
        float newAimAngle = aimAngle - (float) Math.PI / 2.0f + (right ? 0f : (float) -Math.PI);
        // Draw the gun sprite:
        Game.drawSpriteRotated(gunSpriteID, gunX, gunY, newAimAngle, pivX, pivY);

        // Draw audio gun ray:
        if (DEBUG && shooting) {
            float roomOffX = Game.camera.roomX * ROOM_WIDTH;
            float roomOffY = Game.camera.roomY * ROOM_HEIGHT;
            Sketch.processing.stroke(255, 0, 255);
            Sketch.processing.line(
                pos.x + aimPivot.x - roomOffX,
                pos.y + aimPivot.y - roomOffY,
                pos.x + aimPivot.x + targetCoord.x - roomOffX,
                pos.y + aimPivot.y + targetCoord.y - roomOffY
            );
        }
    }
}
