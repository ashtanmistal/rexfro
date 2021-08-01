package persistence;

import model.Queue;
import model.exceptions.InvalidLengthException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes CSV representation of Queue to file
// Adapted from JsonSerializationDemo project on Edx
public class CsvWriter {

    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public CsvWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes CSV representation of Queue to file
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
