package gameobject.planets;

import java.awt.Color;
import java.awt.Graphics2D;

import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class Jupiter extends Planet {
	
	public Jupiter(){
		id = ID.Planet;
		rotDelta = (float) Math.toRadians(0.005);
		worldSize = 2.0f;
		worldPosition = new Vector2f(19.0f, 0.0f);
		getSunMat();
	}
	
	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		g2d.setColor(Color.ORANGE);
		super.draw(g2d, view);
	}
}