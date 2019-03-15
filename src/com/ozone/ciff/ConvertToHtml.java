package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConvertToHtml {

	public static void main(String[] args) throws IOException {
//		htmlConverter(new File(System.getProperty("user.dir"),"CIFF" + (Run.YEAR-1976)+ "_Shorts.json"));
		htmlConverter(new File(System.getProperty("user.dir"),"CIFF" + (Run.YEAR-1976) + "_Films.json"));
	}
	
	public static void htmlConverter(File file) throws IOException{
		
		List<HashMap<String, String>> films = parseData(file);

		List<String> rows = convert(films);
		FileWriter writer = new FileWriter("index.html");
		for(String row : rows)
			writer.write(row + "\n");
		writer.close();
	}
	
	public static List<String> convert(List<HashMap<String, String>> films){
		List<String> rows = startHTML();
		
		int max = Integer.valueOf(films.get(0).get("likes"));
		int min = Integer.valueOf(films.get(films.size()-1).get("likes"));
		
		
		int rank = 0;
		for(HashMap<String, String> film : films){
			if(film.get("title").toLowerCase().contains("awards program"))
				continue;
			
			String stub = film.get("url").replaceAll(".*/","");
			String locations = film.get("locations");
			String row = " <a href=\"" + film.get("url") + "\" title=\"" + film.get("title") + "\" class=\"film\" target=\"_blank\">";
			row += "<img class=\"img-fluid\" src=\"images/" + stub + "\" alt=\"\" height=\"300\" width=\"450\">";
			row += "<div class=\"details\" id=\"details\"><p class=\"title\" style=\"font-family:arial;\" id=\"title\"><font color=\"#007777\">" + (++rank) + ". " + film.get("title") + "</font></p>";
			row += "<p class=\"lucy-wrapper\" style=\"font-family:arial;\" id=\"location\"><strong><font color=\"#03B1CF\">" + locations + "</font></strong><br>";
			
			row += "<p class=\"lucy-wrapper\">Rating: " + normalize(film.get("likes"), max, min) + "<br>Length: "+film.get("length") + "'<br>Description: " + film.get("description") + "</p><p></p></div></a>";
			rows.add(row);
		}
		rows.addAll(endHTML());
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
		String line = buffer.readLine();
		buffer.close();
		String[] parts = line.split("\"type\":");
		for(String part : parts) {
			String[] pieces = part.split(",\"");
			
			HashMap<String, String> film = new HashMap<String, String>();
			for(String piece : pieces) {
				if(piece.startsWith("likes")) {
					film.put("likes", piece.replaceAll(".*:",""));
				}
			}
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
	
	public static List<String> endHTML(){
		List<String> rows = new ArrayList<String>();
		rows.add("</div>");
		rows.add("</div>");
		rows.add("<script>");
		rows.add("window.onscroll = function() {myFunction()};");
		rows.add("var header = document.getElementById(\"myHeader\");");
		rows.add("var sticky = header.offsetTop;");
		rows.add("function myFunction() {");
		rows.add("  if (window.pageYOffset >= sticky) {");
		rows.add("    header.classList.add(\"sticky\");");
		rows.add("  } else {");
		rows.add("    header.classList.remove(\"sticky\");");
		rows.add("  }");
		rows.add("}");
		rows.add("</script>");
		rows.add("<script>");
		rows.add("function checkbutton(button){	");
		rows.add("	if(button.style.backgroundColor == document.getElementById(\"default\").style.backgroundColor){");
		rows.add("		button.style.backgroundColor = document.getElementById(\"checked\").style.backgroundColor;");
		rows.add("	}else{");
		rows.add("		button.style.backgroundColor = document.getElementById(\"default\").style.backgroundColor;");
		rows.add("	}");
		rows.add("	filter();");
		rows.add("}");
		rows.add("</script>");
		rows.add("<script src=\"js/filter.js\"></script>");
		rows.add("<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\" integrity=\"sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN\" crossorigin=\"anonymous\"></script>");
		rows.add("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" integrity=\"sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q\" crossorigin=\"anonymous\"></script>");
		rows.add("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" integrity=\"sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl\" crossorigin=\"anonymous\"></script>");
		rows.add("<script src=\"https://code.jquery.com/jquery-1.11.3.min.js\"></script>");
		rows.add("<script src=\"https://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js\"></script>");
		rows.add("</body>");
		rows.add("</html>");
		return rows;
	}
	
	public static List<String> startHTML(){
 		List<String> rows = new ArrayList<String>();
		rows.add("<html>");
		rows.add("<head>");
		rows.add("<title>CIFF RANKER</title>");
		rows.add("<head>");
		rows.add("<meta charset=\"utf-8\">");
		rows.add("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">");
		rows.add("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">");
		rows.add("<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"icon.ico\" />");
		rows.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"css/site.css\">");
		rows.add("<link type=\"text/css\" rel=\"stylesheet\" href=\"http://fast.fonts.com/cssapi/cd3eef30-2267-4f91-abfd-d90805bf0fec.css\">");
		rows.add("<style>");
		rows.add("body {");
		rows.add("  margin: 0;");
		rows.add("  font-family: Arial;");
		rows.add("}");
		rows.add("");
		rows.add(".top-container {");
		rows.add("  background-color: #f1f1f1;");
		rows.add("  background: linear-gradient(180deg, #666666, #999999);");
		rows.add("  padding: 30px;");
		rows.add("}");
		rows.add("");
		rows.add(".header {");
		rows.add("  padding: 10px 16px;");
		rows.add("  background: #f1f1f1;");
		rows.add("  background: linear-gradient(180deg, #999999,#CCCCCC,#FFFFFF);");
		rows.add("  color: #f1f1f1;");
		rows.add("}");
		rows.add("");
		rows.add(".content {");
		rows.add("}");
		rows.add("");
		rows.add(".sticky {");
		rows.add("  position: fixed;");
		rows.add("  top: 0;");
		rows.add("  width: 100%;");
		rows.add("}");
		rows.add("");
		rows.add(".sticky + .content {");
		rows.add("  padding-top: 160px;");
		rows.add("}");
		rows.add("</style>");
		rows.add("");
		rows.add("</head>");
		rows.add("<body>");
		rows.add("<div class=\"top-container\">");
		rows.add("<!-- <div class=\"header\" id=\"myHeader\"> -->");
		rows.add("<body class=\"no-fix\">");
		rows.add("<img class=\"img-fluid\" src=\"logo.png\">");
		rows.add("</div>");
		rows.add("");
		rows.add("<div class=\"header\" id=\"myHeader\">");
		rows.add("<div class=\"topnav\" id=\"search\">");
		rows.add("<input style=\"width:320px; margin-left: 10px;\" type=\"text\" size=\"2\" placeholder=\"Search...\" onkeyup=\"filter()\" id=\"filter-search\"/>");
		rows.add("</div>");
		rows.add("<button id=\"default\" style=\"display:none; background:#68C090;\"/>");
		rows.add("<button id=\"checked\" style=\"display:none; background:#03B1CF;\"/>");
		rows.add("<button id=\"forums\" style=\"font-family:arial; margin-left: 10px; background:#03B1CF;\" type=\"button\" class=\"btn btn-primary\" onclick=\"checkbutton(this)\">FORUMS</button>");
		rows.add("<button id=\"awards\" style=\"font-family:arial; background:#03B1CF;\" type=\"button\" class=\"btn btn-primary\" onclick=\"checkbutton(this)\">AWARDS</button>");
		rows.add("<button id=\"premiere\" style=\"font-family:arial; background:#03B1CF;\" type=\"button\" class=\"btn btn-primary\" onclick=\"checkbutton(this)\">PREMIERE</button>");
		rows.add("<!-- ");
		rows.add("<button id=\"weekends\" style=\"font-family:arial; background:#03B1CF;\" type=\"button\" class=\"btn btn-primary\" onclick=\"checkbutton(this)\">WEEKENDS</button>");
		rows.add("<button id=\"evenings\" style=\"font-family:arial; background:#03B1CF;\" type=\"button\" class=\"btn btn-primary\" onclick=\"checkbutton(this)\">EVENINGS</button>");
		rows.add(" -->");
		rows.add("");
		rows.add("</div>");
		rows.add("");
		rows.add("<div class=\"content\">");
		rows.add("<div id=\"mobile-schedule-list\">");
		return rows;
	}
	
	public static void writeToFile(String fileName, List<String> lines) {
		File file = new File(fileName);
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file.getAbsoluteFile());
			BufferedWriter buffer = new BufferedWriter(writer);
			for(String line : lines)
				buffer.write(line + "\n");
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
