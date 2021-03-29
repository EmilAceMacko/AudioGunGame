package Game;

import processing.core.PImage;
import processing.core.PVector;
import java.util.ArrayList;

public class Game implements Constants {
    // Game data:
    public static Byte[][][][] tileData; // [roomX][roomY][tileX][tileY]: Which tiles to draw in each room.
    public static ArrayList<GameObject>[][] roomObjects; // [roomX][roomY] <[objIndex]>: A 2D array, filled with ArrayLists.
    public static ArrayList<GameObject> globalObjects; // <[objIndex]>: A regular ArrayList.
    // Game components:
    public static AssetManager assetMgr; // Manages the game's assets (graphics and audio).
    public static LevelLoader loader; // Loads the game's levels into memory from the room files.
    public static Mixer mixer; // Handles the Mixer GUI and Mixer states.
    public static Camera camera; // Handles area-dependant drawing of graphics.
    public static DialogueBox dialogueBox; // Handles text dialogue.
    // Game Input:
    public static int[] input; // Stores the input states of each button (keyboard and mouse).
    public static char[] inputMap = {'w', 'a', 's', 'd', ' '}; // The keyboard keys to use for input.
    public static PVector mouse; // Stores the coordinte of the cursor.

    // Constructor:
    public Game() {
        init();
    }

    public static void init() {
        console("Initalizing");
        // Tile data:
        tileData = new Byte[WORLD_WIDTH][WORLD_HEIGHT][ROOM_WIDTH/TILE][ROOM_HEIGHT/TILE];
        // Room objects:
        roomObjects = new ArrayList[WORLD_WIDTH][WORLD_HEIGHT];
        for (int y = 0; y < WORLD_HEIGHT; y++) {
            for (int x = 0; x < WORLD_WIDTH; x++) {
                roomObjects[y][x] = new ArrayList<>();
            }
        }
        // Global objects:
        globalObjects = new ArrayList<>();

        // Create game components:
        assetMgr = new AssetManager();
        loader = new LevelLoader();
        mixer = new Mixer();
        camera = new Camera();
        dialogueBox = new DialogueBox();

        // Button array:
        input = new int[B_AMOUNT];
        for (int i = 0; i < B_AMOUNT; i++) input[i] = NONE; // Initialize buttons to "NONE" state.
        // Mouse coordinates:
        mouse = new PVector(0, 0);

        // Load levels:
        loader.loadRoom("test.room");
    }

    // Update game code:
    public static void update() {
        // Update mouse coordinates:
        updateMouse();
        // Update game object code:
        mixer.update(); // Update mixer before other objects.
        for(GameObject obj : globalObjects) obj.update(); // Update all global objects (including DeeJay).
        camera.update(); // Update the camera (so it looks at DeeJay's current room).
        for(GameObject obj : roomObjects[(int)camera.roomPos.x][(int)camera.roomPos.y]) obj.update(); // Update all other objects in the current room.
        dialogueBox.update(); // Update dialogue box.
        // Update inputs (change old press/release states):
        updateInput();
    }

    public static void updateMouse() {
        // Update mouse coordinates:
        mouse.x = Sketch.processing.mouseX;
        mouse.y = Sketch.processing.mouseY;
    }
    public static void updateInput() {
        // For every key and mouse button:
        for (int i = 0; i < B_AMOUNT; i++) {
            if (input[i] == PRESS) input[i] = HOLD; // If key/button was pressed, change it to hold.
            else if (input[i] == RELEASE) input[i] = NONE; // If key/button was released, change it to none.
        }
    }

    // Display game graphics:
    public static void display() {
        // Draw the default background:
        Sketch.processing.background(0);
        // Draw all the objects and tiles via camera:
        camera.display();
        // Draw all GUI elements:
        dialogueBox.display();
        mixer.display();
    }

    /*
     * ====================================================================================
     * ============================== GENERIC GAME FUNCTIONS ==============================
     * ====================================================================================
     */

    // Check if a specific button/key is in a specific input state:
    public static boolean getInput(int button, int state) {
        return ((state == PRESS && input[button] == PRESS) ||
                (state == HOLD && (input[button] & HOLD) == HOLD) ||
                (state == RELEASE && input[button] == RELEASE) ||
                (state == NONE && input[button] == NONE));
    }

    // Output a string from the game to the console:
    public static void console(String msg) {
        System.out.println("GAME: " + msg);
    }

    // Easily draw a sprite:
    public static void drawSprite(int spriteID, float x, float y) {
        PImage spr = assetMgr.spriteSheet[spriteID]; // Get the sprite.
        Sketch.processing.image(spr, x, y, spr.width * SCALE, spr.height * SCALE); // Draw the sprite.
    }
    public static void drawSprite(int spriteID, PVector pos) {
        drawSprite(spriteID, pos.x, pos.y);
    }
    public static void drawSpriteRotated(int spriteID, float x, float y, float rot, float xPivot, float yPivot) {
        Sketch.processing.pushMatrix(); // Backup the current draw settings.
        Sketch.processing.translate(x + xPivot, y + yPivot); // Translate so our pivot point is at the (0,0) origin.
        Sketch.processing.rotate(-rot); // Rotate (positive = counter-clockwise).
        Sketch.processing.translate(-x - xPivot, -y - yPivot); // Undo translation from before.
        drawSprite(spriteID, x, y); // Draw the sprite.
        Sketch.processing.popMatrix(); // Restore the previous draw settings.
    }
    public static void drawSpriteRotated(int spriteID, PVector pos, float rot, PVector pivot) {
        drawSpriteRotated(spriteID, pos.x, pos.y, rot, pivot.x, pivot.y);
    }

    // Get the distance between two points:
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2));
    }
    public static double distance(float x1, float y1, float x2, float y2) {
        return distance((double)x1, (double)y1, (double)x2, (double)y2);
    }
    public static double distance(int x1, int y1, int x2, int y2) {
        return distance((double)x1, (double)y1, (double)x2, (double)y2);
    }
    public static double distance(PVector p1, PVector p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }
}
