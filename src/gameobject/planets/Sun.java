package gameobject.planets;

import java.awt.Color;
import java.awt.Graphics2D;

import gameobject.GameObject;
import main.ID;
import util.Matrix3x3f;
import util.Vector2f;

//the sun is handling all its own methods. At some point I may need to go and clean this up
public class Sun extends GameObject {
	
	private float worldSize;
	private Vector2f worldPosition;
	private Vector2f topLeftWorld;
	private Vector2f screenSun;
	private Vector2f screenTopLeft;
	private Matrix3x3f sunMat; //may not need this
	//protected ID id;
	
	
	public Sun(){
		id = ID.Planet;
		//worldSun = new Vector2f(0.0f,0.0f);
		worldPosition = new Vector2f(0.0f,0.0f);
		worldSize = 4.0f;
		//throwing this in the constructor to avoid the null pointer exception
	}


	@Override
	public void update(float delta) {
		Matrix3x3f sunMat = Matrix3x3f.identity();
		sunMat = sunMat.mul(Matrix3x3f.translate(0.0f, 0.0f));
		worldPosition = sunMat.mul(new Vector2f());
		topLeftWorld = new Vector2f(worldPosition.x - worldSize/2, 
				worldPosition.y + worldSize/2);
	}


	@Override
	public void draw(Graphics2D g2d, Matrix3x3f view) {
		g2d.setColor(Color.YELLOW);
		screenSun = view.mul(worldPosition);
		screenTopLeft = view.mul(topLeftWorld);
		g2d.fillOval((int) screenTopLeft.x, (int) 
				screenTopLeft.y, 
				//should these be doubled???
				(int)(2 * (Math.abs(screenSun.x - screenTopLeft.x))), 
				(int)(2 * (Math.abs(screenSun.y - screenTopLeft.y))));
		
	}
	
	public Matrix3x3f getSunMat(){
		return sunMat;
	}
	
	public Vector2f getScreenSun(){
		return screenSun;
	}
	
	
	

}
