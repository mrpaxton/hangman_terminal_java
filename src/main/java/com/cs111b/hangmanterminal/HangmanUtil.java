/*
 * Programmer:   Sarn Wattanasri
 * Date: 12/15/13
 * Program Name: HangmanUtil.java
 * Objective: A utility class that contains utility methods that will be 
 *            used by the Hangman Game.
 */

package com.cs111b.hangmanterminal;

import java.util.*;
import java.io.*;

public class HangmanUtil{

    public static boolean sameUniqueCharsInBothLists( List<Character> list1, 
                                                      List<Character> list2 ) {
        //remove duplicates, put elements in the list in order
        Set<Character> s1 = new TreeSet<Character>( list1 );
        Set<Character> s2 = new TreeSet<Character>( list2 );
        return s1.equals(s2);
    }

    public static boolean isCharRepeating( char guess, 
                                     List<Character> charList ) {
        boolean repeating = false;
        for( char c : charList ) {
            if( guess == c ) {
                repeating = true;
                break;
            }
        }
        return repeating;
    }

    public static List<Character> getRandomWordChars( String fileName, 
    	                                              String selectedWord ) {
        return stringToCharArray( selectedWord );
    }

    public static boolean isValidFile( String resources, String fileName ) {
    	boolean validFile = false;
        
        File file = new File( resources, fileName );
        if( file.exists() && file.canRead() ) {
            validFile = true;
        } 
    	return validFile;
    }

    public static boolean isLetterGuess(char guess ) {
    	return Character.isLetter( guess );
    }

    //convert String to char[] array
    public static List<Character> stringToCharArray( String str ) {
        List<Character> charactersOfString = new ArrayList<Character>();
        char[] letters = str.toCharArray();
        for( char c : letters ) 
            charactersOfString.add( c );
        return charactersOfString;
    }

    //read from file, return the list of lines
    public static List<String> readLines( String resources, String fileName) {
        List<String> resultList = new ArrayList<String>();
        File textFile = new File( resources, fileName );
        BufferedReader br = null;
        
        try{
            //System.out.println("debug: textFile.getCanonicalPath() : " + 
            //                   textFile.getCanonicalPath() );
            
            br = new BufferedReader( 
                            new InputStreamReader( 
                            new FileInputStream( textFile )));
            String lineOfText;
            while( (lineOfText = br.readLine()) != null ) {
                resultList.add( lineOfText );
            }

        } catch( IOException ioe) {
            System.err.println("BufferedReader does not read");
        } finally { 
            try { 
                br.close(); 
            } catch (IOException ioe) {
                System.err.println("BufferedReader did not close " +
                                   "properly.");
            }
        }

        
        return resultList;
    }

    //auto generate a random number from a (inclusive) to b (inclusive)
    public static int rand( int a, int b) {
        return ( (int)( (b-a+1) * Math.random() + a) );
    }

    //get the number of words
    public static int getNumWords( List<String> words ) {
      if ( words != null && ! words.isEmpty() ) 
          return words.size();
      else 
          return 0;
    }

    //randomly choose word from a given list
    public static String chooseWord( List<String> words ) {
        int randIndex = rand( 0, getNumWords( words ) - 1);
        return words.get( randIndex );
    }

    //Determines if the guess is correct
    public static boolean isCorrectGuess( List<Character> selectedChars, 
    	                                  char guess ) {
        boolean correctGuess = false;
        for( char c : selectedChars ) {
            if( guess == c ) {
                correctGuess = true;
                break;
            }
        }
        return correctGuess;
    }
}
