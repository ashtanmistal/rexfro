package model;

import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;
import model.exceptions.InvalidBooleanException;

import java.util.Collections;
import java.util.LinkedList;

public class Queue {
    // fields go here
    private final LinkedList<String> findQueue = new LinkedList<>();
    private final LinkedList<String> replaceQueue = new LinkedList<>();
    private final LinkedList<String> replaceAllQueue = new LinkedList<>();

    // Constructor
    public Queue() {
    }

    // Requires: None
    // Modifies: this
    // Effects: Appends
    public void addToQueue(String find, String replace, String replaceAll) {
        findQueue.add(find);
        replaceQueue.add(replace);
        replaceAllQueue.add(replaceAll);
    }

    public Integer getLength() throws InvalidLengthException {
        if (findQueue.size() != replaceQueue.size() || replaceQueue.size() != replaceAllQueue.size()) {
            throw new InvalidLengthException();
        } else {
            return findQueue.size();
        }
    }
    
    // modifies: this.findQueue
    // effects: returns first element of findQueue and removes it from the queue
    public String getFirstFind() {
        return findQueue.remove(0);
    }

    public String getFirstReplace() {
        return replaceQueue.remove(0);
    }

    public Boolean getFirstBool() throws InvalidBooleanException {
        String replace = replaceAllQueue.remove(0);
        if (replace.equalsIgnoreCase("True") || replace.equalsIgnoreCase("T") || replace.equals("1")) {
            return true;

        } else if (replace.equalsIgnoreCase("False") || replace.equalsIgnoreCase("F") || replace.equals("0")) {
            return false;
        } else {
            throw new InvalidBooleanException();
        }
    }

    // modifies: this
    // effects: re-orders elements i1 and i2 in all lists
    public void swap(int i1, int i2) throws InvalidIntegerException {
        if (i1 < 0 || i1 > findQueue.size() || i2 < 0 || i2 > findQueue.size()) {
            throw new InvalidIntegerException();
        } else {
            Collections.swap(findQueue, i1, i2);
            Collections.swap(replaceQueue, i1, i2);
            Collections.swap(replaceAllQueue, i1, i2);
        }
    }

    public Boolean isEmpty() throws Exception {
        return getLength() == 0;
    }
}
