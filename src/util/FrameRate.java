package util;

public class FrameRate {
	
	private String frameRate;
	private long lastTime;
	private long delta;
	private int frameCount;
	
	public void initialize(){
		lastTime = System.currentTimeMillis();
		frameRate = "FPS 0";
	}
	
	public void calculate(){
		long currentTime = System.currentTimeMillis();
		delta += currentTime - lastTime;
		lastTime = currentTime;
		frameCount++;
		if ( delta > 1000 ) { //if one entire second passes
			delta -= 1000;
			frameRate = String.format( "FPS %s", frameCount);
			frameCount = 0;
		}
	}
	
	public String getFrameRate() {
		return frameRate;
	}

}
