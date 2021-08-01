package persistence;

import model.Queue;
import model.exceptions.InvalidLengthException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that writes TSV representation of Queue to file
public class TsvWriter {

    private PrintWriter writer;
    private final String destination;

    // EFFECTS: Constructs writer to write to destination file
    public TsvWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes TSV representation of Queue to file
    public void write(Queue queue) throws InvalidLengthException {
        for (int i = 0; i < queue.getLength(); i++) {
            writer.write(queue.getFind(i) + "\t"
                    + queue.getReplace(i) + "\t" +  queue.getBoolString(i) + "\n");
        }
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }
}
