package com.oxca2.cyoat;

import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.utils.Timer;

public class CompletionObserver implements Observer{
	final SceneScreen scene;
	String data;
	Timer.Task command;
	Timer timer;
	
	public CompletionObserver(final SceneScreen scene, final String data) {
		this.scene = scene;
		this.data = data;
		timer = new Timer();
		command = new Timer.Task() {
			public void run() {
				//scene.removeAnimatedText(data);
			}
		};
	}
	
	@Override
	public void update(Observable o, Object arg) {
		AnimatedText test = (AnimatedText)o;
		System.out.println(test.finished());
		if (test.finished()){
			timer.schedule(command, 1.5f);
		}
	}

}
