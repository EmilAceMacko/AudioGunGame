package Game;

import Objects.DeeJay;
import processing.core.PVector;

public class Camera implements Constants {
    // Camera variables:
    public PVector roomPos;
    public GameObject target;

    // Constructor:
    public Camera() {
        // Default values:
        roomPos = new PVector(0, 0); // Which room the camera is viewing.
        target = null; // The target that the camera will follow.
    }

    public void update() {
        if (target == null) { // If no target:
            // Find target (DeeJay):
            for (GameObject obj : Game.globalObjects) {
                if (obj instanceof DeeJay) {
                    target = obj; // Found target!
                    break;
                }
            }
        } else { // Got target:
            // Set camera position to the room that the target is in:
            roomPos.x = (float) Math.floor(target.pos.x / ROOM_WIDTH);
            roomPos.y = (float) Math.floor(target.pos.y / ROOM_HEIGHT);
        }
    }

    public void display() {
        int roomX = (int) roomPos.x;
        int roomY = (int) roomPos.y;
        // Draw the room tiles:
        for (int y = 0; y < ROOM_HEIGHT / TILE; y++) {
            for (int x = 0; x < ROOM_WIDTH / TILE; x++) {
                try {
                    byte i = Game.tileData[roomX][roomY][x][y];
                    if (i != 0x00) Sketch.processing.image(Game.assetMgr.tileSheet[i], x * TILE, y * TILE, TILE, TILE);
                } catch (NullPointerException e) {
                    //e.printStackTrace();
                }
            }
        }
        // Draw the room objects:
        for (GameObject obj : Game.roomObjects[roomX][roomY]) obj.display();
        // Draw global objects:
        for (GameObject obj : Game.globalObjects) obj.display();

        // DEBUG - Draw all object bounding boxes:
        if (DEBUG) {
            Sketch.processing.pushMatrix();
            Sketch.processing.noFill();
            Sketch.processing.strokeWeight(2);
            Sketch.processing.stroke(0, 255, 128);
            for (GameObject obj : Game.roomObjects[roomX][roomY]) {
                int xLocal = (int) (obj.pos.x - roomPos.x * ROOM_WIDTH);
                int yLocal = (int) (obj.pos.y - roomPos.y * ROOM_HEIGHT);
                Sketch.processing.rect(xLocal, yLocal, obj.width, obj.height);
            }
            Sketch.processing.stroke(255, 128, 0);
            for (GameObject obj : Game.globalObjects) {
                int xLocal = (int) (obj.pos.x - roomPos.x * ROOM_WIDTH);
                int yLocal = (int) (obj.pos.y - roomPos.y * ROOM_HEIGHT);
                Sketch.processing.rect(xLocal, yLocal, obj.width, obj.height);
            }
            Sketch.processing.popMatrix();
        }
    }
}
