package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class StringReader {

    private final String source;

    public StringReader(String source) {
        this.source = source;
    }

    public String read() throws FileNotFoundException {
        Scanner input = new Scanner(new File(source));
        StringBuilder builder = new StringBuilder("");
        while (input.hasNextLine()) {
            builder.append(input.nextLine()).append("\n");
        }
        return builder.toString();
    }
}
