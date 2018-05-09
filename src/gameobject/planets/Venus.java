package gameobject.planets;

import java.awt.Color;
import java.awt.Graphics2D;

import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class Venus extends Planet {
	
	public Venus(){
		id = ID.Planet;
		rotDelta = (float) Math.toRadians(0.025);
		worldSize = 0.35f;
		worldPosition = new Vector2f(-6.0f, 0.0f);
		getSunMat();
	}
	
	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		g2d.setColor(Color.GREEN);
		super.draw(g2d, view);
	}

}
