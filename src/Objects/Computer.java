package Objects;

import Game.GameObject;

import Game.Game;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Computer extends GameObject {

    public String url = "http://google.com/";

    public Computer() {
        super();
        spriteID = SPR_COMPUTER;
    }

    public void openBrowser() {
        try {
            Desktop d = Desktop.getDesktop();
            d.browse(new URI(url));
        } catch (URISyntaxException | IOException e) {
            Game.console("ERROR: Could not open url " + url);
            e.printStackTrace();
        }
    }

    public void update() {
        super.update();
    }
    public void display() {
        super.display();
    }

}
