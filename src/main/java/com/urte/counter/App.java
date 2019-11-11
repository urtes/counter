package com.urte.counter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        final String PATH = "IOFiles/";
        List<File> files = new ArrayList<>();
        WordCountingService reader = new WordCountingService();

        if (args.length < 1) {
            exit();
        }

        for (int i = 0; i < args.length; i++) {
            File inputFile = new File(PATH + args[i]);
            if(inputFile.exists() && inputFile.getName().endsWith(".txt")) {
                files.add(inputFile);
            } else {
                exit();
            }
        }

        reader.generateWordCountReports(files);
    }

    static void exit() {
        final String ERROR_MESSAGE = "Enter names of existing .txt input files";
        System.out.println(ERROR_MESSAGE);
        System.exit(1);
    }
}
