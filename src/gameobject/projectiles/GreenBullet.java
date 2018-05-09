package gameobject.projectiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class GreenBullet extends Projectile{

	protected ID id;
	
	private int damage;
	private float range;
	
	private Vector2f velocity;
	private Vector2f position;
	private Color color;
	private float width;
	
	//private Rectangle2D bulletRect;

	public GreenBullet(Vector2f position, float bearing){
		
		id = ID.Projectile;
		
		damage = 2;
		range = 8.0f;
		
		this.position = position;
		velocity = Vector2f.polar(bearing, 2.0f);
		width = 0.012f;
		color = Color.GREEN;
		
		rect = new Rectangle2D.Float(position.x - width/2,
				position.y + width/2, width, width);
	}
	
		
	//need this so other objects can detect collision
	public Vector2f getPosition(){
		return position;
	}
	
	public float getWidth(){
		return width;
	}
	public Vector2f getVelocity(){
		return velocity;
	}
	
		
	@Override
	public void update(float delta) {
		Vector2f oldPos = position;
		position = position.add(velocity.mul(delta));	
		
		rect = new Rectangle2D.Float(position.x - width/2,
				position.y + width/2, width, width);
		//System.out.println(rect);
		
		//remove bullets when they are out of range
		range -= position.sub(oldPos).len();
		
		if (range <= 0) removeObject = true;
		//remove bullets when they collide with planets
	}

	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		g2d.setColor(color);
		Vector2f topLeft = new Vector2f(position.x - width/2, position.y + width/2);
		topLeft = view.mul(topLeft);
		Vector2f bottomRight = new Vector2f(position.x + width/2, position.y - width/2);
		bottomRight = view.mul(bottomRight);
		//there was a bug causing these round down to zero wh0en you zoom out
		g2d.fillOval((int) topLeft.x, 
				(int) topLeft.y, 
				(int) Math.max(bottomRight.x - topLeft.x, 2), 
				(int) Math.max(bottomRight.y - topLeft.y, 2));
	}

}
