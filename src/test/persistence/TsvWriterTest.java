package persistence;

import model.Queue;
import model.exceptions.InvalidBooleanException;
import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TsvWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Queue queue = new Queue();
            TsvWriter writer = new TsvWriter("./data/my\0illegal:fileName.tsv");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyQueue() {
        try {
            Queue queue = new Queue();
            TsvWriter writer = new TsvWriter("./data/testWriterEmptyQueue.tsv");
            writer.open();
            writer.write(queue);
            writer.close();
            CsvReader reader = new CsvReader("./data/testWriterEmptyQueue.tsv");
            queue = reader.read();
            assertEquals(0, queue.getLength());
        } catch (IOException | InvalidLengthException | InvalidIntegerException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testGeneralQueue() {
        try {
            Queue queue = new Queue();
            queue.addToQueue("Find0", "Replace0", "T");
            queue.addToQueue("find1", "replace1", "f");
            TsvWriter writer = new TsvWriter("./data/testWriterGeneralQueue.tsv");
            writer.open();
            writer.write(queue);
            writer.close();

            TsvReader reader = new TsvReader("./data/testWriterGeneralQueue.tsv");
            queue = reader.read();
            assertEquals(2, queue.getLength());
            assertEquals("Find0", queue.getFind(0));
            assertEquals("find1", queue.getFind(1));
            assertEquals("Replace0", queue.getReplace(0));
            assertEquals("replace1", queue.getReplace(1));
            assertEquals("T", queue.getBoolString(0));
            assertEquals("f", queue.getBoolString(1));
            assertTrue(queue.getBool(0));
            assertFalse(queue.getBool(1));

        } catch (IOException | InvalidLengthException | InvalidBooleanException | InvalidIntegerException e) {
            fail("Should not have caught error");
        }
    }
}
