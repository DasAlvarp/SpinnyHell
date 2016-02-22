import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Created by alvarpq on 2/19/2016.
 */
public class Shield {

    Position pos;
    ImagesLoader imgLoader;
    ImageSFXs imgSfx;
    BufferedImage shieldImg;
    BufferedImage drawIm;
    int maxRotate = 5;
    Random randy;
    int frameX;
    int frameY;
    Rectangle collider;
    Color theBlueThing = new Color(75,198,243);

    KeysList keys = new KeysList();
    public Shield(Position shipPos, int frameX, int frameY)
    {
        imgLoader = new ImagesLoader("Images/imsInfo.txt");
        imgSfx = new ImageSFXs();
        shieldImg = imgLoader.getImage("shield");
        drawIm = shieldImg;
        pos = new Position(shipPos.getX(), shipPos.getY());
        randy = new Random();
        this.frameX = frameX;
        this.frameY = frameY;
    }


    public void updatePos(int keyDown)
    {
        if(keys.getShieldRight() == keyDown)
        {
            pos.setRotationVelocity(pos.getRotationVelocity() + 2);
        }
        else if(keys.getShieldLeft() == keyDown)
        {
            pos.setRotationVelocity(pos.getRotationVelocity() - 2);
        }
    }

    public void update(Position shipPos)//updates sheld's position and rotational details
    {
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

        pos.setX(shipPos.getX());
        pos.setY(shipPos.getY());

        rotate((int)pos.getRotationVelocity());

        collider = new Rectangle(pos.getX(), pos.getY(), shieldImg.getWidth(), shieldImg.getHeight());

    }

    public void rotate(int rotation)//rotates a certain amount of degrees
    {
        pos.setRotationVelocity(rotation);
        drawIm = imgSfx.getRotatedImage(shieldImg, (int)pos.getOrientation());
    }

    public void draw(Graphics g)//draws player. Adding position data soon.
    {
        drawImage(g, drawIm, pos.getX() - drawIm.getWidth() / 2, pos.getY() - drawIm.getHeight() / 2);
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
            g2d.drawImage(im, x, y, null);
    } // end of drawImage()

    public boolean intersects(Obstacle obs)//checks for intersection, first over image, then over color
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
                System.out.print("\n");
                for (int y = obs.getY(); y < maxY; y++)
                {
                    System.out.println("DOOT DOOT MOTHEFUCKERS");
                    if (getPixelAt(x, y).equals(theBlueThing))
                    {
                        System.out.println("THAT'S RIGHT BITCH");

                        return true;
                    }
                }
            }
        }
        return false;
    }


    public Color getPixelAt(int x, int y)
    {
        BufferedImage drawIm = imgSfx.getRotatedImage(shieldImg, (int)pos.getOrientation());
        int imgLeft = pos.getX() - 35;
        int imgTop = pos.getY() - 35;
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
            return new Color(shieldImg.getRGB(x - imgLeft, y - imgTop));
        }
    }


}
