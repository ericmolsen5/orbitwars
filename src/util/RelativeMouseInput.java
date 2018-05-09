package util;

import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

public class RelativeMouseInput implements 
MouseListener, MouseMotionListener, MouseWheelListener {
	
	private static final int BUTTON_COUNT = 3;
	
	private Point mousePos;
	private Point currentPos;
	private boolean[] mouse;
	private int[] pressed;
	private int notches;

	private int polledNotches;
	
	//this is for the relative mouse parts
	private int dx, dy;
	private Robot robot;
	private Component component;
	private boolean relative; //for bouncing between relative and non-relative
	
	public RelativeMouseInput( Component component) {
		
		this.component = component;
		try {
			robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//instantiate all the fields that we'll need
		mousePos = new Point (0, 0);
		currentPos = new Point (0, 0);
		mouse = new boolean [BUTTON_COUNT]; //should be 3 buttons pretty much all the time
		pressed = new int [BUTTON_COUNT];
	}
	
	public boolean isRelative(){
		return relative;
	}
	
	//our way of seeing which mouse buttons were pressed and for how long
	public synchronized void poll() {
		
		if (isRelative() ) {
			mousePos = new Point (dx, dy); //we calculate this below
		} else {
			mousePos = new Point (currentPos);
		}
		dx = 0;
		dy = 0;
		
		polledNotches = notches;
		notches = 0;
		
		
		for (int i = 0; i < mouse.length; ++i) {
			if (mouse[i]) {
				pressed[i]++;
			} else {
				pressed[i] = 0;
			}
		}
	}
	
	public Point getPosition(){
		return mousePos;
	}
	
	public int getNotches(){
		return notches;
	}
	
	public boolean buttonDown( int button){
		return pressed [ (button - 1) ] > 0;
	}
	
	public boolean buttonDownOnce (int button) {
		return pressed [ (button - 1)] == 1;
	}
	
	public synchronized void mousePressed ( MouseEvent e ) {
		int button = e.getButton() - 1;
		if (button >= 0 && button < mouse.length) {
			mouse [ button ] = true;
		}
	}
	
	public synchronized void mouseReleased ( MouseEvent e ) {
		int button = e.getButton() - 1;
		if (button >= 0 && button < mouse.length) {
			mouse [ button ] = false;
		}
	}
	
	public void mouseClicked (MouseEvent e) {
		//not used...required by interface
	}
	
	public synchronized void mouseMoved (MouseEvent e){
		if (isRelative() ) {
			Point p = e.getPoint();
			Point center = getComponentCenter();
			dx += p.x - center.x;
			dy += p.y - center.y;
			centerMouse();
		} else {
			currentPos = e.getPoint();
		}
	}
	
	public synchronized void mouseWheelMoved (MouseWheelEvent e){
		notches += e.getWheelRotation();
	}
	
/////////////////////////methods for when the mouse enters and leaves/////////////////////////
	
	public synchronized void mouseEntered(MouseEvent e){
		mouseMoved(e);
	}
	
	public synchronized void mouseExited(MouseEvent e){
		mouseMoved(e);
	}
	
	public synchronized void mouseDragged (MouseEvent e){
		mouseMoved(e);
	}
	
	
	/////////////////////////Relative Mouse movement stuff////////////////////////////////////////
	public void setRelative (boolean relative){
		this.relative = relative;
		if (relative) {
			centerMouse();
		}
	}
	
	private void centerMouse() {
		if (robot != null && component.isShowing() ) {
			Point center = getComponentCenter();
			SwingUtilities.convertPointToScreen(center, component);
			robot.mouseMove(center.x, center.y);
		}
	}
	
	private Point getComponentCenter() {
		int w = component.getWidth();
		int h = component. getHeight();
		return new Point (w/2, h/2);
	}
	
}
