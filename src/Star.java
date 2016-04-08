import java.awt.*;

/**
 * Created by alvarpq on 2/17/2016.
 *
 */
public class Star {
    Position pos;//position data of obstacle

    int height = 1;
    int width = 1;
    Rectangle rect;
    public Star(Position pos) {
        this.pos = pos;
        rect = new Rectangle(pos.getX(), pos.getY(), width, height);
    }

    //draws the ghing.
    public void draw(Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(pos.getX(), pos.getY(), width, height);
    }

    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }

    public int getX()
    {
        return pos.getX();
    }

    public int getY()
    {
        return pos.getY();
    }
    public void update() {
        pos.update();
        rect = new Rectangle(pos.getX(), pos.getY(), width, height);

    }

    public Rectangle getRect()
    {
        return rect;
    }
}
