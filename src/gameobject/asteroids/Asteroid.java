package gameobject.asteroids;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import gameobject.GameObject;
import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

//I've gutted most of the polygon asteroids because it was killing the game's
//performance. I may come back and add a rotation thing once I have sprites loaded
//on the floating ovals

public class Asteroid extends GameObject{
	
	protected ID id;
	
	private Vector2f position;
	private float orbitVelocity;
	private float rotationDelta;
	private Random rand = new Random();
	private Matrix3x3f sunMat;
	private Matrix3x3f asteroidMat;
	//private Rectangle2D astRect;
	
	//temp stuff
	private Vector2f topLeft;
	private float width;
	private Vector2f screenPosition;
	private Vector2f screenTopLeft;
	
	
	public Asteroid(Vector2f position){
		id = ID.Asteroid;
		this.position = position;
		orbitVelocity = generateOrbitVelocity();
		width = generateWidth();
		rotationDelta = getRandomRotationDelta();
		
		sunMat = Matrix3x3f.identity();
		sunMat = sunMat.mul(Matrix3x3f.translate(0.0f, 0.0f));
		
		rect = new Rectangle2D.Float(position.x - width/2,
				position.y + width/2, width, width);
	}
	
	public float getOrbitVelocity(){
		return orbitVelocity;
	}
	
	public void nudgeAsteroid(float nudgeFactor1, float nudgeFactor2){
		position.x += nudgeFactor1;
		position.y += nudgeFactor2;
	}
	
	private float generateOrbitVelocity() {
		float otbitVelocity = 0.00025f + (rand.nextInt(10)/10000);
		return otbitVelocity;
	}
	
	private float getRandomRotationDelta(){
		int tempInt = rand.nextInt(45) + 5; //between 40 and 5 degrees
		return (float) Math.toRadians(tempInt);
	}
	
	private float generateWidth(){
		float newWidth = rand.nextFloat()/3;
		return newWidth;
	}
	
	public Vector2f getPosition(){
		return position;
	}
	
	public float getWidth(){
		return width;
	}
	

	@Override
	public void update(float delta) {
		
		asteroidMat = Matrix3x3f.translate(position);
		asteroidMat = asteroidMat.mul(Matrix3x3f.rotate(orbitVelocity));
		asteroidMat = asteroidMat.mul(sunMat);
		position = asteroidMat.mul(new Vector2f());
		topLeft = new Vector2f(position.x - width/2,
				position.y + width/2);		
		
		rect = new Rectangle2D.Float(position.x - width/2, 
				position.y - width/2,
				width, width);
		
	}
	
	public boolean contains(Rectangle2D objRect){
		if (rect.contains(objRect) || rect.intersects(objRect))
			return true;
		else
			return false;
	}
	
	public void nudgePosition(Vector2f collidingObjPos, Vector2f objVelocity){
		//bullet speed is currently 2.0f
		//float temp = Vector2f.polar(angle, radius)
	}
	
	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
				
		g2d.setColor(Color.GRAY);
		screenPosition = view.mul(position);
		screenTopLeft = view.mul(topLeft);
		g2d.fillOval((int) screenTopLeft.x, 
				(int) screenTopLeft.y, 
				(int) (2 * (Math.abs(screenPosition.x - screenTopLeft.x))), 
				(int) (2 * (Math.abs(screenPosition.y - screenTopLeft.y))) );
	}
	

}
