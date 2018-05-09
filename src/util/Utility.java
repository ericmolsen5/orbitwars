package util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Utility {
	
	public static Matrix3x3f createViewport(
			float worldWidth, float worldHeight,
			float screenWidth, float screenHeight )  {
		float scaled_x = (screenWidth - 1) / worldWidth;
		float scaled_y = (screenHeight - 1) / worldHeight;
		float translated_x = (screenWidth - 1) / 2.0f;
		float translated_y = (screenHeight - 1) / 2.0f;
		Matrix3x3f viewport = Matrix3x3f.scale(scaled_x, -scaled_y); //make the y axis work like a cartesian plane
		viewport = viewport.mul(Matrix3x3f.translate(translated_x, translated_y)); //moves the 0,0 point to the middle of the screen
		return viewport;
	}
	
	public static Matrix3x3f createReverseViewport(
			float worldWidth, float worldHeight,
			float screenWidth, float screenHeight )  {
		float scaled_x = worldWidth / (screenWidth - 1);
		float scaled_y = worldHeight / (screenHeight - 1);
		float translated_x = (screenWidth - 1) / 2.0f;
		float translated_y = (screenHeight - 1) / 2.0f;
		Matrix3x3f viewport = Matrix3x3f.translate(-translated_x, -translated_y);
		viewport = viewport.mul(Matrix3x3f.scale(scaled_x, -scaled_y));
		return viewport;
	}
	
	public static void drawPolygon(Graphics g, Vector2f[] polygon) {
		Vector2f P;
		Vector2f S = polygon[polygon.length - 1];
		for (int i = 0; i < polygon.length; ++i) {
			P = polygon[i];
			g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
			S = P;
		}
	}
	
	public static void fillPolygon(Graphics2D g, Vector2f[] polygon) {
		Polygon p = new Polygon();
		for (Vector2f v : polygon) {
			p.addPoint((int) v.x, (int) v.y);
		}
		g.fill(p);
	}

}
