package com.urte.counter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCountingService {

    private final String PATH;
    private final List<File> INPUT_FILES;
    final String EXCLUDE_FILENAME = "exclude.txt";
    final String EXCLUDE_COUNT_FILENAME = "exclude_count.txt";
    private Map<String, Long> words = new TreeMap<>();
    private Set<String> wordsToExclude = new HashSet<>();
    private long excludeCounter = 0;

    public WordCountingService(List<File> inputFiles, String path) {
        this.INPUT_FILES = inputFiles;
        this.PATH = path;
    }

    public void generateWordCountReports() {
        setExcludes();
        countWordsInFiles();
        generateReports();
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
                    .filter(array -> array.length != 0 || !array[0].equals(""))
                    .flatMap(array -> Arrays.stream(array))
                    .collect(Collectors.toSet());
            wordsToExclude.forEach(word -> wordsToExcludeInLowerCase.add(word.toLowerCase()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordsToExcludeInLowerCase;
    }

    private void countWordsInFiles() {
        INPUT_FILES.forEach(file -> countWordsInFile(file));
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

        for (int i = 0; i < wordsInLine.length; i++) {
            String word = wordsInLine[i].toLowerCase();
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

    private void generateReports() {
        generateCountReports();
        generateExcludesReport();
    }

    private void generateCountReports()  {
        char previousLetter = ' ';
        char currentLetter;
        PrintWriter writer = null;

        for (Map.Entry<String, Long> word : words.entrySet()) {
            currentLetter = word.getKey().toUpperCase().charAt(0);

            if (currentLetter != previousLetter) {

                if (writer != null) {
                    writer.close();
                }

                File file = new File(String.format("%s%s.txt", PATH, currentLetter));
                try {
                    writer = new PrintWriter(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                previousLetter = currentLetter;
            }

            writer.println(String.format("%s: %d", word.getKey(), word.getValue()));
        }

        if (writer != null) {
            writer.close();
        }
    }

    public void generateExcludesReport() {
        File file = new File(PATH + EXCLUDE_COUNT_FILENAME);
        file.getParentFile().mkdirs();
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(file);
            printWriter.println(String.format("Number of excluded words %d", excludeCounter));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }
}
