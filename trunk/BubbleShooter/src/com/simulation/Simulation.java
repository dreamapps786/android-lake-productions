package com.simulation;

import com.gui.Frame;

public class Simulation {
	
	
	private Frame frame;

	public Simulation(Frame frame) {
		this.frame = frame;
		populate();
	}

	public void update(float delta) {
		pushPlay(delta);
	}

	private void populate() {
		
	}

	public void pushPlay(float delta) {
//		button.update(delta);
	}
}
