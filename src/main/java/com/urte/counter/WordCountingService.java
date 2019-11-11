package com.urte.counter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class WordCountingService {

    private final String PATH;
    private final List<File> INPUT_FILES;
    private Map<String, Long> words = new TreeMap<>();

    public WordCountingService(List<File> inputFiles, String path) {
        this.INPUT_FILES = inputFiles;
        this.PATH = path;
    }

    public void generateWordCountReports() {
        countWordsInFiles();
        outputReports();
    }

    private void countWordsInFiles() {
        INPUT_FILES.forEach(file -> countWordsInFile(file));
        words.entrySet().forEach(count -> System.out.println(count.getKey() + ": " + count.getValue()));
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
        for (int i = 0; i < wordsInLine.length; i++) {
            if (!words.containsKey(wordsInLine[i])) {
                words.put(wordsInLine[i], 1l);
            } else {
                long count = words.get(wordsInLine[i]);
                words.put(wordsInLine[i], count+1l);
            }
        }
    }

    private void outputReports() {

        File file = new File(PATH + "output.txt");
        file.getParentFile().mkdirs();
        PrintWriter printWriter = null;

        try {
            file.getParentFile().mkdirs();
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
}
