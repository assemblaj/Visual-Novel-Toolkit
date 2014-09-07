package com.oxca2.cyoat;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class TimeTriggerHandler extends Timer {
	final SceneScreen scene;
	Array<Trigger> things;
	String[][] timeTriggers;
	
/*	
	public TimeTriggerHandler(SceneScreen scene, String[][] triggers){
		this.scene = scene;
		this.timeTriggers = triggers;
		
		initTimer();
	}
*/
	
	public TimeTriggerHandler(SceneScreen scene, Array<Trigger> things){
		this.scene = scene;
		this.things = things;
		initTimer();
	}
	
	public void initTimer(){
		for (int i = 0; i < things.size; i++){
			Trigger thing = things.get(i);
			scheduleTask(new TimeBasedTrigger(thing), thing.time);
		}
	}
	
	public float convertTime(String num) {
		return Float.parseFloat(num);
	}
	
	public class TimeBasedTrigger extends Task {
		String[] data;
		Trigger thing;
		
		public TimeBasedTrigger(String[] data){
			this.data = data;
		}
		
		public TimeBasedTrigger(Trigger thing){
			this.thing = thing;
		}
		
		@Override
		public void run() {
			//scene.runTrigger(data);
			thing.execute();
		}

	}

}
