package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.Scanner;


// Code for UI adapted from Teller App (from Edx)
public class FindAndReplaceApp {
    private Scanner input;


    //EFFECTS: Runs the FindAndReplace application
    public FindAndReplaceApp() {
        runFindAndReplace();
    }

    private void runFindAndReplace() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        System.out.println("Process finished");
    }

    private void displayMenu() {
    }

    private void processCommand(String command) {
    }

    private void init() {
        input = new Scanner(System.in);
    }


}
