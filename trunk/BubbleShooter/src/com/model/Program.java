package com.model;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class Program {
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new JoglApplication(new BubbleShooter(), "Bubble Shooter", WIDTH, HEIGHT, false);
	}
}