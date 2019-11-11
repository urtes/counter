package com.urte.counter;

import org.junit.Test;

import java.io.File;
import java.util.*;

import static org.junit.Assert.*;

public class WordCounterTest
{
    @Test
    public void countsWordsInLineCorrectly()
    {
        File file = new File("test.txt");
        File[] files = {file};
        String[] excluded = { "excludeThis" };
        WordCounter wordCounter = new WordCounter(Arrays.asList(files), ".");
        Set<String> wordsToExclude = new HashSet<>(Arrays.asList(excluded));
        String[] words = {"dog", "CAT", "excludeThis", "DOG"};

        wordCounter.setWordsToExclude(wordsToExclude);
        wordCounter.countWordsInLine(words);

        Map<String, Long> countedWords = wordCounter.getWords();

        assertTrue(countedWords.get("dog") == 2);
        assertTrue(countedWords.get("cat") == 1);
        assertFalse(countedWords.containsKey("excludeThis"));
    }
}
