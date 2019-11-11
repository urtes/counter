package com.urte.counter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCountingService {

    private Map<String, Long> words = new HashMap<>();

    public void generateWordCountReports(List<File> files) {
        countWordsInFiles(files);
        outputReports();
    }

    private void countWordsInFiles(List<File> files) {
        files.forEach(file -> countWordsInFile(file));
        words.entrySet().forEach(count -> System.out.println(count.getKey() + ": " + count.getValue()));
    }

    private void countWordsInFile(File file) {
        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()))) {
            stream
                    .map(line -> line.split("[\\s\\p{Punct}]+"))
                    .forEach(array -> {
                        for (int i = 0; i < array.length; i++) {
                            if (!words.containsKey(array[i])) {
                                words.put(array[i], 1l);
                            } else {
                                long count = words.get(array[i]);
                                words.put(array[i], count+1l);
                            }
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void outputReports() {

    }
}
