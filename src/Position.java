/**
 * Created by alvarpq on 2/17/2016.
 */
public class Position {
    int x,y;
    double xVelocity, yVelocity;

    public double getOrientation() {
        return orientation;
    }

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
        orientation += rotationVelocity + 360;
        orientation %= 360;

    }

    public void goForward(double speed)
    {
        x+=Math.cos(Math.toRadians(orientation)) * speed;
        y += Math.sin(Math.toRadians(orientation)) * speed;
    }

    public String toString()
    {
        return "x: " + x + "\ny: " + y;
    }

    public void update() {
        x += xVelocity;
        y += yVelocity;
    }
}
