package com.mindhash.MindhashApp;

import java.util.HashMap;

public class ObjTypeNum {
	public static HashMap<String, Integer> getObjTypeNumMap() {
		HashMap<String, Integer> map = new HashMap<>();
		map.put("two-wheeler", 0);
		map.put("pedestrian", 0);
		map.put("vehicle", 0);
		return map;
	} 
}
