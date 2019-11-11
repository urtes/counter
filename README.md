# Counter

Counter is an application that generates simple word occurrences count reports. It takes as an input txt files with 
words to count and file, named "exclude.txt", with words to exclude from counting. As a result it creates a file for each letter 
that occurred and exports the results for each word and the number of its occurrences, as well as the file named 
"exclude_count.txt" containing the number of excluded words encountered.

Application is developed in Java, using Maven.

## Installation

To run the application download counter-1.0-RELEASE.jar file from Releases. In the same directory, where counter-1.0-RELEASE.jar file
 is located, create directory named IOFiles and put txt input files into it.

## Usage

To start the application go to directory where counter-1.0-RELEASE.jar file is located and enter `java -jar counter-1.0-RELEASE.jar yourinputfile.txt`. The application will report files in IOFiles directory as a result.
