package com.model;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new JoglApplication(new BubbleShooter(), "Hello World", 480, 320, false);

	}

}
