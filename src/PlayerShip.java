import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by alvarpq on 2/17/2016.
 */
public class PlayerShip implements ImagesPlayerWatcher, ImageObserver {
    private final int MAX_SIDE_VELOCITY = 4;
    private int mavForward = 7;

    private int frameX, frameY;//screen dimensions
    private Position pos;//position data of ship
    private KeysList ks = new KeysList();
    private int dimsX = 45, dimsY = 45;
    private int hitX = 16, hitY = 42;
    private int points = 0;
    private int hp = 50;
    private int grab = 0;


    private ClipsLoader clippy = new ClipsLoader("clipsInfo.txt");

    private BufferedImage shipImg;
    private BufferedImage drawIm;

    private BufferedImage left;
    private BufferedImage right;
    private BufferedImage flame;
    private int lNum = 0;
    private int rNum = 0;
    private int fNum = 0;

    private int player;
    private Shield shield;
    private Color theGray = new Color(127, 127, 127);

    private Random randy = new Random();

    private double forwardVelocity = 0;
    private double sideVelocity = 0;

    private int maxRotate = 3;
    private Rectangle collideRect;

    private ImagesLoader imgLoader;
    private ImageSFXs imgSfx;
    private int boost = 25;

    private Polygon hitbox;

    private String[] songNames = {"engine", "rotate", "side", "sound1"};

    public PlayerShip(int width, int height, int cont)
    {
        player = cont;
        frameX = width;
        frameY = height;
        pos = new Position(width / 2 + 500 * (cont * 2 - 1), height / 2);//start player in middle of screen

        imgSfx = new ImageSFXs();
        imgLoader = new ImagesLoader("Images/imsInfo.txt");
        right = imgLoader.getImage("blank");
        left = imgLoader.getImage("blank");
        shipImg = imgLoader.getImage("ship");
        flame = imgLoader.getImage("blank");
        drawIm = shipImg;

        hitbox = new Polygon(new int[]{pos.getX() + hitX / 2, pos.getX() + hitX / 2,pos.getX() - hitX / 2,pos.getX() - hitX / 2}, new int[]{pos.getY() + hitY / 2, pos.getY() + hitX / 2,pos.getY() - hitX / 2,pos.getY() - hitY / 2},4);

        shield = new Shield(pos, frameX, frameY, cont);
        clippy.setVolume(songNames[0], -20.0f);

        for(int x = 2; x < 4; x++)
        {
            clippy.setVolume(songNames[x], -20.0f);
        }

    }


    public void checkOverlap(PlayerShip otherShip)
    {

        Polygon sh1 = shield.getHitbox();
        Polygon sh2 = otherShip.shield.getHitbox();
        if(overlaps(sh1, sh2))
        {
            double vel = getShield().pos.getRotationVelocity();
            getShield().pos.setRotationVelocity(2 * otherShip.getShield().pos.getRotationVelocity());
            otherShip.getShield().pos.setRotationVelocity(2 * vel);


            double xv = 2 * getXvelocity(otherShip.forwardVelocity, otherShip.sideVelocity, otherShip.pos.getOrientation(), pos.getOrientation());
            double yv = 2 * getYvelocity(otherShip.forwardVelocity, otherShip.sideVelocity, otherShip.pos.getOrientation(), pos.getOrientation());
            otherShip.forwardVelocity -= 2 * getYvelocity(forwardVelocity, sideVelocity, pos.getOrientation(), pos.getOrientation());
            otherShip.sideVelocity -= 2 * getXvelocity(forwardVelocity, sideVelocity, pos.getOrientation(), pos.getOrientation());

            forwardVelocity += yv;
            sideVelocity += xv;
        }

        Polygon shi1 = getUpdatedHitbox();
        Polygon shi2 = otherShip.getUpdatedHitbox();

        if(overlaps(shi1, shi2))
        {
            double sv = 2 * getXvelocity(otherShip.forwardVelocity, otherShip.sideVelocity, otherShip.pos.getOrientation(), pos.getOrientation());
            double fv = 2 * getYvelocity(otherShip.forwardVelocity, otherShip.sideVelocity, otherShip.pos.getOrientation(), pos.getOrientation());

            otherShip.forwardVelocity += 2 * getYvelocity(forwardVelocity, sideVelocity, pos.getOrientation(), otherShip.pos.getOrientation());
            otherShip.sideVelocity -= 2 * getXvelocity(forwardVelocity, sideVelocity, pos.getOrientation(), otherShip.pos.getOrientation());

            forwardVelocity += fv;
            sideVelocity += sv;
        }


        if(overlaps(shi1, sh2))
        {
            hp -= 1;
            double sv = 2 * getXvelocity(otherShip.forwardVelocity, otherShip.sideVelocity, otherShip.pos.getOrientation(), pos.getOrientation());
            double fv = 2 * getYvelocity(otherShip.forwardVelocity, otherShip.sideVelocity, otherShip.pos.getOrientation(), pos.getOrientation());

            otherShip.forwardVelocity += 2 * getYvelocity(forwardVelocity, sideVelocity, pos.getOrientation(), otherShip.pos.getOrientation());
            otherShip.sideVelocity -= 2 * getXvelocity(forwardVelocity, sideVelocity, pos.getOrientation(), otherShip.pos.getOrientation());

            forwardVelocity += fv;
            sideVelocity += sv;
        }

        if(overlaps(shi2, sh1))
        {

            otherShip.setHp(otherShip.getHp() - 1);
            double sv = 2 * getXvelocity(otherShip.forwardVelocity, otherShip.sideVelocity, otherShip.pos.getOrientation(), pos.getOrientation());
            double fv = 2 * getYvelocity(otherShip.forwardVelocity, otherShip.sideVelocity, otherShip.pos.getOrientation(), pos.getOrientation());

            otherShip.forwardVelocity += 2 * getYvelocity(forwardVelocity, sideVelocity, pos.getOrientation(), otherShip.pos.getOrientation());
            otherShip.sideVelocity -= 2 * getXvelocity(forwardVelocity, sideVelocity, pos.getOrientation(), otherShip.pos.getOrientation());

            forwardVelocity += fv;
            sideVelocity += sv;
        }


    }

    public boolean overlaps(Polygon a, Polygon b)
    {
        Area overlap = new Area(a);

        overlap.intersect(new Area(b));
        return !overlap.isEmpty();
    }


    public double getXvelocity(double forwardVelocity, double sideVelocity, double thisAngle, double otherAngle)
    {
        return Math.sin(Math.toRadians(thisAngle - otherAngle)) * forwardVelocity;

    }

    public double getYvelocity(double forwardVelocity, double sideVelocity, double thisAngle, double otherAngle)
    {
        return Math.cos(Math.toRadians(thisAngle - otherAngle)) * forwardVelocity;
    }

    public int getHp(){
        return hp;
    }
    public void setHp(int x)
    {
        hp = x;
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

        //little fire animations
        if(fNum > 0 && fNum < 3)
        {
            flame = imgLoader.getImage("flame-" + fNum);
        }
        else if(fNum != 0)
        {
            flame = imgLoader.getImage("flame-" + (fNum- 2));

        }
        else
        {
            flame = imgLoader.getImage("blank");
        }

        if(lNum != 0)
        {
            left = imgLoader.getImage("left-" + lNum);
        }
        else
        {
            left = imgLoader.getImage("blank");
        }

        if(rNum != 0)
        {
            right = imgLoader.getImage("right-" + rNum);
        }
        else
        {
            right = imgLoader.getImage("blank");
        }

        //drawing shield and player with rotation
        shield.draw(g);
        drawImage(g,  imgSfx.getRotatedImage(right, (int)pos.getOrientation()), pos.getX() - dimsX / 2, pos.getY() - dimsY / 2);
        drawImage(g, imgSfx.getRotatedImage(left, (int)pos.getOrientation()), pos.getX() - dimsX / 2, pos.getY() - dimsY / 2);
        drawImage(g, drawIm, pos.getX() - dimsX / 2, pos.getY() - dimsY / 2);
        if(fNum > 2)
        {
            imgSfx.drawResizedImage((Graphics2D)g, imgSfx.getRotatedImage(flame, (int) pos.getOrientation()), pos.getX() - dimsX / 2, pos.getY() - dimsY / 2, Math.abs(2 * Math.sin(Math.toRadians(pos.getOrientation()))), Math.abs(2 * Math.cos(Math.toRadians(pos.getOrientation()))));
        }
        drawImage(g, imgSfx.getRotatedImage(flame, (int) pos.getOrientation()), pos.getX() - dimsX / 2, pos.getY() - dimsY / 2);

        //boost bar has to look fancy
        g.setColor(Color.white);
        if(player == 0)//boost bar management
        {
            g.fillRect(20,frameY - 60, 300, 50);//white rectangle
            g.setColor(Color.red);
            g.fillRect(20, frameY - 60, 3 * boost, 50);//red bar
            if(boost < 100) {
                g.setColor(Color.black);
                g.drawString("B O O S T", 110, frameY - 25);
            }
            else
            {
                g.setColor(Color.cyan);
                g.fillRect(20, frameY - 60, 3 * boost, 50);//red bar
            }
            if(grab > 0) {

                g.setColor(new Color(Color.cyan.getRed(), Color.cyan.getBlue(), Color.cyan.getGreen(), grab ));

                g.fillRect(20, frameY - 60, 3 * boost, 50);//red bar
                grab-=5;
            }
        }
        else
        {
            g.fillRect(frameX - 320,frameY - 60, 300, 50);

            g.setColor(Color.red);
            g.fillRect(frameX - 20 - (3 * boost), frameY - 60, 3 * boost, 50);//red bar
            if(boost < 100) {
                g.setColor(Color.black);
                g.drawString("B O O S T", frameX - 220, frameY - 25);
            }
            else
            {
                g.setColor(Color.cyan);
                g.fillRect(frameX - 20 - (3 * boost), frameY - 60, 3 * boost, 50);//red bar

            }
            if(grab > 0) {
                g.setColor(new Color(Color.cyan.getRed(), Color.cyan.getBlue(), Color.cyan.getGreen(), grab ));
                g.fillRect(20, frameY - 60, 3 * boost, 50);//red bar
                grab-=5;
            }
        }

        Font curF = g.getFont();
        g.setFont(new Font("Courier", Font.CENTER_BASELINE, 12));
        g.setColor(Color.white);
        g.drawString("PLAYER " + (player + 1), pos.getX() - 25, pos.getY() - 51);
        g.setFont(curF);
        //g.fillPolygon(getUpdatedHitbox());
        //g.fillPolygon(shield.getHitbox());
    }


    //updates. Has a queue of key events.
    public void update(boolean[] updateQueue)
    {
        fNum = 0;
        lNum = 0;
        rNum = 0;
        for (int x = 0; x < updateQueue.length; x++) {//for the whole list. In case multiple keys are down.
            if(updateQueue[x]) {
                updatePos(x);
                shield.updatePos(x);
            }
        }

        shield.setImg((50 - hp) / 10);

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
        if(forwardVelocity > mavForward)
        {
            forwardVelocity = mavForward;
        }
        else if(forwardVelocity < -1 * mavForward)
        {
            forwardVelocity = -1 * mavForward;
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


    public Point rotatePoint(double x, double y, Position pos, double rotation)
    {
        //from stackexchange
        double x1 = x - pos.getX();
        double y1 = y - pos.getY();

//APPLY ROTATION
        double temp_x1 = x1 * Math.cos(Math.toRadians(rotation)) - y1 * Math.sin(Math.toRadians(rotation));
        double temp_y1 = x1 * Math.sin(Math.toRadians(rotation)) + y1 * Math.cos(Math.toRadians(rotation));

//TRANSLATE BACK
        return new Point((int)temp_x1 + pos.getX(), (int)temp_y1 + pos.getY());
    }

    public Polygon getUpdatedHitbox()
    {
        hitbox = new Polygon(new int[]{pos.getX() + hitX / 2, pos.getX() + hitX / 2,pos.getX() - hitX / 2,pos.getX() - hitX / 2}, new int[]{pos.getY() + hitY / 2, pos.getY() - hitY / 2,pos.getY() - hitY / 2,pos.getY() + hitY / 2},4);

        Polygon out = new Polygon();
        Point p;
        for(int x = 0; x < 4; x++)
        {
            p = rotatePoint(hitbox.xpoints[x], hitbox.ypoints[x], pos, pos.getOrientation());
            out.addPoint((int)p.getX(), (int)p.getY());
        }

        hitbox = out;
        return out;
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
        if(x == ks.getBoostUp(player))
        {

            playClip(0,false);
            fNum = randy.nextInt(2) + 1;
            forwardTranslate(4);
            mavForward = 7;
        }
        else if(x == ks.getRbLeft(player))
        {
            playClip(2,false);
            rNum = lNum % 2 + 1;

            relativeTranslate(3);
            pos.setRotationVelocity(pos.getRotationVelocity() - 1);
        }
        else if(x == ks.getRbRight(player))
        {

            lNum = randy.nextInt(2) + 1;
            playClip(2,false);
            relativeTranslate(-3);
            pos.setRotationVelocity(pos.getRotationVelocity() + 1);
        }
        else if( x == ks.getShieldLeft(player) || x == ks.getShieldRight(player))
        {
            playClip(1, false);
        }
        else if(x == ks.getBoost(player) && boost > 0)
        {
            mavForward = 14;
            boost--;
            forwardTranslate(8);
            fNum = randy.nextInt(2) + 3;
            playClip(0,false);

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
            if(shield.intersects(obs.getObstacles().get(x)) || intersects(obs.getObstacles().get(x), collideRect, theGray))
            {
                playClip(3,false);
                if(boost < 100)
                    boost += 5;
                if(boost > 100)
                    boost = 100;
                grab = 255;
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
                        playClip(3, false);
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

    public void playClip(int i, boolean bool)
    {
        clippy.play(songNames[i], bool);
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
    }
}
