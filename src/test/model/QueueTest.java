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

    @Test
    public void testModifyItemOneItem() {
        queue.addToQueue("5 jetting", "rupture", "True");
        try {
            queue.modifyItem(0, "hi", "try", "True");
            assertTrue(queue.getFirstBool());
            assertEquals(queue.getFirstFind(), "hi");
            assertEquals(queue.getFirstReplace(), "try");
        } catch (InvalidLengthException e) {
            fail("Unexpected InvalidLengthException");
        } catch (InvalidBooleanException e) {
            fail("Unexpected InvalidBoolean Exception");
        }
    }

    @Test
    public void testDeleteItem() {
        queue.addToQueue("Hello there", "Good Morning", "True");
        try {
            queue.deleteItem(0);
            try {
                assertTrue(queue.isEmpty());
            } catch (Exception e) {
                fail("Unexpected exception");
            }
        } catch (InvalidLengthException e) {
            fail("Queue is not of same length");
        } catch (InvalidIntegerException e) {
            fail("Integer was not handled correctly");
        }
    }

    @Test
    public void testModifyItemInvalidInteger() {
        queue.addToQueue("Hello there", "Good Morning", "True");
        try {
            queue.modifyItem(6, "rth n", "wrth bsfvxc", "True");
            fail("Did not catch invalid integer for index");
        } catch (InvalidLengthException e) {
            // all good
        }
    }

    @Test
    public void testGetFind() {
        queue.addToQueue("Hello there", "Good Morning", "True");
        try {
            queue.getFind(7);
            fail("Did not catch invalid integer exception");
        } catch (InvalidLengthException e) {
            // all good
        }
    }

    @Test
    public void testGetReplace() {
        queue.addToQueue("Hello there", "Good Morning", "True");
        try {
            queue.getReplace(7);
            fail("Did not catch invalid integer exception");
        } catch (InvalidLengthException e) {
            // all good
        }
    }

    @Test
    public void testGetBool() {
        queue.addToQueue("Hello there", "Good Morning", "True");
        try {
            queue.getBool(7);
            fail("Did not catch invalid integer exception");
        } catch (InvalidBooleanException e) {
            fail("You should not be catching a boolean error!");
        } catch (InvalidLengthException e) {
            // all good
        }
    }

    @Test
    public void getBoolStringValid() {
        queue.addToQueue("Hello there", "werhfgnd", "True");
        try {
            String temp = queue.getBoolString(0);
            assertEquals(temp, "True");
        } catch (InvalidLengthException e){
            fail("Unexpected error in getting boolean as string");
        }
    }

    @Test
    public void getBoolStringInvalid() {
        queue.addToQueue("Hello there", "werhfgnd", "True");
        try {
            queue.getBoolString(7);
            fail("Did not catch incorrect integer");
        } catch (InvalidLengthException e) {
            // all good
        }
    }

    @Test
    public void testValidTrueReturnTrue() {
        assertTrue(queue.validTrue("T"));
    }

    @Test
    public void testValidTrueReturnFalse() {
        assertFalse(queue.validTrue("k ujrynhdg"));
    }

    @Test
    public void testValidFalseReturnTrue() {
        assertTrue(queue.validFalse("F"));
    }

    @Test
    public void testValidFalseReturnFalse() {
        assertFalse(queue.validFalse(" htreb"));
    }

    @Test
    public void testGetFindValid() {
        queue.addToQueue("rty", "ye", "T");
        try {
            queue.getFind(0);
        } catch (InvalidLengthException e) {
            fail("Caught unexpected error");
        }
    }

    @Test
    public void testGetReplaceValid() {
        queue.addToQueue("rty", "ye", "T");
        try {
            queue.getReplace(0);
        } catch (InvalidLengthException e) {
            fail("Caught unexpected error");
        }
    }

    @Test
    public void testGetBoolValidTrue() {
        queue.addToQueue("rty", "ye", "T");
        try {
            assertTrue(queue.getBool(0));
        } catch (InvalidLengthException e) {
            fail("Caught unexpected error, the integer is within the bounds");
        } catch (InvalidBooleanException e) {
            fail("The boolean was valid but returned as not valid");
        }
    }

    @Test
    public void testGetBoolValidFalse() {
        queue.addToQueue("rty", "ye", "F");
        try {
            assertFalse(queue.getBool(0));
        } catch (InvalidLengthException e) {
            fail("Caught unexpected error, the integer is within the bounds");
        } catch (InvalidBooleanException e) {
            fail("The boolean was valid but returned as not valid");
        }
    }

    @Test
    public void testGetBoolInvalidFalse() {
        queue.addToQueue(" htre", "e rhbfds", "e rbd");
        try {
            queue.getBool(0);
            fail("Exception was not thrown");
        } catch (InvalidBooleanException e) {
            // all good
        } catch (InvalidLengthException e) {
            fail("Unexpected length issue");
        }
    }

    @Test
    public void testDeleteItemInvalidInteger() {
        queue.addToQueue("23456y wh", "urthjfngc", "True");
        try {
            queue.deleteItem(7);
            fail("Did not catch expected integer exception");
        } catch (InvalidIntegerException e) {
            // all good
        } catch (InvalidLengthException e) {
            fail(" The queue should be of the same size!");
        }
        try {
            assertFalse(queue.isEmpty());
        } catch (Exception e) {
            fail("Caught unexpected error in length check");
        }
    }

}