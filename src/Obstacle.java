import java.awt.*;

/**
 * Created by alvarpq on 2/17/2016.
 */
public class Obstacle {
    Position pos;//position data of obstacle

    int height = 15;
    int width = 15;
    Rectangle rect;
    public Obstacle(Position pos) {
        this.pos = pos;
        rect = new Rectangle(pos.getX(), pos.getY(), width, height);
    }


    public void draw(Graphics g)
    {
        g.setColor(Color.red);
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
