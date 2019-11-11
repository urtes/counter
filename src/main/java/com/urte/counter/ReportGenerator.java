package com.urte.counter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Generates reports from given word count data
 */
public class ReportGenerator {

    final String EXCLUDE_COUNT_FILENAME = "exclude_count.txt";
    private final String PATH;

    public ReportGenerator(String path) {
        this.PATH =  path;
    }

    /**
     * Generates and outputs word occurrences reports from given word occurrences count data
     *
     * @param words word occurrences count data
     */
    public void generateCountReports(TreeMap<String, Long> words) {

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

    /**
     * Outputs the the number of excluded words encountered to "exclude_count.txt" a file
     *
     * @param excludedWordCount input data from which report will be generated
     */
    public void generateExcludesReport(Long excludedWordCount) {

        File file = new File(PATH + EXCLUDE_COUNT_FILENAME);
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(file);
            printWriter.println(String.format("Number of excluded words %d", excludedWordCount));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

}
