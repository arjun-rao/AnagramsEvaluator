package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private HashSet<String> wordSet;
    private ArrayList<String> wordList;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer,ArrayList<String>> sizeToWord;
    private int wordLength;


    //Helper method to generate a key for the hashmap.
    private String keyGenerator(String s)
    {
        //Take string as parameter and return the sorted set of alphabets as array

        //Convert the string parameter to a character array that can be sorted
        char[] letters = s.toCharArray();
        //Call the built in sort method
        Arrays.sort(letters);
        //Return the sorted letters as a string
        return new String(letters);
    }

    public AnagramDictionary(InputStream wordListStream) throws IOException {

        //AnagramActivity passes words.txt as an InputStream for use in AnagramDictionary
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        //Declare a HashSet, ArrayList and HashMap (from SortedLetters String to ArrayList of words)
        wordSet = new HashSet<>();
        wordList = new ArrayList<>();
        lettersToWord = new HashMap<>();

        //Declare a HashMap to map from Length of String to ArrayList of words
        sizeToWord = new HashMap<>();

        //Set a default length for words
        wordLength = DEFAULT_WORD_LENGTH;

        //Read  each word from words.txt and add it to the hashmap
        while((line = in.readLine()) != null) {
            String word = line.trim();

            //add to wordSet and wordList using standard methods
            wordList.add(word); //used to demonstrate that ArrayList is less efficient than HashSet - wordList is not used in the final solution
            wordSet.add(word);

            //find key using a helper method that sorts letters in alphabetical order
            String wordKey = keyGenerator(word);

            //First checkSt!if the hashmap already has a list associated with this key.
            ArrayList<String> anagrams = lettersToWord.get(wordKey);
            if (anagrams == null) {
                //if no existing list is present, create a new arraylist,
                //associate the list with the current word key
                anagrams = new ArrayList<String>();
                lettersToWord.put(wordKey, anagrams);
            }
            //Add the current word to the anagrams list
            anagrams.add(word);

            //add the word to the arraylist that has
            // all words of the same length as the current word
            ArrayList<String> sameSized = sizeToWord.get(word.length());
            if (sameSized == null) {
                sameSized = new ArrayList<String>();
                sizeToWord.put(word.length(), sameSized);
            }
            sameSized.add(word);


        }

    }

    public boolean isGoodWord(String word, String base) {

        //check if word exists in wordSet first
        //now check if it doesn't contain the base as a substring
        /*
             if(wordSet.contains(word))
            {
                //now check if it doesn't contain the base as a substring
                if(word.contains(base))
                    return false;
                else
                    return true;

            }
            return false;
        */

        return wordSet.contains(word) && !word.contains(base);
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {

        //Create an ArrayList for returning anagrams
        ArrayList<String> result = new ArrayList<String>();

        // find the key for anagrams map from given word + 1 alphabet and add all anagrams matching to result set.
        // Add a letter to word (A-Z, one at a time)
        for (char i = 'a'; i <= 'z'; i++) {

            //augment char to word
            String testString = word+i;

            //generate the hashmap key for the augmented word
            String genKey = keyGenerator(testString);

            //Check if there are possible anagrams for this key
            if(lettersToWord.containsKey(genKey)) {
                ArrayList<String> anagrams = lettersToWord.get(genKey);

                //add the obtained set of anagrams for this key to the result set
                result.addAll(anagrams);

            }
        }

        //include only those words that are valid solutions in anagrams.
        //Basically remove those which are not "good words" getAnagrams() method would return result at this point.

        //Since we want to use the for each loop, we clone the result ArrayList as the iterator in for each loop,
        //must not be mutated by deletions..
        ArrayList<String> solution = (ArrayList<String>) result.clone();
        for (String anagramword:result) {
            if(!isGoodWord(anagramword,word))
                solution.remove(anagramword);
        }

        //Just to demonstrate clone() method once again - we can actually return solution itself.
        result =(ArrayList<String>) solution.clone();

        return result;
    }

    public String pickGoodStarterWord() {


        //To choose a good starter word - with increasing lengths on each play
        //Logic example:
        // we start with default wordLength of 3,
        // from SizeToWord, for the key 3, we get a list of words like { BAT, CAT, PAT, ...}
        // We check the number of anagrams possible by adding 1 letter to one of these words selected at random.
        // For example, if we select PAT, the list of anagrams are {PANT, TAPS, TAPE, ... }
        // Since there are a sufficient number we can select PAT as the starting word.
        // If PAT didn't give enough anagrams, we go to the next index after PAT, and check the same
        // If we run out of indices, we wrap around to BAT and check again.
        // for the next play we update wordLength to 4 and repeat the same process.



        //Check if we have found a candidate word
        boolean found = false;

        //Default selected word is something like "post" (used before this is completely implemented)
        String selectedWord = "post";

        //Get the list of words that match to the current play's wordLength- initially set to a default length
        ArrayList<String> currentList = sizeToWord.get(wordLength);

        if(wordLength<=MAX_WORD_LENGTH)
            wordLength+=1; //increment starting word length for next play until we reach max_word_length

        int index = random.nextInt(currentList.size() + 1); //get an index from 0 to Current List's Size randomly


        //Check if the currently selecte word has minimum number of anagrams that we need,
        //as defined by Min_Num_Anagrams, if not, we increment index and check for the next word in currentList
        for(int i=index;!found;i++)
        {
            //if we run out of indices to check, wrap around and start from beginning of list.
            if(i>=currentList.size()) i = 0;
            //Check for minimum number of anagrams
            if(getAnagramsWithOneMoreLetter(currentList.get(i)).size() >= MIN_NUM_ANAGRAMS){
                //if we have a sufficient number, select this as the starting word
                selectedWord=currentList.get(i);
                found=true;
            }

        }

        return selectedWord;
    }
}
