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
        countWordsInFiles();
        generateReports();
    }

    private void countWordsInFiles() {
        INPUT_FILES.forEach(file -> countWordsInFile(file));
//        words.entrySet().forEach(count -> System.out.println(count.getKey() + ": " + count.getValue()));
    }

    private void countWordsInFile(File file) {
        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()))) {
            stream
                    .map(line -> line.split("[\\s\\p{Punct}]+"))
                    .forEach(array -> countWordsInLine(array));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void countWordsInLine(String[] wordsInLine) {

        setExcludes();

        for (int i = 0; i < wordsInLine.length; i++) {
            String word = wordsInLine[i];
            if (wordsToExclude.contains(word)) {
                excludeCounter++;
            } else {
                if (!words.containsKey(word)) {
                    words.put(wordsInLine[i], 1l);
                } else {
                    long count = words.get(wordsInLine[i]);
                    words.put(wordsInLine[i], count + 1l);
                }
            }
        }
    }

    private void generateReports() {
        generateCountReports();
        generateExcludesReport();
    }

    private void generateCountReports() {

        File file = new File(PATH + "output.txt");
        file.getParentFile().mkdirs();
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(file);
            for(Map.Entry<String, Long> word : words.entrySet()) {
                printWriter.println(String.format("%s: %d", word.getKey(), word.getValue()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
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

    private void setExcludes() {
        File exclude = new File(PATH + EXCLUDE_FILENAME);
        if(exclude.exists()) {
            wordsToExclude = getWordsToExclude(exclude);
        }
    }

    private Set<String> getWordsToExclude(File exclude) {

        Set<String> wordsToExclude = new HashSet<>();

        try (Stream<String> stream = Files.lines(Paths.get(exclude.getPath()))) {
            wordsToExclude = stream
                    .map(line -> line.split("[\\s\\p{Punct}]+"))
                    .flatMap(array -> Arrays.stream(array))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordsToExclude;
    }

}
