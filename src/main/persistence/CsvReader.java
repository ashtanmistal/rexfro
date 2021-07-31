package persistence;

import model.Queue;
import model.exceptions.InvalidIntegerException;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CsvReader {
    private final String source;

    public CsvReader(String source) {
        this.source = source;
    }

    public Queue read() throws IOException, InvalidIntegerException {
        Queue queue = new Queue();
        Scanner scanner = new Scanner(new File(source));
        while (scanner.hasNextLine()) {
            try {
                String temp = scanner.nextLine();
                String[] tempArray = temp.split(",");
                queue.addToQueue(tempArray[0], tempArray[1], tempArray[2]);
            } catch (Exception e) {
                throw new InvalidIntegerException();
            }
        }
        return queue;
    }
}
