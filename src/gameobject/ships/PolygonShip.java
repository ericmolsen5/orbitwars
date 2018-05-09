package gameobject.ships;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import gameobject.GameObject;
import gameobject.projectiles.GreenBullet;
import gameobject.Particle;
import main.ID;
import util.Matrix3x3f;
import util.Utility;
import util.Vector2f;

public class PolygonShip extends GameObject {
	private static final int MAX_PARTICLES = 300;
	private ID id;
	private float angle;
	private float acceleration;
	private float friction;
	private float maxVelocity;
	private float rotationDelta;
	private float curAcceleration;
	private Random random;
	private Vector2f position;
	private Vector2f velocity;
	private ArrayList<Particle> particles;
	private Vector2f[] polygonPoints;
	private ArrayList<Vector2f[]> renderList;
	private float width;
	
	public PolygonShip(Vector2f startingPos){
		id = ID.FriendlyShip;
		friction = 0.25f;
		rotationDelta = (float) Math.toRadians(180.0);
		acceleration = 1.0f;
		maxVelocity = 0.75f;
		velocity = new Vector2f();
		random = new Random();
		position = new Vector2f(startingPos); //ship starting position
		polygonPoints = new Vector2f[]{
				new Vector2f(0.0325f, 0.0f),
				new Vector2f(-0.0325f, -0.0325f),
				new Vector2f(0.0f, 0.0f),
				new Vector2f(-0.0325f, 0.0325f),
		};
		width = 0.065f;
		particles = new ArrayList<Particle>();
		renderList = new ArrayList<Vector2f[]>();
	}
	
	//don't care about damage and collision just yet
	
	//this may be a sign of sloppy programming, but we'll use a getter here
	public Vector2f getPosition(){
		return position;
	}
	
	public float getAngle(){
		return angle;
	}
	
	public void rotateLeft(float delta){
		angle += rotationDelta * delta;
	}
	
	public void rotateRight(float delta){
		angle -= rotationDelta * delta;
	}
	
	//review....this thing still confuses me
	public void setThrusting (boolean thrusting){
		curAcceleration = thrusting ? acceleration : 0.0f;
		if (thrusting) {
			while (particles.size() < MAX_PARTICLES) {
				particles.add(createRandomParticle());
			}
		}
	}
	
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	public GreenBullet fireBullet(){
		Vector2f bulletPos = position.add(Vector2f.polar(angle, 0.0325f)); //should fire from front
		return new GreenBullet(bulletPos, angle);
	}
	
	public void update(float time){
		updatePosition(time);
		updateParticles(time);
		renderList.clear();
		Vector2f[] world = transformPolygon();
		renderList.add(world);
		
		rect = new Rectangle2D.Float(position.x - width/2, 
				position.y - width/2,
				width, width);
	}
	
	public void changeVelocity(Vector2f newVelocity){
		velocity = newVelocity;
	}
	
	private void updatePosition (float time){
		//System.out.println(angle);
		Vector2f accel = Vector2f.polar(angle, curAcceleration);
		velocity = velocity.add(accel.mul(time)); //????????
		float curSpeedOrMaxSpeed = Math.min(maxVelocity / velocity.len(), 1.0f);
		velocity = velocity.mul(curSpeedOrMaxSpeed);
		float slowDown = 1.0f - friction * time;
		velocity = velocity.mul(slowDown);
		position = position.add(velocity.mul(time));
	}
	
	//passing the delta time, colliding object velocity vector, and it's collision rectangle
	public void bouncePosition (float time, float rotationSpeed, 
			Rectangle2D collRect){
		
		//Vector2f collidingVelocity = position;
		//collidingVelocity.polar(rotationSpeed, position.lenSqr());
		//determine which side of the rotation the collision happened
		     
		
		float collisionAngle = (float) Math.atan2(collRect.getCenterX(), 
				collRect.getCenterY());
		float collisionCos = (float) Math.cos(collisionAngle);
		float collisionSin = (float) Math.sin(collisionAngle);
		
		float shipAngle = (float) Math.atan2(rect.getX(), rect.getY());
		float shipCos = (float) Math.cos(shipAngle);
		float shipSin = (float) Math.sin(shipAngle);
		
		
		
		float newAngle = position.angle();
		
				
		float deltaX = (float) Math.abs(position.x - (collRect.getX()+(collRect.getWidth())));
		float deltaY = (float) Math.abs(position.y - (collRect.getY()+(collRect.getHeight())));
		
		double collisionLen = Math.sqrt((collRect.getCenterX() * collRect.getCenterY()) + 
				collRect.getY() * collRect.getY());
		
		double shipLen = Math.sqrt((rect.getX() * rect.getX()) + 
				rect.getY() * rect.getY());	
		
		//System.out.println(shipAngle + " collision Angle" + collisionAngle);// + "ship len: " + 
		//shipLen + " colliison len" + collisionLen );
		
	
		//need the impact angle
		float impactAngle = velocity.angle();
		
				
		Vector2f accel = new Vector2f();
		
		if (shipAngle < collisionAngle){

			
			if (shipLen > collisionLen){
				accel = position.perp(); 
				System.out.println("1");
			} if (shipLen < collisionLen){
					//accel = position.inv().perp(); 
					System.out.println("2");
				}
					
		}
				
				
					
				//System.out.println("1, \nshipCos: " + shipCos + " object Cos: " + collisionCos + "shipCos - objCos = " + (shipCos - collisionCos)
				//		+ " \n shipSin: " + shipSin + "objectSin" +  collisionSin);
			//}else{
				//if (shipSin > collisionSin && shipCos < collisionCos)
				//accel = position.inv().perp();
				//accel = position.mul((float)collisionLen);
				//System.out.println("2");
			//}	
		
		
		if (shipAngle > collisionAngle){
			if (shipLen > collisionLen ){
				accel = position.perp().inv();
				//accel = position.mul((float)collisionLen);
				System.out.println("3");//, shipCos: " + shipCos + " shipSin: " + shipSin);
			} else{
				accel = position;
				//accel = position.mul((float)collisionLen);
				System.out.println("4");//, shipCos: " + shipCos + " shipSin: " + shipSin);
			}		
		}
		/*
		Vector2f collRectVector = new Vector2f((float) collRect.getCenterX(), 
				(float) collRect.getCenterY());
		
		Vector2f accel = calculateReflection(velocity, position, collRectVector);
		*/
		//works...don't touch it!!!
		velocity = velocity.add(accel.mul(time)); //????????
		
		float curSpeedOrMaxSpeed = Math.min(maxVelocity / velocity.len(), 1.0f);
		velocity = velocity.mul(curSpeedOrMaxSpeed);
		float slowDown = 1.0f - friction * time;
		velocity = velocity.mul(slowDown);
		position = position.add(velocity.mul(time));
	}
	
	private Vector2f[] transformPolygon(){
		Matrix3x3f mat = Matrix3x3f.rotate(angle);
		mat = mat.mul(Matrix3x3f.translate(position));
		return transform(polygonPoints, mat);
	}
	private Vector2f[] transform(Vector2f[] poly, Matrix3x3f mat){
		Vector2f[] copy = new Vector2f[poly.length];
		for (int i = 0; i < poly.length; ++i){
			copy[i] = mat.mul(poly[i]);
		}
		return copy;
	}
	
	public void draw(Graphics2D g, Matrix3x3f view){
		//g.setColor(new Color(50,50,50));
		for (Vector2f[] poly: renderList){
			for (int i = 0; i < poly.length; ++i){
				poly[i] = view.mul(poly[i]);
			}
			g.setColor(Color.DARK_GRAY);
			Utility.fillPolygon(g, poly);
			g.setColor(Color.RED);
			Utility.drawPolygon(g, poly);
		}
		for (Particle p : particles) {
			p.draw(g, view);
		}
	}
	
	private Particle createRandomParticle() {
		Particle p = new Particle();
		p.setRadius(0.002f + random.nextFloat() * 0.004f);
		p.setLifeSpan(random.nextFloat() * 0.5f);
		switch (random.nextInt(5)) {
		case 0:
			p.setColor(Color.WHITE);
			break;
		case 1:
			p.setColor(Color.RED);
			break;
		case 2:
			p.setColor(Color.YELLOW);
			break;
		case 3:
			p.setColor(Color.ORANGE);
			break;
		case 4:
			p.setColor(Color.PINK);
			break;
		}
		int thrustAngle = 80;
		float a = (float) Math.toRadians(random.nextInt(thrustAngle)
				- (thrustAngle / 2));
		float velocity = random.nextFloat() * 0.375f;
		Vector2f bulletPos = position.add(Vector2f.polar(angle, -0.0325f));
		p.setPosition(bulletPos);
		p.setVector(angle + (float) Math.PI + a, velocity);
		return p;
	}
	
	private void updateParticles(float delta) {
		Iterator<Particle> part = particles.iterator();
		while (part.hasNext()) {
			Particle p = part.next();
			Vector2f bulletPos = position.add(Vector2f.polar(angle, -0.0325f));
			p.setPosition(bulletPos);
			p.update(delta);
			if (p.hasDied()) {
				part.remove();
			}
		}
	}



	
}

