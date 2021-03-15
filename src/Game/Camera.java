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
        roomPos = new PVector(0, 0);
        target = null;
    }

    public void update() {
        if(target == null) { // If no target:
            // Find target (DeeJay):
            for(GameObject obj : Game.globalObjects) {
                if(obj instanceof DeeJay) {
                    target = obj; // Found target!
                    break;
                }
            }
        }
        else { // Got target:
            // Set camera position to the room that the target is in:
            roomPos.x = (float)Math.floor(target.pos.x / ROOM_WIDTH);
            roomPos.y = (float)Math.floor(target.pos.y / ROOM_HEIGHT);
        }
    }

    public void display() {
        // Draw the room tiles:
        for(int y = 0; y < ROOM_HEIGHT/TILE; y++) {
            for(int x = 0; x < ROOM_WIDTH/TILE; x++) {
                int i = Game.tileData[(int)roomPos.x][(int)roomPos.y][x][y];
                if(i > 0) Sketch.processing.image(Game.assetMgr.tileSheet[i], x, y, TILE, TILE);
            }
        }
        // Draw the room objects:
        for(GameObject obj : Game.roomObjects[(int)roomPos.x][(int)roomPos.y])
        {
            int xLocal = (int)(obj.pos.x - roomPos.x * ROOM_WIDTH);
            int yLocal = (int)(obj.pos.y - roomPos.y * ROOM_HEIGHT);
            if(obj.spriteID > 0) Sketch.processing.image(Game.assetMgr.spriteSheet[obj.spriteID], xLocal, yLocal, obj.width, obj.height);
        }
        // Draw global objects:
        for(GameObject obj : Game.globalObjects)
        {
            int xLocal = (int)(obj.pos.x - roomPos.x * ROOM_WIDTH);
            int yLocal = (int)(obj.pos.y - roomPos.y * ROOM_HEIGHT);
            if(obj.spriteID > 0) Sketch.processing.image(Game.assetMgr.spriteSheet[obj.spriteID], xLocal, yLocal, obj.width, obj.height);
        }
    }
}
