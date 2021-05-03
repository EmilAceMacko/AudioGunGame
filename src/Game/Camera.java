package Game;

import Objects.DeeJay;
import processing.core.PVector;
import java.util.ArrayList;

public class Camera implements Constants {
    // Camera variables:
    public int roomX, roomY;
    public GameObject target;
    // Constructor:
    public Camera() {
        // Default values:
        roomX = 0; // Which room on the X axis the camera is viewing.
        roomY = 0; // Which room on the Y axis the camera is viewing.
        target = null; // The target that the camera will follow.
    }

    public void update() {
        if (target == null) { // If no target:
            // Find target (DeeJay):
            ArrayList<DeeJay> list = Game.listGlobalObjects(DeeJay.class); // Get list of "all DeeJays" (we know only one should ever exist!)
            if(!list.isEmpty()) target = list.get(0); // Found target!
        } else { // Got a target:
            // Set camera position to the room that the target is in:
            roomX = (int) Math.floor(target.pos.x / ROOM_WIDTH);
            roomY = (int) Math.floor(target.pos.y / ROOM_HEIGHT);
        }
    }

    public void display() {
        // Draw the room tiles:
        for (int y = 0; y < ROOM_HEIGHT / TILE; y++) {
            for (int x = 0; x < ROOM_WIDTH / TILE; x++) {
                try {
                    byte i = Game.tileData[roomX][roomY][x][y];
                    if (i != 0x00) Sketch.processing.image(Game.assetMgr.tileSheet[i], x * TILE, y * TILE, TILE, TILE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
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
            Sketch.processing.stroke(0, 255, 128);
            for (GameObject obj : Game.roomObjects[roomX][roomY]) {
                if (obj.isHitByAudio) Sketch.processing.strokeWeight(4);
                else Sketch.processing.strokeWeight(2);
                PVector localPos = Game.getLocalCoordinates(obj.pos);
                Sketch.processing.rect(localPos.x, localPos.y, obj.width, obj.height);
            }
            Sketch.processing.stroke(255, 128, 0);
            for (GameObject obj : Game.globalObjects) {
                if (obj.isHitByAudio) Sketch.processing.strokeWeight(4);
                else Sketch.processing.strokeWeight(2);
                PVector localPos = Game.getLocalCoordinates(obj.pos);
                Sketch.processing.rect(localPos.x, localPos.y, obj.width, obj.height);
            }
            Sketch.processing.popMatrix();
        }
    }
}
