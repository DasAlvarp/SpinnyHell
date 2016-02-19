import java.awt.*;

/**
 * Created by alvarpq on 2/17/2016.
 */
public class Obstacle {
    Position pos;//position data of obstacle

    public Obstacle(Position pos) {
        this.pos = pos;
    }


    public void draw(Graphics g)
    {
        g.setColor(Color.cyan);
        g.fillRect(pos.getX(), pos.getY(), 3, 3);
    }
    public void update() {
        pos.update();
    }
}
