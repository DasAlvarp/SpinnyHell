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

        imgLoader = new ImagesLoader("Images/imsInfo.txt");
        imgSfx = new ImageSFXs();
        shipImg = imgLoader.getImage("ship");
        drawIm = shipImg;

    }

    public void draw(Graphics g)//draws player. Adding position data soon.
    {
        drawImage(g, drawIm, pos.getX() - dimsX / 2, pos.getY() - dimsY / 2);
    }


    //updates. Has a queue of key events.
    public void update(ArrayList<Integer> updateQueue)
    {
        for (Integer anUpdateQueue : updateQueue) {
            updatePos(anUpdateQueue);
        }


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
        rotate((int)pos.getRotationVelocity());

    }

    public void updatePos(int x)//update positioning.
    {
        if(x == ks.getBoostUp())
        {
            pos.goForward(5);
        }
        else if(x == ks.getRbLeft())
        {
            pos.setRotationVelocity(pos.getRotationVelocity() - 4);
        }
        else if(x == ks.getRbRight())
        {
            pos.setRotationVelocity(pos.getRotationVelocity() + 4);
        }





    }

    public void rotate(int rotation)//rotates a certain amount of degrees
    {
        pos.setRotationVelocity(rotation);
        drawIm = imgSfx.getRotatedImage(shipImg, (int)pos.getOrientation());


    }

    private void drawImage(Graphics g2d, BufferedImage im, int x, int y) {
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
        return true;
    }
}
