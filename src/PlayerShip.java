import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by alvarpq on 2/17/2016.
 */
public class PlayerShip implements ImagesPlayerWatcher, ImageObserver {
    private final int MAX_SIDE_VELOCITY = 4;
    private final int MAX_FORWARD_VELOCITY = 7;

    int frameX, frameY;//screen dimensions
    Position pos;//position data of ship
    KeysList ks = new KeysList();
    int dimsX = 45, dimsY = 45;
    int points = 0;

    BufferedImage literallyTheWholeScreen;

    BufferedImage shipImg;
    BufferedImage drawIm;

    Shield shield;
    Color theGray = new Color(127, 127, 127);


    Random randy = new Random();

    double forwardVelocity = 0;
    double sideVelocity = 0;

    int maxRotate = 3;
    Rectangle collideRect;

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

        shield = new Shield(pos, frameX, frameY);
    }


    public int getPoints()
    {
        return points;
    }
    //returns color of pixel on ship image at specific coordinates on the whole screen
    public Color getPixelAt(int x, int y)
    {
        BufferedImage drawIm = imgSfx.getRotatedImage(shipImg, (int)pos.getOrientation());
        int imgLeft = pos.getX() - dimsX / 2;
        int imgTop = pos.getY() - dimsY / 2;
        if(x >= imgLeft + drawIm.getWidth() || x < imgLeft)
        {
            return Color.BLACK;
        }
        else if(y >= imgTop + drawIm.getHeight() || y < imgTop)
        {
            return Color.BLACK;
        }
        else
        {
            return new Color(shipImg.getRGB(x - imgLeft, y - imgTop));
        }
    }

    public void draw(Graphics g)//draws player. Adding position data soon.
    {
        shield.draw(g);
        drawImage(g, drawIm, pos.getX() - dimsX / 2, pos.getY() - dimsY / 2);
        g.setColor(Color.MAGENTA);
        //g.fillRect((int)collideRect.getX(), (int)collideRect.getY(), (int)collideRect.getWidth(), (int)collideRect.getHeight());
        literallyTheWholeScreen = new BufferedImage(frameX, frameY, BufferedImage.TYPE_4BYTE_ABGR);
    }


    //updates. Has a queue of key events.
    public void update(boolean[] updateQueue)
    {
        for (int x = 0; x < updateQueue.length; x++) {//for the whole list. In case multiple keys are down.
            if(updateQueue[x]) {
                updatePos(x);
                shield.updatePos(x);
            }
        }

        //updating rotational velocity
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

        //updating side velocity
        if(sideVelocity > MAX_SIDE_VELOCITY)
        {
            sideVelocity = MAX_SIDE_VELOCITY;
        }
        else if(sideVelocity < -1 * MAX_SIDE_VELOCITY)
        {
            sideVelocity = -1 * MAX_SIDE_VELOCITY;
        }
        else if(sideVelocity > 0)
        {
            sideVelocity -= randy.nextInt(2);
        }
        else if(sideVelocity < 0)
        {
            sideVelocity += randy.nextInt(2);
        }

        //updating forward velocity
        if(forwardVelocity > MAX_FORWARD_VELOCITY)
        {
            forwardVelocity = MAX_FORWARD_VELOCITY;
        }
        else if(forwardVelocity < -1 * MAX_FORWARD_VELOCITY)
        {
            forwardVelocity = -1 * MAX_FORWARD_VELOCITY;
        }
        else if(forwardVelocity > 0)
        {
            forwardVelocity -= randy.nextInt(2);
        }
        else if(forwardVelocity < 0)
        {
            forwardVelocity += randy.nextInt(2);
        }

        pos.goForward(forwardVelocity);
        pos.relativeTranslate(sideVelocity);
        rotate((int)pos.getRotationVelocity());

        wrapAround(pos);
        shield.update(pos);
    }

    public void wrapAround(Position p)
    {
        if(p.getX() < 0)
        {
            p.setX(frameX - 1);
        }
        else if(p.getX() > frameX)
        {
            p.setX(1);
        }

        if(p.getY() < 0)
        {
            p.setY(frameY - 1);
        }
        else if(p.getY() > frameY)
        {
            p.setY(1);
        }

    }


    public void updatePos(int x)//update positioning.
    {
        if(x == ks.getBoostUp())
        {
            forwardTranslate(4);
        }
        else if(x == ks.getRbLeft())
        {
            relativeTranslate(3);
            pos.setRotationVelocity(pos.getRotationVelocity() - 1);
        }
        else if(x == ks.getRbRight())
        {
            relativeTranslate(-3);
            pos.setRotationVelocity(pos.getRotationVelocity() + 1);
        }
    }

    public Shield getShield()
    {
        return shield;
    }

    public void forwardTranslate(double distance)
    {
        forwardVelocity += distance;
    }

    public void relativeTranslate(double delta)
    {
        sideVelocity += delta;
    }

    public void rotate(int rotation)//rotates a certain amount of degrees
    {
        pos.setRotationVelocity(rotation);
        drawIm = imgSfx.getRotatedImage(shipImg, (int)pos.getOrientation());
    }

    //checks to see if stuff intersects
    public synchronized void checkHits(Obstacles obs)
    {
        ArrayList<Obstacle> newObs = new ArrayList<>();
        collideRect = new Rectangle(pos.getX(), pos.getY() - drawIm.getHeight() / 2, drawIm.getWidth(), drawIm.getHeight());

        for(int x = 0; x < obs.getObstacles().size(); x++)
        {
            if(shield.intersects(obs.getObstacles().get(x)))
            {
               points++;
            }
            else if(intersects(obs.getObstacles().get(x), collideRect, theGray))
            {
                //points--;
                newObs.add(obs.getObstacles().get(x));

            }
            else
            {
                newObs.add(obs.getObstacles().get(x));
            }
        }

        obs.setObstacles(newObs);
    }

    private boolean intersects(Obstacle obs, Rectangle collider, Color theGray)//checks for intersection, first over image, then over color
    {
        if(collider.intersects(obs.getRect()))
        {
            int maxX = frameX;
            int maxY = frameY;
            if(maxX > obs.getX() + obs.getWidth())
            {
                maxX = obs.getX() + obs.getWidth();
            }
            if(maxY > obs.getY() + obs.getHeight())
            {
                maxY = obs.getY() + obs.getHeight();
            }
            for (int x = obs.getX(); x < maxX; x++)
            {
                for (int y = obs.getY(); y < maxY; y++)
                {
                    if (getPixelAt(x, y).equals(theGray))
                    {
                        return true;
                    }
                }
            }

        }
        return false;
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
