package persistence;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class StringWriter {

    private PrintWriter writer;
    private final String destination;

    public StringWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    public void write(String string) {
        writer.write(string);
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

}
