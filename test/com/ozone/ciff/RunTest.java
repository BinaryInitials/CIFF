package com.ozone.ciff;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class RunTest {
	
	@Test
	public void testGetCurrentYear(){
		Assert.assertEquals(20, Run.getCurrentYear()/100);
	}
	
	@Test
	public void testInputSplit(){
		String singleton = "ABC";
		List<String> list = new ArrayList<String>();
		for(String item : singleton.split(",")){
			list.add(item);
		}
		Assert.assertEquals(1, list.size());
	}
	
	@Test
	public void testSpecificMovie(){
		String url = "https://www.clevelandfilm.org/films/2017/lucy-in-my-eyes";
		Movie movie = Run.getMovieFromUrl(url);
		System.out.println(movie);
	}

}
