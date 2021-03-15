package Objects;

import Game.GameObject;

public class Wall extends GameObject {

    boolean wallSolid = solid;

    Wall () {
        wallSolid = false;
    }
}
