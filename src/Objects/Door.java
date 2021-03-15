package Objects;

import Game.GameObject;

public class Door extends GameObject {
    public boolean open;

    public Door() {
        this.open = false;
    }

    //These two methods are used to change open/close status on the door object
    public void setDoorOpen () { this.open = true; }
    public void setDoorClose() { this.open = false; }

    //This method will check the status of the door and will pass information trough to updateGraphics() thats start the door animation, depending on the variable passed to it
    public void updateActions(){
        if(this.open == true) {
            this.updateGraphics(this, 1);
        }

        if(this.open == false) {
            this.updateGraphics(this, 0);
        }
    }

    /* doorInput is the door object that should be visually updated. The int closeOrOpen receives either a 1 or 0
       0 means that it should begin the close door animation. 1 means that it should begin the open door animation    */
    public void updateGraphics(Door doorInput, int closeOrOpen) {
        if(closeOrOpen == 1) {
            doorInput.openAnimation(); // Initiates the open door animation
            doorInput.playOpenSound(); // This method could be added so a specific sound effect would be played
        } else if (closeOrOpen == 0) {
            doorInput.closeAnimation();// Initiates the close door animation
            doorInput.playCloseSound(); //This method could be added so a specific sound effect would be played
        }
    }
}
