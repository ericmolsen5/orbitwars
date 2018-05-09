package gameobject.planets;

import java.awt.Color;
import java.awt.Graphics2D;

import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class Mercury extends Planet {
	
	public Mercury(){
		id = ID.Planet;
		rotDelta = (float) Math.toRadians(0.05); //for gameplay put it to 0.00000010f
		worldSize = 0.25f;
		worldPosition = new Vector2f(4.0f, 0.0f);
		getSunMat();
	}
	

	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		g2d.setColor(Color.GRAY);
		super.draw(g2d, view);
	}

}
