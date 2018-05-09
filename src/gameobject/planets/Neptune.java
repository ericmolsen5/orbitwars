package gameobject.planets;

import java.awt.Color;
import java.awt.Graphics2D;

import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

public class Neptune extends Planet{
	
	public Neptune(){
		id = ID.Planet;
		rotDelta = (float) Math.toRadians(0.0015);
		worldSize = 0.75f;
		worldPosition = new Vector2f(-29f, 0.0f);
		getSunMat();
	}
	
	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		g2d.setColor(Color.BLUE);
		super.draw(g2d, view);
	}

}
