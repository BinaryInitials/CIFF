package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConvertToHtml {

//	private static final int IMG_W = 200;
//	private static final int IMG_H = 140;

	private static final int BG_IMG_W = 500;
	private static final int BG_IMG_H = 350;
	
	public static void main(String[] args) throws IOException {
//		htmlConverter(new File(System.getProperty("user.dir"),"CIFF_SHORTS.txt"));
		htmlConverter(new File(System.getProperty("user.dir"),"CIFF_FILMS.txt"));
	}
	
	public static void htmlConverter(File file) throws IOException{
		
		List<HashMap<String, String>> films = parseData(file);

		List<String> rows = convert(films);
		FileWriter writer = new FileWriter("index.html");
		for(String row : rows){
			writer.write(row + "\n");
//			System.out.println(row);
		}
		writer.close();
	}
	
	public static List<String> convert(List<HashMap<String, String>> films){
		List<String> rows = new ArrayList<String>();
		rows.add("<html><head><title>CIFF RANKER</title>");
		rows.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"site.css\">");
		rows.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"http://fast.fonts.com/cssapi/cd3eef30-2267-4f91-abfd-d90805bf0fec.css\">");
		rows.add("</head>");
		
		
		rows.add("<body class=\" no-fix\">");
		rows.add("<img src=\"http://www.clevelandfilm.org/assets/images/downloads/ciff_logos/CIFF_wFullName_Logo.jpg\" style=\"width:"+BG_IMG_W+";height="+BG_IMG_H+";\">");
		rows.add("<div id=\"mobile-schedule-list\">");
		
		int max = Integer.valueOf(films.get(0).get("likes"));
		int min = Integer.valueOf(films.get(films.size()-1).get("likes"));
		
		
		int rank = 0;
		for(HashMap<String, String> film : films){
			
			if(film.get("title").toLowerCase().contains("awards program"))
				continue;
			
			String row = " <a href=\"" + film.get("url") + "\" title=\"" + film.get("title") + "\" class=\"film\">";
			row += "<img src=\"" + film.get("image").replaceAll("/films/", "/films/grid/") + "\" alt=\"\">";
			row += "<div class=\"details\"><p class=\"title\">" + (++rank) + ". " + film.get("title") + "</p>";
			row += "<p class=\"lucy-wrapper\">Rating: " + normalize(film.get("likes"), max, min) + "<br>Length: "+film.get("length") + "'</p><p></p></div></a>";
			rows.add(row);
		}
		rows.add("</body>");
		rows.add("</html>");
		return rows;
	}
	
	public static String normalize(String likes, int max, int min){
		int likesNum = Integer.valueOf(likes);
		double percentage = 0.1*Math.round((1000.0*(linearize(likesNum) - linearize(min))) / (0.01+linearize(max)-linearize(min))); 
		return String.format("%.1f", percentage) + "%";
	}
	
	public static Double linearize(int num){
		return Math.sqrt(Math.log(num+1.0));
	}
	
	public static List<HashMap<String, String>> parseData(File file) throws IOException{
		BufferedReader buffer = null;
		List<HashMap<String, String>> films = new ArrayList<HashMap<String, String>>();
		buffer = new BufferedReader(new FileReader(file));
		String line = "";
		while((line=buffer.readLine()) != null){
			String[] pieces = line.split(", [A-Za-z]+=");
			
			HashMap<String, String> film = new HashMap<String, String>();
			film.put("likes", pieces[1]);
			film.put("length", pieces[2]);
			film.put("year", pieces[3]);
			film.put("title", pieces[4]);
			film.put("url", pieces[5]);
			film.put("image", pieces[6]);
			film.put("description", pieces[7].replaceAll("WORLD PREMIER\\.","").replaceAll("\\..*","."));
			films.add(film);
		}
		buffer.close();
		return films;
	}
}
