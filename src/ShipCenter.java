// WormChase.java
// Roger Mailler, January 2009, adapted from
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A worm moves around the screen and the user must
 click (press) on its head to complete the game.

 If the user misses the worm's head then a blue box
 will be added to the screen (if the worm's body was
 not clicked upon).

 A worm cannot move over a box, so the added obstacles
 *may* make it easier to catch the worm.

 A worm starts at 0 length and increases to a maximum
 length which it keeps from then on.

 A score is displayed on screen at the end, calculated
 from the number of boxes used and the time taken. Less
 boxes and less time mean a higher score.

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
import java.awt.image.Raster;
import java.text.DecimalFormat;
import java.util.ArrayList;

//go here for help with controllers
//http://www.java-gaming.org/index.php?PHPSESSID=pvdddk0s18t5qg4qhl316e1880&topic=16866.0

public class ShipCenter extends GameFrame implements KeyListener {

	private static final long serialVersionUID = -2450477630768116721L;

	private static int DEFAULT_FPS = 60;

	private PlayerShip fred; // the worm
	private Obstacles obs; // the obstacles
	private int boxesUsed = 0;
    private boolean[] inputs;

	private int score = 0;
	private Font font;
	private FontMetrics metrics;
	private ArrayList<Integer> keys;


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

	@Override
	protected void simpleInitialize() {
		// create game components
        inputs = new boolean[256];
        for(int x  = 0; x < inputs.length; x++)
        {
            inputs[x] = false;
        }
		obs = new Obstacles(1, pWidth, pHeight);
		fred = new PlayerShip(pWidth, pHeight);
		addKeyListener(this);

		keys = new ArrayList<>();
		// set up message font
		font = new Font("SansSerif", Font.BOLD, 24);
		metrics = this.getFontMetrics(font);

		// specify screen areas for the buttons
		pauseArea = new Rectangle(pWidth - 100, pHeight - 45, 70, 15);
		quitArea = new Rectangle(pWidth - 100, pHeight - 20, 70, 15);
	}



	@Override
	protected void mouseMove(int x, int y) {
		if (running) { // stops problems with a rapid move after pressing 'quit'
			isOverPauseButton = pauseArea.contains(x, y) ? true : false;
			isOverQuitButton = quitArea.contains(x, y) ? true : false;
		}
	}

	public void setBoxNumber(int no)
	// called from Obstacles object
	{
		boxesUsed = no;
	}



	@Override
	protected void simpleRender(Graphics gScr) {

		gScr.setColor(Color.blue);
		gScr.setFont(font);
		gScr.setColor(Color.blue);
	    gScr.setFont(font);

	    // report frame count & average FPS and UPS at top left
		gScr.drawString("Average FPS/UPS: " + df.format(averageFPS) + ", " +
	                                df.format(averageUPS), 20, 25);  // was (10,55)
		
		// report time used and boxes used at bottom left
//		gScr.drawString("MouseY : " + getMousePosition().getY(), 260, pHeight - 15);

		// draw the pause and quit 'buttons'
/*
		try {
			Robot robo = new Robot();
            BufferedImage efficency = robo.createScreenCapture(new Rectangle(0,0,getWidth(), getHeight()));
            Color newCol = new Color(efficency.getRGB((int)getMousePosition().getX(), (int)getMousePosition().getY()));
			gScr.drawString("R : " + newCol.getRed() + " G : " + newCol.getGreen() + " B : " + newCol.getBlue(), 10, pHeight - 15);

		} catch (AWTException e) {
			e.printStackTrace();
		}
*/

		gScr.setColor(Color.black);

		// draw game elements: the obstacles and the worm
		obs.draw(gScr);
		fred.draw(gScr);
	} // end of simpleRender()

	private void drawButtons(Graphics g) {
		g.setColor(Color.black);

		// draw the pause 'button'
		if (isOverPauseButton)
			g.setColor(Color.green);

		g.drawOval(pauseArea.x, pauseArea.y, pauseArea.width, pauseArea.height);
		if (isPaused)
			g.drawString("Paused", pauseArea.x, pauseArea.y + 10);
		else
			g.drawString("Pause", pauseArea.x + 5, pauseArea.y + 10);

		if (isOverPauseButton)
			g.setColor(Color.black);

		// draw the quit 'button'
		if (isOverQuitButton)
			g.setColor(Color.green);

		g.drawOval(quitArea.x, quitArea.y, quitArea.width, quitArea.height);
		g.drawString("Quit", quitArea.x + 15, quitArea.y + 10);

		if (isOverQuitButton)
			g.setColor(Color.black);
	} // drawButtons()

	@Override
	protected void gameOverMessage(Graphics g)
	// center the game-over message in the panel
	{
		String msg = "Game Over. Your Score: " + score;
		int x = (pWidth - metrics.stringWidth(msg)) / 2;
		int y = (pHeight - metrics.getHeight()) / 2;
		g.setColor(Color.red);
		g.setFont(font);
		g.drawString(msg, x, y);
	} // end of gameOverMessage()

	@Override
	protected void simpleUpdate() {
		fred.update(inputs);
		obs.update(inputs);
		keys.clear();
		fred.checkHits(obs);
	}

	@Override
	protected void mousePress(int x, int y) {

	}

	public static void main(String args[])
    {
		int fps = DEFAULT_FPS;
		if (args.length != 0)
			fps = Integer.parseInt(args[0]);

		long period = (long) 1000.0 / fps;
		System.out.println("fps: " + fps + "; period: " + period + " ms");
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

