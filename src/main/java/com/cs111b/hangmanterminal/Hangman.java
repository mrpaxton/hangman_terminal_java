/*
 * Programmer:   Sarn Wattanasri
 * Date: 12/15/13
 * Program Name: Hangman.java
 * Objective: An abstract class that defines a Hangman Game.
 *            This class also contains exceptions inner class to handle 
 *            illegal guesses
 */


package com.cs111b.hangmanterminal;

public abstract class Hangman {

    public static final int MAX_NUM_GUESSES = 7;

    public abstract void start();
    public abstract void reset();

    //inner class used when the guess is not a letter
    protected class NotALetterException extends Exception {
        public void printExceptionMessage() {
            System.out.println( "Enter only a letter.");
        }
    }

    //inner class used when the guess is longer than one character
    protected class GreaterThanOneCharException extends Exception {
        public void printExceptionMessage() {
            System.out.println("The guess must be a single letter.");
        }
    }     
}

