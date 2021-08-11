package persistence;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class StringReaderTest {

    @Test
    void testReaderNonExistentFile() {
        StringReader reader = new StringReader("./data/noSuchFile.txt");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // all good
        }
    }

    @Test
    void testReaderEmptyString() {
        StringReader reader = new StringReader("./data/testReaderEmptyString.txt");
        try {
            assertEquals("", reader.read());
        } catch (FileNotFoundException e) {
            fail("Did not correctly read file");
        }
    }

    @Test
    void testReaderTextWithLines() {
        StringReader reader = new StringReader("./data/testReaderTextWithLines.txt");
        try {
            assertEquals("Hello there!\nThis is a test of a string with multiple lines.\n\nGoodbye!\n",
                    reader.read());
        } catch (FileNotFoundException e) {
            fail("Did not correctly read file");
        }
    }

    @Test
    void testReaderTextLatexFormat() {
        StringReader reader = new StringReader("./data/main.tex");
        try {
            assertTrue(reader.read().contains("\\documentclass{article}"));
        } catch (FileNotFoundException e) {
            fail(".tex file was not supported");
        }
    }
}
