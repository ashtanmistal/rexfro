package persistence;

import model.Queue;
import model.exceptions.InvalidLengthException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class CsvWriter {

    private PrintWriter writer;
    private final String destination;

    public CsvWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    public void write(Queue queue) throws InvalidLengthException {
        for (int i = 0; i < queue.getLength(); i++) {
            writer.write(queue.getFind(i) + ","
                     + queue.getReplace(i) + "," +  queue.getBoolString(i) + "\n");
        }
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }
}
