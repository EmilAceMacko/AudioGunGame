package Game;

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
    public static Mixer mixer; // Handles the Mixer GUI and
    public static Camera camera;
    public static DialogueBox dialogue;
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

        // Create components:
        assetMgr = new AssetManager();
        loader = new LevelLoader();
        mixer = new Mixer();
        camera = new Camera();
        dialogue = new DialogueBox();

        // Button array:
        input = new int[B_AMOUNT];
        for (int i = 0; i < B_AMOUNT; i++) {
            input[i] = NONE;
        }
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
        dialogue.update(); // Update dialogue box.
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
        dialogue.display();
        mixer.display();
    }

    // Check if a specific button/key is in a specific input state:
    public static boolean getInput(int button, int state) {
        if (state == PRESS && input[button] == PRESS) return true;
        else if (state == HOLD && (input[button] & HOLD) == HOLD) return true;
        else if (state == RELEASE && input[button] == RELEASE) return true;
        else return false;
    }

    // Output a string from the game to the console:
    public static void console(String msg) {
        System.out.println("GAME: " + msg);
    }
}
