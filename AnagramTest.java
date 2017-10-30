/**
* Applied CS PBL MSRIT
* Anagrams Evaluator
* @author  Arjun Rao
* @version 1.0
* @since   2017-02-6 
*/


import com.google.engedu.anagrams.AnagramDictionary;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.*;

public class AnagramTest {
  private final static String WORDS = "./words.txt";
  
  public static void main(String args[]) throws Exception
  {
  
    //Setup words.txt
    String wordsFile =  WORDS;    
    AnagramDictionary dictionary = new AnagramDictionary(new FileInputStream(wordsFile));

    System.out.println(dictionary.getAnagramsWithOneMoreLetter("post"));
  }
}
