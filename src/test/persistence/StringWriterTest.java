package persistence;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class StringWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            StringWriter writer = new StringWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyString() {
        try {
            String string = "";
            StringWriter writer = new StringWriter("./data/testWriterEmptyString.txt");
            writer.open();
            writer.write(string);
            writer.close();
            StringReader reader = new StringReader("./data/testWriterEmptyString.txt");
            string = reader.read();
            assertEquals("", string);
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralString() {
        try {
            String string = "Lorem ipsum dolor\n3.141592653589793238462643383279502884197169";
            StringWriter writer = new StringWriter("./data/testWriterGeneralString.txt");
            writer.open();
            writer.write(string);
            writer.close();
            StringReader reader = new StringReader("./data/testWriterGeneralString.txt");
            string = reader.read();
            assertEquals("Lorem ipsum dolor\n3.141592653589793238462643383279502884197169\n", string);
        } catch (FileNotFoundException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralStringMarkdown() {
        try {
            String string = "# Lorem ipsum dolor\n3.141592653589793238462643383279502884197169";
            StringWriter writer = new StringWriter("./data/testWriterGeneralString.md");
            writer.open();
            writer.write(string);
            writer.close();
            StringReader reader = new StringReader("./data/testWriterGeneralString.md");
            string = reader.read();
            assertEquals("# Lorem ipsum dolor\n3.141592653589793238462643383279502884197169\n", string);
        } catch (FileNotFoundException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralStringLatex() {
        try {
            String string = "\\documentclass{article}\n" +
                    "\\usepackage[utf8]{inputenc}" + "\\begin{document}\nLorem ipsum dolor\n\\end{document}";
            StringWriter writer = new StringWriter("./data/testWriterGeneralString.tex");
            writer.open();
            writer.write(string);
            writer.close();
            StringReader reader = new StringReader("./data/testWriterGeneralString.tex");
            string = reader.read();
            assertEquals("\\documentclass{article}\n" +
                    "\\usepackage[utf8]{inputenc}" + "\\begin{document}\nLorem ipsum dolor\n\\end{document}\n", string);
        } catch (FileNotFoundException e) {
            fail("Exception should not have been thrown");
        }
    }
}
