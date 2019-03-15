package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Run {
	
	public static final int TOP_LIMIT = 20;
	public static final int YEAR = getCurrentYear();
	public static final String URL_TEMPLATE = "https://www.clevelandfilm.org/";
	public static final String URL_LIKE_TEMPLATE = "https://www.facebook.com/plugins/like.php?app_id=278343225630630&href=https%3A%2F%2Fwww.clevelandfilm.org%2Ffilms%2F" + YEAR + "%2F";
	
	public static void main(String[] args) throws Exception {
		String urlFestivalGuide = URL_TEMPLATE + "festival/festival-guide/films-a-z/" + YEAR;
		Date tic = new Date();
		List<String> stubs = getUrlStubs(urlFestivalGuide);
		System.out.println("# Stubs:\t" + stubs.size());
		
		HashMap<Type, List<Movie>> movies = new HashMap<Type, List<Movie>>();
		movies.put(Type.SHORT, new ArrayList<Movie>());
		movies.put(Type.FILM, new ArrayList<Movie>());

		int delta = stubs.size()/20;
		int s=0;
		for(String stub : stubs){
			if((s++)%delta == 0) 
				System.out.println(String.format("%.2f",(100*s)/(0.0+stubs.size())) + "% processed");
			Movie movie = getMovieFromUrl(URL_TEMPLATE + "films/" + YEAR + "/"  +  stub);
			if(movie.getType() != null && movie.getSchedule() != null){
				movie.setLikes(getMovieLikes(stub));
				movies.get(movie.getType()).add(movie);
			}
		}
		
		List<Movie> shortList = movies.get(Type.SHORT);
		List<Movie> filmList = movies.get(Type.FILM);
		
		Collections.sort(shortList, MovieComparator);
		Collections.sort(filmList, MovieComparator);
		
		FileWriter shorts = new FileWriter("CIFF_SHORTS" + (YEAR-1976) + ".txt");
		
		System.out.println("TOP " + TOP_LIMIT + " SHORTS:");
		int rankS = 0;
		for(Movie movie : movies.get(Type.SHORT)){
			if(rankS < TOP_LIMIT)	System.out.println(++rankS + "\t" + movie);
			shorts.write(movie + "\n");
		}

		shorts.close();
		
		
		FileWriter films = new FileWriter("CIFF_FILMS" + (YEAR-1976) + ".txt");
		
		System.out.println("TOP " + TOP_LIMIT + " FILMS:");
		int rankM = 0;
		for(Movie movie : movies.get(Type.FILM)){
			if(rankM < TOP_LIMIT) System.out.println(++rankM + "\t" + movie);
			films.write(movie + "\n");
		}
		
		films.close();
		
		Date toc = new Date();
		long time = toc.getTime() - tic.getTime();
		System.out.println(time + "ms");
		
	}
	
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
	

	public static Movie getMovieFromUrl(String url) {
		Movie movie = new Movie();
		movie.setUrl(url);
		try{
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String inputLine;
			while((inputLine=buffer.readLine()) != null){
				if(inputLine.contains("<title>")){
					movie.setTitle(inputLine.replaceAll(".*<title>(.*) - Cleveland International Film Festival.*", "$1"));
				}else if(inputLine.contains("<h3>CIFF" + (YEAR-1976) + " Screenings</h3>")){
					String[] parts = inputLine.split("<strong>");
					for(String part : parts){
						if(part.startsWith("Tower City Cinemas")){
							String[] items = part.split("<.p>");
							List<String> schedule = new ArrayList<String>();
							for(int i=1;i<items.length-1;i++){
								schedule.add(items[i].replaceAll("^<p>", "").replaceAll(",", ""));
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
				}else if(inputLine.contains("og:image")){
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
					movie.setDescription(inputLine.replaceAll(".*<span itemprop=\"description\"><p>(.*)<.p><.span>.*", "$1").replaceAll("&mdash;.*", "").replaceAll("<[^>]+>",""));
				}else if(inputLine.contains("<p><strong>Director Bio</strong><br />")){
					inputLine = buffer.readLine();
					movie.setBiography(inputLine.replaceAll("^[^a-zA-Z]+","").replaceAll("<[^>]+>",""));
				}
				
			}
		}catch(Exception e){}
		
		return movie;
	}

	private static List<String> getCountries(String inputLine) {
		List<String> countries = new ArrayList<String>();
		for(String country : inputLine.split(","))
			countries.add(country);
		return countries;
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
	
	
	public static int getCurrentYear(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	
	public static final Comparator<Movie> MovieComparator = new Comparator<Movie>() {

	    @Override
	    public int compare(Movie left, Movie right) {
	      
	      if(left == null && right == null) {
	    	  return 0;
	      }else if(left == null && right != null) {
	    	  return Integer.compare(0,1);
	      }else if(right == null && left != null) {
	    	  return Integer.compare(1,0);
	      }

	      int count1 = left.getLikes();
	      int count2 = right.getLikes();

	      if(count1 == count2){
	    	  return Integer.compare(calculateScore(left), calculateScore(right));
	      }
	      
	      return Integer.compare(count2, count1);
	    }
	};

	public static int calculateScore(Movie movie){
		int score = 0;
		if(movie.getBiography() != null && movie.getDescription() != null){
			score += movie.getBiography().toLowerCase().contains("award") || movie.getDescription().toLowerCase().contains("award") ? 1 : 0;
			score += movie.getBiography().toLowerCase().contains("toronto") || movie.getDescription().toLowerCase().contains("toronto") ? 1 : 0;
			score += movie.getBiography().toLowerCase().contains("sundance") || movie.getDescription().toLowerCase().contains("sundance") ? 1 : 0;
			score += movie.getBiography().toLowerCase().contains("cannes") || movie.getDescription().toLowerCase().contains("cannes") ? 1 : 0;
		}
		score += movie.getSchedule().size();
		if(movie.getLocations() != null)
			score += movie.getLocations().size();
		
		for(String time : movie.getSchedule()){
			if(time.startsWith("Fri") || time.startsWith("Sat") || time.startsWith("Sun"))
				score++;
			if(time.endsWith("PM"))
				score++;
		}
		return score;
	}

}
