package gameobject.planets;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import gameobject.GameObject;
import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class Planet extends GameObject {
	
	protected float rotDelta;
	protected float worldSize;
	protected Vector2f worldPosition; //center point
	protected Vector2f topLeftWorld;
	protected Vector2f screenPosition;
	protected Vector2f screenTopLeft;
	protected Matrix3x3f planetMat;
	protected Matrix3x3f sunMat;
	protected Rectangle2D planetRect;
	
	protected ID id;
	
	//early attempts at collision detection
	
	
	public Matrix3x3f getSunMat(){
		sunMat = Matrix3x3f.identity();
		sunMat = sunMat.mul(Matrix3x3f.translate(0.0f, 0.0f));
		return sunMat;
	}
	
	public Vector2f getPosition(){
		return worldPosition;
	}
	
	public float getOrbitVelocity(){
		return rotDelta;
	}
	
	public float getWorldSize(){
		return worldSize;
	}
	
	public void update(float delta){
		//update planet movement
		planetMat = Matrix3x3f.translate(worldPosition);
		planetMat = planetMat.mul(Matrix3x3f.rotate(rotDelta));
		planetMat = planetMat.mul(sunMat);		
		
		//check for collisions
		
		//for screen rendering
		worldPosition = planetMat.mul(new Vector2f());
		topLeftWorld = new Vector2f(worldPosition.x - worldSize/2,
				worldPosition.y + worldSize/2);
		
		rect = new Rectangle2D.Float(worldPosition.x - worldSize/2, 
				worldPosition.y - worldSize/2,
				worldSize, worldSize);
	}
	
		
	public void draw(Graphics2D g2d, Matrix3x3f view){
		screenPosition = view.mul(worldPosition);
		screenTopLeft = view.mul(topLeftWorld);
		g2d.fillOval((int) screenTopLeft.x, 
				(int) screenTopLeft.y, 
				(int) (2 * (Math.abs(screenPosition.x - screenTopLeft.x))), 
				(int) (2 * (Math.abs(screenPosition.y - screenTopLeft.y))) );
		
		//rectPosition = view.add(m1)
		//g2d.drawRect((int) screenTopLeft.x, (int) screenTopLeft.y, 
		//		(int) (2 * (Math.abs(screenPosition.x - screenTopLeft.x))), 
		//		(int) (2 * (Math.abs(screenPosition.y - screenTopLeft.y))) );
				
	}
	
	
	

	


}
