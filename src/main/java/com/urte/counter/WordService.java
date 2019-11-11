package com.urte.counter;

import java.io.File;
import java.util.*;

/**
 * Triggers word counting and reports generating
 */
public class WordService {

    public void generateWordCountReports(List<File> inputFiles, String PATH) {

        WordCounter wordCounter = new WordCounter(inputFiles, PATH);
        wordCounter.countWordsInFiles();

        ReportGenerator reportGenerator = new ReportGenerator(PATH);
        reportGenerator.generateCountReports(wordCounter.getWords());
        reportGenerator.generateExcludesReport(wordCounter.getExcludeCounter());
    }
}
