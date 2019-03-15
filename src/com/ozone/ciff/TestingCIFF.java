package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TestingCIFF {
	
	public static final String YEAR = "2019";
	public static final String STUB = "werewolf";
	public static final String URL = "https://www.facebook.com/plugins/like.php?app_id=278343225630630&href=https%3A%2F%2Fwww.clevelandfilm.org%2Ffilms%2F"+YEAR+"%2F"+STUB+"&layout=button_count";
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		Date tic = new Date();
		BufferedReader buffer = new BufferedReader(new InputStreamReader(new URL(URL).openStream()));
		String line;
		while((line=buffer.readLine())!=null){
			if(line.contains(">Like<")){
				String likes = line.replaceAll(".*u_0_2\">([0-9]+)<.*", "$1");
				System.out.println(STUB + "\t" + likes);
				break;
			}
		}
		buffer.close();
		System.out.println((new Date().getTime() - tic.getTime()) + "ms");
	}

}
