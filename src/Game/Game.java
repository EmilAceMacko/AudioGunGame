package Game;

import processing.core.PVector;
import java.util.ArrayList;

public class Game implements Constants {
    // Game data:
    public static Byte[][][][] tileData; // [roomX][roomY][tileX][tileY]: Which tiles to draw in each room.
    public static ArrayList<GameObject>[][] roomObjects; // [roomX][roomY] <[objIndex]>: A 2D array, filled with ArrayLists.
    public static ArrayList<GameObject> globalObjects; // <[objIndex]>: A regular ArrayList.
    // Game components:
    public static AssetManager assetMgr;
    public static LevelLoader loader;
    public static Mixer mixer;
    public static Camera camera;
    public static DialogueBox dialogue;
    // Game Input:
    public static int[] input;
    public static char[] inputMap = {'w', 'a', 's', 'd', ' '};
    public static PVector mouse;

    // Constructor:
    public Game() {
        // Tile data:
        tileData = new Byte[WORLD_WIDTH][WORLD_HEIGHT][ROOM_WIDTH/TILE][ROOM_HEIGHT/TILE];
        // Room objects:
        roomObjects = new ArrayList[WORLD_WIDTH][WORLD_HEIGHT];
        for(int y = 0; y < WORLD_HEIGHT; y++) {
            for(int x = 0; x < WORLD_WIDTH; x++) {
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
        for(int i = 0; i < B_AMOUNT; i++) {
            input[i] = NONE;
        }
        // Mouse coordinates:
        mouse = new PVector(0, 0);
    }

    // Update game code:
    public static void update() {
        // Update mouse coordinates:
        updateMouse();
        // Update game objects!
        camera.update();
        dialogue.update();
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
        for(int i = 0; i < B_AMOUNT; i++) {
            if(input[i] == PRESS) input[i] = HOLD; // If key/button was pressed, change it to hold.
            else if(input[i] == RELEASE) input[i] = NONE; // If key/button was released, change it to none.
        }
    }


    // Display game graphics:
    public static void display() {
        // Draw the background:
        Sketch.processing.background(0);

        // Tell the camera to draw all objects and tiles!
        // TODO - Draw game graphics via camera!

        camera.display();
        // Check above if needed
        dialogue.display();
    }

    // Check if a specific button/key is in a specific input state:
    public static boolean getInput(int button, int state) {
        if(state == PRESS && input[button] == PRESS) return true;
        else if(state == HOLD && (input[button] & HOLD) == HOLD) return true;
        else if(state == RELEASE && input[button] == RELEASE) return true;
        else return false;
    }

    public static void loadAssets() {

    }
}
