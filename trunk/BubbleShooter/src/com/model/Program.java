package com.model;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new JoglApplication(new BubbleShooter(), "Bubble Shooter", 460, 800, false);
	}
}