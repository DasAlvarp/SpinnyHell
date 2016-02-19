import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by alvarpq on 2/17/2016.
 */
public class PlayerShip {
    int frameX, frameY;//screen dimensions
    Position pos;//position data of ship
    KeysList ks = new KeysList();
    int dimsX = 16, dimsY = 48;

    Random randy = new Random();


    int maxRotate = 40;

    int[] xPts;
    int[] yPts;

    Polygon craft;


    public PlayerShip(int width, int height)
    {
        frameX = width;
        frameY = height;
        pos = new Position(width / 2, height / 2);//start player in middle of screen
        xPts = new int[4];
        yPts = new int[4];
        setXYpoints();

    }

    public void draw(Graphics g)//draws player. Adding position data soon.
    {
        g.setColor(Color.GRAY);

        g.drawPolygon(craft);
        g.setColor(Color.cyan);
        g.drawRect(findCenter()[0], findCenter()[1], 1,1);
    }


    //updates. Has a queue of key events.
    public void update(ArrayList<Integer> updateQueue)
    {
        for (Integer anUpdateQueue : updateQueue) {
            updatePos(anUpdateQueue);
        }

        rotate((int)pos.getRotationVelocity());

        if(pos.getRotationVelocity() > maxRotate)
        {
            pos.setRotationVelocity(maxRotate);
        }
        else if (pos.getRotationVelocity() < -1 * maxRotate)
        {
            pos.setRotationVelocity(-1 * maxRotate);
        }
        else if(pos.getRotationVelocity() > 0)
        {
            pos.setRotationVelocity(pos.getRotationVelocity() - randy.nextInt(2));
        }
        else if(pos.getRotationVelocity() < 0)
        {
            pos.setRotationVelocity(pos.getRotationVelocity() + randy.nextInt(2));
        }

    }


    public int[] findCenter()
    {
        int xSum = 0;
        int ySum = 0;
        for(int x = 0; x < 4; x++)
        {
            xSum += craft.xpoints[x];
            ySum += craft.ypoints[x];
        }
        return new int[]{xSum / 4, ySum / 4 };
    }

    public void updatePos(int x)//update positioning.
    {

        if(x == ks.getBoostUp())
        {
            pos.setY(pos.getY() - 1);
            craft.translate(0, -1);
        }
        if(x == ks.getRbLeft())
        {
            pos.setRotationVelocity(pos.getRotationVelocity() + 4);
        }
        if(x == ks.getRbRight())
        {
            pos.setRotationVelocity(pos.getRotationVelocity() - 4);
        }





    }

    public void rotate(int rotation)//rotates a certain amount of degrees
    {
        int[] center = findCenter();
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rotation), center[0], center[1]);

        pos.setRotationVelocity(rotation);
        Polygon p = new Polygon();

        PathIterator i = craft.getPathIterator(at);
        int it = 0;
        while(!i.isDone() && it < 4)
        {
            double[] xy = new double[2];
            i.currentSegment(xy);
            p.addPoint((int) xy[0], (int) xy[1]);
            i.next();
            it++;//should only happen 4 times. I enforce this with bad programming.
        }

        craft = p;
    }

    public void setXYpoints()
    {
        xPts[0] = pos.getX() + dimsX / 2;
        xPts[1] = pos.getX() + dimsX / 2;
        xPts[2] = pos.getX() - dimsX / 2;
        xPts[3] = pos.getX() - dimsX / 2;

        yPts[0] = pos.getY() + dimsY / 2;
        yPts[1] = pos.getY() - dimsY / 2;
        yPts[2] = pos.getY() - dimsY / 2;
        yPts[3] = pos.getY() + dimsY / 2;
        craft = new Polygon(xPts, yPts, 4);
    }
}
