package model;

import model.exceptions.InvalidLengthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class OperatorTest {
    Operator operator;
    Queue queue;
    @BeforeEach
    public void setUpBeforeEachTest(){
        queue = new Queue();
        operator = new Operator();

    }

    @Test
    public void testSingularFR() {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
                "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris";
        String replace = operator.singular(text, "Lorem ipsum dolor sit amet", "Hello", true);
        assertEquals(replace, "Hello, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut "+
                "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris");
    }

    @Test
    public void testReplaceOnce() {
        String text = "sdfsdfsdfsdf";
        String replace = operator.singular(text, "sdf", "bnm", false);
        assertEquals(replace, "bnmsdfsdfsdf");
    }

    @Test
    public void testMultipleReplace() {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
                "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris";
        queue.addToQueue("L", "ELL", "True");
        queue.addToQueue("o", "0", "false");
        queue.addToQueue("cons", "4576", "F");
        try {
            String replace = operator.iterator(text, queue);
            assertEquals(replace, "ELL0rem ipsum dolor sit amet, 4576ectetur adipiscing elit, sed do eiusmod" +
                    " tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud " +
                    "exercitation ullamco laboris");
        } catch (Exception e) { fail("Unexpected error in an iteration"); }
    }

    @Test
    public void testIncorrectQueue() {
        String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
                "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris";
        queue.addToQueue("L", "ELL", "NotABoolean");
        queue.addToQueue("o", "0", "false");
        queue.addToQueue("cons", "4576", "F");
        try {
            String replace = operator.iterator(text, queue);
            fail("Iterator did not catch NotABoolean");
        } catch (Exception e) {
            // all good
        }
    }

    @Test
    public void testMultipleStringsSingularOperation() {
        String text1 = "Hello there, ladies and gentlemen!";
        String text2 = "Hello there! General Kenobi";
        String replace1 = operator.singular(text1, "Hello there", "Good morning", true);
        String replace2 = operator.singular(text2, "Hello there", "Good morning", true);
        assertEquals(replace1, "Good morning, ladies and gentlemen!");
        assertEquals(replace2, "Good morning! General Kenobi");
    }

    @Test
    public void testEditAfterOneReplaceBeforeAnother() {
        queue.addToQueue("Hello there", "Good Morning", "True");
        queue.addToQueue("L", "ELL", "False");
        String text1 = "Hello there, ladies and gentlemen!";
        String text2 = "Hello there! General Kenobi";
        try {
            String replace1 = operator.iterator(text1, queue);
            assertEquals(replace1, "Good Morning, ladies and gentlemen!");
        } catch (Exception e) {
            fail("Unexpected iteration error");
        }
        try {
            queue.modifyItem(1, "l", "L", "true");
        } catch (InvalidLengthException e) {
            fail("The index should be in range");
        }
        try {
            String replace2 = operator.iterator(text2, queue);
            assertEquals("Good Morning! GeneraL Kenobi", replace2);
        } catch (Exception e) {
            fail("Unexpected iteration exception");
        }
    }


}
