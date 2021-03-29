package Objects.Enemies;
import Game.Game;
import java.util.*;
import java.util.TimerTask.*;
import java.util.concurrent.TimeUnit;
import Objects.Enemy;


public class Troll extends Enemy {

    public boolean patrolling = true;
    private int move = 1;
    private int timeleft = 0;
    private int timeright = 0;
    private int timeMax = 180;
    private int pause = 0;

    public Troll() {
        super();
    }

    public void update() {
        Patrol();



        super.update();
    }

    public void display() {
    }

    public void Patrol() {
        //This code makes the troll move forward and backward patrolling 'vel.x'
        if (patrolling) {
            // The Troll patrols if "move == 1 or 3". If it is 2 or 0, it will stand still for a second....
            if (move == 1) {
                timeleft +=1;
                vel.x = -1.5f;
                if (timeleft >= timeMax) {
                    move = 2;
                    timeleft = 0;


                }
            }
            if (move == 2 || move == 0) {
                vel.x = 0;
                pause += 1;
                if (pause == 60){
                    if (move == 2) {
                        move = 3;
                        pause= 0;
                    }
                    if (move == 0) {
                        move = 1;
                        pause = 0;
                    }
                }


            }
            if (move == 3) {
                timeright +=1;
                vel.x = 1.5f;
                if (timeright >= timeMax) {
                    move = 0;
                    timeright = 0;

                }
            }

        } else if (!patrolling) {
            vel.x = 0;
        }
    }

}

