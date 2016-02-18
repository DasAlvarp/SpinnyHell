/**
 * Created by alvarpq on 2/17/2016.
 */
public class Position {
    int x,y;
    double xVelocity, yVelocity;
    double orientation;
    double rotationVelocity;

    //Constructors
    public Position(int x, int y, double xVel, double yVel, double or, double rVel)
    {
        this.x = x;
        this.y = y;
        xVelocity = xVel;
        yVelocity = yVel;
        orientation = or;
        rotationVelocity = rVel;
    }

    public Position(int inX, int inY)
    {
        this(inX, inY, 0,0);
    }

    public Position(int inX, int inY, double xVel, double yVel)
    {
        this(inX, inY, xVel, yVel, 0, 0);
    }

    //Getters, setters, and other housework

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public double getRotationVelocity() {
        return rotationVelocity;
    }

    public void setRotationVelocity(double rotationVelocity) {
        this.rotationVelocity = rotationVelocity;

    }


    public String toString()
    {
        return "x: " + x + "\ny: " + y;
    }

}
