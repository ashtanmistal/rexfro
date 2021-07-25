package model;

import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;
import model.exceptions.InvalidBooleanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {
    private Queue queue;

    @BeforeEach
    public void setUpBeforeEachTest(){
        queue = new Queue();
    }

    @Test
    public void testSingleAdd(){
        queue.addToQueue("The", "Another", "True");
        try {
            assertEquals(1, queue.getLength());
        } catch (InvalidLengthException e) {
            fail("Unequal elements in queue");
        }
        assertEquals(queue.getFirstFind(), "The");
        assertEquals(queue.getFirstReplace(), "Another");
        try {
            assertTrue(queue.getFirstBool());
        } catch (InvalidBooleanException e) {
            fail("Did not convert to boolean correctly");
        }
        try {
            assertEquals(queue.getLength(), 0);
        } catch (InvalidLengthException e) {
            fail("Did not remove all elements correctly");
        }
    }

    @Test
    public void testAddMultipleItems() {
        for (int i = 0; i < 10; i++) {
            queue.addToQueue("The", "rth nab", "True");
        }
        try {
            assertEquals(queue.getLength(), 10);
        } catch (InvalidLengthException e) {
            fail("Did not add multiple items to queue correctly");
        }
        for (int i = 0; i < 10; i++) {
            assertEquals(queue.getFirstFind(), "The");
            assertEquals(queue.getFirstReplace(), "rth nab");
            try {
                assertTrue(queue.getFirstBool());
            } catch (InvalidBooleanException e) {
                fail("Did not convert to boolean correctly on element" + i);
            }
        }
        try {
            assertTrue(queue.isEmpty());
        } catch (Exception e) {
            fail("Items not removed correctly");
        }


    }

    @Test
    public void testSwapTwoElements() {
        queue.addToQueue("This is item 1", "This is return item 1", "True");
        queue.addToQueue("This is item 2", "This is return item 2", "False");
        try {
            queue.swap(0,1);
            assertEquals(queue.getFirstFind(), "This is item 2");
            assertEquals(queue.getFirstReplace(), "This is return item 2");
            assertEquals(queue.getFirstBool(), false);
            assertEquals(queue.getFirstFind(), "This is item 1");
            assertEquals(queue.getFirstReplace(), "This is return item 1");
            assertEquals(queue.getFirstBool(), true);
        } catch (InvalidBooleanException e) {
            fail("Unexpected Boolean Exception");
        } catch (InvalidIntegerException e) {
            fail("Invalid Integer Exception");
        }
    }

    @Test
    public void testMakeInvalidBoolean() {
        queue.addToQueue("etjy ndgfb", " herbstgf", "r nbgf");
        try {
            queue.getFirstBool();
            fail("Did not catch boolean exception");
        } catch (InvalidBooleanException e) {
            // all good
        }
    }

    @Test
    public void testLengthExceptions() {
        queue.addToQueue("rwth sbfxzvdc", "rwth bsvfzdcx", "True");
        try {
            queue.getFirstBool();
        } catch (InvalidBooleanException e) {
            fail("Unexpected Boolean Exception");
        }
        try {
            queue.getLength();
            fail("Did not catch different length exception");
        } catch (InvalidLengthException e){
            // all good
        }
    }

    @Test
    public void testIntegerException() {
        queue.addToQueue("w45htrs bgf", "5e6 hrntbsgfx", "True");
        try {
            queue.swap(0, 3);
            fail("Did not catch integer exception");
        } catch (InvalidIntegerException e) {
            // all good
        }
    }
}