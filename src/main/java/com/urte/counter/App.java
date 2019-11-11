package com.urte.counter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App
{
    final static String PATH = "IOFiles/";
    final static String EXTENTION = ".txt";

    public static void main( String[] args )
    {
        List<File> files = new ArrayList<>();

        if (args.length < 1) {
            exit();
        }

        for (int i = 0; i < args.length; i++) {
            File inputFile = new File(PATH + args[i]);
            if(inputFile.exists() && inputFile.getName().endsWith(EXTENTION)) {
                files.add(inputFile);
            } else {
                exit();
            }
        }

        WordService wordService = new WordService();
        wordService.generateWordCountReports(files, PATH);
    }

    static void exit() {
        final String ERROR_MESSAGE = String.format("Enter names of existing %s input files", EXTENTION);
        System.out.println(ERROR_MESSAGE);
        System.exit(1);
    }
}
