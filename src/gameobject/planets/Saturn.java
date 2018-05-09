package gameobject.planets;

import java.awt.Color;
import java.awt.Graphics2D;

import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class Saturn extends Planet {
	
	public Saturn(){
		id = ID.Planet;
		rotDelta = (float) Math.toRadians(0.003);
		worldSize = 1.5f;
		worldPosition = new Vector2f(-23.0f, 0.0f);
		getSunMat();
	}
	
	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		g2d.setColor(Color.GREEN);
		super.draw(g2d, view);
	}

}
