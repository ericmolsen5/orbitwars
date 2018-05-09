package util;

import gameobject.ships.PolygonShip;

/*
 * There's a very weird dance going on between the GameFrameWork and Camera Class
 * - Since GameFramework is actually rendering the screen, I'm keeping most of the
 *   camera zoom variables over there. It's zoom methods are also in the GameFramework class
 *   since it's pretty integral to the viewport calculations
 *   
 *   - The Camera class will probably update the camera position, but the Framework
 *     will worry about updating the frame
 */

public class Camera {
	
	private Vector2f cameraPos;
	private float cameraWidth;
	private float cameraHeight;
	private float zoom;
	private float worldLimitX;
	private float worldLimitY;
	private float panStep = 0.01f;
	
	
	public Camera (Vector2f startingPosition, float width, float height, float zoom,
			float worldWidth, float worldHeight) {

		
		cameraWidth = width;
		cameraHeight = height;
		this.zoom = zoom;
		worldLimitX = worldWidth;
		worldLimitY = worldHeight;

		cameraPos = new Vector2f(startingPosition);

	}

		
	public void followPlayer(PolygonShip player, float updatedCameraWidth,
			float updatedCameraHeight, float zoom){
		
		cameraWidth = updatedCameraWidth;
		cameraHeight = updatedCameraHeight;
				
		Vector2f cameraOffset = new Vector2f (player.getPosition().x -(cameraWidth/2), 
				player.getPosition().y + (cameraHeight/2));	
		
		cameraOffset = clamp(cameraOffset);
		cameraPos = cameraOffset;
	}
	
	public void keyboardPan(float updatedCameraWidth, float updatedCameraHeight){
		cameraWidth = updatedCameraWidth;
		cameraHeight = updatedCameraHeight;
		
		/*
		Vector2f cameraOffset = new Vector2f (cameraPos.x -(cameraWidth/2), 
				cameraPos.y + (cameraHeight/2));	
		*/
		cameraPos = clamp(cameraPos);
	}
	
	public void panLeft(){
		
		cameraPos = new Vector2f(cameraPos.x - panStep, cameraPos.y );
		//cameraPos = clamp(cameraPos);
	}
	
	public void panRight(){
		
		cameraPos = new Vector2f(cameraPos.x + panStep, cameraPos.y );
		//cameraPos = clamp(cameraPos);
	}
	
	public void panUp(){
		cameraPos = new Vector2f(cameraPos.x, cameraPos.y + panStep);
		//cameraPos = clamp(cameraPos);
	}
	
	public void panDown(){
		cameraPos = new Vector2f(cameraPos.x, cameraPos.y - panStep);
		//cameraPos = clamp(cameraPos);
	}
		
	private Vector2f clamp(Vector2f cameraOffset){
		//System.out.println(cameraWidth);
		float tempX;
		if ((cameraOffset.x < -worldLimitX/2)) 
			tempX = -worldLimitX/2; //stops at the left. This works fine
		else if (cameraOffset.x > (worldLimitX/2 - cameraWidth) ) 
			tempX = worldLimitX/2 - cameraWidth; //stops at right
		else 
			tempX = cameraOffset.x;
		
		float tempY;
		if (cameraOffset.y < -worldLimitY/2 + cameraHeight) tempY = -worldLimitY/2 + cameraHeight;
		else if (cameraOffset.y > worldLimitY/2) tempY = worldLimitY/2;
		else tempY = cameraOffset.y;
		
		Vector2f clampedCamera = new Vector2f(tempX, tempY);
		
		return clampedCamera;
	}
	
	
	public Vector2f getCameraPos(){
		return cameraPos;
	}
	
	public float getCameraWidth(){
		return cameraWidth;
	}
	
	public float getCameraHeight(){
		return cameraHeight;
	}

}
