package com.oxca2.cyoat;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public interface Transitionable extends Screen {
	
	public void makeScreenCopy(FrameBuffer buffer, SpriteBatch batch);
}
