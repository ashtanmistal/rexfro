package persistence;

import model.Queue;
import model.exceptions.InvalidBooleanException;
import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CsvReaderTest {

    @Test
    void testReaderNonExistentFile() {
        CsvReader reader = new CsvReader("./data/noSuchFile.csv");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException | InvalidIntegerException e) {
            // all good
        }
    }

    @Test
    void testReaderEmptyQueue() {
        CsvReader reader = new CsvReader("./data/testReaderEmptyQueue.csv");
        try {
            Queue queue = reader.read();
            assertEquals(0, queue.getLength());
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (InvalidLengthException | InvalidIntegerException e) {
            fail("Error in reading the file");
        }
    }

    @Test
    void testReaderGeneralQueue() {
        CsvReader reader = new CsvReader("./data/testReaderGeneralQueue.csv");
        try {
            Queue queue = reader.read();
            assertEquals(6, queue.getLength());
            assertEquals("∧", queue.getFind(0));
            assertEquals("¬", queue.getFind(1));
            assertEquals("\\wedge", queue.getReplace(0));
            assertEquals("\\lnot", queue.getReplace(1));
            assertEquals("⊼", queue.getFind(5));
            assertEquals("\\bar{\\wedge}", queue.getReplace(5));
            assertTrue(queue.getBool(0));
        } catch (InvalidIntegerException | IOException | InvalidLengthException | InvalidBooleanException e) {
            fail("Unexpected error caught");
        }
    }

    @Test
    void testInvalidInteger() {
        CsvReader reader = new CsvReader("./data/testReaderBadQueue.csv");
        try {
            Queue queue = reader.read();
            assertEquals(6, queue.getLength());
            assertEquals("∧", queue.getFind(6));
            fail("Did not catch error");
        } catch (InvalidIntegerException | IOException | InvalidLengthException e) {
            // all good
        }
    }
}
