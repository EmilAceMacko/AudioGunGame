package Game;

import Objects.Door;
import Objects.Wall;
import Objects.DeeJay;
import Objects.NPCs.*;
import Objects.Enemies.*;
import Objects.Doors.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LevelLoader implements Constants {
    // Constructor:
    public LevelLoader() {

    }

    // Load room data into the game from a room file:
    public void loadRoom(String filename) {
        // Set up the loader variables:
        int roomX = 0; // X coordinate of the room that is currently being loaded.
        int roomY = 0; // Y coordinate of the room that is currently being loaded.
        int tileX = 0; // X coordinate of the tile that has been read so far (when reading tiles).
        int tileY = 0; // Y coordinate of the tile that has been read so far (when reading tiles).
        boolean done = false; // Whether we're done loading a room.
        boolean readingRoom = false; // Whether we're currently reading room data.
        boolean readingTiles = false; // Whether we're currently reading tile data.
        boolean readingObject = false; // Whether we're currently reading object data.
        GameObject currentObject = null; // Holds the address of the object that is created and put data into.
        boolean globalObject = false; // Whether the object that is being read should be treated as a global object.
        BufferedReader reader; // The string reader that reads the room file.

        try { // Try loading the file and catch I/O exceptions.
            Game.console("ROOM: Loading: " + filename);
            // Open the room file:
            reader = new BufferedReader(new FileReader("resources/rooms/" + filename));
            // Read the first line (as lowercase text):
            String line = reader.readLine().toLowerCase().split("#")[0];
            // Loop through each line until the end of the room file:
            while (line != null && !done) {
                line = line.replace("\t", " ").replaceAll("\\s+", " "); // Replace tabs with spaces and remove consecutive spaces.
                if (!line.equals("")) { // If not a blank line:
                    for (int i = 0; i < line.length(); i++) { // Loop through all chars in this line:
                        if (line.charAt(i) != ' ') { // If char is not a space.
                            String[] args = line.substring(i).split(" "); // Split the line into separate strings, with spaces as separators.
                            if (readingRoom) { // Read room data:
                                if (readingTiles) { // If reading tile data:
                                    if (args[0].equals("\\tiles")) { // If end of tile block:
                                        readingTiles = false; // Stop reading tile data.
                                    } else {
                                        for (String arg : args) { // Loop through each tile in the current row and apply it to the current room:
                                            Game.tileData[roomX][roomY][tileX][tileY] = Byte.parseByte(arg);
                                            tileX++; // Next tile.
                                        }
                                        tileX = 0;
                                        tileY++; // Move on to the next tile row.
                                    }
                                } else if (readingObject) { // Read object data:
                                    if (args[0].equals("\\object")) {
                                        readingObject = false; // Stop reading object data.
                                        if (currentObject != null) {
                                            // Add the object to the game:
                                            if (globalObject) Game.globalObjects.add(currentObject);
                                            else Game.roomObjects[roomX][roomY].add(currentObject);
                                            // Reset object-reader states:
                                            currentObject = null;
                                            globalObject = false;
                                        }
                                    } else if (currentObject == null) { // If we're not already creating an object:
                                        if (args[0].equals("-type")) { // Create new object based on object type:
                                            switch (args[1]) {
                                                case "wall" -> currentObject = new Wall();
                                                case "player" -> currentObject = new DeeJay();
                                                case "enemy" -> {
                                                    switch(args[2]) {
                                                        case "slime" -> currentObject = new Slime();
                                                        case "bigslime" -> currentObject = new BigSlime();
                                                    }
                                                }
                                                case "npc" -> {
                                                    switch(args[2]) {
                                                        case "intro" -> currentObject = new NPC_IntroGuide();
                                                        case "treeperson" -> currentObject = new NPC_TreePerson();
                                                        case "farmer" -> currentObject = new NPC_Farmer();
                                                        case "neighbor1" -> currentObject = new NPC_Neighbor1();
                                                        case "neighbor2" -> currentObject = new NPC_Neighbor2();
                                                        case "oldman" -> currentObject = new NPC_Oldman();
                                                        case "wallteacher" -> currentObject = new NPC_WallTeacher();
                                                    }
                                                }
                                                case "door" -> {
                                                    switch(args[2]) {
                                                        case "tree" -> currentObject = new Tree();
                                                        case "gateh" -> currentObject = new Gate_H();
                                                        case "gatev" -> currentObject = new Gate_V();
                                                        case "wallh" -> currentObject = new Wall_H();
                                                        case "wallv" -> currentObject = new Wall_V();
                                                    }
                                                }
                                            }
                                        }
                                    } else { // We're creating an object:
                                        switch (args[0]) { // Object parameters:
                                            case "-x" -> currentObject.pos.x = Float.parseFloat(args[1]) * TILE + roomX * ROOM_WIDTH;
                                            case "-y" -> currentObject.pos.y = Float.parseFloat(args[1]) * TILE + roomY * ROOM_HEIGHT;
                                            case "-w" -> currentObject.width = Integer.parseInt(args[1]) * TILE;
                                            case "-h" -> currentObject.height = Integer.parseInt(args[1]) * TILE;
                                            case "-global" -> globalObject = true;
                                        }
                                        if (currentObject instanceof Slime) { // If the object is a Slime:
                                            Slime slimeObject = (Slime) currentObject; // Treat the object like a Slime.
                                            switch (args[0]) { // Slime parameters:
                                                case "-rx" -> slimeObject.roamAreaPos.x = Float.parseFloat(args[1]) * TILE + roomX * ROOM_WIDTH;
                                                case "-ry" -> slimeObject.roamAreaPos.y = Float.parseFloat(args[1]) * TILE + roomY * ROOM_HEIGHT;
                                                case "-rw" -> slimeObject.roamAreaWidth = Integer.parseInt(args[1]) * TILE;
                                                case "-rh" -> slimeObject.roamAreaHeight = Integer.parseInt(args[1]) * TILE;
                                                case "-rsx" -> slimeObject.roamSpeed.x = Float.parseFloat(args[1]);
                                                case "-rsy" -> slimeObject.roamSpeed.y = Float.parseFloat(args[1]);
                                            }
                                        }
                                        if (currentObject instanceof Door) { // If the object is a Door:
                                            Door doorObject = (Door) currentObject; // Treat the object like a Door.
                                            switch(args[0]) { // Door parameters:
                                                case "-open" -> {
                                                    doorObject.open = true;
                                                    doorObject.solid = false;
                                                }
                                                case "-closed" -> {
                                                    doorObject.open = false;
                                                    doorObject.solid = true;
                                                }
                                            }
                                        }
                                    }
                                } else { // Not reading tile or object data:
                                    switch (args[0]) { // Room parameters:
                                        case "-x" -> roomX = Integer.parseInt(args[1]);
                                        case "-y" -> roomY = Integer.parseInt(args[1]);
                                        case "-tiles" -> readingTiles = true; // Start reading tile data.
                                        case "-object" -> readingObject = true; // Start reading object data.
                                        case "\\room" -> done = true; // Stop reading room data.
                                    }
                                }
                            } else { // Not currently reading room data:
                                if (args[0].equals("-room")) readingRoom = true; // Start reading room data.
                            }
                            break;
                        }
                    }
                }
                // Read the next line and continue the loop.
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace(); // File could not be accessed!
        }
    }
}
