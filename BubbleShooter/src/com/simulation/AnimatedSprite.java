package com.simulation;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite extends Sprite {
	protected int mRowCount;
	protected int mColumnCount;
	protected float mAnimationRateInSeconds = 0.02f;
	protected boolean mIsPlay = false;
	protected int mCurRow;
	protected int mCurColumn;
	protected int mStartX;
	protected int mStartY;
	protected float mFrameTimeCounter;
	private List<TextureRegion> frameRegions;

	public AnimatedSprite(final Texture pTexture, final int pSrcX,
			final int pScrY, final int pTileWidth, final int pTileHeight,
			final int pRowCount, final int pColumnCount) {
		super(pTexture, pSrcX, pScrY, pTileWidth, pTileHeight);
		frameRegions = new ArrayList<TextureRegion>();
		mRowCount = pRowCount;
		mColumnCount = pColumnCount;
		mStartX = pSrcX;
		mStartY = pScrY;
		addFrames(pTexture);
	}
	
	public void addFrames(Texture texture){
		for (int column = 0; column < mColumnCount; column++) {
			for (int row = 0; row < mRowCount; row++) {
				frameRegions.add(new TextureRegion(texture, mStartX + column * (int) getWidth(), mStartY
						+ row * (int) getHeight(), (int) getWidth(),
						(int) getHeight()));
			}			
		}	
	}

	public void setAnimationRate(final float pAnimationRateInSeconds) {
		mAnimationRateInSeconds = pAnimationRateInSeconds;
	}

	public void play() {
		mIsPlay = true;
		mFrameTimeCounter = 0;
	}

	public void pause() {
		mIsPlay = false;
		mFrameTimeCounter = 0;
	}
	
	public void goToFrame(int pFrameRow, int pFrameColumn){
		setRegion(frameRegions.get(pFrameColumn));
		if (pFrameColumn == mRowCount-1) {
			pause();
		}
	}

	public void update(final float pSecondsElapsed) {
		if (mIsPlay) {
			mFrameTimeCounter += pSecondsElapsed;
			if (mFrameTimeCounter > mAnimationRateInSeconds) {
				mFrameTimeCounter = 0;
				++mCurColumn;
				if (mCurColumn == mColumnCount) {
					mCurRow = ++mCurRow % mRowCount;
				}
				mCurColumn = mCurColumn % mColumnCount;
				goToFrame(mCurColumn, mCurRow);
			}
		}
	}
}