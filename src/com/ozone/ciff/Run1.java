package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Run1 {
	
	public static final int YEAR = getCurrentYear();
	private static final String URL_TEMPLATE = "https://www.clevelandfilm.org/";
	
	public static void main(String[] args) {
		String urlFestivalGuide = URL_TEMPLATE + "festival/festival-guide/films-a-z/" + YEAR;
		Date tic = new Date();
		List<String> stubs = getUrlStubs(urlFestivalGuide);
		for(String stub : stubs) {
			try {
				BufferedReader buffer = new BufferedReader(new InputStreamReader(new URL(URL_TEMPLATE + "films/" + YEAR + "/" + stub).openStream()));
				String line;
				List<String> lines = new ArrayList<String>();
				while((line=buffer.readLine()) != null) 
					lines.add(line);
				buffer.close();
				ConvertToHtml.writeToFile(stub + "_stub.txt", lines);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("# Stubs:\t" + stubs.size());
		Date toc = new Date();
		long time = toc.getTime() - tic.getTime();
		System.out.println(time + "ms");
	}
	
	public static int getCurrentYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	
	public static List<String> getUrlStubs(String url){
		List<String> stubs = new ArrayList<String>();
		try{
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String inputLine;
			while((inputLine=buffer.readLine()) != null)
				if(inputLine.contains("<a href=\"films/" + YEAR))
					stubs.add(inputLine.replaceAll(".*<li><a href=\"films/" + YEAR + "/([^\"]+)\".*", "$1"));
			buffer.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return stubs;
	}

}
