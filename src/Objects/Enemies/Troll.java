package Objects.Enemies;
import Game.Game;
import java.util.*;
import java.util.TimerTask.*;
import Objects.Enemy;

public class Troll extends Enemy {

    public boolean patrolling = true;
    private boolean moveleft = true;
    private int timeleft = 0;
    private int timeright = 0;
    private int timeMax = 600;


    public void update() {
        Patrol();



        super.update();
    }

    public void display() {
    }

    public void Patrol() {
        //This code makes the troll move forward and backward patrolling 'vel.x'
        if (patrolling) {
            if (moveleft) {
                timeleft +=1;
                vel.x = -1.5f;
                if (timeleft >= timeMax) {
                    moveleft = false;
                    timeleft = 0;
                }
            }
            if (!moveleft) {
                timeright +=1;
                vel.x = 1.5f;
                if (timeright >= timeMax) {
                    moveleft = true;
                    timeright = 0;
                }
            }

        } else if (!patrolling) {
            vel.x = 0;
        }
    }
}
