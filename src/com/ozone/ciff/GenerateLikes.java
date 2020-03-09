package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GenerateLikes {
	
	public static void main(String[] args) throws IOException {
		
		Date tic = new Date();
		
		int year = getCurrentYear();
		List<String> stubs = getStubs();
		
		HashMap<String, Integer> likesMap = new HashMap<String, Integer>();
		
		for(String stub : stubs) {
			String URL = "https://www.facebook.com/plugins/like.php?app_id=278343225630630&href=https%3A%2F%2Fwww.clevelandfilm.org%2Ffilms%2F"+year+"%2F"+stub+"&layout=button_count";
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new URL(URL).openStream()));
			String line;
			while((line=buffer.readLine())!=null){
				if(line.contains(">Like<")){
					int likes = Integer.valueOf(line.replaceAll(".*u_0_2\">([0-9]+)<.*", "$1"));
					likesMap.put(stub, likes);
					System.out.println(stub + "\t" + likes);
					break;
				}
			}
			buffer.close();
		}
		
		System.out.println((new Date().getTime() - tic.getTime()) + "ms");

	}
	
	public static List<String> getStubs(){
		List<String> stubs = new ArrayList<String>();
		
		return null;
	}
	
	public static int getCurrentYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

}
