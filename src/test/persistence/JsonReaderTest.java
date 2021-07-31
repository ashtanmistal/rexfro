package persistence;

import model.Queue;
import model.exceptions.InvalidBooleanException;
import model.exceptions.InvalidLengthException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JsonReaderTest extends JsonTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // all good
        }
    }

    @Test
    void testReaderEmptyQueue() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyQueue.json");
        try {
            Queue queue = reader.read();
            assertEquals(0, queue.getLength());
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (InvalidLengthException e) {
            fail("Error in reading the file");
        }
    }

    @Test
    void testReaderGeneralQueue() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralQueue.json");
        try {
            Queue queue = reader.read();
            assertEquals(3, queue.getLength());
            assertEquals("Find0", queue.getFind(0));
            assertEquals("find1", queue.getFind(1));
            assertEquals("Replace0", queue.getReplace(0));
            assertEquals("replace1", queue.getReplace(1));
            assertEquals("T", queue.getBoolString(0));
            assertEquals("f", queue.getBoolString(1));
            assertTrue(queue.getBool(0));
            assertFalse(queue.getBool(1));
            assertEquals("r", queue.getFind(2));
            assertEquals("r n rr nrn ", queue.getReplace(2));
            assertEquals("t", queue.getBoolString(2));
            assertTrue(queue.getBool(2));
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (InvalidLengthException e) {
            fail("Unable to correctly parse queue");
        } catch (InvalidBooleanException e) {
            fail("Did not parse boolean correctly");
        }
    }
}
