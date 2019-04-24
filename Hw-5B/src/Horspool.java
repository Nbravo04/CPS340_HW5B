import java.util.HashMap;
import java.util.Random;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;

/**
 * A collection of methods to support string matches.
 * 
 * @author eickh1jl
 * @since 2018-03-15
 */

public class Horspool {

	public static final String CHARACTER_POOL = "ABCDEFGHIJLKMNOPQRSTUVWXYZ ,!?\"'/\\-()";

	public static void main(String[] args) throws IOException {

		Scanner scnr = new Scanner(new File("98-0.txt"));
 		String[] patternBag = {"HELLO", "WORK", "SHALL", "ROB", "MOB"};
        String corpus = "";
		char[] alphabet, randomAlphabet;
		char[] text, randomText;
		char[] pattern;
		
		for(int i = 0; i < 100 && scnr.hasNextLine(); i++) {
        	scnr.nextLine();

		}

		for(int i = 0; i < 100 && scnr.hasNextLine(); i++) {
			corpus += " " + scnr.nextLine().toUpperCase();
		}

		alphabet = ExtractAlphabet(corpus);
		text = corpus.toCharArray();
		
		System.out.printf("%-15s %-10s %-4s %-12s %-4s\n-------\n", 
				"Pattern", "Horspool", "%", "Brute Force", "%");
		
		for(int i = 0; i < patternBag.length; i++) {
			pattern = patternBag[i].toCharArray();
			ShiftTable st = new ShiftTable(pattern, alphabet);

			int HorspoolCompCount = HorspoolMatch(text, pattern, st);
			int BruteCompCount = BruteForceMatch(text, pattern);
			
			System.out.printf("%-15s %-10d %-4.2f %-12d %-4.2f\n", 
					patternBag[i], HorspoolCompCount, (float) HorspoolCompCount / text.length, 
					BruteCompCount, (float) BruteCompCount / text.length);
			
		}
 						
		// Also do for random string
		Random r = new Random();
		randomAlphabet = CreateRandomAlphabet(r.nextInt(CHARACTER_POOL.length()));
		randomText = CreateRandomText(randomAlphabet, 3000);
		
		System.out.println("=================================================");
		System.out.println("                  Random String                  ");
		System.out.println("=================================================");
		System.out.printf("%-15s %-10s %-4s %-12s %-4s\n-------\n", 
				"Pattern", "Horspool", "%", "Brute Force", "%");
		
		for(int i = 0; i < patternBag.length; i++) {
			pattern = patternBag[i].toCharArray();
			ShiftTable st2 = new ShiftTable(pattern, randomAlphabet);

			int HorspoolCompCountRandom = HorspoolMatch(randomText, pattern, st2);
			int BruteCompCountRandom = BruteForceMatch(randomText, pattern);
			
			System.out.printf("%-15s %-10d %-4.2f %-12d %-4.2f\n", 
					patternBag[i], HorspoolCompCountRandom, (float) HorspoolCompCountRandom / text.length, 
					BruteCompCountRandom, (float) BruteCompCountRandom / text.length);
			
		}
		
		//Close the freakin' scanner!
		scnr.close();
	}

	
	/**
	 * Extract all of the characters in a provided string.
	 * 
	 * @param text A string representing a source text.
	 * @return An array containing all characters present in the string.
	 */
	public static char[] ExtractAlphabet(String text) {
		String alphabet = "";

		for(int i = 0; i < text.length(); i++) {
			if(alphabet.indexOf(text.charAt(i)) == -1) {
				alphabet += text.charAt(i);
			} 
		}

		return alphabet.toCharArray();

	}

	/**
	 * Creates an alphabet that is a randomly selected subset of
	 *   of the CHARACTER_POOL. 
	 * 
	 * @param size The number of characters to select for the alphabet.
	 * @return An array of characters that represents the alphabet.
	 */
	public static char[] CreateRandomAlphabet(int size) {
		Random r = new Random();
		String alphabet = "";

		int i = 0;
		while(i < size) {

			int idx = r.nextInt(CHARACTER_POOL.length());
			char c = CHARACTER_POOL.charAt(idx);   
			if(alphabet.indexOf(c) == -1) {
				alphabet += c;
				i++;
			}

		}

		return alphabet.toCharArray();
	}

	/**
	 * Create a random text of a specified size using a provided alphabet.
	 * 
	 * @param alphabet Source of characters that can be used to construct a text.
	 * @param size The length of the text to construct.
	 * @return An array of characters representing the text.
	 */
	public static char[] CreateRandomText(char[] alphabet, int size) {

		Random r = new Random();

		String text = "";

		for(int i = 0; i < size; i++) {
			text += alphabet[r.nextInt(alphabet.length)]; 
		}

		return text.toCharArray();
	}

	/**
	 *   Searches for a pattern in a text using Horspool's algorithm.  It
	 *   steps through the text, shifting the pattern to the left using an 
	 *   offset from a shift table.  
	 *   
	 *   THE METHOD SHOULD COUNT THE NUMBER OF COMPARISIONS AND RETURN THIS
	 *   VALUE.  DO NOT RETURN THE LOCATION OF THE MATCH OR -1 IF A MATCH
	 *   DOES NOT EXIST.
	 * 
	 * @param text The source text to search
	 * @param pattern The pattern to search for in the source text.
	 * @param shiftTable A ShiftTable constructed specifically for provided pattern.
	 * @return The number of comparisons performed to find a match of the pattern in 
	 *    the next or determine that the pattern is not present.
	 */
	public static int HorspoolMatch(char[] text, char[] pattern, ShiftTable shiftTable) {

		int comparisonCount = 0;
		
		//m = length of pattern
		//n = length of text
		//k = index marker
		int m = pattern.length;
		int n = text.length;
		int i = m - 1;
		int k;
		
		while(i <= n - 1 ) {
			k = 0;
			comparisonCount += 1;
			//Starting at the end of the pattern going left comparisons.
			while(k <= m-1 && pattern[m-k-1] == text[i-k]) {
				k += 1;				
				comparisonCount += 1;
			}
			//Complete Match found
			if(k == m) {
				return comparisonCount;
			}
			//Shift by char
			else {
				i += shiftTable.getShiftByChar(text[i]);
			}
		}

		return comparisonCount;
	}

	/**
	 * A brute force text match algorithm.  Aligns the pattern to the text
	 *   and steps through the text, shifting the pattern to the left by
	 *   one character until a match is or no more alignments exist.
	 *   
	 *   THE METHOD SHOULD COUNT THE NUMBER OF COMPARISIONS AND RETURN THIS
	 *   VALUE.  DO NOT RETURN THE LOCATION OF THE MATCH OR -1 IF A MATCH
	 *   DOES NOT EXIST.
	 * 
	 * @param text The source text to search
	 * @param pattern The pattern to search for in the source text.
	 * @return The number of comparisons performed to find a match of the pattern in 
	 *    the next or determine that the pattern is not present.
	 */
	public static int BruteForceMatch(char[] text, char[] pattern) {
		int comparisonCount = 0;
		boolean flag = false;
		
		for(int i=0; i < (text.length - pattern.length); i++) {
			//First Char match
			comparisonCount += 1;
			if (text[i] == pattern[0]) {
				//Loop to see if all chars match
				for( int j=1; j < pattern.length; j++) {
					comparisonCount +=1;
					//Char match and set flag to true
					if (text[i+j] == pattern[j]) {
						flag = true;
					}
					//If a single char doesn't match, flag is set to false and
					//break.
					else {
						flag = false;
						break;
					}
				}
				//If the entire pattern was matched increment the count.
				if(flag) {
					return comparisonCount;
				}
			}
		}
		

		return comparisonCount;

	}


}

