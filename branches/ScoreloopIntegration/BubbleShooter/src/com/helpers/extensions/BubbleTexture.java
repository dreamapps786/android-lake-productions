package com.helpers.extensions;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class BubbleTexture extends Texture {

	private BubbleColor color;
	
	public BubbleTexture(FileHandle file, boolean useMipMaps, BubbleColor color) {
		super(file, useMipMaps);
		this.color = color;
	}
	
	public BubbleColor getColor() {
		return color;
	}

	public void setColor(BubbleColor color) {
		this.color = color;
	}

	public enum BubbleColor{
		green, blue, yellow
	}
	
	@Override
	public String toString() {
		return this.color+"";
	}
}


