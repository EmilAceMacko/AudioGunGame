package Game;

import Objects.DeeJay;
import processing.core.PVector;
import java.util.ArrayList;

public class Camera implements Constants {
    // Camera variables:
    public int roomX, roomY;
    public GameObject target;
    // GUI variables:
    public float coinAnim = 0f;
    // GUI constants:
    public static final float COIN_ANIM_SPEED = 0.25f;
    public static final float COIN_ANIM_LENGTH = 10;
    public static final int[] COIN_ANIM_SPRITES = {0, 0, 0, 1, 1, 2, 3, 4, 5, 5};
    public static final int COIN_COUNTER_X = 16 * SCALE;
    public static final int COIN_COUNTER_Y = 2 * SCALE;
    public static final int COIN_DIGIT_W = 8 * SCALE;

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
                    //e.printStackTrace(); // Produces a lot of spam for empty rooms.
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
        // GUI:
        drawCoinGUI();
    }

    // Draw the coin icon and coin counter.
    void drawCoinGUI() {
        // Animate coin iccon:
        coinAnim += COIN_ANIM_SPEED;
        if (coinAnim >= COIN_ANIM_LENGTH) coinAnim = 0f;
        // Draw coin icon:
        int frame = (int) Math.floor(coinAnim);
        Game.drawSprite(SPR_GUI_COIN_START + COIN_ANIM_SPRITES[frame], 0, 0);
        // Draw digits of coin counter:
        String count = Integer.toString(Game.coin);
        for (int i = 0; i < count.length(); i++) {
            int f = count.charAt(i) - '0'; // Get the current char in the number string, and subtract the char index of the "zero" char.
            Game.drawSprite(SPR_GUI_COIN_DIGIT_START + f, COIN_COUNTER_X + COIN_DIGIT_W * i, COIN_COUNTER_Y); // Draw the digit.
        }
    }
}
