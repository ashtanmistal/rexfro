package model;

import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;
import model.exceptions.InvalidBooleanException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.Collections;
import java.util.LinkedList;

public class Queue  implements Writable {
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

    // getter; clauses omitted
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

    // modifies: this.replaceQueue
    // effects: returns first element of replaceQueue and removes it from the queue
    public String getFirstReplace() {
        return replaceQueue.remove(0);
    }

    // modifies: this.replaceAllQueue
    // effects: returns first element of replaceAllQueue and removes it from the queue
    public Boolean getFirstBool() throws InvalidBooleanException {
        String replace = replaceAllQueue.remove(0);
        if (validTrue(replace)) {
            return true;

        } else if (validFalse(replace)) {
            return false;
        } else {
            throw new InvalidBooleanException();
        }
    }

    // NOTE: Following 2 getters do not modify anything and are simple getters

    public String getFind(int i) throws InvalidLengthException {
        if (i >= getLength()) {
            throw new InvalidLengthException();
        } else {
            return findQueue.get(i);
        }
    }

    public String getReplace(int i) throws InvalidLengthException {
        if (i >= getLength()) {
            throw new InvalidLengthException();
        } else {
            return replaceQueue.get(i);
        }
    }

    // Effects: Converts the String (True, t, 1, etc) into a Boolean and returns it
    public Boolean getBool(int i) throws InvalidBooleanException, InvalidLengthException {
        if (i >= getLength()) {
            throw new InvalidLengthException();
        } else {
            String replace = replaceAllQueue.get(i);
            if (validTrue(replace)) {
                return true;

            } else if (validFalse(replace)) {
                return false;
            } else {
                throw new InvalidBooleanException();
            }
        }
    }

    // Effects: Returns the boolean as a string
    public String getBoolString(int i) throws InvalidLengthException {
        if (i >= getLength()) {
            throw new InvalidLengthException();
        } else {
            return replaceAllQueue.get(i);
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

    // effects: returns whether or not the queue is empty
    public Boolean isEmpty() throws Exception {
        return getLength() == 0;
    }

    // Modifies: this
    // Effects: Replaces item in index i with respective new find, replace, replaceAll strings
    public void modifyItem(Integer i, String newFind, String newReplace, String newAll) throws InvalidLengthException {
        if (i < getLength()) {
            findQueue.set(i, newFind);
            replaceQueue.set(i, newReplace);
            replaceAllQueue.set(i, newAll);
        } else {
            throw new InvalidLengthException();
        }
    }

    // Modifies: this
    // Effects: Removes element of index i from the queue
    public void deleteItem(int i) throws InvalidLengthException, InvalidIntegerException {
        if (i >= getLength()) {
            throw new InvalidIntegerException();
        } else {
            findQueue.remove(i);
            replaceQueue.remove(i);
            replaceAllQueue.remove(i);
        }
    }

    // Effects: Checks if the given string is a valid True boolean statement and returns True; false otherwise
    public boolean validTrue(String replace) {
        if (replace.equalsIgnoreCase("True")) {
            return true;
        } else if (replace.equalsIgnoreCase("T")) {
            return true;
        } else {
            return replace.equals("1");
        }
    }

    // Effects: Checks if the given string is a valid False boolean statement and returns True, False otherwise
    public boolean validFalse(String replace) {
        if (replace.equalsIgnoreCase("False")) {
            return true;
        } else if (replace.equalsIgnoreCase("F")) {
            return true;
        } else {
            return replace.equals("0");
        }
    }

    private int getLengthSafe() {
        try {
            return getLength();
        } catch (InvalidLengthException e) {
            return 0;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Queue", "Queue");
        json.put("Find operations", findToJson());
        json.put("Replace operations", replaceToJson());
        json.put("Replace All operations", replaceAllToJson());
        return json;
    }

    // EFFECTS: returns find operations in the queue as a JSON array
    private JSONArray findToJson() {
        JSONArray jsonarray = new JSONArray();

        for (int i = 0; i < getLengthSafe(); i++) {
            try {
                jsonarray.put(getFind(i));
            } catch (InvalidLengthException e) {
                jsonarray.put(""); // default case
            }
        }
        return jsonarray;
    }

    // EFFECTS: returns replace operations in the queue as a JSON array
    private JSONArray replaceToJson() {
        JSONArray jsonarray = new JSONArray();

        for (int i = 0; i < getLengthSafe(); i++) {
            try {
                jsonarray.put(getReplace(i));
            } catch (InvalidLengthException e) {
                jsonarray.put(""); // default case
            }
        }
        return jsonarray;
    }

    // EFFECTS: returns find operations in the queue as a JSON array
    private JSONArray replaceAllToJson() {
        JSONArray jsonarray = new JSONArray();
        for (int i = 0; i < getLengthSafe(); i++) {
            try {
                jsonarray.put(getBoolString(i));
            } catch (InvalidLengthException e) {
                jsonarray.put("true"); // default case
            }
        }
        return jsonarray;
    }

    // NOTE: Above default cases ensure that no operations get performed for these default cases

    // THE FOLLOWING METHODS ARE TO ONLY BE USED WITH JSON FILES
    public void addToFindQueue(String str) {
        findQueue.add(str);
    }

    public void addToReplaceQueue(String str) {
        replaceQueue.add(str);
    }

    public void addToReplaceAllQueue(String str) {
        replaceAllQueue.add(str);
    }
}
