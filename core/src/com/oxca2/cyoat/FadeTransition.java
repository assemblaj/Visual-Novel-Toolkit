package com.oxca2.cyoat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.TimeUtils;

public class FadeTransition implements Screen{
	final Main game;
	OrthographicCamera camera;
	
	Transitionable nextScreen; 
	
	long fadeNanos;
	boolean fadeOut = true;
	Sprite preScreen;
	Sprite toScreen;
	
	long FADE_DUR_NANOS = 900000000;
	float interpCoef = 0f;
	boolean fadeIn = false;
	
	float inSeconds;
	float outSeconds;
	long fadeMillis;
	float totalMillis;
	final int MILLIS_IN_SECS = 1000;
	float fadeInTime;
	float fadeOutTime;
	
	
	public FadeTransition(
		Main game, Sprite preScreen, 
		Transitionable nextScreen, float inSecs, float outSecs) 
	{
		this.game = game;
		this.preScreen = preScreen;
		this.nextScreen = nextScreen;
		
		this.inSeconds = inSecs;
		this.fadeInTime = inSeconds * MILLIS_IN_SECS;
		
		this.outSeconds = outSecs;
		this.fadeOutTime = outSeconds * MILLIS_IN_SECS;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		initFade();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		if (fadeOut){
			fadeOut();
		}else if (fadeIn){
			fadeIn();
		}
		
		game.batch.end();
	}


	public void initFade() {
		fadeNanos = TimeUtils.nanoTime();
		fadeMillis = TimeUtils.millis();
		
		fadeOut = true;
		//screen = new Sprite(ScreenUtils.getFrameBufferTexture());
	}

	public void fadeOut() {	
		System.out.println("In fadeOut");
		if (TimeUtils.millis() - fadeMillis < fadeOutTime){ // changed fadeInTime to fadeOutTime (Tue Aug5)
			//interpCoef = (float)(TimeUtils.nanoTime() - fadeNanos) / (float)FADE_DUR_NANOS;
			interpCoef = (TimeUtils.millis() - fadeMillis) / fadeOutTime;
			System.out.println(TimeUtils.nanoTime() - fadeNanos);
			System.out.println(interpCoef);
			System.out.println(preScreen.getColor().cpy().lerp(Color.BLACK, interpCoef));
			preScreen.setColor(Color.WHITE.cpy().lerp(Color.BLACK, interpCoef));
		}else {
			System.out.println("I'm changing");
			fadeOut = false;
			fadeIn = true;
			interpCoef = 0f;
			// create new picture 
			makeNewScreen();
			fadeNanos = TimeUtils.nanoTime();
			fadeMillis = TimeUtils.millis();
			//game.setScreen(new WinScreen(game));
			// need to make sure the screen is set back to white
		}
		preScreen.setPosition(0, 0);
		preScreen.draw(game.batch);
	}

	public void fadeIn() {
		System.out.println("In fadeIn");
		if (TimeUtils.millis() - fadeMillis < fadeInTime){ // changed fadeOutTime to fadeInTime (Tue Aug5)
			System.out.println("I'm here");
			interpCoef = (TimeUtils.millis() - fadeMillis) / fadeInTime;
			System.out.println(TimeUtils.nanoTime() - fadeNanos);
			System.out.println("interpCoef: " + interpCoef);
			System.out.println(toScreen.getColor().cpy().lerp(Color.WHITE, interpCoef));
			toScreen.setColor(toScreen.getColor().cpy().lerp(Color.WHITE, interpCoef));
		}else {
			game.setScreen(nextScreen);
			dispose();
		}
		toScreen.setPosition(0, 0);
		toScreen.draw(game.batch);
		
	}
	
	public void makeNewScreen() {
		FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, 800, 480, false);
		SpriteBatch fbBatch = new SpriteBatch();
				
		buffer.begin();
		nextScreen.makeScreenCopy(buffer, fbBatch);
		buffer.end();
 
		toScreen = new Sprite(buffer.getColorBufferTexture());
		toScreen.flip(false, true);
		toScreen.setColor(Color.BLACK);
		
		System.out.println("color: " + toScreen.getColor());
	}
	
	
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
