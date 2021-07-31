package persistence;

import model.Queue;
import model.exceptions.InvalidBooleanException;
import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TsvReaderTest {

    @Test
    void testReaderNonExistentFile() {
        TsvReader reader = new TsvReader("./data/noSuchFile.tsv");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException | InvalidIntegerException e) {
            // all good
        }
    }

    @Test
    void testReaderEmptyQueue() {
        TsvReader reader = new TsvReader("./data/testReaderEmptyQueue.tsv");
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
        TsvReader reader = new TsvReader("./data/testReaderGeneralQueue.tsv");
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
        TsvReader reader = new TsvReader("./data/testReaderBadQueue.tsv");
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
