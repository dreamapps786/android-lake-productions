package com.helpers.extensions;

import java.util.ArrayList;

import com.simulation.AnimatedBubbleSprite;

@SuppressWarnings("serial")
public class BubbleQueueList extends ArrayList<AnimatedBubbleSprite> {

	public void addBubble(BubbleTexture bubbleTexture) {
		int index = this.size() + 1;
		System.out.println(index);
		AnimatedBubbleSprite bubble = new AnimatedBubbleSprite("upcoming" + index, bubbleTexture,
				480 - 300 - (index * 32), 32, 32, 32, 0, 0, 0, 0);

		this.add(bubble);
	}

	public AnimatedBubbleSprite takeBubble() {
		AnimatedBubbleSprite bubble = this.get(0);
		for (int i = 1; i < this.size(); i++) {
			this.get(i).setPosition(this.get(i).getX() + 32, 32);
		}
		this.remove(0);
		return bubble;
	}
}
