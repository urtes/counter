package com.urte.counter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCounter {

    private final String PATH;
    private final String EXCLUDE_FILENAME = "exclude.txt";
    private final List<File> inputFiles;
    private Set<String> wordsToExclude = new HashSet<>();
    private Map<String, Long> words = new TreeMap<>();
    private long excludeCounter = 0;

    public Map<String, Long> getWords() {
        return words;
    }

    public long getExcludeCounter() {
        return excludeCounter;
    }

    public WordCounter(List<File> inputFiles, String path) {
        this.inputFiles = inputFiles;
        this.PATH = path;
    }

    public Map<String, Long> countWordsInFiles() {
        setExcludes();
        inputFiles.forEach(file -> countWordsInFile(file));
        return words;
    }

    private void setExcludes() {
        File exclude = new File(PATH + EXCLUDE_FILENAME);
        if(exclude.exists()) {
            wordsToExclude = getWordsToExclude(exclude);
        }
    }

    private Set<String> getWordsToExclude(File exclude) {

        Set<String> wordsToExcludeInLowerCase = new HashSet<>();

        try (Stream<String> lines = Files.lines(Paths.get(exclude.getPath()))) {
            Set<String> wordsToExclude = lines
                    .map(line -> line.trim())
                    .filter(line -> !line.isEmpty())
                    .map(line -> line.split("[\\s\\p{Punct}]+"))
                    .flatMap(array -> Arrays.stream(array))
                    .collect(Collectors.toSet());
            wordsToExclude.forEach(word -> wordsToExcludeInLowerCase.add(word.toLowerCase()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordsToExcludeInLowerCase;
    }

    private void countWordsInFile(File file) {
        try (Stream<String> lines = Files.lines(Paths.get(file.getPath()))) {
            lines
                    .map(line -> line.trim())
                    .filter(line -> !line.isEmpty())
                    .map(line -> line.split("[\\s\\p{Punct}]+"))
                    .forEach(array -> countWordsInLine(array));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void countWordsInLine(String[] wordsInLine) {
        for (String word : wordsInLine) {
            word = word.toLowerCase();
            if (wordsToExclude.contains(word)) {
                excludeCounter++;
            } else {
                if (!words.containsKey(word)) {
                    words.put(word, 1l);
                } else {
                    long count = words.get(word);
                    words.put(word, count + 1l);
                }
            }
        }
    }
}
