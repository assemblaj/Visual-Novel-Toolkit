package com.oxca2.cyoat;

import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

/**
 * The completion observer monitors whether or not 
 * a sequence of any given events is completed and
 * then performs a task on completion. 
 * 
 * The completion observer may or may not need a delay
 * as some triggers should probably a have pause before
 * being executed.
 * @author 0xCA2
 *
 */
public abstract class CompletionObserver implements Observer{
	SceneScreen scene;
	Array<Trigger> triggers;
	Timer.Task trigger;
	float delay = 1.5f;
	
	public CompletionObserver(SceneScreen scene, Array<Trigger> triggers, float delay) {
		this.scene = scene;
		this.triggers = triggers;
		this.delay = delay;
		
		trigger = new Timer.Task() {
			public void run(){
				runOnCompletion();
			}
		};
	}
	
	@Override
	public void update(Observable o, Object arg) {

	}
	
	public void runOnCompletion() {
		for (Trigger trigger : triggers)
			trigger.execute();
	}
}

class AnimatedTextCompletionObserver extends CompletionObserver {

	public AnimatedTextCompletionObserver(
		SceneScreen scene, Array<Trigger> triggers, float delay) 
	{
		super(scene, triggers, delay);
	}
	
	
	@Override
	public void update(Observable animText, Object arg){
		AnimatedText object = (AnimatedText) animText;
		if (object.finished()){
			Timer.schedule(trigger, delay);
		}
	}
}
