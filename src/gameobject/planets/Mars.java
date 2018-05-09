package gameobject.planets;

import java.awt.Color;
import java.awt.Graphics2D;

import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class Mars extends Planet {
	
	public Mars(){
		id = ID.Planet;
		rotDelta = (float) Math.toRadians(/*0.015*/ 0.0);
		worldSize = 0.5f;
		worldPosition = new Vector2f(10.0f, 0.0f);
		getSunMat();
	}
	
	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		g2d.setColor(Color.RED);
		super.draw(g2d, view);
	}
	
}
