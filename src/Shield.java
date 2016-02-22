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

    KeysList keys = new KeysList();
    public Shield(Position shipPos)
    {
        imgLoader = new ImagesLoader("Images/imsInfo.txt");
        imgSfx = new ImageSFXs();
        shieldImg = imgLoader.getImage("shield");
        drawIm = shieldImg;
        pos = new Position(shipPos.getX(), shipPos.getY());
        randy = new Random();
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

    public boolean intersects(Obstacle obs)
    {
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
            g2d.drawImage(im, x, y, null);
    } // end of drawImage()

}
