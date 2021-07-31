package model;


public class Operator {

    public Operator() {
    }

    // Effects: Performs find-and-replace operation without modifying the original text;
    //          returns the new operated-upon text
    public String singular(String text, String find, String replace, Boolean all) {
        String newText;
        if (all) {
            newText = text.replaceAll(find, replace);
        } else {
            newText = text.replaceFirst(find, replace); // REPLACE ONCE USER INPUT
        }
        return newText;
    }

    //Modifies: text
    // Effects: Iterates method singular through a single piece of text and returns result
    //          after performing all of the find and replace operations
    public String iterator(String text, Queue queue) throws Exception {

        for (int k = 0; k < queue.getLength(); k++) {
            text = singular(text, queue.getFind(k), queue.getReplace(k), queue.getBool(k));
        }
        return text;
    }
}
