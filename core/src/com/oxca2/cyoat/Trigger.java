package com.oxca2.cyoat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Trigger {
	int type;
	String id;
	String name;
	int layer;
	int time;
	int line;
	
	
	SceneScreen scene;
	Main game;
	
	abstract void execute();
	
	public void setScene(SceneScreen scene){
		this.scene = scene;
	}
	
	public void setGame(Main game){
		this.game = game;
	}
}

abstract class DrawingCommand {
	SpriteBatch batch;
	Sprite sprite;
	Texture texture;
	String id;
	int layer;
	
	private int x, y;
	private int width, height;
	
	abstract void draw(SpriteBatch batch);
	
	public void setBounds(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}


class AddNewBackground extends Trigger {
	String bgPath;
	
	@Override
	void execute() {
		scene.addCommandToLayer(new DrawBackground(layer, id, bgPath));
	}
}

class SetBackground extends Trigger {
	String bgPath;
	
	@Override
	void execute() {
		scene.setBackground(layer, id, new Texture(Gdx.files.internal(bgPath)));
	}	
}


class RemoveBackground extends Trigger {
	@Override
	void execute() {
		scene.removeBackground(this);
	}
}

class DrawBackground extends DrawingCommand {
	
	public DrawBackground(int layer, String id, String bgPath) {
		this.layer = layer;
		this.id = id;
		texture = new Texture(Gdx.files.internal(bgPath));
		sprite  = new Sprite(texture);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(sprite, 0, 0, Main.WIDTH, Main.HEIGHT);
	}
}

class AddTextbox extends Trigger {
	private String bgPath;
	private int x, y;
	private int width, height;
	
	@Override
	void execute() {
		scene.addCommandToLayer(
			new DrawTextbox(layer, id, bgPath,
			x, y, width, height));
	}
}

class SetTextbox extends Trigger {
	private String bgPath;
	private int x, y;
	private int width, height;
	
	@Override
	void execute() {
		scene.setTextbox(
			layer, id, new Texture(Gdx.files.internal(bgPath)), 
			x, y, width, height);
	}
	
}

class RemoveTextbox extends Trigger {

	@Override
	void execute() {
		scene.removeTextbox(this);
	}
	
}

class DrawTextbox extends DrawingCommand {
	private int x, y;
	private int width, height;
	
	public DrawTextbox(
		int layer, String id, String bgPath, 
		int x, int y, int width, int height) 
	{
		this.layer = layer;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		texture = new Texture(Gdx.files.internal(bgPath));
		sprite  = new Sprite(texture);		
	}
	
	@Override
	void draw(SpriteBatch batch) {
		batch.draw(sprite, x, y, width, height);
	}
}

class AddAnimatedText extends Trigger {
	String[] textArray;
	Trigger[] lineTriggers;
	String font;
	
	int x,  y; 
	int lineLength; 
	int maxLines;
	float speed;

	@Override
	void execute() {
		scene.addCommandToLayer(
				new DrawAnimatedText(layer, id, game,
				AnimatedText.join(textArray, " "), lineTriggers,
				font, x, y, lineLength, maxLines, speed));		
	}
}

class RemoveAnimatedText extends Trigger {
	@Override
	void execute() {
		scene.removeAnimatedText(this);
	}
}

class DrawAnimatedText extends DrawingCommand {
	AnimatedText animText;
	LineTriggerObserver observer;
	
	public DrawAnimatedText(
		int layer, String id, Main game,
		String text, Trigger[] lineTriggers,
		String font, int x, int y, int lineLength,
		int maxLine, float speed)
	{
		this.layer = layer;
		this.id = id;
		
		animText = new AnimatedText(
			game, text, font, 
			x, y, lineLength, 
			maxLine, speed			
		);
		
		if (lineTriggers != null){
			observer = new LineTriggerObserver(lineTriggers);
			animText.addObserver(observer);
		}
	}
	
	@Override
	void draw(SpriteBatch batch) {
		animText.draw(batch);
	}
	
	public void clearObservers(){
		animText.deleteObservers();
	}
}

class AddStaticText extends Trigger {
	String text;
	int x, y;
	String font;
	
	@Override
	void execute() {
		scene.addCommandToLayer(
			new DrawStaticText(
			game, layer, id, text, x, y, font));
	}
	
}

class RemoveStaticText extends Trigger {

	@Override
	void execute() {
		scene.removeStaticText(this);
	}
	
}

class DrawStaticText extends DrawingCommand {
	String text;
	int x, y;
	String font;
	BitmapFont bFont;
	
	public DrawStaticText(
		Main game,  int layer, String id,
		String text, int x, int y, String font)
	{
		this.layer = layer;
		this.id = id;
		
		this.text = text;
		this.x = x;
		this.y = y;
		this.font = font;
		
		bFont = game.fonts.get(font);
	}
	
	
	@Override
	void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		bFont.draw(batch, text, x, y);
	}
	
}

class AddMenu extends Trigger {

	@Override
	void execute() {
				
	}
	
	
}

class RemoveMenu extends Trigger {

	@Override
	void execute() {
		
		
	}
	
}

class DrawMenu extends DrawingCommand {

	@Override
	void draw(SpriteBatch batch) {
				
	}
	
}
