/*
 * Programmer:   Sarn Wattanasri
 * Date: 12/15/13
 * Program Name: HangmanTerminal.java
 * Objective: The Text-Based version of the Hangman Game. 
 *            Allow 7 guesses and only a single letter guess.
 *            The dictionary file can vary.
 */

package com.cs111b.hangmanterminal;

import java.util.*;
import java.io.*;
import java.text.*;

public class HangmanTerminal extends Hangman{

    public static final String PROJECT_RESOURCES = 
           "/Users/Pax/maven_projects/maven3example/hangmanterminal/" +
           "hangmanterminal/src/main/resources/";
    private Scanner scanner;
    private int numIncorrectGuesses;
    private int maxNumGuessesAllowed;
    private List<Character> correctGuesses;
    private List<Character> allUserGuesses;
    private List<Character> selectedChars; 
    private boolean userWins;
    private String selectedWord;

    //Extra credit #1
    private int numOfWins;
    private int numOfLosses;
    private double winPercentage;

    public HangmanTerminal() {
        numIncorrectGuesses = 0;
        maxNumGuessesAllowed = MAX_NUM_GUESSES;
        correctGuesses = new ArrayList<Character>();
        allUserGuesses = new ArrayList<Character>();
        selectedChars = new ArrayList<Character>();
        userWins = false;
        scanner = new Scanner(System.in);
        numOfWins = 0;
        numOfLosses = 0;
        winPercentage = 0.0;
    }

    public void start() {
        boolean playing = true;
        String fileName, userInput;
        List<String> words;
        
        while( playing ) {
            fileName = readFileName( scanner );
            words = HangmanUtil.readLines( PROJECT_RESOURCES, fileName );
            selectedWord = HangmanUtil.chooseWord( words ).toUpperCase();
            selectedChars = 
                HangmanUtil.getRandomWordChars( fileName, selectedWord );

            play();
            showsStats();
            System.out.print( "Play again? (y/n): ");
            userInput = scanner.next();
            if( userInput.equalsIgnoreCase("n") ||
            	userInput.equalsIgnoreCase("no")) {
                playing = false;
            } else if ( userInput.equalsIgnoreCase("y") ||
            	        userInput.equalsIgnoreCase("yes")) {
                reset();
                continue;
            }
        }
    }

    public void reset() {
        numIncorrectGuesses = 0;
        maxNumGuessesAllowed = MAX_NUM_GUESSES;
        correctGuesses = new ArrayList<Character>();
        allUserGuesses = new ArrayList<Character>();
        selectedChars = new ArrayList<Character>();
        userWins = false;
    }

    private void play() {
        char guess;
        //the currentNumIncorrectGuesses is for the drawing purposes
        int currentNumIncorrectGuesses = numIncorrectGuesses;

        //show all blank letters representing the hidden word
        System.out.println("\n\nLet's Start!");
        showGuessResult( selectedChars, correctGuesses);

        //A game completes when the user guessed all letters correctly or
        //the user has exceeded the number of incorrect guesses
        while( ( numIncorrectGuesses < maxNumGuessesAllowed ) &&
                ! HangmanUtil.sameUniqueCharsInBothLists( selectedChars, 
                	                                      correctGuesses)) {
            //get the user guess via the scanner in the readUserGuess() method
            guess = readUserGuess();

            //skip the process of hangman when the guess is repeating
            if( HangmanUtil.isCharRepeating( guess, allUserGuesses)) {
                System.out.println("You have already guessed: " + guess);
                printHangmanStatus();
                continue;
            } else {
                allUserGuesses.add( guess );
            } 
            
            if( HangmanUtil.isCorrectGuess( selectedChars, guess ) ) {
                correctGuesses.add( guess );
            } else {
                numIncorrectGuesses++;
                //The currentNumIncorrectguesses is for drawing the picture
                currentNumIncorrectGuesses = numIncorrectGuesses;
            }

            //display the result of each guess: underscores as hidden letters
            //and revealed letters for the correct guesses
            showGuessResult( selectedChars, correctGuesses); 
            
            showHangmanPictureAndStatus( currentNumIncorrectGuesses, 
                                         selectedWord );
        }
    }

    private void showHangmanPictureAndStatus(int currentNumIncorrectGuesses,
    	                                     String selectedWord ) {
    	//draw a hangman picture based on the current number of 
        //incorrect guess
        if(currentNumIncorrectGuesses != 0 )
            drawHangman( currentNumIncorrectGuesses );

        //print the status of hangman report. 
        //Ex: You have n guesses remaining.  
        //    You have guessed: [a, b, c ]
        printHangmanStatus(); 

        //add the number of wins and draw the picture to reflect 
        //that the user has won the game
        if( HangmanUtil.sameUniqueCharsInBothLists( selectedChars, 
        	                                        correctGuesses )) {
            numOfWins++;
            drawWinHangman();
        }

        //when no further guesses is allowed, show the result of loss,
        //and reveal the selected word
        if( numIncorrectGuesses == maxNumGuessesAllowed ) {
            System.out.println("Sorry, you did not guess it. " + 
                               " The word was " + selectedWord);
            numOfLosses++;
        }
    }

    private void printHangmanStatus() {
        int numRemaining = maxNumGuessesAllowed - numIncorrectGuesses; 
        if( numRemaining > 0 ) {
            System.out.println("You have " + numRemaining + 
                               " guesses remaining.");
            System.out.println("You have guessed: " + allUserGuesses );
        }
    }

    //Shows the stats of the wins and losses and the win percentage
    private void showsStats() {
        int totalPlays = numOfWins + numOfLosses;
        winPercentage = ( 1.0 * numOfWins ) / totalPlays;
        NumberFormat formatter = NumberFormat.getPercentInstance();
        System.out.println("Stats: " + numOfWins + " Wins and " +
                            numOfLosses + " Losses");
        System.out.println("You have won " + formatter.format(winPercentage) + 
                           " of the time.\n\n");
    }

    private char readUserGuess( ) {
        boolean singleLetter = false;
        String guessString = "";
        while( ! singleLetter ) {
            System.out.print( "\n\nGuess a letter: ");
            guessString = scanner.next();

            //Part2: Situation3 : the user guess can't be longer than one char
            try{
                if( guessString.length() > 1 ) {
                    throw new GreaterThanOneCharException();
                }
            } catch( GreaterThanOneCharException gtoce) {
                gtoce.printExceptionMessage();
                continue;
            }

            //the program will not take the special character that is not a letter
            //Part2: Situation2 : the user enters a guess that is not
            //       a character( like + or $)
            try{
                if( ! HangmanUtil.isLetterGuess( guessString.charAt(0))) {
                    throw new NotALetterException();
                }
            } catch( NotALetterException nale ) {
                nale.printExceptionMessage();
                continue;
            }
            
            singleLetter = true;
        }
        
        char guess = guessString.toUpperCase().charAt(0);
        return guess;
    }

    private void showGuessResult( List<Character> selectedChars, 
                                  List<Character> correctGuesses ) {
        boolean correctLetter = false;
        for( char letter : selectedChars ) {
            for( char guess : correctGuesses ) {
                if ( guess == letter ) 
                    correctLetter = true;
            }
            
            if ( correctLetter ) 
                System.out.print( letter + " " );
            else 
                System.out.print( "_ ");

            correctLetter = false;
        }
        System.out.println("\n");
    } 

    private String readFileName( Scanner scanner ) {

        boolean validFile = false;
        String fileName = "";

        // //Part2 Situation1 and Extra Credit #2
        // //The dictionary file must exist; otherwise the program asks
        // //for it repeatedly
        while( ! validFile ) {
            System.out.println("Enter the dictionary file name: ");
            fileName = scanner.next();

            try {
                if( fileName.equalsIgnoreCase("q") ||
                    fileName.equalsIgnoreCase("quit") ||
                    fileName.equalsIgnoreCase("exit")) {
                    System.exit(0);
                }
                //try to use a scanner with the file to test the file
                new Scanner( new File( PROJECT_RESOURCES, fileName ));
                validFile = HangmanUtil.isValidFile( PROJECT_RESOURCES, fileName );
                
            } catch( FileNotFoundException fnfe ) {
                 System.err.println("The file does not exist!");
                 System.err.println("Please try again.( Q or Quit to exit )");
                 continue;
            }
        }
        return fileName;
    }
    
    //Draw a picture of hangman when the user has won the game.
    private void drawWinHangman(){
    	System.out.println("\nCongrats! You've guessed the word correctly");
    	System.out.println("========================================");
        System.out.println("Thanks!!! You Saved My Life!!!");
        System.out.println("     ");
        System.out.println("   \\O/");
        System.out.println("    |");
        System.out.println("   / \\");
        System.out.println("     ");
        System.out.println("========================================");
    }

    //Draw a picture of hangman when the user has lost the game.
    private void drawLostHangman(){
        System.out.println(" ----");
        System.out.println(" |  |");
        System.out.println(" |  O");
        System.out.println(" | /|\\");
        System.out.println(" | / \\");
        System.out.println(" ----");
    }

    //Draw a hangman given the number of the incorrect guesses
    private void drawHangman(int numIncorrectGuesses){
        String[] manParts = new String[7];
        manParts[0] = "    O";
        manParts[1] = "    O\n" + "    |"; 
        manParts[2] = "    O\n" + "   /|";
        manParts[3] = "    O\n" + "   /|\\";
        manParts[4] = "    O\n" + "   /|\\\n" + "   / ";
        manParts[5] = "    O\n" + "   /|\\\n" + "   / \\";
        manParts[6] = "    |\n" + "    O\n" + "   /|\\\n" + "   / \\";
        
        switch(numIncorrectGuesses)
        {
            case 1: System.out.println(manParts[0]);break;
            case 2: System.out.println(manParts[1]);break;
            case 3: System.out.println(manParts[2]);break;
            case 4: System.out.println(manParts[3]);break;
            case 5: System.out.println(manParts[4]);break;
            case 6: System.out.println(manParts[5]);break;
            case 7: drawLostHangman(); break;
        }
    }
}
