package com.oxca2.cyoat;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class TimeTriggerHandler extends Timer {
	
	
	public static void scheduleTriggers(Array<Trigger> triggers){
		for (Trigger trigger: triggers){
			Timer.instance().scheduleTask(new TimeBasedTrigger(trigger), trigger.time);
		}
	}
	
	public static void scheduleTrigger(Trigger trigger){
		Timer.instance().scheduleTask(new TimeBasedTrigger(trigger), trigger.time);
	}
	
	public static class TimeBasedTrigger extends Task {
		Trigger thing;
		

		public TimeBasedTrigger(Trigger thing){
			this.thing = thing;
		}
		
		@Override
		public void run() {
			thing.execute();
		}

	}

}
