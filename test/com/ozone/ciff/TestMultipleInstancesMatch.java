package com.ozone.ciff;

import org.junit.Test;

public class TestMultipleInstancesMatch {

	String AWARD_PATTERN = "(([A-Z][a-z]+ ?)+(Award|Prize))";
	
	@Test
	public void testMultipleInstancesOfAwards(){
		String bio = "Won't You Be My Neighbor?	Morgan Neville was born in 1967 in Los Angeles, California. He graduated from the University of Pennsylvania and originally worked as a journalist in New York City and San Francisco. In 2014 His film 20 Feet From Stardom won the Academy Award for Best Documentary, as well as a Grammy Award for Best Music Film.";
		int numOfAwards = bio.split(AWARD_PATTERN).length-1;
		for(int i=0;i<numOfAwards;i++){
			String award = bio.replaceAll(".* [a-z]+ " + AWARD_PATTERN + ".*", "$1");
			bio = bio.replaceAll(award,"");
			System.out.println(award);
		}
	}
	
	@Test
	public void testMultipleWordsMatchingOfAwards(){
		String bio = "In the Shadows Dipesh Jain is a graduate of University of Southern California Film School. He has written, directed, and produced award-winning shorts, documentaries, and stage plays in India, Prague, and the U.S. A recipient of the Panavision Young Filmmaker Award, Dipesh was selected for the Talents Lab at the 64th Berlin International Film Festival and the Producers Lab at the International Film Festival of India. IN THE SHADOWS won the Jury Grand Prize at the Mumbai Film Festival.";
		int numOfAwards = bio.split(AWARD_PATTERN).length-1;
		for(int i=0;i<numOfAwards;i++){
			String award = bio.replaceAll(".* [a-z]+ " + AWARD_PATTERN + ".*", "$1");
			bio = bio.replaceAll(award,"");
			System.out.println(award);
		}
	}
}
