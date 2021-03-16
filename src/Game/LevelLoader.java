package Game;

import Objects.DeeJay;
import Objects.Wall;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class LevelLoader implements Constants {

    // Constructor:
    public LevelLoader() {

    }

    // Load room data into the game from a room file:
    public void loadRoom(String filename) {
        // Set up the loader variables:
        int roomX = 0;
        int roomY = 0;
        int tileX = 0;
        int tileY = 0;
        boolean done = false;
        boolean readingRoom = false;
        boolean readingTiles = false;
        boolean readingObject = false;
        GameObject currentObject = null;
        boolean globalObject = false;

        // The reader that reads the room file:
        BufferedReader reader;
        try {
            Game.console("ROOM: Loading: " + filename);
            // Open the room file:
            reader = new BufferedReader(new FileReader("resources/rooms/" + filename));
            // Read the first line (as lowercase text):
            String line = reader.readLine().toLowerCase().split("#")[0];
            // Loop through each line until the end of the room file:
            while (line != null && !done) {
                line = line.replace("\t", " "); // Replace tabs with spaces.
                if(!line.equals("")) {
                    for (int i = 0; i < line.length(); i++) {
                        if (line.charAt(i) != ' ') {
                            String[] args = line.substring(i).split(" ");
                            if (readingRoom) { // Read room data:
                                if (readingTiles) { // Read tile data:
                                    if (args[0].equals("\\tiles")) {
                                        readingTiles = false; // Stop reading tile data.
                                    } else {
                                        for (String arg : args) { // Loop through each tile in the current row and apply it to the current room:
                                            Game.tileData[roomX][roomY][tileX][tileY] = Byte.parseByte(arg);
                                            //Game.console("ROOM: Read tile at " + tileX + ", " + tileY);
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
                                            }
                                        }
                                    } else { // We're creating an object:
                                        switch (args[0]) { // Object parameters:
                                            case "-x" -> currentObject.pos.x = Float.parseFloat(args[1]) * TILE;
                                            case "-y" -> currentObject.pos.y = Float.parseFloat(args[1]) * TILE;
                                            case "-w" -> currentObject.width = Integer.parseInt(args[1]) * TILE;
                                            case "-h" -> currentObject.height = Integer.parseInt(args[1]) * TILE;
                                            case "-global" -> globalObject = true;
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
