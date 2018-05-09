package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
	
	private boolean[] keys;
	private int[] pressed;
	
	public KeyboardInput() {
		keys = new boolean[256];
		pressed = new int [256];
	}
	
	public synchronized void poll() {
		for (int i = 0; i < keys.length; ++i){
			if (keys[i]){
				pressed[i]++;
			} else {
				pressed[i] = 0;
			}
		}
	}
	
	public boolean keyDown(int keyCode){
		return pressed [keyCode] > 0;
	}
	
	public boolean keyDownOnce (int keyCode){
		return pressed [keyCode] == 1;
	}

	public synchronized void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode >= 0 && keyCode < keys.length ) {
			keys [ keyCode ] = true;
		}
	}

	public synchronized void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if ( keyCode >= 0 && keyCode < keys.length ) {
			keys [ keyCode ] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// We're not using this one yet. We may if we want to type free text into the game
		
	}
	
	

}
