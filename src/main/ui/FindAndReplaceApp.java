package ui;

import model.Operator;
import model.Queue;
import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;
import persistence.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
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

    public void displayMenu() {
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
                break;
            default:
                System.out.println("Invalid key selection");
                doRun();
        }
    }

    private void doRunQueueAll() {
        LinkedList<String> outputAll = new LinkedList<>();
        for (String s: stringLinkedList) {
            try {
                String output = operator.iterator(s, queue);
                outputAll.add(output);
                System.out.println("\n" + output);
            } catch (Exception e) {
                System.out.println("Error in iterating through string: " + s);
            }
        }
        System.out.println("\nDone. Do you want to save all? [Y/N]");
        String command = input.next();
        input.nextLine();
        if (Objects.equals(command, "Y") || Objects.equals(command, "y")) {
            doSaveStringAll(outputAll);
        }
    }

    private void doSaveStringAll(LinkedList<String> outputAll) {
        System.out.println("Please enter the folder to save the operated texts to: ");
        String ou = input.nextLine();
        String dt = String.valueOf(java.time.Clock.systemUTC().instant());
        int totalCount = outputAll.size();
        for (String s : outputAll) {
            StringWriter w = new StringWriter(ou + "FROP_" + dt + "(" + outputAll.indexOf(s) + ")");
            try {
                w.open();
                w.write(s);
                w.close();
            } catch (FileNotFoundException e) {
                totalCount -= 1;
            }
        }
        System.out.println("Files saved: " + ou + "FROP_" + dt + "(" + 0 + ") and " + (totalCount - 1) + " more. "
                 + (outputAll.size() - totalCount) + " files saved incorrectly");
    }

    private void doIndividualRun() {
        try {
            System.out.println("What is the index of the string you want to operate on?");
            int index = input.nextInt();
            input.nextLine();
            if (index >= stringLinkedList.size()) {
                System.out.println("Index is larger than size of list");
            } else {
                try {
                    String output = operator.iterator(stringLinkedList.get(index), queue);
                    System.out.println(output + "\nDo you want to save this output? [Y/N]");
                    String command = input.next();
                    input.nextLine();
                    if (Objects.equals(command, "Y") || Objects.equals(command, "y")) {
                        doSaveString(output);
                    }
                } catch (Exception e) {
                    System.out.println("Unexpected error in iteration");
                }
            }
        } catch (Exception e) {
            System.out.println("Not an integer. ");
            doRun();
        }
    }

    private void doSaveString(String output) {
        System.out.println("\nEnter the location and filename to save the string to: ");
        String location = input.nextLine();
        try {
            StringWriter writer = new StringWriter(location);
            writer.open();
            writer.write(output);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Invalid filename. Do you want to try again? [Y/N]");
            String command = input.next();
            input.nextLine();
            if (Objects.equals(command, "Y") || Objects.equals(command, "y")) {
                doSaveString(output);
            }
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
            String output = operator.singular(text, find, replace, toBool(all));
            System.out.println(output);
            System.out.println("\nDo you want to save this output? [Y/N]");
            String command = input.next();
            input.nextLine();
            if (Objects.equals(command, "Y") || Objects.equals(command, "y")) {
                doSaveString(output);
            }
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
        System.out.println("\tc -> Current queue...");
        System.out.println("\tl -> Load queue...");
        System.out.println("\ts -> Save queue...");
        System.out.println("\th -> home");
        String command = input.next();
        input.nextLine();
        processQueueCommand(command);
    }

    private void processQueueCommand(String command) {
        switch (command) {
            case "c":
                doThisQueue();
                break;
            case "l":
                doLoadQueue();
                break;
            case "s":
                doSaveQueue();
                break;
            case "h":
                break;
            default:
                doQueue();
        }
    }


    private void doLoadQueue() {
        System.out.println("\nLoad a:");
        System.out.println("\tc -> .csv file");
        System.out.println("\tt -> .tsv file");
        System.out.println("\tj -> .json file");
        System.out.println("\th -> Cancel and return home");
        String command = input.next();
        input.nextLine();
        processLoadQueueCommand(command);

    }

    private void processLoadQueueCommand(String command) {
        switch (command) {
            case "c":
                doReadCsv();
                break;
            case "t":
                doReadTsv();
                break;
            case "j":
                doReadJson();
                break;
            case "h":
                break;
            default:
                doLoadQueue();
        }
    }

    private void doReadCsv() {
        System.out.println("\nEnter the location and filename to load:");
        String filename = input.next();
        input.nextLine();
        CsvReader reader = new CsvReader(filename);
        try {
            queue = reader.read();
            System.out.println("Successfully loaded queue");
        } catch (InvalidIntegerException e) {
            System.out.println("Invalid .csv file, mismatched rows");
        } catch (IOException e) {
            System.out.println("Invalid file name");
        }
    }

    private void doReadTsv() {
        System.out.println("\nEnter the location and filename to load:");
        String filename = input.next();
        input.nextLine();
        TsvReader reader = new TsvReader(filename);
        try {
            queue = reader.read();
            System.out.println("Successfully loaded queue");
        } catch (InvalidIntegerException e) {
            System.out.println("Invalid .csv file, mismatched rows");
        } catch (IOException e) {
            System.out.println("Invalid file name");
        }
    }

    private void doReadJson() {
        System.out.println("\nEnter the location and filename to load:");
        String filename = input.next();
        input.nextLine();
        JsonReader reader = new JsonReader(filename);
        try {
            queue = reader.read();
            System.out.println("Successfully loaded queue");
        } catch (IOException e) {
            System.out.println("Invalid file or file name");
        }
    }

    private void doSaveQueue() {
        System.out.println("\nSave as:");
        System.out.println("\tc -> .csv file");
        System.out.println("\tt -> .tsv file");
        System.out.println("\tj -> .json file");
        System.out.println("\th -> Cancel and return home");
        String command = input.next();
        input.nextLine();
        processSaveQueueCommand(command);
    }

    private void processSaveQueueCommand(String command) {
        switch (command) {
            case "c":
                doSaveCsv();
                break;
            case "t":
                doSaveTsv();
                break;
            case "j":
                doSaveJson();
                break;
            case "h":
                break;
            default:
                doSaveQueue();
        }
    }


    private void doSaveTsv() {
        try {
            System.out.println("\nEnter the location and filename to save:");
            String filename = input.next();
            input.nextLine();
            TsvWriter writer = new TsvWriter(filename);
            writer.open();
            writer.write(queue);
            writer.close();
            System.out.println("Saved to " + filename);
        } catch (IOException | InvalidLengthException e) {
            System.out.println("Invalid file name");
            doSaveQueue();
        }
    }


    private void doSaveCsv() {
        try {
            System.out.println("\nEnter the location and filename to save:");
            String filename = input.next();
            input.nextLine();
            CsvWriter writer = new CsvWriter(filename);
            writer.open();
            writer.write(queue);
            writer.close();
            System.out.println("Saved to " + filename);
        } catch (IOException | InvalidLengthException e) {
            System.out.println("Invalid file name");
            doSaveQueue();
        }
    }

    private void doSaveJson() {
        try {
            System.out.println("\nEnter the location and filename to save:");
            String filename = input.next();
            input.nextLine();
            JsonWriter writer = new JsonWriter(filename);
            writer.open();
            writer.write(queue);
            writer.close();
            System.out.println("Saved to " + filename);
        } catch (IOException e) {
            System.out.println("Invalid file name");
            doSaveQueue();
        }
    }

    private void doThisQueue() {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add new operation");
        System.out.println("\te -> edit an operation");
        System.out.println("\ts -> swap queue elements");
        System.out.println("\td -> delete an operation");
        System.out.println("\tv -> view queue");
        System.out.println("\th -> home");
        String command = input.next();
        input.nextLine();
        processThisQueueCommand(command);
    }

    private void processThisQueueCommand(String command) {
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
                doThisQueue();
            case "h":
                break;
            default:
                doThisQueue();
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
            doThisQueue();
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
        doThisQueue();
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
        doThisQueue();
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
            doThisQueue();
        } catch (Exception e) {
            System.out.println("Input is not an integer");
            doThisQueue();
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
        System.out.println("\tl -> Load string from text file");
        System.out.println("\ts -> Save strings as a file");
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
            case "l":
                doLoadString();
                break;
            case "d":
                doDeleteString();
                break;
            case "v":
                doViewStrings();
            case "h":
                break;
            default:
                doStrings();
        }
    }

    private void doLoadString() {
        try {
            System.out.println("\nEnter the location and filename to load:");
            String filename = input.next();
            input.nextLine();
            StringReader reader = new StringReader(filename);
            stringLinkedList.add(reader.read());
            System.out.println("Successfully loaded string");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
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
                break;
            default:
                System.out.println("Invalid key selection, returning home...");
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
