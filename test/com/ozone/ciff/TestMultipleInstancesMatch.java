package com.ozone.ciff;

import java.util.ArrayList;
import java.util.List;

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
	
	
	@Test
	public void testItalizedAndSpecialCharacters(){
		String bio = "Yeva is in trouble. She s fled to an Armenian village with her young daughter Nareh, as she is suspected of having a role in her husband Vrouyr s death. Hiding out with her aunt and uncle in the place where she once worked as a doctor during the Armenia-Azerbaijan war, Yeva attempts to start over as a teacher. Her presence soon draws suspicion, as she can t produce any personal identification, but when a sudden tragedy strikes, some villagers question how a teacher can have her medical skills. As it turns out, Yeva is also carrying burdensome secrets (and her own scars from the war), which soon come to light as other facets of her past become known. When YEVA s drama and tension start to ramp up, the truth finally starts to emerge although will it be enough to successfully help her escape? This moving film illustrates the deep sacrifices parents make to protect their children and the myriad ways in which family secrets reverberate across generations. (In Armenian with subtitles)  A.Z.YEVA was selected as Armenia s entry for Best Foreign Language Film for this year s Academy Awards .";
		List<String> rewards = Run2.getPrestigiousAwards(bio);
		for(String reward : rewards){
			System.out.println(reward);
		}
	}
	
	@Test
	public void testSubstringFromAnotherAward(){
		String bio = "When Mia s husband Frederik is caught embezzling 12 million dollars from the school where they both work, his behavior is blamed on a brain tumor found in his frontal lobe. Mood swings, fits of anger, poor judgement, and impulse control are all signals of disturbance to this area of the brain. However, through flashbacks and testimonials, it is revealed that many of these symptoms resemble Frederik s natural temperament before he got sick. As the line between Frederik s personality and the effects of his tumor becomes less clear, Mia is led down a rabbit hole that will tear her family apart and leave her questioning if she ever really knew her husband. YOU DISAPPEAR is a haunting and thought-provoking psychological thriller that explores the mysteries of the brain along with one big question: Are we driven purely by chemistry, or does free will exist? (In Danish and Swedish with subtitles)  G.S.YOU DISAPPEAR was selected as Denmark s entry for Best Foreign Language Film for this year s Academy Awards . Peter Schnau Fog was born in 1971 in Denmark. He studied at the University of Copenhagen and FAMU in Prague before graduating in 1999 from the National Film School of Denmark. His graduation project Lille Mnsk was shortlisted for Best Foreign Film at the Student Academy Awards.";
		List<String> awards = new ArrayList<String>();
		String processedText = bio.replaceAll("&[a-z]+;","");
		int numOfAwards = processedText.split(Run2.AWARD).length-1;
		
		for(int i=0;i<numOfAwards;i++){
			String award = processedText.replaceAll(".*[^A-Za-z][a-z]+ " + Run2.AWARD + ".*", "$1");
			awards.add(award);
			String primer = processedText.replaceAll(".*(([^A-Za-z][a-z]+){2} " + Run2.AWARD + ").*", "$1");
			System.out.println("AWARD: " + award);
			System.out.println("PRIMER: " + primer);
			processedText = processedText.replaceAll(primer + ".*","");
		}
		System.out.println(awards);
	}
}
