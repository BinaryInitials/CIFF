package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Run2 {

	public static final String PATH = "/Users/oizad/CIFF/";
	public static final String FESTIVAL = ".*(toronto|cannes|venice|berlin inter|tribeca|sundance|raindance).*festival.*";
	public static final String AWARD = "((([A-Z]+)|([A-Z][a-z]+) ?)+(Emmy|Award|Prize|dOr|(Golden|Silver) (Pram|Globe|Lion|Bear)))";

	public static void main(String[] args) throws IOException {
		File file = new File(PATH);
		HashMap<String, String> likeMap = Likes.getLikeMap();
		List<Movie> movies = new ArrayList<Movie>();
		for(File fileInFolder : file.listFiles()){
			if(fileInFolder.toString().endsWith("_stub.txt")){
				Movie movie = extractMovieFromSource(fileInFolder);
				String stub = movie.getUrl().replaceAll(".*/", "");
				if(movie != null && likeMap.get(stub) != null){
					movie.setLikes(Integer.valueOf(likeMap.get(stub)));
					movies.add(movie);
				}
			}
		}
		Collections.sort(movies, MovieComparator);
		ObjectMapper om = new ObjectMapper();
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
		om.writeValue(new File("CIFF42_Films.json"), films);
		om.writeValue(new File("CIFF42_Shorts.json"), shorts);
		int rank = 1;
		for(Movie movie : shorts){
			List<String> prestigiousAwards = getPrestigiousAwards(movie);
			boolean isPrestigiousAwards = prestigiousAwards.size() > 0;
			String schedule = "";
			String shortPrograms = "";
			if(movie.getLocations() != null && movie.getSchedule() != null){
				for(String event : movie.getSchedule()){
					schedule += "<br>" + event;
				}
				for(String shortProgram : movie.getShortPrograms()){
					shortPrograms += "<br>" + shortProgram;
				}
				if(movie.getLocations() != null && movie.getSchedule() != null){
					System.out.println(
							"<a href=\"" + movie.getUrl() + "\"  title=\"" + movie.getTitle() + "\" class=\"film\" target=\"_blank\">" + 
							"<img src=\"" + movie.getImageUrl() + "\"  alt=\"\" height=\"267\" width=\"400\">" + 
							"<div class=\"details\" id=\"details\">" +
							"<p class=\"title\" id=\"title\" style=\"font-size:14px\"><font color=\"#007777\">" + rank++ + ". " + movie.getTitle().toUpperCase().trim() + "</font></p>" + 
							"<p class=\"lucy-wrapper\" id=\"location\" style=\"font-size:10px\"><strong><font color=\"#03B1CF\">" + movie.getLocations().toString().replaceAll("[\\[|\\]]","") + "</font></strong>" +
							schedule +  
							shortPrograms +  
							"<br>Popularity: " + movie.getLikes() + 
							"<br>Duration: " + movie.getLength() + "'" + 
							(movie.isPremier()? "<br>Premiere: " + movie.getPremierDescription():"") + 
							(isPrestigiousAwards ? "<br>Awards: " + prestigiousAwards.toString().replaceAll("[\\[|\\]]","") : "") + 
							"<br>Description: " + movie.getDescription().replaceAll("([a-z][a-z])\\. .*","$1...") +
							"</p></div></a>");
				}
			}
		}
	}
	
	public static List<String> getPrestigiousAwards(String text) {
		List<String> awards = new ArrayList<String>();
		String processedText = text.replaceAll("&[a-z]+;","").replaceAll("[+|\\(|\\)]", "").replaceAll("[Aa]ward[^A-Za-z]+[Ww]inning", "").replaceAll(" +", " ");
		int numOfAwards = processedText.split(AWARD).length-1;
		
		if(numOfAwards == 0)
			return awards;
		
		for(int i=0;i<numOfAwards;i++){
			String award = processedText.replaceAll(".*(,|[^A-Za-z][a-z]+) " + AWARD + ".*", "$2");
			awards.add(award);
			String primer = processedText.replaceAll(".*((,|([^A-Za-z][a-z]+){1}) " + award + ").*", "$1");
			processedText = processedText.replaceAll(primer + ".*","");
		}
		
		return awards;
	}
	public static List<String> getPrestigiousAwards(Movie movie) {
		List<String> awards = new ArrayList<String>();
		if(movie.getTitle() == null || movie.getBiography() == null)
			return awards;
		return getPrestigiousAwards(movie.getDescription() + " " + movie.getBiography());
	}

	public static boolean isPrestigiousFestival(Movie movie){
		return movie.getTitle().matches(FESTIVAL) || movie.getBiography().matches(FESTIVAL);
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
