// WormChase.java
// Roger Mailler, January 2009, adapted from
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* You control a ship. WAD do move, J and L to move shield
Hit red things with shield to get points.
Hit red things with ship to get dead.

I plan on implementing controller support and making this PvP only, because that sounds more interesting to me.
Maybe add red things as power ups, buffs, or maybe make it so the ships can shoot them at each other.

Anyway, hopefully that explains why this isn't totally asteroids

 -------------

 Uses full-screen exclusive mode, active rendering,
 and double buffering/page flipping.

 On-screen pause and quit buttons.

 Using Java 3D's timer: J3DTimer.getValue()
 *  nanosecs rather than millisecs for the period

 Average FPS / UPS
 20			50			80			100
 Win 98:         20/20       50/50       81/83       84/100
 Win 2000:       20/20       50/50       60/83       60/100
 Win XP (1):     20/20       50/50       74/83       76/100
 Win XP (2):     20/20       50/50       83/83       85/100

 Located in /WormFSEM

 Updated: 12th Feb 2004
 * added extra sleep to the end of our setDisplayMode();

 * moved createBufferStrategy() call to a separate
 setBufferStrategy() method, with added extras
 ----
 */


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

//go here for help with controllers
//http://www.java-gaming.org/index.php?PHPSESSID=pvdddk0s18t5qg4qhl316e1880&topic=16866.0

public class ShipCenter extends GameFrame implements KeyListener {


	private static final long serialVersionUID = -2450477630768116721L;

	private static int DEFAULT_FPS = 60;

	private PlayerShip fred; // the worm
	private PlayerShip bill;
	private Obstacles obs; // the obstacles
    private boolean[] inputs;

    private int playerWin = 0;
    private int p1win = 0;
    private int p2win = 0;

    private boolean menuState = true;

    private BufferedImage play;
    private BufferedImage control;
    private ImagesLoader imgLoader;


    private Font font;
	private FontMetrics metrics;


	// used by quit 'button'
	private volatile boolean isOverQuitButton = false;
	private Rectangle quitArea;

	// used by the pause 'button'
	private volatile boolean isOverPauseButton = false;
	private Rectangle pauseArea;

    private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp


	public ShipCenter(long period) {

        super(period);

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
	protected void simpleInitialize() {
        pWidth = getBounds().width;
        pHeight = getBounds().height;


        System.out.println(pWidth + " D A N  K  " + pHeight);
		// create game components
        inputs = new boolean[256];
        for(int x  = 0; x < inputs.length; x++)
        {
            inputs[x] = false;
        }
		obs = new Obstacles(35, pWidth, pHeight);

        fred = new PlayerShip(pWidth, pHeight, 0);
		bill = new PlayerShip(pWidth, pHeight, 1);

		addKeyListener(this);

		// set up message font
		font = new Font("SansSerif", Font.BOLD, 24);
		metrics = this.getFontMetrics(font);

		// specify screen areas for the buttons
		pauseArea = new Rectangle(93, 233, 700, 225);
		quitArea = new Rectangle(54, 667, 750, 260);

        imgLoader = new ImagesLoader("Images/imsInfo.txt");
        play = imgLoader.getImage("start2play");
        control = imgLoader.getImage("controls");

    }



	@Override
	protected void mouseMove(int x, int y) {
        isOverPauseButton = pauseArea.contains(x, y) ? true : false;
        isOverQuitButton = quitArea.contains(x, y) ? true : false;

        if (isOverPauseButton)
        {
            menuState = true;//game is paused on start because that's secretly the main menu.
        }
        else if(isOverQuitButton)
        {
            menuState = false;//"quit" button doesn't quit, it's just settings
        }

	}



	@Override
	protected void simpleRender(Graphics gScr) {

		gScr.setColor(Color.blue);
		gScr.setFont(font);
		gScr.setColor(Color.blue);
	    gScr.setFont(font);

	    // report frame count & average FPS and UPS at top left
		//gScr.drawString("x" + MouseInfo.getPointerInfo().getLocation().getX() + "; y " + MouseInfo.getPointerInfo().getLocation().getY() + " ms" + df.format(averageFPS) + ", " +
	    //                            df.format(averageUPS), 20, 25);  // was (10,55)
		

		// report time used and boxes used at bottom left
/*
        Color color = fred.getShield().getPixelAt((int)getMousePosition().getX(), (int)getMousePosition().getY());
		gScr.drawString("Red : " + color.getRed() + " Green : " + color.getBlue() + " Blue : " + color.getGreen(), 260, pHeight - 15);
*/
		gScr.drawString(fred.getPoints() + " points", 23, pHeight - 15);
		gScr.drawString(fred.getHp() + " HP", 23, pHeight - 35);

		// draw the pause and quit 'buttons'
/*
\
*/
		gScr.setColor(Color.black);

		// draw game elements: the obstacles and the worm
		obs.draw(gScr);
		fred.draw(gScr);
		bill.draw(gScr);
		drawButtons(gScr);


    } // end of simpleRender()

	private void drawButtons(Graphics g) {
        if(isPaused)
        {
            if(menuState)
            {
                g.drawImage(play, 0,0, pWidth, pHeight, null);



                Font curF = g.getFont();
                g.setFont(new Font("Monospace", Font.PLAIN, 45));
                g.setColor(Color.black);

                if(playerWin == 2)
                {
                    g.drawString("PLAYER 2", (int)(1242 / (double)(pWidth / 1920)), (int)(263 / (double)(pHeight / 1080)));
                }
                else if( playerWin == 1)
                {
                    g.drawString("PLAYER 1",(int)(1242 / (double)(pWidth / 1920)), (int)(263/ (double)(pHeight / 1080)));
                }
                else
                {

                }
                g.drawString("" + p1win, (int)(1278 / (double)(pWidth / 1920)), (int)(581 / (double)(pHeight / 1080)));
                g.drawString("" + p2win, (int)(1278 / (double)(pWidth / 1920)), (int)(685 / (double)(pHeight / 1080)));



                g.setFont(curF);
            }
            else
            {
                g.drawImage(control, 0, 0, pWidth, pHeight, null);
            }



        }
	} // drawButtons()


	@Override
	protected void gameOverMessage(Graphics g)
	// center the game-over message in the panel
	{
		String msg = "Game Over. Your Score: " + fred.getPoints();
		int x = (pWidth - metrics.stringWidth(msg)) / 2;
		int y = (pHeight - metrics.getHeight()) / 2;
		g.setColor(Color.red);
		g.setFont(font);
		g.drawString(msg, x, y);
        obs = null;
        fred = null;
        bill = null;
        simpleInitialize();
        pauseGame();
        gameOver = false;
	} // end of gameOverMessage()

	@Override
	protected void simpleUpdate() {
		obs.update(inputs);
		fred.update(inputs);
		bill.update(inputs);
		fred.checkHits(obs);
		bill.checkHits(obs);
        fred.checkOverlap(bill);
		if(fred.getHp() <= 0)
		{
			gameOver = true;
            playerWin = 2;
            p2win++;
		}
		if(bill.getHp() <= 0)
		{
			gameOver = true;
            playerWin = 1;
            p1win++;
		}
	}

	@Override
	protected void mousePress(int x, int y) {
        if(quitArea.contains(x,y))
        {
            //stopGame();
        }
        else if(pauseArea.contains(x, y))
        {
            resumeGame();
        }
	}

	public static void main(String args[])
    {
		int fps = DEFAULT_FPS;
		if (args.length != 0)
			fps = Integer.parseInt(args[0]);

		long period = (long) 1000.0 / fps;
		System.out.println("x" + MouseInfo.getPointerInfo().getLocation().getX() + "; y " + MouseInfo.getPointerInfo().getLocation().getY() + " ms");
		new ShipCenter(period * 1000000L); // ms --> nanosecs
	} // end of main()



	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e)
    {
        if (!isPaused && !gameOver)
        {
			inputs[e.getKeyCode()] = true;
        }

	}

	@Override
	public void keyReleased(KeyEvent e) {
        if(!isPaused && !gameOver) {
            inputs[e.getKeyCode()] = false;
        }
	}

	@Override
	public void sequenceEnded(String imageName) {

	}
} // end of WormChase class

