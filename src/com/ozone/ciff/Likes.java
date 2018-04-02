package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Likes {
	
	public static final String PATH = "/Users/oizad/CIFF/likes.txt";
	
	public static HashMap<String, String> getLikeMap(){
		HashMap<String, String> likeMap = new HashMap<String, String>();
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(PATH));
			String line;
			while((line=buffer.readLine())!=null){
				String[] parts = line.split("=");
				likeMap.put(parts[0], parts[1]);
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return likeMap;
	}
}
