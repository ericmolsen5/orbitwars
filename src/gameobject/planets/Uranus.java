package gameobject.planets;

import java.awt.Color;
import java.awt.Graphics2D;

import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class Uranus extends Planet {
	
	public Uranus(){
		id = ID.Planet;
		rotDelta = (float) Math.toRadians(0.002);
		worldSize = 1.0f;
		worldPosition = new Vector2f(26.5f, 0.0f);
		getSunMat();
	}
	
	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		g2d.setColor(Color.CYAN);
		super.draw(g2d, view);
	}

}
