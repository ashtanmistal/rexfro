package model;

import java.util.LinkedList;

public class Operator {

    public Operator() {
    }

    public String singular(String text, String find, String replace, Boolean all) {
        String newText;
        if (all) {
            newText = text.replaceAll(find, replace);
        } else {
            newText = text.replaceFirst(find, replace); // REPLACE ONCE USER INPUT
        }
        return newText;
    }

    public String iterator(String text, Queue queue) throws Exception {

        for (int k = 0; k < queue.getLength(); k++) {
            text = singular(text, queue.getFind(k), queue.getReplace(k), queue.getBool(k));
        }
        return text;
    }
}
