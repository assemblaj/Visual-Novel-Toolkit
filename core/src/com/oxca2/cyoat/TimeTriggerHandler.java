package com.oxca2.cyoat;

import com.badlogic.gdx.utils.Timer;

public class TimeTriggerHandler extends Timer {
	final SceneScreen scene;
	
	String[][] timeTriggers;
	
	public TimeTriggerHandler(SceneScreen scene, String[][] triggers){
		this.scene = scene;
		this.timeTriggers = triggers;
		
		initTimer();
	}
	
	public void initTimer(){
		for (int i = 0; i < timeTriggers.length; i++){
			scheduleTask(new TimeBasedTrigger(timeTriggers[i]), convertTime(timeTriggers[i][0]));
		}
	}
	
	public float convertTime(String num) {
		return Float.parseFloat(num);
	}
	
	public class TimeBasedTrigger extends Task {
		String[] data;
		
		public TimeBasedTrigger(String[] data){
			this.data = data;
		}
		
		@Override
		public void run() {
			scene.runTrigger(data);
		}

	}

}
