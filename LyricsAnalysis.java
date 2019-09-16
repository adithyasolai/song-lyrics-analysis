import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class LyricsAnalysis {
	
	private static String FRANK_OCEAN_BLONDE = "FrankOceanBlondeLyrics.txt";
	private static String DANIEL_CAESAR_FREUDIAN = "DanielCaesarFreudianLyrics.txt";
	private static String AESOP_ROCK_NONE_SHALL_PASS = "AesopRockNoneShallPassLyrics.txt";
	
	private Path lyricsTextPath;
	private String lyricsTextFileName;
	
	private String artist;
	
	private int totalWords, uniqueWords;
	
	public double uniqueToTotalRatio;
	
	private HashMap<String, Integer> randomOrder;
	private LinkedHashMap ascendingOrder, descendingOrder;
	
	public LyricsAnalysis(String artistName) throws IOException{
		if(artistName.equals("Frank Ocean")) {
			lyricsTextFileName = LyricsAnalysis.FRANK_OCEAN_BLONDE;
			artist = "Frank Ocean";
		} else if(artistName.equals("Daniel Caesar")) {
			lyricsTextFileName = LyricsAnalysis.DANIEL_CAESAR_FREUDIAN;
			artist = "Daniel Caesar";
		} else if(artistName.equals("Aesop Rock")) {
			lyricsTextFileName = LyricsAnalysis.AESOP_ROCK_NONE_SHALL_PASS;
			artist = "Aesop Rock";
		}
		
		System.out.println(artist + " Initialized.");
		
		lyricsTextPath = Paths.get(lyricsTextFileName);
		
		randomOrder = new HashMap<String, Integer>();
		this.readFromFile(randomOrder);
		
		ascendingOrder = this.sortByAscendingFrequency();
		
		descendingOrder = this.sortByDescendingFrequency();
		
		uniqueWords = randomOrder.size();

		int total=0;
		for(String key : randomOrder.keySet()) {
			total += randomOrder.get(key);
		}
		totalWords = total;
		
		uniqueToTotalRatio = uniqueWords / (double) totalWords;
	}
	// The following code taken from class example! Written by Fawzi Emad.
	private void readFileAndDisplayCounts(Map<String, Integer> map) throws IOException {
		readFromFile(map);
		showMap(map);
	}
	
	private void readFromFile(Map<String, Integer> map) throws IOException {
		
		Iterator<String> s = new Scanner(lyricsTextPath);   
		
		while (s.hasNext()) {
			String word = s.next();
			word = forceUppercaseAndRemoveWeirdSymbols(word);
			if (word != "") {
				Integer currentCount = map.get(word);  // may be null
				if (currentCount == null) {
					currentCount = 0;
				}
				map.put(word, currentCount + 1);
			}
		}
		((Scanner) s).close();
	}
	
	private void showPartOfMap(Map<String, ?> map) {
		int count = 0;
		for (String key : map.keySet()) {
			System.out.print(key + ": " + map.get(key) + "  ");
			if (count++ == 100) {
				break;
			}
		}
		System.out.println("\n\n");
	}
	
	
	private String forceUppercaseAndRemoveWeirdSymbols(String word) {
		if(word.startsWith("[") || word.endsWith("]")) {
			return "";
		}
		word = word.toUpperCase();
		String newWord ="";
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				newWord += c;
			}
		}
		return newWord;
	}
	
	/*
	 * This method sorts the words in the given by lyrics by the frequency 
	 * in which they occur. The words are stored in descending order of 
	 * frequency with the most frequently used words first and the least 
	 * frequently used words last. 
	 */
	public LinkedHashMap<String, Integer> sortByDescendingFrequency() throws IOException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		readFromFile(map);
		List<String> wordList = new ArrayList<>(map.keySet());
		Collections.sort(wordList, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				return map.get(b) - map.get(a);
			}
		});
		LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (String s : wordList) {
			sortedMap.put(s, map.get(s));
		}
		
		return sortedMap;
	}
	
	private void showByDescendingFrequency() {
		System.out.println("DESCENDING FREQUENCY: ");
		showPartOfMap(this.descendingOrder);
	}
	
	//The code above is from a class example! Written by Fawzi Emad.
	
	//The code below is written by Adithya Solai. 
	
	/*
	 * This method sorts the words in the given by lyrics by the frequency 
	 * in which they occur. The words are stored in descending order of 
	 * frequency with the least frequently used words first and the most
	 * frequently used words last. 
	 */
	private LinkedHashMap<String, Integer> sortByAscendingFrequency() throws IOException {
		Map<String, Integer> words = new HashMap<String, Integer>();
		readFromFile(words);

		Comparator<String> comparator = new Comparator<String>() {

			@Override
			public int compare(String word1, String word2) {
				return words.get(word1) - words.get(word2);
			}
			
		};
		
		List<String> sortedByInt = new ArrayList<String>();
		for(String key: words.keySet()) {
			sortedByInt.add(key);
		}
		Collections.sort(sortedByInt, comparator);
		
		LinkedHashMap<String, Integer> sortedByIntLL = new LinkedHashMap<String, Integer>();
		for(String key: sortedByInt) {
			sortedByIntLL.put(key, words.get(key));
		}
		
		return sortedByIntLL;
		
	}
	
	private void showByAscendingFrequency() {
		System.out.println("ASCENDING FREQUENCY: ");
		showPartOfMap(this.ascendingOrder);
	}
	//Prints out all elements in a specified Map to the console. 
	private void showMap(Map<String, Integer> map) { //O(N)
		for (String key : map.keySet()) {
			System.out.print(key + ": " + map.get(key) + "  ");
		}
		System.out.println("\n\n");
	}
	
	private void showTotalNumberOfWords() {
		System.out.println("TOTAL NUMBER OF WORDS: " + this.totalWords);
	}
	//Prints out the number of unique words in the specified lyrics text. 
	private void showUniqueWords() {
		System.out.println("NUMBER OF UNIQUE WORDS: " + this.uniqueWords);
	}
	
	private void showUniqueToTotalRatio() {
		System.out.print("UNIQUE WORDS TO TOTAL WORDS RATIO: ");
		System.out.printf("%.2f",this.uniqueToTotalRatio * 100);
		System.out.println("%");
	}
	//Prints out the number of unique phrases of length 2 to the console. 
	private void showUniquePhrasesLength2() throws IOException {
		Iterator<String> s = new Scanner(lyricsTextPath); 
		
		Set<String> words = new HashSet<String>();
		
		String prevWord=null;
		
		while (s.hasNext()) {
			String currWord = s.next();
			currWord = forceUppercaseAndRemoveWeirdSymbols(currWord);
			if(prevWord!=null) {
				if(currWord.equals("")==false && prevWord.equals("")==false) {
					words.add(prevWord + " " + currWord);
				}
			}
			prevWord=currWord;
		}
		
		((Scanner) s).close();
		
		System.out.println("NUMBER OF UNIQUE PHRASES OF LENGTH 2: " + words.size());
	}
	
	private void sortPhraseOfLength2ByDescendingFrequency() throws IOException{
		Iterator<String> s = new Scanner(lyricsTextPath); 
		
		Map<String, Integer> arbitraryOrderMap = new HashMap<String, Integer>();
		Map<String, Integer> consecutiveOrderMap = new LinkedHashMap<String, Integer>();
		
		String prevWord=null;
		
		while (s.hasNext()) {
			String currWord = s.next();
			currWord = forceUppercaseAndRemoveWeirdSymbols(currWord);
			
			if(prevWord!=null) {
				if(currWord.equals("")==false && prevWord.equals("")==false) {
					String phrase = prevWord + " " + currWord;
					Integer currCount = arbitraryOrderMap.get(phrase);
					if(currCount==null) {
						arbitraryOrderMap.put(phrase, 1);
						consecutiveOrderMap.put(phrase, 1);
					} else {
						arbitraryOrderMap.put(phrase, currCount+1);
						consecutiveOrderMap.put(phrase, currCount+1);
					}
				}
			}
			prevWord=currWord;
		}
		
		((Scanner) s).close();
		
		System.out.println("PHRASES IN RANDOM ORDER");
		showPartOfMap(arbitraryOrderMap);
		
		System.out.println("PHRASES IN (SOMEWHAT)CONSECUTIVE ORDER");
		showPartOfMap(consecutiveOrderMap);
	}
	
	private void showUniqueToTotalRatio(Set<String> names) throws IOException {
		Map<String, Integer> ratios = new HashMap<String, Integer>();
		for(String name : names) {
			LyricsAnalysis curr = new LyricsAnalysis(name);
			ratios.put(name, (int)(curr.uniqueToTotalRatio*100));
		}
		List<String> ascendingArtists = new ArrayList<String>();
		ascendingArtists.addAll(ratios.keySet());
		Collections.sort(ascendingArtists, new Comparator<String>() {

			@Override
			public int compare(String artist1, String artist2) {
				return ratios.get(artist1)-ratios.get(artist2);
			}
		
		});
		
		Map<String, Integer> sorted = new LinkedHashMap<String, Integer>();
		for(String name : ascendingArtists) {
			sorted.put(name, ratios.get(name));
		}
		showPartOfMap(sorted);
	}
	public static void main(String[] args) throws IOException {
		//O(N) to load this set
		Set<String> artists = new HashSet<String>(Arrays.asList("Frank Ocean", 
																"Daniel Caesar",
																"Aesop Rock"));
		
		LyricsAnalysis a = new LyricsAnalysis("Frank Ocean");
		a.showByDescendingFrequency();
		a.showByAscendingFrequency();
		a.showTotalNumberOfWords();
		a.showUniqueWords();
		a.showUniqueToTotalRatio();
		//a.showUniquePhrasesLength2();
		//sortPhraseOfLength2ByDescendingFrequency();
		a.showUniqueToTotalRatio(artists);
	}
}
