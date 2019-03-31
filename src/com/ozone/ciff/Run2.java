package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Run2 {

	public static final String URL_LIKE_TEMPLATE = "https://www.facebook.com/plugins/like.php?app_id=278343225630630&href=https%3A%2F%2Fwww.clevelandfilm.org%2Ffilms%2F" + Run1.YEAR + "%2F";
	
	public static int getMovieLikes(String stub){
		int likes = 0;
		try{
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new URL(URL_LIKE_TEMPLATE + stub).openStream()));
			String nextline;
			while((nextline=buffer.readLine()) != null){
				if(nextline.contains(">Like<")){
					if(nextline.contains("You and ")){
						likes = Integer.valueOf(nextline.replaceAll(".*You and ([0-9]+) others like this.*","$1"));
					}else if(nextline.contains("people like this")){
						likes = Integer.valueOf(nextline.replaceAll(".*>([0-9]+) people like this.*","$1"));
					}
					break;
				}
			}
			buffer.close();
		}catch(Exception e){}
		return likes;
	}
	
	public static void main(String[] args) throws IOException {
		Date t = new Date();
		File file = new File(System.getProperty("user.dir"));
		List<Movie> movies = new ArrayList<Movie>();
		System.out.println("1. Parsing data...");
		int count=0;
		int diff = file.listFiles().length / 20;
		for(File fileInFolder : file.listFiles()){
			if(fileInFolder.toString().endsWith("_stub.txt")){
				if(count++ % diff == 0)
					System.out.println("Processed: " + String.format("%.1f", (100*count)/(0.0+file.listFiles().length)) + "%");
				
				Movie movie = extractMovieFromSource(fileInFolder);
				String stub = movie.getUrl().replaceAll(".*/", "");
				if(movie != null){
					movie.setLikes(Integer.valueOf(getMovieLikes(stub)));
					movies.add(movie);
				}
			}
		}
		Collections.sort(movies, MovieComparator);
//		ObjectMapper om = new ObjectMapper();
		List<Movie> shorts = new ArrayList<Movie>();
		List<Movie> films = new ArrayList<Movie>();
		List<Movie> perspectives = new ArrayList<Movie>();
		for(Movie movie : movies){
			if(movie.getType() == null){
				perspectives.add(movie);
			}else{
				switch(movie.getType()){
				case FILM:
					films.add(movie);
					break;
				case SHORT:
					shorts.add(movie);
					break;
				default:
					break;
				}
			}
		}
		int rank = 1;
		List<String> rows = new ArrayList<String>();
		rows.addAll(ConvertToHtml.startHTML());
		System.out.println("2. Creating website...");
		for(Movie movie : films){
//			List<String> prestigiousAwards = getPrestigiousAwards(movie);
//			boolean isPrestigiousAwards = prestigiousAwards.size() > 0;
			String schedule = "";
			if(movie.getLocations() != null && movie.getSchedule() != null){
				for(String event : movie.getSchedule()){
					schedule += "<br>" + event;
				}
				if(movie.getLocations() != null && movie.getSchedule() != null){
					String stub = movie.getUrl().replaceAll(".*/",""); 
					rows.add(
							"<a href=\"" + movie.getUrl() + "\"  title=\"" + movie.getTitle() + "\" class=\"film\" target=\"_blank\">" + 
							"<img src=\"images/" + stub + "\"  alt=\"\" height=\"300\" width=\"450\">" + 
							"<div class=\"details\" id=\"details\">" +
							"<p class=\"title\" id=\"title\" ><font color=\"#007777\">" + rank++ + ". " + movie.getTitle().toUpperCase().trim() + "</font></p>" + 
							"<p class=\"lucy-wrapper\" style=\"font-family:arial;\" id=\"location\"><strong><font color=\"#03B1CF\">" + movie.getLocations().toString().replaceAll("[\\[|\\]]","") + "</font></strong>" +
							schedule +  
							"<br>Popularity: " + movie.getLikes() + 
							"<br>Duration: " + movie.getLength() + "'" + 
							(movie.isPremier()? "<br>Premiere: " + movie.getPremierDescription():"") + 
//							(isPrestigiousAwards ? "<br>Awards: " + prestigiousAwards.toString().replaceAll("[\\[|\\]]","") : "") + 
//							"<br>Description: " + movie.getDescription().replaceAll("([a-z][a-z])\\. .*","$1...") +
							"<br>Description: " + movie.getDescription() +
							"<br>Director: " + movie.getBiography() + 
							"</p></div></a>");
				}
			}
		}
		rows.addAll(ConvertToHtml.endHTML());
		ConvertToHtml.writeToFile("index.html", rows);
		System.out.println("Done. Ellapsed time: " + (new Date().getTime() - t.getTime()));
	}
	
	public static Movie extractMovieFromSource(File source){
		Movie movie = new Movie();
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(source));
			String inputLine;
			while((inputLine=buffer.readLine()) != null){
				if(inputLine.contains("<title>")){
					movie.setTitle(inputLine.replaceAll(".*<title>(.*) - Cleveland International Film Festival.*", "$1"));
				}else if(inputLine.contains("Screenings</h3>")){
					String[] parts = inputLine.split("<strong>");
					for(String part : parts){
						if(part.contains("Shorts Program")){
							List<String> shorts = new ArrayList<String>();
							String[] miniParts = part.split("<[^>]+>");
							for(String miniPart : miniParts){
								if(miniPart.contains("Program") && !miniPart.matches("^Shorts Programs?$")){
									shorts.add(miniPart);
								}
							}
							movie.setShortPrograms(shorts);
						}
						
						
						if(part.startsWith("Tower City Cinemas")){
							String[] items = part.split("<.p>");
							List<String> schedule = new ArrayList<String>();
							for(int i=1;i<items.length-1;i++){
								if(items[i].contains("FilmForum"))
									schedule.set(schedule.size()-1, schedule.get(schedule.size()-1)+ " w FILM FORUM");
								schedule.add(items[i].replaceAll("^<p>", "").replaceAll(",", "").replaceAll("<[^>]+>","").replaceAll(".*([A-Z][a-z]+ [A-Z][a-z]+ [0-9]+ [0-9]{4}.*)","$1"));
							}
							movie.setSchedule(schedule);
						}else if(part.startsWith("Sidebars")){
							String[] items = part.split("<br />");
							List<String> sidebars = new ArrayList<String>();
							for(int i=1;i<items.length-1;i++){
								sidebars.add(items[i].replaceAll("<[^>]+>", ""));
							}
							movie.setSidebars(sidebars);
						}
					}
				}else if(inputLine.contains("<strong>Director")){
					inputLine = buffer.readLine();
					movie.setBiography(inputLine.replaceAll("Filmography.*","").replaceAll(".*span>","").replaceAll("<[^>]+>", "").replaceAll("^\t+", "").replaceAll("&[a-z]+;", ""));
				}else if(inputLine.contains("og:url")){
					movie.setUrl(inputLine.replaceAll(".*content=\"([^\"]+)\".*","$1"));
				}else if(inputLine.contains("og:image") && !inputLine.contains("share-fb")){
					movie.setImageUrl(inputLine.replaceAll(".*content=\"([^\"]+)\".*", "$1"));
				}else if(inputLine.contains("<strong>Year:</strong>")){
					movie.setYear(Integer.valueOf(inputLine.replaceAll(".*<strong>Year:</strong> ([0-9]+)<.*", "$1")));
				}else if(inputLine.contains("countryOfOrigin")){
					inputLine = buffer.readLine();
					List<String> countries = getCountries(inputLine.replaceAll(".*<span itemprop=\"name\">(.*)</span>.*","$1"));
					movie.setLocations(countries);
				}else if(inputLine.contains("<span itemprop=\"duration\"")){
					inputLine = buffer.readLine();
					movie.setLength(Integer.valueOf(inputLine.replaceAll("^[^0-9]+([0-9]+)[^0-9].*min.*", "$1")));
					movie.setType(movie.getLength() < 59 ? Type.SHORT: Type.FILM);
				}else if(inputLine.contains("<span itemprop=\"description\"")){
					if(inputLine.contains("PREMIER")){
						movie.setPremier(true);
						movie.setPremierDescription(inputLine.replaceAll("<[^>]+>", ""));
						inputLine = buffer.readLine();
					}else{
						movie.setPremier(false);
					}
					String description = "";
					while(!inputLine.contains("<ul id=\"film-social\">")){
						description += inputLine.replaceAll("&[a-z]+;", " ").replaceAll("<[^>]+>","").trim();
						inputLine = buffer.readLine();
					}
					movie.setDescription(description);
				}
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return movie;
	}
	
	private static List<String> getCountries(String inputLine) {
		List<String> countries = new ArrayList<String>();
		for(String country : inputLine.split(","))
			countries.add(country);
		return countries;
	}
	
	public static final Comparator<Movie> MovieComparator = new Comparator<Movie>() {

	    @Override
	    public int compare(Movie left, Movie right) {
	      

	      int count1 = left.getLikes();
	      int count2 = right.getLikes();

	      return Integer.compare(count2, count1);
	    }
	};
}
