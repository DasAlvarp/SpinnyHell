import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by alvarpq on 2/17/2016.
 */
public class PlayerShip {
    int frameX, frameY;//screen dimensions
    Position pos;//position data of ship
    KeysList ks = new KeysList();
    int dimsX = 16, dimsY = 40;

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
    }


    //updates. Has a queue of key events.
    public void update(ArrayList<Integer> updateQueue)
    {
        for(int x = 0; x < updateQueue.size(); x++)
        {
            updatePos(updateQueue.get(x));
        }

    }

    public void updatePos(int x)//update positioning.
    {
        if(x == ks.getBoostUp())
        {
            pos.setY(pos.getY() - 1);
            craft.translate(0, -1);
        }
        else if(x == ks.getRbLeft())
        {
            AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(90), pos.getX(), pos.getY());

            Polygon p = new Polygon();

            PathIterator i = craft.getPathIterator(at);
            int it = 0;
            while(!i.isDone() && it < 4)
            {

                double[] xy = new double[2];
                i.currentSegment(xy);
                p.addPoint((int) xy[0], (int) xy[1]);
                System.out.println(Arrays.toString(xy) + "what a name! " + it);
                i.next();
                it++;//should only happen 4 times. I enforce this with bad programming.
            }

            craft = p;
        }

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

    public void rotateXYpoints(int angle)
    {
        int[] xPtsN = new int[4];
        int[] yPtsN = new int[4];

        for(int x = 0; x < 4; x++) {
            double dx = xPts[x] - pos.getX();
            double dy = yPts[x] - pos.getY();
            xPtsN[x] = (int)(pos.getX() - dx * Math.cos(angle) + dy * Math.sin(angle));
            yPtsN[x] = (int)(pos.getX() - dx * Math.sin(angle) - dy * Math.cos(angle));

        }

        xPts = xPtsN;
        yPts = yPtsN;


    }

    private void rotateX(int index, int angle)
    {
        xPts[index] = (int)(xPts[index] * Math.cos(Math.toRadians(angle)) - yPts[index] * Math.sin(Math.toRadians(angle)));//top right
    }

    private void rotateY(int index, int angle)
    {
        yPts[index] = (int)(xPts[index] * Math.sin(Math.toRadians(angle)) + yPts[index] * Math.cos(Math.toRadians(angle)));//top right

    }



}
