package com.oxca2.cyoat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ObjectMap;


public class Main extends Game {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 480;	
	
	SpriteBatch batch;
	String[] fontArray = {"vs_f5", "libz4", "gothic2"};
	ObjectMap<String, BitmapFont> fonts;
	BitmapFont font;
	OrthographicCamera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		initFonts();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Main.WIDTH, Main.HEIGHT);
		//this.setScreen(new SceneScreen(this, "test1"));
		this.setScreen(new MainMenu(this));
	}
	
	public void initFonts() {
		fonts = new ObjectMap<String, BitmapFont>();
		for (int i = 0; i < fontArray.length; i++){
			fonts.put(fontArray[i], new BitmapFont(
				  Gdx.files.internal("fonts/" + fontArray[i] + ".fnt")));
		}
		
	}
	
	@Override
	public void render () {
		super.render();
	}
	
	public void dispose() {
		batch.dispose();
		
	}
}
