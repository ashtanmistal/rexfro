package persistence;

import model.Queue;
import model.exceptions.InvalidIntegerException;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

// Represents a reader that reads a Queue from TSV data stored in a file
// ADAPTED FROM: JsonReader class (in this project), which itself is adapted from JsonSerializationDemo project on Edx
public class TsvReader {
    private final String source;

    // EFFECTS: Constructs reader to read from source file
    public TsvReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads Queue from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Queue read() throws IOException, InvalidIntegerException {
        Queue queue = new Queue();
        Scanner scanner = new Scanner(new File(source));
        while (scanner.hasNextLine()) {
            try {
                String temp = scanner.nextLine();
                String[] tempArray = temp.split("\t");
                queue.addToQueue(tempArray[0], tempArray[1], tempArray[2]);
            } catch (Exception e) {
                throw new InvalidIntegerException();
            }
        }
        return queue;
    }
}
