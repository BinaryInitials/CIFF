package com.ozone.ciff;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CIFF {

	private static List<Film> allFilmsAndShorts = new ArrayList<Film>();
	public static class Film {
		@Override
		public String toString() {
			return "Film [title=" + title + ", year=" + year + ", runTime="
					+ runTime + ", schedule=" + schedule + ", countries="
					+ countries + ", description=" + description
					+ ", directorBio=" + directorBio + "]";
		}
		String title;
		int year;
		int runTime;
		List<String> schedule;
		List<String> countries;
		String description;
		String directorBio;
		public Film(){}
		public Film(String title, int year, int runTime, List<String> schedule, List<String> countries, String description, String directorBio){
			this.title = title;
			this.year = year;
			this.runTime = runTime;
			this.schedule = schedule;
			this.countries = countries;
			this.description = description;
			this.directorBio = directorBio;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
		}
		public int getRunTime() {
			return runTime;
		}
		public void setRunTime(int runTime) {
			this.runTime = runTime;
		}
		public List<String> getSchedule() {
			return schedule;
		}
		public void setSchedule(List<String> schedule) {
			this.schedule = schedule;
		}
		public void addSchedule(String event){
			if(schedule == null){
				schedule = new ArrayList<String>();
			}
			schedule.add(event);
		}
		public List<String> getCountries() {
			return countries;
		}
		public void setCountries(List<String> countries) {
			this.countries = countries;
		}
		public void addCountry(String country){
			if(countries == null){
				countries = new ArrayList<String>();
			}
			countries.add(country);
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getDirectorBio() {
			return directorBio;
		}
		public void setDirectorBio(String directorBio) {
			this.directorBio = directorBio;
		}
	}
	
	public static void main(String[] args) throws IOException{
		BufferedReader buffer = new BufferedReader(new FileReader("/Users/oizad/CIFF_DATA.txt"));
		String line = "";
		List<String> lines = new ArrayList<String>();
		while((line=buffer.readLine())!=null){
			lines.add(line);
		}
		buffer.close();
		
		for(int i=0;i<lines.size();i++){
			if(lines.get(i).startsWith("Title: ")){
				Film film = new Film();
				int j=i;
				film.setTitle(lines.get(j).replaceAll("^Title: ", ""));
				j++;
				if(j<lines.size() && lines.get(j).startsWith("Year: ")){
					int year = Integer.valueOf(lines.get(j).replaceAll("^Year: ", ""));
					film.setYear(year);
					j++;
				}
				if(j<lines.size() && lines.get(j).startsWith("Country: ")){
					String[] countries = lines.get(j).replaceAll("^Country: ", "").split(",");
					film.setCountries(Arrays.asList(countries));
					j++;
				}
				if(j<lines.size() && lines.get(j).startsWith("Run Time: ")){
					int runTime = Integer.valueOf(lines.get(j).replaceAll("^Run Time: ([0-9]+) minutes", "$1"));
					film.setRunTime(runTime);
					j++;
				}
				while(j<lines.size() && lines.get(j).startsWith("Schedule: ")){
					film.addSchedule(lines.get(j).replaceAll("^Schedule: ", ""));
					j++;
				}
				if(j<lines.size() && lines.get(j).startsWith("Description: ")){
					film.setDescription(lines.get(j).replace("^ Description", ""));
					j++;
				}
				if(j<lines.size() && lines.get(j).startsWith("Director Bio: ")){
					film.setDirectorBio(lines.get(j).replaceAll("^Director Bio: ", ""));
				}
				allFilmsAndShorts.add(film);
			}
		}
		List<Film> shorts = new ArrayList<Film>();
		List<Film> films = new ArrayList<Film>();
		for(Film film : allFilmsAndShorts){
			if(film.getRunTime() < 40){
				shorts.add(film);
			}else{
				films.add(film);
			}
		}

//		for(Film film : shorts){
//			if(film.getSchedule() != null){
//				for(String schedule : film.getSchedule()){
//					System.out.println(displayFilm(film, schedule));
//				}	
//			}
//		}
		for(Film film : films){
//			if(film.getDescription() != null && 
//					film.getDescription().toLowerCase().matches(".*(passion|courag|inspir).*(passion|courag|inspir).*") && film.getDescription().matches(".*") &&  
//					film.getDirectorBio() != null && film.getDirectorBio().toLowerCase().matches(".*(award).*")){
//			if(film.getDescription() != null && film.getDirectorBio() != null && film.getDirectorBio().matches(".*(won|earn).*([Bb]est|[Aa]ward|[Pp]rize).*(Fest).*") && !film.getDirectorBio().matches(".*Israel.*") && !film.getDescription().matches(".*Israel.*")){	
//				System.out.println(film.getTitle() + "\t" + film.getDescription() + "\t" + film.getDirectorBio());
//			}
			if(film.getDescription() != null && film.getDescription().toLowerCase().matches(".*(powerful).*")){
				System.out.println(film.getTitle() + "\t" + film.getDescription().replaceAll(".*\\.([^.]+powerful[^.]+)\\..*", "$1") + "\t" + film.getDirectorBio());
			}
		}
	}

	public static String displayFilm(Film film, String schedule) {
		return film.getTitle() + "\t" + film.getYear() + "\t" + film.getCountries() + "\t" + film.getRunTime() + "\t" + schedule + "\t" + film.getDescription() + "\t" + film.getDirectorBio();
	}

}
