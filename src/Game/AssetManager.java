package Game;

import processing.core.PImage;
//import processing.sound.SoundFile;

public class AssetManager implements Assets, Constants {
    // Asset lists:
    PImage[] tileSheet, spriteSheet;
    //SoundFile[] soundList;
    // Asset amounts:
    int tileCount, spriteCount, soundCount;

    public AssetManager() {
        loadAllAssets();
    }

    public void loadAllAssets() {
        // Load tiles:
        PImage tileImage = Sketch.processing.loadImage(tileFile);
        int tile = TILE / SCALE;
        int imageW = tileImage.width;
        int imageH = tileImage.height;
        int sheetW = imageW / tile;
        int sheetH = imageH / tile;
        tileCount = sheetW * sheetH;
        tileSheet = new PImage[tileCount];
        for (int y = 0; y < sheetH; y++) {
            for (int x = 0; x < sheetW; x++) {
                tileSheet[x + y * sheetW] = tileImage.get(x * tile, y * tile, tile, tile);
            }
        }

        // Load sprites:
        spriteCount = spriteFiles.length;
        spriteSheet = new PImage[spriteCount];
        for(int i = 0; i < spriteCount; i++) {
            spriteSheet[i] = Sketch.processing.loadImage(spriteFiles[i]);
        }

        // Load sounds:

    }
}
