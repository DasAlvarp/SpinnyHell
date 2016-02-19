import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by alvarpq on 2/17/2016.
 */
public class PlayerShip implements ImagesPlayerWatcher, ImageObserver {
    int frameX, frameY;//screen dimensions
    Position pos;//position data of ship
    KeysList ks = new KeysList();
    int dimsX = 17, dimsY = 41;

    BufferedImage shipImg;
    BufferedImage drawIm;


    Random randy = new Random();


    int maxRotate = 40;

    int[] xPts;
    int[] yPts;

    Polygon craft;

    ImagesLoader imgLoader;
    ImageSFXs imgSfx;

    public PlayerShip(int width, int height)
    {
        frameX = width;
        frameY = height;
        pos = new Position(width / 2, height / 2);//start player in middle of screen
        xPts = new int[4];
        yPts = new int[4];
        setXYpoints();
        imgLoader = new ImagesLoader("Images/imsInfo.txt");
        imgSfx = new ImageSFXs();
        shipImg = imgLoader.getImage("ship");
        drawIm = shipImg;

    }

    public void draw(Graphics g)//draws player. Adding position data soon.
    {
        g.setColor(Color.GRAY);

        g.drawPolygon(craft);
        g.setColor(Color.cyan);
        g.drawRect(findCenter()[0], findCenter()[1], 1,1);
        drawImage((Graphics2D)g, drawIm, pos.getX() - dimsX / 2, pos.getY() - dimsY / 2);
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
        /*
        else if(pos.getRotationVelocity() > 0)
        {
            pos.setRotationVelocity(pos.getRotationVelocity() - randy.nextInt(2));
        }
        else if(pos.getRotationVelocity() < 0)
        {
            pos.setRotationVelocity(pos.getRotationVelocity() + randy.nextInt(2));
        }*/

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
            pos.setRotationVelocity(pos.getRotationVelocity() + 2);
        }
        if(x == ks.getRbRight())
        {
            pos.setRotationVelocity(pos.getRotationVelocity() - 2);
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

        drawIm = imgSfx.getRotatedImage(shipImg, (int)pos.getOrientation());


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

    private void drawImage(Graphics2D g2d, BufferedImage im, int x, int y) {
		/* Draw the image, or a yellow box with ?? in it if there is no image. */
        if (im == null) {
            // System.out.println("Null image supplied");
            g2d.setColor(Color.yellow);
            g2d.fillRect(x, y, 20, 20);
            g2d.setColor(Color.black);
            g2d.drawString("??", x + 10, y + 10);
        } else
            g2d.drawImage(im, x, y, this);
    } // end of drawImage()

    @Override
    public void sequenceEnded(String imageName) {
        System.out.println(imageName + " sequence has ended");

    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }
}
