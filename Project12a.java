
/*
 * Project12.java
 * 
 *   A program that plays simple word guessing game.  In this game the user provides a list of 
 *   words to the program.  The program randomly selects one of the words to be guessed from 
 *   this list.  The player then guesses letters in an attempt to figure out what the hidden 
 *   word might be.  The number of guesses that the user takes are tracked and reported at the 
 *   end of the game.
 *   
 *   See the write-up for Project 12 for more details.
 * 
 * @author Tyler McDowell
 * 
 */
package osu.cse1223;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class Project12a {

	
	public static void main(String[] args) {
		// Fill in the body
		Scanner keyboard = new Scanner(System.in);
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Character> charList = new ArrayList<Character>();
		String word = "";
		String blankWord = "";
		String currentGuess = "";
		String wordGuess = "";
		String rematch = "";
		int count = 0;
		char guess = ' ';
		boolean playGame = true;
		boolean keepPlaying = true;
		boolean checkWord = false;
		boolean checkList = false;
		
		System.out.print("Enter a filename containing your wordlist: ");
		String fileName = keyboard.nextLine();
		
		try {
			Scanner inputFile = new Scanner(new File(fileName));
			list = getList(inputFile);
			int listSize = list.size()-1;
			inputFile.close();
			System.out.println("Read "+listSize+" words from the file");
			System.out.println();
			
			while (playGame == true){
				//reset game
				word = getRandomWord(list);
				blankWord = starWord(word);
				keepPlaying = true;
				count = 0;
				charList.removeAll(charList);
				
				if (count == 0){
					currentGuess = blankWord;
				}
			
				System.out.println("The word to guess is: "+ blankWord);
				
				while (keepPlaying == true){
					if (count > 0){
					System.out.println("The word to guess is now: "+ currentGuess);
					}
					//System.out.println("Previous characters guessed: "+ charGuessed(charList)); see method below (can not get to work) =(
					System.out.println("Previous characters guessed: "+ charList);
					guess = getCharacterGuess(keyboard);
					count = count + 1;
					
					checkList = checkInList(guess, charList);
					if (checkList == true){
						System.out.println("You have already gueed "+guess+" try another character");
						System.out.println();
					}
					else {
						checkList = false;
						charList.add(guess);
						System.out.println("The character "+guess+" occurs in "+checkChar(guess, word)+" positions");
						System.out.println();
						currentGuess = modifyGuess(guess, word, currentGuess);//may be a bad place for this
						System.out.println("The word to guess is now: "+ currentGuess);//check to see if guess is correct
					}	
					
					System.out.print("Enter your guess: ");
					wordGuess = keyboard.next();
					wordGuess = wordGuess.toUpperCase();
					checkWord = checkWord(wordGuess, word);
				
					if (checkWord == true){
						System.out.println("Congratulations! "+word+" is the correct word.");
						System.out.println();
						System.out.println("You achieved the correct answer in "+count+" guesses!");
						System.out.print("Would you like a rematch [y/n]? ");
						rematch = keyboard.next();
							if (rematch.charAt(0) == 'y' || rematch.charAt(0) == 'Y'){
								playGame = true;
								keepPlaying = false;
								System.out.println();
							}
							else if  (rematch.charAt(0) == 'n' || rematch.charAt(0) == 'N'){
								playGame = false;
								keepPlaying = false;
								System.out.println();
								System.out.println("Thanks for playing! Goodbye!");
							}
					}
					else {
						keepPlaying = true;
						System.out.println("That is not the correct word.");
						System.out.println();
						System.out.println("Please guess another letter and try again.");
					}
						
					
					//System.out.println();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR! while trying to read input file.");
		}
		
	}
	
	//does not work, an attempt to make the characters guessed look like a nice clean string, puts the program
	//into an infinite loop.
	private static String charGuessed(ArrayList<Character> inChar){
		String charGuessed = "";
		
		for (int i = 0; i < inChar.size(); i++){
			charGuessed = charGuessed + " , " + inChar.add(inChar.get(i));
		}
		return charGuessed;
	}
	
	// Given a Scanner as input, returns a List<String> of strings read from the Scanner.
	// The method should read words from the Scanner until there are no more words in the file
	// (i.e. inScanner.hasNext() is false).  The list of strings should be returned to the calling program.
	private static ArrayList<String> getList(Scanner inScanner) {
		// works
		ArrayList<String> list = new ArrayList<String>();
		String line = inScanner.nextLine();
		while (inScanner.hasNext()){
			list.add(line);
			line = inScanner.nextLine();
		}
		
		return list;
	}

	// Given two strings as input, compares the first string (guess) to the second
	// (solution) character by character.  If the two strings are not exactly the same,
	// return false.  Otherwise return true.
	private static boolean checkWord(String guess, String solution) {
		// Finished
		boolean checkWord = false;
		if (guess.equals(solution)){
			checkWord = true;
		}
		else {
			checkWord = false;
		}
		return checkWord;
	}
	
	
	// Given a ArrayList<String> list of strings as input, randomly selects one of the strings
	// in the list and returns it to the calling program.
	private static String getRandomWord(ArrayList<String> inList) {
		// works
		int random = (int)(Math.random()*(inList.size()-1));
		String word = inList.get(random);
		//forces everything to be in upper case
		String toUpper = word.toUpperCase();
		return toUpper;
	}
	

	// Given a Scanner as input, prompt the user to enter a character.  If the character
	// enters anything other than a single character provide an error message and ask
	// the user to input a single character.  Otherwise return the single character to
	// the calling program.
	private static char getCharacterGuess(Scanner inScanner) {
		// finished
		String guess = "";
		char guessCharacter = ' ';
		int breakOut = 0;
		String convertCase = "";

		while (breakOut == 0){
			
		System.out.print("Enter a character to guess: ");
		guess = inScanner.next();
			
			if (guess.length() == 1){
				guessCharacter = guess.charAt(0);
				breakOut = 1;
			}
			else {
				System.out.println("ERROR! enter a single character.");
			}
		}
		//forces everything to be in upper case
		convertCase = "" + guessCharacter;
		String toUpper = convertCase.toUpperCase();
		guessCharacter = toUpper.charAt(0);
		
		//System.out.println("test guess" +guessCharacter);
		
		return guessCharacter;
	}
	
	// Given a character inChar and a ArrayList<Character> list of characters, check to see if the
	// character inChar is in the list.  If it is, return true.  Otherwise, return false. 
	private static boolean checkInList(char inChar, ArrayList<Character> inList) {
		// Fill in the body
		boolean checkList = false;
		for (int i = 0; i < inList.size(); i++){
			 if (inChar == inList.get(i)){
				checkList = true;
			 }
		}
		return checkList;
	}
	
	// Given a String, return a String that is the exact same length but consists of 
	// nothing but '*' characters.  For example, given the String DOG as input, return
	// the string ***
	private static String starWord(String inWord) {
		// works
		String starWord = "";
		for (int i = 0; i < inWord.length(); i++){
			starWord = starWord + "*";
		}
		
		return starWord;
	}
	
	// Given a character and a String, return the count of the number of times the
	// character occurs in that String.
	private static int checkChar(char guessChar, String guessWord) {
		// Finished
		int checkChar = 0;
		
		for (int i = 0; i < guessWord.length(); i++){
			if (guessChar == guessWord.charAt(i)){
				checkChar = checkChar + 1;
			}
		}
		
		return checkChar;
	}

	// Given a character, a String containing a word, and a String containing a 'guess'
	// for that word, return a new String that is a modified version of the 'guess' 
	// string where characters equal to the character inChar are uncovered.
	// For example, given the following call:
	//   modfiyGuess('G',"GEOLOGY", "**O*O*Y")
	// This functions should return the String "G*O*OGY".
	private static String modifyGuess(char inChar, String word, String currentGuess) {
		// works
		String modifyGuess = "";
		int i = 0;
		
		while (i < word.length()){
			if (inChar == word.charAt(i)){
				modifyGuess = modifyGuess + inChar;
			}
			else if (currentGuess.charAt(i) != '*'){
				modifyGuess = modifyGuess + currentGuess.charAt(i);
			}
			else{
				modifyGuess = modifyGuess + "*";
			}
			i++;
		}
		return modifyGuess;
	}
}

