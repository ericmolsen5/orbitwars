package gameobject.projectiles;

import java.awt.Color;
import java.awt.Graphics2D;

import gameobject.GameObject;
import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class Projectile extends GameObject{
	
	private ID id;
	
	private int damage;
	private float range;
	
	private Vector2f velocity;
	private Vector2f position;
	private Color color;
	private float width;
	
		
	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		// TODO Auto-generated method stub
		
	}

}
