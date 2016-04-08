import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by alvarpq on 2/17/2016.
 */
public class Obstacles {
    int maxNum;
    int frameX, frameY;
    Random randy;

    ArrayList<Obstacle> pickup = new ArrayList<>();
    ArrayList<Star> stars = new ArrayList<>();

    public Obstacles(int num, int width, int height)//creates all obstacles
    {
        randy = new Random();
        maxNum = num;
        frameX = width;
        frameY = height;
        for(int x = 0; x < 500; x++)
        {
            stars.add(new Star(new Position(randy.nextInt(frameX), randy.nextInt(frameY))));
        }
    }


    public synchronized void update(boolean[] inputs)//updates obstacles. If there aren't enough, there's a chance to make more
    {

        for (Obstacle ship : pickup)
        {
            ship.update();
        }

        int size = pickup.size();

        ArrayList<Obstacle> shipsNew = new ArrayList<>();
        for (Obstacle ship : pickup) {
            if (!(ship.pos.getX() <= 0 || ship.pos.getY() <= 0 || ship.pos.getX() >= frameX || ship.pos.getY() >= frameY)) {
                shipsNew.add(ship);
            }
        }

        pickup = shipsNew;

        if(size < maxNum * randy.nextInt(2))
        {
            pickup.add(generateObstacle(frameX, frameY));
        }
    }

    public synchronized ArrayList<Obstacle> getObstacles()
    {
        return pickup;
    }

    public synchronized void setObstacles(ArrayList<Obstacle> nObs)
    {
        pickup = nObs;
    }

    public synchronized Obstacle generateObstacle(int frameX, int frameY)
    {
        int x, y;
        double velY, velX;

        switch (randy.nextInt(4)) {
            case 0:
                x = randy.nextInt(frameX);
                y = 1;
                velX = 4 * (randy.nextDouble() - 0.5);
                velY = 2 * randy.nextDouble();
                break;
            case 1:
                x = randy.nextInt(frameX);
                y = frameY - 1;
                velX = 4 * (randy.nextDouble() - 0.5);
                velY = -2 * randy.nextDouble();
                break;
            case 2:
                x = 1;
                y = randy.nextInt(frameY);
                velX = 2 * randy.nextDouble();
                velY = 4 * (randy.nextDouble() - 0.5);
                break;
            default:
                x = frameX - 1;
                y = randy.nextInt(frameY);
                velX = -2 * randy.nextDouble();
                velY = 4 * (randy.nextDouble() - 0.5);
                break;
        }


        return new Obstacle(new Position(x, y, velX, velY));
    }

    public void draw(Graphics g)
    {
        for (Obstacle picku : pickup) {
            picku.draw(g);
        }
        for(Star star: stars)
        {
            star.draw(g);
        }
    }



}
