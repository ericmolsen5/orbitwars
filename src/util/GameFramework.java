
package util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import gameobject.ships.PolygonShip;

public class GameFramework extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	private static final int SCREEN_W = 800;
	private static final int SCREEN_H = 800;
	private static final float WORLD_WIDTH = 64.0f; //using our viewport data
	private static final float WORLD_HEIGHT = 64.0f;
	private static final float MAX_ZOOM_IN = 0.5f;
	private static final float MAX_ZOOM_OUT = 8.0f;
	private static final int STAR_COUNT = (int) (100 * WORLD_WIDTH);
	
	private static float cameraWidth = 1.6f; //16x9 aspect ratio
	private static  float cameraHeight = 0.9f;
	private float zoom = 1.0f;
	private float zoomIncrement = 0.05f;
	
	private BufferStrategy bs;
	private volatile boolean running;
	private Thread gameThread;
	protected FrameRate frameRate;
	protected Canvas canvas;
	protected RelativeMouseInput mouse;
	protected KeyboardInput keyboard;
	
	protected Color appBackground = Color.BLACK;
	protected Color appBorder = Color.LIGHT_GRAY;
	protected Color appFPSColor = Color.GREEN;
	protected String appTitle = "Orbit Wars";
	protected Font appFont = new Font("Courier New", Font.PLAIN, 14);
	protected float appBorderScale = 0.99f;
	
	protected int appWidth = SCREEN_W;
	protected int appHeight = SCREEN_H;
	protected float appWorldWidth = cameraWidth;
	protected float appWorldHeight = cameraHeight;
	protected long appSleep = 10L;
	protected boolean appMaintainRatio = true;
	
	//protected Handler handler;
	
	protected boolean followShip;
	protected Camera camera;
	protected double cameraDeltaX;
		
	//////////// here's some stars and planet stuff so we can form a basis for//////////// vectors and matrix math//////////////
	protected PolygonShip ship;
	
	private Vector2f[] stars;
	private Color[] colors;
	private Random rand = new Random();
	///////////// end of planet	///////////// stuff//////////////////////////////////////////////////////

	public GameFramework() {

	}

	public void displayGame() {
		canvas = new Canvas();
		canvas.setBackground(appBackground);
		canvas.setIgnoreRepaint(true);
		getContentPane().add(canvas);
		setLocationByPlatform(true); // this let's the OS decide where to put the window
		
		if (appMaintainRatio){
			getContentPane().setBackground(appBorder);
			setSize(appWidth, appHeight);
			setLayout(null);
			getContentPane().addComponentListener(new ComponentAdapter(){
				public void componentResized(ComponentEvent e){
					onComponentResized(e);
				}
			});
		} else {
			canvas.setSize(appWidth, appHeight);
			pack();
		}

		setTitle(appTitle);
		keyboard = new KeyboardInput();
		canvas.addKeyListener(keyboard);
		mouse = new RelativeMouseInput(canvas);
		canvas.addMouseListener(mouse);
		canvas.addMouseMotionListener(mouse);
		canvas.addMouseWheelListener(mouse);
		setVisible(true);
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		canvas.requestFocus();
		gameThread = new Thread(this);
		gameThread.start();

	}
	
	//I may have added some redundant stuff in here
	protected void onComponentResized (ComponentEvent e){
		Dimension size = getContentPane().getSize();
		int viewportWidth = (int) (size.width * appBorderScale);
		int viewportHeight = (int) (size.height * appBorderScale);
		int viewportX = (size.width - viewportWidth) / 2;
		int viewportY = (size.height - viewportHeight) / 2;
		int newWidth = viewportWidth;
		int newHeight = (int) (viewportWidth * cameraHeight / cameraWidth);
		if (newHeight > viewportHeight){
			newWidth = (int) (viewportHeight * cameraWidth / cameraHeight);
			newHeight = viewportHeight;
		}
		//this should center the thing
		viewportX += (viewportWidth - newWidth) / 2;
		viewportY += (viewportHeight - newHeight) / 2;
		canvas.setLocation(viewportX, viewportY);
		canvas.setSize(newWidth, newHeight);
	}
	
	//this was a sloppy copy and paste. I need to review what this is doing
	protected void updateZoom (){
		Dimension size = getContentPane().getSize();
		int viewportWidth = (int) (size.width * appBorderScale);
		int viewportHeight = (int) (size.height * appBorderScale);
		int viewportX = (size.width - viewportWidth) / 2;
		int viewportY = (size.height - viewportHeight) / 2;
		int newWidth = viewportWidth;
		int newHeight = (int) (viewportWidth * cameraHeight / cameraWidth);
		if (newHeight > viewportHeight){
			newWidth = (int) (viewportHeight * cameraWidth / cameraHeight);
			newHeight = viewportHeight;
		}
		//this should center the thing
		viewportX += (viewportWidth - newWidth) / 2;
		viewportY += (viewportHeight - newHeight) / 2;
		canvas.setLocation(viewportX, viewportY);
		canvas.setSize(newWidth, newHeight);
		
	}
	
	protected void zoomIn(){
		if ((zoom > MAX_ZOOM_IN)  ){
			zoom -= zoomIncrement;
			//reset to default values
			cameraWidth = 1.6f; 
			cameraHeight = 0.9f;
			//calculate the new zoom
			cameraWidth *= zoom;
			cameraHeight *= zoom;
		}
	}
	//you need to make sure that it stops zooming out if that goes outside the world boundaries
	protected void zoomOut(){
		if ( (zoom < MAX_ZOOM_OUT) ){
			zoom += zoomIncrement;
			//reset to default values
			cameraWidth = 1.6f;
			cameraHeight = 0.9f;
			//calculate the new zoom
			cameraWidth *= zoom;
			cameraHeight *= zoom;
		}
	}
	
	protected void switchCameraMode(){
		followShip = !followShip;
	}

	
	protected Matrix3x3f getViewportTransform(){
		return Utility.createViewport(cameraWidth, cameraHeight, 
				canvas.getWidth(), canvas.getHeight());
	}
	
	protected Matrix3x3f getReverseViewPortTransform(){
		return Utility.createReverseViewport(cameraWidth, cameraHeight, 
				canvas.getWidth(), canvas.getHeight());
	}
	
	protected Vector2f getWorldMousePosition(){
		Matrix3x3f screenToWorld = getReverseViewPortTransform();
		Point mousePos = mouse.getPosition();
		Vector2f screenPos = new Vector2f(mousePos.x, mousePos.y);
		return screenToWorld.mul(screenPos);
	}
	

	public void run() {
		running = true;
		initialize();
		long curTime = System.nanoTime();
		long lastTime = curTime;
		double nanoSecondsPerFrame;
		while (running) {
			curTime = System.nanoTime();
			nanoSecondsPerFrame = curTime - lastTime;
			gameLoop((float) (nanoSecondsPerFrame / 1.0E9));
			lastTime = curTime;
		}
		terminate();
	}

	private void gameLoop(float delta) {
		processInput(delta);
		updateObjects(delta);
		renderFrame();
		sleep(appSleep);
	}

	private void renderFrame() {
		do {
			do {
				Graphics g = null;
				try {
					g = bs.getDrawGraphics();
					g.clearRect(0, 0, getWidth(), getHeight());
					render(g);
				} finally {
					if (g != null) {
						g.dispose();
					}
				}
			} while (bs.contentsRestored());
			bs.show();
		} while (bs.contentsLost());
	}

	private void sleep(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException ex) {

		}
	}

	protected void initialize() {
		frameRate = new FrameRate();
		frameRate.initialize();
		
		//handler = new Handler();
		
		Vector2f cameraStart = new Vector2f(0,0);
		camera = new Camera(cameraStart, cameraWidth, cameraHeight, zoom,
				WORLD_WIDTH, WORLD_HEIGHT);
		
		createStars();
		
	}
	
	private void createStars(){
		stars = new Vector2f[STAR_COUNT];
		colors = new Color[STAR_COUNT];
		for (int i = 0; i < stars.length; ++i){
			float x = rand.nextFloat() * WORLD_WIDTH - (WORLD_WIDTH / 2);
			float y = rand.nextFloat() * WORLD_HEIGHT - (WORLD_HEIGHT / 2);
			stars[i] = new Vector2f(x, y);
			float color = rand.nextFloat();
			colors[i] = new Color (color, color, color);
		}
	}

	protected void terminate() {
		// we will probably use this when we start messing with threads
	}

	protected void processInput(float delta) {
		keyboard.poll();
		mouse.poll();
	}

	protected void updateObjects(float delta) {
		updateAllGameObjects(delta);
				
		//I need to re-engineer this so it follows a selected unit
		if (followShip)
			camera.followPlayer(ship, cameraWidth, cameraHeight, zoom);
		else 
			camera.keyboardPan(cameraWidth, cameraHeight);
	}
	
	protected void updateAllGameObjects(float delta){
		//handler.removeOldObjects();
		//handler.update(delta);
	}

	protected void render(Graphics g) {
		g.setFont(appFont);
		g.setColor(appFPSColor);
		frameRate.calculate();
		g.drawString(frameRate.getFrameRate(), 20, 20);
		g.drawString("Screen Mouse Position: " + mouse.getPosition().toString(), 20, 35);
		g.drawString("World Mouse Position: " + getWorldMousePosition(), 20, 50);
		g.drawString("Player Pos: " + ship.getPosition(), 20, 65);
		g.drawString("Camera top left pos: " + camera.getCameraPos(), 20, 80);
		g.drawString("Zoom: " + zoom , 20, 95);
		g.drawString("9 zooms out, 0 zooms in: " + zoom , 20, 110);
		g.drawString("'p' toggles between camera modes: ", 20, 125);
		g.drawString("Ship Angle: " + ship.getAngle(), 20, 140);
		g.drawString("Ship world angle: " + String.valueOf((float)Math.atan2(ship.getPosition().x, 
				ship.getPosition().y)), 20, 155);
		
		
		updateZoom();
		Graphics2D g2d = (Graphics2D) g;
		//turn on anti-aliasing rendering hint on
		Matrix3x3f view = getViewportTransform(); //we're putting all rendering into world floating point system

		Vector2f cameraCorner = view.mul(camera.getCameraPos());
		
		g2d.translate((int) -cameraCorner.x, (int) -cameraCorner.y);
		/////////////////////all stuff inside the camera///////////////////////////
		
		drawStars(g2d, view); //background
		
		drawAllGameObjects(g2d, view);
		
		
		g2d.translate((int) cameraCorner.x, (int) cameraCorner.y);
		g2d.dispose();
	}
	
	protected void drawAllGameObjects(Graphics2D g2d, Matrix3x3f view){
		//handler.draw(g2d, view);
	}
	
	private void drawStars(Graphics2D g, Matrix3x3f view){
		for (int i = 0; i < stars.length; ++i){
			g.setColor(colors[i]);
			Vector2f screen = view.mul(stars[i]);  //this converts it from world to screen positions
			g.fillRect((int) screen.x, (int) screen.y, 1, 1);
			//System.out.println("star " + i + " x: " + screen.x + " y: " + screen.y);
		}
	}
	
	
	// we'll probably eventually use this, should be in the gameframework when you make it
	private void disableCursor() {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image image = tk.createImage("");
		Point point = new Point(0, 0);
		String name = "irrelevant...isn't really used";
		Cursor cursor = tk.createCustomCursor(image, point, name);
		setCursor(cursor);
	}

	// as we start to hyperthread the game beyond just the Java Swing elements, this is our safer way to close the program
	protected void onWindowClosing() {
		try {
			running = false;
			gameThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	// change this to launchGame() as you export this into supporting classes
	protected static void launchApp(final GameFramework app) {
		//final GameFramework app = new GameFramework();
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.onWindowClosing();
			}
		});
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				app.displayGame();
			}
		});
	}

}
