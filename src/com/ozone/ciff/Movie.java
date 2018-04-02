package com.ozone.ciff;

import java.util.List;

public class Movie {
	
	private Type type;
	private int likes;
	private int length;
	private int year;
	private String title;
	private String url;
	private String imageUrl;
	private String description;
	private String biography;
	private List<String> locations;
	private List<String> schedule;
	private List<String> shortPrograms;
	private List<String> sidebars;
	private boolean premier;
	private String premierDescription;
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
	public List<String> getLocations() {
		return locations;
	}
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	public List<String> getSchedule() {
		return schedule;
	}
	public void setSchedule(List<String> schedule) {
		this.schedule = schedule;
	}
	public List<String> getShortPrograms() {
		return shortPrograms;
	}
	public void setShortPrograms(List<String> shortPrograms) {
		this.shortPrograms = shortPrograms;
	}
	public List<String> getSidebars() {
		return sidebars;
	}
	public void setSidebars(List<String> sidebars) {
		this.sidebars = sidebars;
	}
	public boolean isPremier() {
		return premier;
	}
	public void setPremier(boolean premier) {
		this.premier = premier;
	}
	public String getPremierDescription() {
		return premierDescription;
	}
	public void setPremierDescription(String premierDescription) {
		this.premierDescription = premierDescription;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((biography == null) ? 0 : biography.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + length;
		result = prime * result + likes;
		result = prime * result
				+ ((locations == null) ? 0 : locations.hashCode());
		result = prime * result + (premier ? 1231 : 1237);
		result = prime
				* result
				+ ((premierDescription == null) ? 0 : premierDescription
						.hashCode());
		result = prime * result
				+ ((schedule == null) ? 0 : schedule.hashCode());
		result = prime * result
				+ ((shortPrograms == null) ? 0 : shortPrograms.hashCode());
		result = prime * result
				+ ((sidebars == null) ? 0 : sidebars.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + year;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		if (biography == null) {
			if (other.biography != null)
				return false;
		} else if (!biography.equals(other.biography))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (length != other.length)
			return false;
		if (likes != other.likes)
			return false;
		if (locations == null) {
			if (other.locations != null)
				return false;
		} else if (!locations.equals(other.locations))
			return false;
		if (premier != other.premier)
			return false;
		if (premierDescription == null) {
			if (other.premierDescription != null)
				return false;
		} else if (!premierDescription.equals(other.premierDescription))
			return false;
		if (schedule == null) {
			if (other.schedule != null)
				return false;
		} else if (!schedule.equals(other.schedule))
			return false;
		if (shortPrograms == null) {
			if (other.shortPrograms != null)
				return false;
		} else if (!shortPrograms.equals(other.shortPrograms))
			return false;
		if (sidebars == null) {
			if (other.sidebars != null)
				return false;
		} else if (!sidebars.equals(other.sidebars))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type != other.type)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Movie [type=" + type + ", likes=" + likes + ", length="
				+ length + ", year=" + year + ", title=" + title + ", url="
				+ url + ", imageUrl=" + imageUrl + ", description="
				+ description + ", biography=" + biography + ", locations="
				+ locations + ", schedule=" + schedule + ", shortPrograms="
				+ shortPrograms + ", sidebars=" + sidebars + ", premier="
				+ premier + ", premierDescription=" + premierDescription + "]";
	}

}
