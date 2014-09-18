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
import com.oxca2.cyoat.GameChoiceMenu.GameChoice;

public class SceneScreen extends Observable implements Screen, Transitionable{
	final Main game;
	final SceneData data;
	 
	Music bgMusic;
	Sound sfx;
	
	Array<ObjectMap<String, DrawingCommand>> layers;
	ObjectMap<String, AudioCommand> audio;
	ObjectMap<String, Trigger> triggerMap;
	Array<Trigger> triggerList;
	Trigger sceneStarter;
	
	// map for choices made in the game 
	// that are to be checked later on. ;
	ObjectMap<String, Integer> choiceMap;

	public SceneScreen(Main game, String scene) {
		this.game = game;
		// Get the Scene data from the jSon file
		data = SceneMaker.getData(scene);
				
		// Get the triggers from the jSon file 
		// and give each trigger a reference to
		// Main game and the Scene
		triggerList = data.triggerList;
		initTriggers(); 
		
		// Map those triggers to their triggerIDs inside of an 
		// ObjectMap
		triggerMap = new ObjectMap<String, Trigger>();
		initTriggerMap();
		
		choiceMap = new ObjectMap<String, Integer>();
		
		// Fill the layer array with empty objectMaps based on 
		// how many layers were specified in the file. 
		initLayers(); 
		
		// Create the Map for audio objects. 
		audio = new ObjectMap<String, AudioCommand>();
		
		startScene();
	}
	
	public ObjectMap<String, Trigger> getTriggers() {
		return triggerMap;
	}
	
	public Array<ObjectMap<String, DrawingCommand>> getLayers() {
		return layers;
	}
	
	private void startScene() {
		sceneStarter = data.sceneStarter;
		sceneStarter.setGame(game);
		sceneStarter.setScene(this);
		sceneStarter.execute();		
	}
	
	private void initLayers() {
		layers = new Array<ObjectMap<String, DrawingCommand>>();
		for (int i = 0; i < data.startLayers; i++){
			layers.add(new ObjectMap<String, DrawingCommand>());
		}
	}
	
	private void initTriggers() {
		for (Trigger trigger : triggerList ){
			trigger.setGame(game);
			trigger.setScene(this);
		}		
	}
	
	private void initTriggerMap(){
		for (Trigger trigger : triggerList){
			triggerMap.put(trigger.triggerID, trigger);
		}
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update();
		game.batch.begin();
		draw(game.batch);
		game.batch.end();
		
		setChanged();
		notifyObservers();
	}
	
	public void update() {
		
	}
	
	public void draw(SpriteBatch batch) {
		Iterator<ObjectMap<String, DrawingCommand>> layerIter = layers.iterator();
		Iterator<ObjectMap.Entry<String, DrawingCommand>> commandIter;
		
		while(layerIter.hasNext()){
			commandIter = layerIter.next().iterator();
			
			while (commandIter.hasNext()){
				commandIter.next().value.draw(batch);
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
	public void addCommandToLayer(DrawingCommand object){
		// - 1 For array notation
		layers.get(object.layer).put(object.id, object);
	}
	
	public void removeCommandFromLayer(int layer, String id){
		layers.get(layer).remove(id);
	}
	
	public void addAudio(AudioCommand command) {
		audio.put(command.id, command);
	}
	
	public AudioCommand getAudio(String id){
		return audio.get(id);
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

	public void setBackground(int layer, String id, Texture texture){ 
		resetTriggerTexture(layer, id, texture);
	}

	public void removeBackground(Trigger object) {
		//newLayers.get(object.layer).get(object.id).texture.dispose();
		//removeCommandFromLayer(object.layer, object.id);
		removeTriggerWidthTexture(object);
	}
	
	public void setTextbox(
		int layer, String id, Texture newTexture,
		int x, int y, int width, int height)
	{
		DrawingCommand command  = resetTriggerTexture(layer, id, newTexture);
		command.setBounds(x, y, width, height);
	}
	
	public void removeTriggerWidthTexture(Trigger object) {
		layers.get(object.layer).get(object.dataID).texture.dispose();
		removeCommandFromLayer(object.layer, object.dataID);
	}
	
	public DrawingCommand resetTriggerTexture(int layer, String id, Texture newTexture) {
		DrawingCommand command = layers.get(layer).get(id);
		command.texture.dispose();
		command.sprite = new Sprite(newTexture);
		return command;
	}
	
	public void removeTextbox(Trigger object) {
		removeTriggerWidthTexture(object);
	}
	
	public void removeAnimatedText(Trigger object) {
		DrawAnimatedText command = 
				(DrawAnimatedText) layers.get(object.layer).get(object.dataID);	
		command.clearObservers();
		removeCommandFromLayer(object.layer, object.dataID);
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

	
	public void removeStaticText(Trigger object){
		removeCommandFromLayer(object.layer, object.dataID);
	}
	
	public void removeAnimatedTextOnCompletion(String trigger) {
		//animatedTextMap.get(args[1]).addObserver(new CompletionObserver(this, trigger));
	}
	
	// 11 is which menu
	public void testMenu(String i) {
		choiceMap.put("c1", 0);
		if (choiceMap.get("c1") == 1) {
			//ddBackground("1 bg0 pic3.jpg");
		}else {
			//addBackground("1 bg0 ai4.jpg");
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
	
	public void disposeAudio() {
		Iterator<ObjectMap.Entry<String, AudioCommand>> audioIter = audio.iterator();
		while (audioIter.hasNext()) {
			audioIter.next().value.dispose();
		}
	}
	
	public void disposeGraphics() {
		Iterator<ObjectMap<String, DrawingCommand>> layerIter = layers.iterator();
		Iterator<ObjectMap.Entry<String, DrawingCommand>> commandIter;
		
		while(layerIter.hasNext()){
			commandIter = layerIter.next().iterator();
			
			while (commandIter.hasNext()){
				commandIter.next().value.dispose();
			} 
		}			
	}
	
	@Override
	public void dispose() {
		disposeAudio();
		disposeGraphics();		
	}
	
}
