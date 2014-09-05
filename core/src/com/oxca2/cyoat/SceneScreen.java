package com.oxca2.cyoat;

import java.util.Iterator;
import java.util.Observable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class SceneScreen extends Observable implements Screen, Transitionable{
	final Main game;
	final SceneData data;
	 
	String[] startArray;
	Texture background;
	Texture textbox;
	AnimatedText animator;
	String[] staticText;
	Music bgMusic;
	Sound sfx;
	
	// Current drawing Array;
	Array<String> currentDrawing;
	Array<ObjectMap<String, String>> layers; // drawing layers 
	ObjectMap<String, String> textAssets; // assets for the text
	ObjectMap<String, Texture> textureAssets; // assets for the textures	
	ObjectMap<String, LineTriggerObserver> lineObserverMap;
	ObjectMap<String, AnimatedText> animatedTextMap;
	//ObjectMap<String, int>
	//Array<int>
	public SceneScreen(Main game, String scene) {
		this.game = game;
		data = SceneMaker.getData(scene);
		
		
		currentDrawing = new Array<String>();
		textureAssets = new ObjectMap<String, Texture>();
		textAssets = new ObjectMap<String, String>();
		lineObserverMap = new ObjectMap<String, LineTriggerObserver>();
		animatedTextMap = new ObjectMap<String, AnimatedText>();
		
		layers = new Array<ObjectMap<String, String>>();
		for (int i = 0; i < data.startLayers; i++)
			layers.add(new ObjectMap<String, String>());
		
		/*
		String[] args;
		
		for(int i = 0; i < data.startArray.length; i++){
			args = data.startArray[i].split(" ");
			
			if (args[0].equals("drawBackground")){	
				//background = new Texture(Gdx.files.internal(data.bgPath));
				textureAssets.put(args[2], new Texture(Gdx.files.internal(args[3])));
				layers.get(Integer.parseInt(args[1])- 1).put(args[2], data.startArray[i]);
			}else if (args[0].equals("drawTextbox")){
				//textbox = new Texture(Gdx.files.internal(data.tbPath));
				textureAssets.put(args[2], new Texture(Gdx.files.internal(args[3])));
				layers.get(Integer.parseInt(args[1])- 1).put(args[2], data.startArray[i]);
			}else if (args[0].equals("drawAnimatedText")){
				animator = new AnimatedText(
					game, AnimatedText.join(data.animatedText[Integer.parseInt(args[3])], " "), args[4],
					Integer.parseInt(args[5]), Integer.parseInt(args[6]), 
					Integer.parseInt(args[7]), Integer.parseInt(args[8]), Float.parseFloat(args[9]));
				layers.get(Integer.parseInt(args[1])- 1).put(args[2], data.startArray[i]);
				
				if (data.lineTriggers != null){
					if (data.lineTriggers[Integer.parseInt(args[10])] != null){
					   lineObserverMap.put(args[2], new LineTriggerObserver(this, data.lineTriggers[Integer.parseInt(args[10])]));
					   addObserver(lineObserverMap.get(args[2]));
					}
				}
			}else if (args[0].equals("drawStaticText")){
				textAssets.put(args[2], data.staticText[Integer.parseInt(args[3])]);
				layers.get(Integer.parseInt(args[1])- 1).put(args[2], data.startArray[i]);
			}else if (data.startArray[i].equals("playMusic")){
				bgMusic = Gdx.audio.newMusic(Gdx.files.internal(data.bgmPath));
				bgMusic.setLooping(true);
				bgMusic.play();
			}else if (data.startArray[i].equals("playSfx")){
				sfx = Gdx.audio.newSound(Gdx.files.internal(data.sfxPath));
				sfx.play();
			}
		}
		*/
		/*
		for(int i = 0; i < data.startArray.length; i++){
			if (data.startArray[i].equals("drawBackground")){	
				background = new Texture(Gdx.files.internal(data.bgPath));
				currentDrawing.add("drawBackground");
			}else if (data.startArray[i].equals("drawTextbox")){
				textbox = new Texture(Gdx.files.internal(data.tbPath));
				currentDrawing.add("drawTextbox");
			}else if (data.startArray[i].equals("drawAnimatedText")){
				animator = new AnimatedText(
					game, AnimatedText.join(data.animatedText, " "),
					data.atX, data.atY, data.lineLength, data.maxPageLines, data.speed);
				currentDrawing.add("drawAnimatedText");
				if (data.lineTriggers != null)
					addObserver(new LineTriggerObserver(this, data.lineTriggers));
			}else if (data.startArray[i].equals("drawStaticText")){
				currentDrawing.add("drawStaticText");
			}else if (data.startArray[i].equals("playMusic")){
				bgMusic = Gdx.audio.newMusic(Gdx.files.internal(data.bgmPath));
				bgMusic.setLooping(true);
				bgMusic.play();
			}else if (data.startArray[i].equals("playSfx")){
				sfx = Gdx.audio.newSound(Gdx.files.internal(data.sfxPath));
				sfx.play();
			}
		}*/
		
		if (data.timeTriggers != null){
			TimeTriggerHandler timer = new TimeTriggerHandler(this, data.timeTriggers);
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		draw(game.batch);
		game.batch.end();
		bgmUpdate();
		setChanged();
		notifyObservers();
	}
	
	public void draw(SpriteBatch batch) {
		/*
		Iterator<String> iter = currentDrawing.iterator();
		String command;
		
		while (iter.hasNext()){
			command = iter.next();	
			if (command.equals("drawBackground")){
				batch.draw(background, 0, 0, HellStories.WIDTH, HellStories.HEIGHT);
			}else if (command.equals("drawTextbox")){
				batch.draw(textbox,
					data.tbX, data.tbY,data.tbW, data.tbH);
			}else if (command.equals("drawAnimatedText")){
				animator.draw(batch);
			}else if (command.equals("drawStaticText")) {
				game.fonts.get(data.stFont).draw(batch, data.staticText[0], data.stX, data.stY);
			}
		}
		*/
		
		Iterator<ObjectMap<String, String>> layerIter = layers.iterator();
		Iterator<ObjectMap.Entry<String, String>> commandIter;
		String command;
		String[] args;
		/*
		while(layerIter.hasNext()){
			commandIter = layerIter.next().iterator();
			while (commandIter.hasNext()){
				command = commandIter.next().value;
				System.out.println("VALUE IN DRAWING LOOP: " + command);
				if (command.equals("drawBackground")){
					batch.draw(background, 0, 0, HellStories.WIDTH, HellStories.HEIGHT);
				}else if (command.equals("drawTextbox")){
					batch.draw(textbox,
						data.tbX, data.tbY,data.tbW, data.tbH);
				}else if (command.equals("drawAnimatedText")){
					animator.draw(batch);
				}else if (command.equals("drawStaticText")) {
					game.fonts.get(data.stFont).draw(batch, data.staticText[0], data.stX, data.stY);
				}
			}
		}*/
		
		while(layerIter.hasNext()){
			commandIter = layerIter.next().iterator();
			
			while (commandIter.hasNext()){
				command = commandIter.next().value;
				args = command.split(" ");
				
				//System.out.println("VALUE IN DRAWING LOOP: " + command);
				if (args[0].equals("drawBackground")){
					batch.draw(textureAssets.get(args[2]), 0, 0, Main.WIDTH, Main.HEIGHT);
				}else if (args[0].equals("drawTextbox")){
					batch.draw(textureAssets.get(args[2]),
						Integer.parseInt(args[4]), Integer.parseInt(args[5]),
						Integer.parseInt(args[6]), Integer.parseInt(args[7]));
				}else if (args[0].equals("drawAnimatedText")){
					animatedTextMap.get(args[2]).draw(batch);
				}else if (args[0].equals("drawStaticText")) {
					game.fonts.get(args[4]).draw(batch, textAssets.get(args[2]),
							Integer.parseInt(args[5]), Integer.parseInt(args[6]));
				}
			} 
		}	
	}
	
	public void makeScreenCopy(
		FrameBuffer buffer, SpriteBatch batch)
	{
		batch.begin();
		draw(batch);
		batch.end();
	}
	
	// Triggers 
	
	public String[] addCommandToLayer(String trigger, String command) {
		System.out.println("command: " + trigger);
		String[] args = trigger.split(" ");
		layers.get(Integer.parseInt(args[0])- 1).put(args[1], command + trigger);
		return args;
	}
	
	public String[] removeCommandFromLayer(String trigger) {
		String[] args = trigger.split(" ");
		layers.get(Integer.parseInt(args[0])- 1).remove(args[1]);
		return args;
	}
	
	public void playSoundEffect(String sfxPath) {
		sfx = Gdx.audio.newSound(Gdx.files.internal(sfxPath));
		sfx.play();		
		sfx.dispose();
	}
	
	public void playMusic(String bgmPath){
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal(bgmPath));
		bgMusic.setLooping(true);
		bgMusic.play();		
	}
	
	public void stopMusic() {
		bgMusic.stop();
		bgMusic.dispose();
	}
	
	public void setBackground(String bgPath) {
		/*
		background.dispose();
		background = new Texture(Gdx.files.internal(bgPath));
		*/
		
		String[] args = bgPath.split(" ");
	
		textureAssets.get(args[1]).dispose();
		textureAssets.put(args[1], new Texture(Gdx.files.internal(args[0]))); 
	}
	
	public void removeBackground(String command){
		//currentDrawing.removeIndex(0);
		String[] args = removeCommandFromLayer(command);
		textureAssets.get(args[1]).dispose();
	}
	
	public void addBackground(String trigger){
		//currentDrawing.insert(0, "drawBackground");
		String[] args = addCommandToLayer(trigger, "drawBackground ");
		textureAssets.put(args[1], new Texture(Gdx.files.internal(args[2])));
	}
	

	public void addTextbox(String command){
		//currentDrawing.insert(1, "drawTextbox");		
		String[] args = addCommandToLayer(command, "drawTextbox ");
		textureAssets.put(args[1], new Texture(Gdx.files.internal(args[2])));		
	}
	
	public void removeTextbox(String command){
		String[] args = removeCommandFromLayer(command);
		textureAssets.get(args[1]).dispose();
	}
	
	public void setTextbox(String tbPath) {
		textbox.dispose();
		textbox = new Texture(Gdx.files.internal(tbPath));
	}
	
	public void addAnimatedText(String command) {
		//currentDrawing.insert(2, "drawAnimatedText");
		String[] args = command.split(" ");
		addCommandToLayer(command, "drawAnimatedText ");
		animatedTextMap.put(args[1], new AnimatedText(
				game, AnimatedText.join(data.animatedText[Integer.parseInt(args[2])], " "), args[3],
				Integer.parseInt(args[4]), Integer.parseInt(args[5]),
				Integer.parseInt(args[6]), Integer.parseInt(args[7]), Float.parseFloat(args[8])));
		if (data.lineTriggers != null){ 
			if (data.lineTriggers[Integer.parseInt(args[9])] != null)
				lineObserverMap.put(args[1], new LineTriggerObserver(this, data.lineTriggers[Integer.parseInt(args[9])]));
				addObserver(lineObserverMap.get(args[2]));				
				addObserver(new LineTriggerObserver(this, data.lineTriggers[Integer.parseInt(args[9])]));
		}
	}
	
	public void removeAnimatedText(String command) {
		//currentDrawing.removeIndex(2);
		String[] args = removeCommandFromLayer(command);
		deleteObserver(lineObserverMap.get(args[1]));
	}
	
	public void fadeToNextScreen(String args) {
		if (bgMusic != null)
			bgMusic.stop();
		Sprite currentScreen = new Sprite(ScreenUtils.getFrameBufferTexture());
		String[] data = args.split(" ");
		FadeTransition transition = new FadeTransition(game, currentScreen,
				new SceneScreen(game, data[0]), Float.parseFloat(data[1]), Float.parseFloat(data[2]) );
		game.setScreen(transition);
	}
	
	public void bgmUpdate() {
		if (fadeMusic){
			fadeDown();
		}
	}
	
	public void fadeDown() {
		if (bgMusic.getVolume() - .01f >= 0f){
			bgMusic.setVolume(bgMusic.getVolume() - .01f);
			System.out.println("!@$%%%^^^^ ^&***I'm in fadeDown in SceneScreen");
		}else {
			bgMusic.stop();
			fadeMusic = false;
		}
		System.out.println("MUSIC VOLUME: " + bgMusic.getVolume());
	}
	
	boolean fadeMusic;
	long fadeMusicMillis;
	long fadeOutMusicTime;
	public void fadeMusic(String time) {
		fadeMusic = true;
		fadeMusicMillis = TimeUtils.millis();
		fadeOutMusicTime = Long.parseLong(time) * 1000l;
	}
	
	boolean fadeOutBG, fadeInBG;
	long fadeBGMillis, fadeOutBGTime, fadeInBGTime;
	float interpCoef = 0f;
	/*
	public void backgroundUpdate() {
		if (fadeOutBG){
			fadeOutBackground();
		}else if (fadeInBG){
			fadeInBackground();
		}
	}
	
	public void fadeInBackground() {
		
	}
	
	public void fadeOutBackground() {
		if (TimeUtils.millis() - fadeBGMillis < fadeOutBGTime){ 
			interpCoef = (TimeUtils.millis() - fadeBGMillis) / fadeOutBGTime;;
			background.setColor(Color.WHITE.cpy().lerp(Color.BLACK, interpCoef));
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
	}
	
	
	public void fadeToNextBackground(String args) {
		b
		String[] data = args.split(" ");
		String bgPath = data[1];
		
		fadeOutBG = true;
		fadeBGMillis = TimeUtils.millis();
		fadeOutBGTime = Long.parseLong(data[1]) * 1000l;
		fadeInBGTime  = Long.parseLong(data[2]) * 1000l;		
	}*/
	
	public void addStaticText(String command) {
		//currentDrawing.add("drawStaticText");
		String[] args = addCommandToLayer(command, "drawStaticText ");
		textAssets.put(args[1], data.staticText[Integer.parseInt(args[2])]);
	}
	
	public void removeStaticText(String trigger) {
		removeCommandFromLayer(trigger);
	}
	
	public void removeAnimatedTextOnCompletion(String trigger) {
		String[] args = trigger.split(" ");
		System.out.println(" arg0: " + args[0] + " arg1: " + args[1]);
		animatedTextMap.get(args[1]).addObserver(new CompletionObserver(this, trigger));
	}
	
	// Use reflection to match trigger names in the file with their
	// respective methods in the class. 
	public void runTrigger(String[] trigger) {
		String command = trigger[1].split(" ")[0];
		
		Method triggerMethod;
		
		try {
			// It worked
			//Class<?> cls = Class.forName("com.oxca2.cyoat.SceneScreen");
			//triggerMethod = ClassReflection.getDeclaredMethod(cls, trigger[1], String.class);		
			triggerMethod = ClassReflection.getDeclaredMethod(SceneScreen.class, trigger[1], String.class);
			
			// some of the trigger methods have no params
			if (triggerMethod != null) 
				triggerMethod.invoke(this, trigger[2]);
			else {
				triggerMethod = ClassReflection.getDeclaredMethod(SceneScreen.class, trigger[1]);
				triggerMethod.invoke(this);
			}
		}catch (ReflectionException e){
			e.printStackTrace();
		}
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
	
	public void diposeTextures() {
		Iterator<ObjectMap.Entry<String, Texture>> textureIter = textureAssets.iterator();
		while (textureIter.hasNext()){
			textureIter.next().value.dispose();
		}
	}
	
	@Override
	public void dispose() {
		diposeTextures();
	}
	
}
