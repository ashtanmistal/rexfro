package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Represents a reader that reads a string from text file
public class StringReader {

    private final String source;

    // EFFECTS: Constructs reader to read from source file
    public StringReader(String source) {
        this.source = source;
    }

    // Reads string from file and returns it; throws FileNotFound exception if error occurs
    public String read() throws FileNotFoundException {
        Scanner input = new Scanner(new File(source));
        StringBuilder builder = new StringBuilder();
        while (input.hasNextLine()) {
            builder.append(input.nextLine()).append("\n");
        }
        return builder.toString();
    }
}
