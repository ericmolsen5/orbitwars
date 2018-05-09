package gameobject;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public abstract class GameObject {
	
	protected boolean removeObject = false;
	protected ID id;
	
	protected Vector2f position;
	protected Vector2f velocity;
	protected float bearing;
	protected Rectangle2D rect;
	
	
	public GameObject(Vector2f position, Vector2f velocity, float bearing){
		this.position = position;
		this.velocity = velocity;
		this.bearing = bearing;
		
	}
	
	public GameObject(){
		
	}
	
	public Vector2f getPosition(){
		return position;
	}
	
	public Vector2f getVelocity(){
		return velocity;
	}
	
	public ID getID(){
		return id;
	}
	
	public void setID(ID id){
		this.id = id;
	}
	
	public Rectangle2D getRect(){
		return rect;
	}
	
	public abstract void update(float delta);
	
	public abstract void draw (Graphics2D g2d, Matrix3x3f view);
	
	public boolean getRemoveObject(){
		return removeObject;
	}
	//the handler clears all game objects with this boolean set to true before cycling through the rest of the gameloop updates
	public void removeObject(){
		removeObject = true;
	}
}
