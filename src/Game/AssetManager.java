package Game;

import processing.core.PImage;
import processing.sound.SoundFile;

public class AssetManager implements Assets, Constants {
    // Asset lists:
    PImage[] tileSheet, spriteSheet;
    SoundFile[] soundList;
    // Asset amounts:
    int tileCount, spriteCount, soundCount;

    public AssetManager() {
        loadAllAssets();
    }

    public void loadAllAssets() {
        // Load tiles:
        PImage tileImage = Sketch.processing.loadImage(graphicsDir + tileFile); // Get the tile sheet image.
        int tile = TILE / SCALE; // The size of each tile in the image (tiles are always square!)
        int sheetW = tileImage.width / tile; // The width of the tile sheet in tiles.
        int sheetH = tileImage.height / tile; // The height of the tile sheet in tiles.
        tileCount = sheetW * sheetH; // The number of tiles in the tile sheet.
        tileSheet = new PImage[tileCount]; // Create the tile sheet array.
        for (int y = 0; y < sheetH; y++) { // Vertical loop.
            for (int x = 0; x < sheetW; x++) { // Horizontal loop.
                tileSheet[x + y * sheetW] = tileImage.get(x * tile, y * tile, tile, tile);
            }
        }

        // Load sprites:
        spriteCount = spriteFiles.length;
        spriteSheet = new PImage[spriteCount];
        for (int i = 0; i < spriteCount; i++) {
            spriteSheet[i] = Sketch.processing.loadImage(graphicsDir + spriteFiles[i]);
        }

        // Load sounds:
        soundCount = soundFiles.length;
        soundList = new SoundFile[soundCount];
        for (int i = 0; i < soundCount; i++) {
            soundList[i] = new SoundFile(Sketch.processing, audioDir + soundFiles[i]);
        }
    }
}
