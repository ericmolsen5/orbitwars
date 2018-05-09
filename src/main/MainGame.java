/*
 * I'm throwing in the towel on this project so I can get started with school again
 * Here's a summary of what I'm trying to make:
 * - Wave-based space game that transitions between 3 main playing states
 * 	- deep space fighting (everything that you see so far)
 * 	- orbit invasion mode (where you have to land your invading force on the surface)
 * 	- ground based attack (smiple plane (miragine War style) wave fighting)
 * 
 * Here's what I've done so far:
 * 	- There is a decently developed viewport that resizes the floating point world positions to the screen.
 * 	- There is a camera class that pans and zooms. It also clips so that it doesn't extend beyond the 
 * 	  game's world boundaries (took a whole weekend to figure it out).
 * 	- There's a bunch of planets and asteroids. Asteroids are randomly generated, the planets fixed.
 * 	- There is a projectile class and green bullet class
 * 	- There is a polygon ship class which is almost entirely taken from Timothy Wright's book
 * 
 * Here are the kinks that I'm leaving unresolved:
 * 	- The GameFramework class needs to be cleaned up. So far it handles the camera and handler and all the 
 *    extra background stuff. At some point I need to develop a means of switching between Window mode and full screen
 *  - The Projectile class should handle all the rendering and updating (I just haven't moved it over yet). 
 *  - There's not enough fields to control the data. I really need to make an individual class of game constants and 
 *    formulize the specific numbers that create the environment (asteroid positions, object orbit speeds, etc).
 *    
 *  COLLISION DETECTION
 *  - I'm having a heck of a time getting the handler class to recognize gameobjects that are added to the mix at run time.
 *    Right now it only sees the objects created at compile time. I don't want to spread the collision instructions all over
 *    the program and want each game state manager to define what each collision does. I can't seem to find a way to make the
 *    handler do this.
 *    		POSSIBLE SOLUTION:
 *    - Don't use the handler at all. Have the current game state run a for-each loop on all objects that need to be updated.
 *      While doing this, it could calculate its collisions so we're not running a 1000 checks during each game loop
 *      
 *      MAJOR THINGS STILL MISSING
 *      
 *      - Resource Loader
 *      - Sprite sheets
 *      - Sprite overlay
 *      - Sound classes
 *      - Particle Engine
 *      - Animation
 *      - Game State Manager
 *      - XML utilities (for saving and loading games)
 *      - ANT integration
 */

package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import gameobject.ParticleExplosion;
import gameobject.asteroids.Asteroid;
import gameobject.planets.Earth;
import gameobject.planets.Jupiter;
import gameobject.planets.Mars;
import gameobject.planets.Mercury;
import gameobject.planets.Neptune;
import gameobject.planets.Planet;
import gameobject.planets.Saturn;
import gameobject.planets.Sun;
import gameobject.planets.Uranus;
import gameobject.planets.Venus;
import gameobject.projectiles.GreenBullet;
import gameobject.ships.PolygonShip;
import util.GameFramework;
import util.Matrix3x3f;
import util.Vector2f;


//right now this is the main method, but this will become the game playing state
//for the deep space portion of the game.

public class MainGame extends GameFramework {
	

	private static final long serialVersionUID = 1L;
	private static final int NUMBER_OF_ASTEROIDS = 500;
	private static final int ASTEROID_BELT_WIDTH = 5;
	private static final int START_OF_ASTEROIDS = 11;
	
	private Random rand;
	private ArrayList<ParticleExplosion> explosions;
	//gameObject fields
	private Sun sun;
	private Planet[] planets;
	private ArrayList<Asteroid> asteroids; //this may need to become a Linked List if we start destroying them
	private LinkedList<GreenBullet> projectiles;
	private LinkedList<Rectangle2D> collisionList;
	
	MainGame(){
		
	}
	
	@Override
	protected void initialize(){
		super.initialize();
		followShip = true;
		rand = new Random();
		
		sun = new Sun();
		initializeAsteroids();
		initializePlanets();
		projectiles = new LinkedList<GreenBullet>();
		ship = new PolygonShip(new Vector2f(10.0f, 0.5f));
		explosions = new ArrayList<ParticleExplosion>();
		//collisionList = new LinkedList<Rectangle2D>();
		//loadCollisionList();
				
	}
	//object array of planets
	private void initializePlanets(){
		planets = new Planet[8];
		planets[0] = new Mercury();
		planets[1] = new Venus();
		planets[2] = new Earth();
		planets[3] = new Mars();
		planets[4] = new Jupiter();
		planets[5] = new Saturn();
		planets[6] = new Uranus();
		planets[7] = new Neptune();
	}
	//object array of asteroids, randomly generated
	//I'm going to do a check of all the asteroids to see if they overlap
	//This will be terribly expensive for the first run
	private void initializeAsteroids(){
		asteroids = new ArrayList<Asteroid>(NUMBER_OF_ASTEROIDS);
		for (int i = 0; i < NUMBER_OF_ASTEROIDS; i++){
			//int tempInt = rand.nextInt(3);
			Vector2f position = generateAsteroidPosition();
			Asteroid nextAst = new Asteroid(position);
			for (int j = 0; j < asteroids.size(); j++){
				if (nextAst.getRect().contains(asteroids.get(j).getRect())
						|| nextAst.getRect().intersects(asteroids.get(j).getRect())){
					continue;
				}
			}				
			asteroids.add(nextAst);
		}
	}
	
	//next idea. Generate a LinkedList of Rectangles that gets updated every
	//frame. Rather than having each bullet calculate 500 times whether it's
	//collided, it can check this list
	
	
	private Vector2f generateAsteroidPosition(){
		float angle = (float) (rand.nextFloat() * Math.toRadians(360));
		float xPos = (float) Math.cos(angle) * 
				(rand.nextFloat() * ASTEROID_BELT_WIDTH + START_OF_ASTEROIDS);
		float yPos = (float) Math.sin(angle) * 
				(rand.nextFloat() * ASTEROID_BELT_WIDTH + START_OF_ASTEROIDS);
		Vector2f position = new Vector2f (xPos, yPos);
		return position;
	}
	
	@Override
	protected void processInput (float delta){
		super.processInput(delta);
		if (keyboard.keyDown(KeyEvent.VK_LEFT)) {
			ship.rotateLeft(delta);
		}
		if (keyboard.keyDown(KeyEvent.VK_RIGHT)) {
			ship.rotateRight(delta);
		}
		//this input method is feeding a boolean into the ship every frame rate
		ship.setThrusting(keyboard.keyDown(KeyEvent.VK_UP));
		
		if (keyboard.keyDown(KeyEvent.VK_SPACE)){
			//add a bullet to the handler, launching the bullet should instantiate it
			projectiles.add(ship.fireBullet());
		}
		
		if (keyboard.keyDown(KeyEvent.VK_9)) {
			zoomOut();
		}
		
		if (keyboard.keyDown(KeyEvent.VK_0)) {
			zoomIn();
		}
		
		if (keyboard.keyDown(KeyEvent.VK_A)) {
			camera.panLeft();
		}
		if (keyboard.keyDown(KeyEvent.VK_D)) {
			camera.panRight();
		}
		if (keyboard.keyDown(KeyEvent.VK_W)) {
			camera.panUp();
		}
		if (keyboard.keyDown(KeyEvent.VK_S)) {
			camera.panDown();
		}
		if (keyboard.keyDownOnce(KeyEvent.VK_P)) {
			switchCameraMode();
		}
	}
	
	@Override
	protected void updateAllGameObjects(float delta){
		//Timothy Wright to the rescue
		updateBullets(delta);	//checks for collisions
		updateProjectileExplosions(delta);
		//update asteroids
		for (Asteroid ast: asteroids){
			ast.update(delta);
			//collision check
		}
		//update planets
		for (int i = 0; i < planets.length; i++){
			planets[i].update(delta);
			//collision check
		}
		//update sun
		sun.update(delta);
		
				
		updateShip(delta);
		//collision stuff
	}
	
	private void updateBullets(float delta){
		LinkedList<GreenBullet> copy = new LinkedList<GreenBullet>(projectiles);
		for (GreenBullet bullet: copy){
			updateBullet(delta, bullet);
		}
	}
	
	//working through some serious bugs on this
	private void updateBullet(float delta, GreenBullet bullet){
		bullet.update(delta);
		if (bullet.getRemoveObject()){
			projectiles.remove(bullet);
		} else{
			//check planets
			for(int i = 0; i < planets.length; i++){
				//System.out.println(bullet.getBulletRect().getBounds2D());
				if (planets[i].getRect().contains(bullet.getRect()) 
						|| planets[i].getRect().intersects(bullet.getRect())){
					//particle explosion
					//explosion sound
					projectiles.remove(bullet);
					explosions.add(new ParticleExplosion(bullet.getPosition()));
				}
			}
			//check asteroids
			for (Asteroid ast: asteroids){
				if (ast.getRect().contains(bullet.getRect())
						|| ast.getRect().intersects(bullet.getRect())){
					ast.nudgePosition(bullet.getPosition(), bullet.getVelocity());
					//particle explosion
					//explosion sound
					projectiles.remove(bullet);
					explosions.add(new ParticleExplosion(bullet.getPosition()));
					//call for explosion sound
				}
			}
		}
	}
	
	private void updateProjectileExplosions(float delta) {
		for (ParticleExplosion explosion : new ArrayList<ParticleExplosion>(
				explosions)) {
			explosion.update(delta);
			if (explosion.isFinished()) {
				explosions.remove(explosion);
			}
		}
	}
	
	private void updateShip(float delta){
		ship.update(delta);
		//there are some null pointer exceptions going on here
		for(int i = 0; i < planets.length; i++){
			//System.out.println(bullet.getBulletRect().getBounds2D());
			if (planets[i].getRect().contains(ship.getRect()) 
					|| planets[i].getRect().intersects(ship.getRect())){
				ship.bouncePosition(delta, planets[i].getOrbitVelocity(),
						planets[i].getRect());
				
				//bounce sound
				
			}
		}
		//check asteroids
		for (Asteroid ast: asteroids){
			if (ast.getRect().contains(ship.getRect())
					|| ast.getRect().intersects(ship.getRect())){
				ast.nudgePosition(ship.getPosition(), ship.getVelocity());
				ship.bouncePosition(delta, ast.getOrbitVelocity(), ast.getRect());
				//System.out.println("bounce!!!");
				//bounce sound
			}
		}
		
	}
	
	//this should give us everything that's been translated inside the camera 
	@Override
	protected void drawAllGameObjects(Graphics2D g2d, Matrix3x3f view){
		//handler.draw(g2d, view);
		for (GreenBullet bullet: projectiles){
			bullet.draw(g2d, view);
			g2d.setColor(Color.GREEN);
		}
		for (Asteroid ast: asteroids){
			ast.draw(g2d, view);
		}
		for (ParticleExplosion ex: explosions){
			ex.render(g2d,  view);
		}
		for (int i = 0; i < planets.length; i++){
			planets[i].draw(g2d, view);
		}
		sun.draw(g2d, view);
		//will become more complicated later
		ship.draw(g2d, view);
		
	}
		
	public static void main(String[] args){
		launchApp(new MainGame());
	}
	

}
