import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by alvarpq on 2/19/2016.
 */
public class Shield {

    Position pos;
    ImagesLoader imgLoader;
    ImageSFXs imgSfx;
    BufferedImage shieldImg;
    BufferedImage drawIm;

    public Shield(Position shipPos)
    {
        imgLoader = new ImagesLoader("Images/imsInfo.txt");
        imgSfx = new ImageSFXs();
        shieldImg = imgLoader.getImage("shield");
        drawIm = shieldImg;
        pos = shipPos;
    }

    public void updatePos(int keyDown)
    {
        if(keyDown == 123)
        {

        }
    }

    public void update(Position shipPos)
    {

    }

    public void draw(Graphics g)//draws player. Adding position data soon.
    {
        drawImage(g, drawIm, pos.getX() - drawIm.getWidth() / 3, pos.getY() - drawIm.getHeight() / 2);
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
