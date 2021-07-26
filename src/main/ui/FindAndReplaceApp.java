package ui;

import model.Operator;
import model.Queue;
import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

// Code for UI somewhat adapted from Teller App (from Edx)
public class FindAndReplaceApp {
    private Scanner input;
    private Queue queue;
    private Operator operator;
    private LinkedList<String> stringLinkedList;

    //EFFECTS: Runs the FindAndReplace application
    public FindAndReplaceApp() {
        runFindAndReplace();
    }

    private void runFindAndReplace() {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            input.nextLine();
            command = command.toLowerCase();

            if (command.equals("e")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("Process finished");
    }

    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tq -> Queue...");
        System.out.println("\ts -> Strings...");
        System.out.println("\tr -> run operations...");
        System.out.println("\ta -> about");
        System.out.println("\te -> exit application");
    }

    private void processCommand(String command) {
        switch (command) {
            case "q":
                doQueue();
                break;
            case "s":
                doStrings();
                break;
            case "r":
                doRun();
                break;
            case "a":
                doAbout();
                break;
            default:
                System.out.println("Invalid key selection");
                displayMenu();
                break;
        }
    }

    private void doRun() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Run on all strings");
        System.out.println("\ti -> Run on an individual string from list...");
        System.out.println("\tn -> Run queue on a new string");
        System.out.println("\tm -> Manual find-and-replace...");
        System.out.println("\th -> Home");
        String command = input.next();
        input.nextLine();
        processRunCommand(command);
    }

    private void processRunCommand(String command) {
        switch (command) {
            case "a":
                doRunQueueAll();
                break;
            case "i":
                doIndividualRun();
                break;
            case "n":
                doRunNewString();
                break;
            case "m":
                doManualFindReplace();
                break;
            case "h":
                displayMenu();
                break;
            default:
                System.out.println("Invalid key selection");
                doRun();
        }
    }

    private void doRunQueueAll() {
        for (String s: stringLinkedList) {
            try {
                System.out.println("\n" + operator.iterator(s, queue));
            } catch (Exception e) {
                System.out.println("Error in iterating through string: " + s);
            }
        }
        doRun();
    }

    private void doIndividualRun() {
        try {
            doViewStrings();
            System.out.println("What is the index of the string you want to operate on?");
            int index = input.nextInt();
            input.nextLine();
            if (index >= stringLinkedList.size()) {
                System.out.println("Index is larger than size of list");
            } else {
                try {
                    System.out.println((operator.iterator(stringLinkedList.get(index), queue)));
                } catch (Exception e) {
                    System.out.println("Unexpected error in iteration");
                }
            }
            doRun();
        } catch (Exception e) {
            System.out.println("Not an integer. ");
            doRun();
        }
    }

    private void doRunNewString() {
        System.out.println("Enter the text to be operated on:");
        String text = input.nextLine();
        try {
            System.out.println(operator.iterator(text, queue));
        } catch (Exception e) {
            System.out.println("There was an error in iterating through this text");
        }
        doRun();
    }

    private void doManualFindReplace() {
        System.out.println("Enter text:");
        String text = input.nextLine();
        System.out.println("Find:");
        String find = input.nextLine();
        System.out.println("Replace:");
        String replace = input.nextLine();
        System.out.println("Replace All [t] or First Instance [f]:");
        String all = input.next();
        input.nextLine();
        if (checkValid(find, replace, all)) {
            System.out.println(operator.singular(text, find, replace, toBool(all)));
        } else {
            System.out.println("Invalid operation");
        }
        doRun();

    }

    private Boolean toBool(String newReplaceAll) {
        return queue.validTrue(newReplaceAll);
    }

    private void doQueue() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add new operation");
        System.out.println("\te -> edit an operation");
        System.out.println("\ts -> swap queue elements");
        System.out.println("\td -> delete an operation");
        System.out.println("\tv -> view queue");
        System.out.println("\th -> home");
        String command = input.next();
        input.nextLine();
        processQueueCommand(command);
    }

    private void processQueueCommand(String command) {
        switch (command) {
            case "a":
                doAddNewOperation();
                break;
            case "e":
                doEditOperation();
                break;
            case "s":
                doSwap();
                break;
            case "d":
                doDeleteOperation();
                break;
            case "v":
                doViewQueue();
            case "h":
                displayMenu();
                break;
            default:
                doQueue();
        }
    }

    private void doSwap() {
        System.out.println("Please enter the index of the first element to swap:");
        try {
            int index1 = input.nextInt();
            input.nextLine();
            try {
                System.out.println("Please enter the index of the second element to swap:");
                int index2 = input.nextInt();
                input.nextLine();
                queue.swap(index1, index2);
                System.out.println("Items swapped. ");
            } catch (Exception e) {
                System.out.println("Invalid integers, please try again");
            }
        } catch (Exception e) {
            System.out.println("Invalid integers, please try again");
        } finally {
            doQueue();
        }
    }

    private void doAddNewOperation() {
        System.out.println("Enter new Find operation");
        String newFind = input.nextLine();
        System.out.println("Enter new Replace operation");
        String newReplace = input.nextLine();
        System.out.println("Do you want to replace all [T], or just the first instance [F]?");
        String newReplaceAll = input.next();
        input.nextLine();
        if (checkValid(newFind, newReplace, newReplaceAll)) {
            queue.addToQueue(newFind, newReplace, newReplaceAll);
        } else {
            System.out.println("Invalid find and replace operation");
            doAddNewOperation();
        }
        doQueue();
    }

    private void doEditOperation() {
        try {
            doViewQueue();
            System.out.println("What is the index of the item you would like to edit?");
            int index = input.nextInt();
            input.nextLine();
            try {
                if (index >= queue.getLength()) {
                    System.out.println("Index is greater than queue length");
                } else {
                    doEditOperationHelper(index);
                }
            } catch (InvalidLengthException e) {
                System.out.println("Queue is not of equal length");
            }
        } catch (Exception e) {
            System.out.println("Not an integer. ");
            input.nextLine();
        }
        doQueue();
    }

    private void doEditOperationHelper(int index) throws InvalidLengthException {
        System.out.println("Enter new Find operation");
        String newFind = input.nextLine();
        System.out.println("Enter new Replace operation:");
        String newReplace = input.nextLine();
        System.out.println("Is this a replace-all operation [T/F]?");
        String newBool = input.next();
        input.nextLine();
        if (checkValid(newFind, newReplace, newBool)) {
            queue.modifyItem(index, newFind, newReplace, newBool);
        } else {
            System.out.println("Invalid find and replace operation");
        }
    }

    private void doDeleteOperation() {
        try {
            System.out.println("Enter the index of the operation to be deleted:");
            int index = input.nextInt();
            input.nextLine();
            try {
                if (index >= queue.getLength()) {
                    System.out.println("Index is greater than queue length");
                } else {
                    queue.deleteItem(index);
                    System.out.println("Queue item deleted");
                }
            } catch (InvalidLengthException e) {
                System.out.println("Queue is not of equal length");
            } catch  (InvalidIntegerException e) {
                System.out.println("Invalid integer, integer is not within bounds");
            }
            doQueue();
        } catch (Exception e) {
            System.out.println("Input is not an integer");
            doQueue();
        }


    }

    private void doViewQueue() {
        System.out.println("Printing queue...");
        try {
            for (int i = 0; i < queue.getLength(); i++) {
                System.out.println("Item " + i + ": " + "\t" + queue.getFind(i)
                        + "\t" + queue.getReplace(i) + "\t" + queue.getBool(i));
            }
        } catch (Exception e) {
            System.out.println("Queue is not of equal length");
        }
    }

    private boolean checkValid(String newFind, String newReplace, String newReplaceAll) {
        try {
            operator.singular("The quick brown fox jumps over a lazy dog", newFind, newReplace, true);
            if (queue.validTrue(newReplaceAll)) {
                return true;
            } else if (queue.validFalse(newReplaceAll)) {
                return true;
            } else {
                System.out.println("Invalid Replace All boolean, please try again:");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Invalid operation in Find or Replace, please try again");
            return false;
        }
    }

    private void doStrings() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> Add new string");
        System.out.println("\td -> delete a string");
        System.out.println("\tv -> view strings");
        System.out.println("\th -> home");
        String command = input.next();
        input.nextLine();
        processStringCommand(command);
    }

    private void processStringCommand(String command) {
        switch (command) {
            case "a":
                doAddNewString();
                break;
            case "d":
                doDeleteString();
                break;
            case "v":
                doViewStrings();
            case "h":
                displayMenu();
                break;
            default:
                doStrings();
        }
    }

    private void doViewStrings() {
        for (String s : stringLinkedList) {
            System.out.println(s);
        }
    }

    private void doDeleteString() {
        try {
            System.out.println("Enter index of string to delete");
            int index = input.nextInt();
            input.nextLine();
            if (index >= stringLinkedList.size()) {
                System.out.println("Index is larger than size of list");
                doDeleteString();
            } else {
                stringLinkedList.remove(index);
                System.out.println("String deleted");
                doStrings();
            }
        } catch (Exception e) {
            System.out.println("Not an integer. ");
            doStrings();
        }
    }

    private void doAddNewString() {
        System.out.println("Enter a new string:");
        try {
            stringLinkedList.add(input.nextLine());
            System.out.println("String added.");
        } catch (Exception e) {
            System.out.println("Invalid string");
        }
        doStrings();
    }

    private void init() {
        input = new Scanner(System.in);
        queue = new Queue();
        operator = new Operator();
        stringLinkedList = new LinkedList<>();
    }
    
    private void doAbout() {
        System.out.println("Welcome to the Find and Replace Operator, developed"
                + " by Ashtan Mistal. Please note: \n \t - Currently, only strings "
                + "without new lines (\\n) are supported, due to the functionality of Java's input.nextLine() method. "
                + "\n You can add a list of find and replace operations using the Queue, "
                + "and perform this queue on multiple pieces of text using the Strings menu. "
                + "\n \nFor more information, see README.md.");
        System.out.println("\nSelect from:");
        System.out.println("\tr -> View README.md");
        System.out.println("\th -> Home");
        String command = input.next();
        input.nextLine();
        processAboutCommand(command);
    }

    private void processAboutCommand(String command) {
        switch (command) {
            case "r":
                doVisitReadMe();
                break;
            case "h":
                displayMenu();
                break;
            default:
                System.out.println("Invalid key selection, returning home...");
                displayMenu();
                break;
        }
    }

// File reading abilities from https://stackoverflow.com/a/39026036
    private void doVisitReadMe() {
        Scanner input = null;
        try {
            input = new Scanner(new File("README.md"));
        } catch (FileNotFoundException e) {
            System.out.println("README.md file not found");
        }
        while (true) {
            assert input != null;
            if (!input.hasNextLine()) {
                break;
            }
            System.out.println(input.nextLine());
        }
    }

}
