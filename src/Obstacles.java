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

    ArrayList<Obstacle> ships = new ArrayList<>();

    public Obstacles(int num, int width, int height)//creates all obstacles
    {
        randy = new Random();
        maxNum = num;
        frameX = width;
        frameY = height;
    }

    public void update(ArrayList<Integer> updateQueue)//updates obstacles. If there aren't enough, there's a chance to make more
    {
        int size = ships.size();
        for(int x = 0; x < size; x++)
        {
            ships.get(x).update();
        }
        if(size < maxNum * randy.nextInt(2))
        {
            ships.add(generateObstacle(frameX, frameY));
        }



    }

    public Obstacle generateObstacle(int frameX, int frameY)
    {
        int x, y;
        double velY, velX;
        Position pos;
        if(randy.nextInt(2) == 1)
        {
            x = randy.nextInt(frameX);
            y = randy.nextInt(2);
            velX = 2 * (randy.nextDouble() - .5);
            velY = randy.nextDouble() * (y * 2 - 1);
            pos = new Position(x, y * frameY, velX, velY);
        }
        else
        {
            x = randy.nextInt(2);
            y = randy.nextInt(frameY);
            velY = 2 * (randy.nextDouble() - .5);
            velX = randy.nextDouble() * (x * 2 - 1);
            pos = new Position(x * frameX, y, velX, velY);
        }
        return new Obstacle(pos);
    }

    public void draw(Graphics g)
    {
        for(int x = 0; x < ships.size(); x++)
        {
            ships.get(x).draw(g);
        }
    }

}
