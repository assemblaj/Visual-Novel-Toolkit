package com.oxca2.cyoat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

public class SceneMaker {
	final static Json parser = new Json();
	
	public static SceneData getData(String scene){
		return  parser.fromJson(
				SceneData.class, 
				Gdx.files.internal(scene + ".json"));
	}
	
}
