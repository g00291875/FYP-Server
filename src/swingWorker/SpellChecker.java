/*
 * spellchecker.java
 *
 * Author: Shyam Shankar <syamsankar91@gmail.com>
 * Licensed under GPL Version 3
 *
 */
package swingWorker;

import java.io.*;
import java.util.*;

public class SpellChecker {

    Hashtable<String, String> dictionary;   // To store all the words of the dictionary
    boolean suggestWord;           // To indicate whether the word is spelled correctly or not.

    public SpellChecker()
    {
        dictionary = new Hashtable<String,String>();

        try
        {
            //Read and store the words of the dictionary 
            BufferedReader dictReader = new BufferedReader(new FileReader("C:\\year 5\\Git\\Server\\FYP-Server\\src\\dictionary.txt"));

            while (dictReader.ready())
            {
                String dictInput = dictReader.readLine() ;
                String [] dict = dictInput.split("\\s");//split white spaces

                for(int i = 0; i < dict.length;i++)
                {
                    // key and value are identical
                    dictionary.put(dict[i], dict[i]);// store in has table
                }
            }
            dictReader.close();
        }
        catch (IOException e)
        {
            System.out.println("IOException Occured! ");
            e.printStackTrace();
      //      System.exit(-1);
        }
    }

    public Boolean checkWord(String wordToCheck)
    {
        String wordCheck, unpunctWord;
        String word = wordToCheck.toLowerCase();

        // if word is found in dictionary then it is spelt correctly, so return as it is.
        //note: inflections like "es","ing" provided in the dictionary itself.
        if ((wordCheck = (String)dictionary.get(word)) != null)
        {
            suggestWord = false;            // no need to ask for suggestion for a correct word.
            return true;
        }

        // Removing punctuations at end of word and giving it a shot ("." or "." or "?!")
        int length = word.length();

        //Checking for the beginning of quotes(example: "she )
        if (length > 1 && word.substring(0,1).equals("\""))
        {
            unpunctWord = word.substring(1, length);

            if ((wordCheck = (String)dictionary.get(unpunctWord)) != null)
            {
                suggestWord = false;            // no need to ask for suggestion for a correct word.
                return true ;
            }
            else // not found
                return false;                  // removing the punctuations and returning
        }

        // Checking if "." or ",",etc.. at the end is the problem(example: book. when book is present in the dictionary).
        if( word.substring(length - 1).equals(".")  || word.substring(length - 1).equals(",") ||  word.substring(length - 1).equals("!")
                ||  word.substring(length - 1).equals(";") || word.substring(length - 1).equals(":"))
        {
            unpunctWord = word.substring(0, length-1);

            if ((wordCheck = (String)dictionary.get(unpunctWord)) != null)
            {
                suggestWord = false;            // no need to ask for suggestion for a correct word.
                return true;
            }
            else
            {
                return false;                  // removing the punctuations and returning
            }
        }

        // After all these checks too, word could not be corrected, hence it must be misspelt word. return and ask for suggestions
        return false;
    }

}

