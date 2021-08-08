package ui;


import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.io.File;
import java.util.Objects;
import java.util.Scanner;

import model.Operator;
import model.Queue;
import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;
import persistence.*;

public class Rexfro extends JFrame implements ActionListener {
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    private static Queue queue;

    private Operator operator;
    private LinkedList<String> stringLinkedList;
    private LinkedList<String> fileNameLinkedList;
    private LinkedList<JEditorPane> textTabs;
    private LinkedList<String> textTabsFilenames;

    // dynamically updated menus
    JMenu textSave = new JMenu("Save");
    JMenu runOnIndividual = new JMenu("Run on individual text");
    JMenu textRemove = new JMenu("Remove");
    JTabbedPane tabs = new JTabbedPane();
    JTextComponent queueTextComponent = new JEditorPane();



    public Rexfro() {
        super("test");
        initializeFields();
        initializeGraphics();
    }

    // getters
    public static Queue getQueue() {
        return queue;
    }

    public Operator getOperator() {
        return operator;
    }

    public LinkedList<String> getStrings() {
        return stringLinkedList;
    }

    // MODIFIES: this
    // EFFECTS: Draws the JFrame window where this Rexfro instance will open, populates tools to be used
    private void initializeGraphics() {

        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.setJMenuBar(menuSystem());
        tabSystem();
        this.add(tabs);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeFields() {
        queue = new Queue();
        operator = new Operator();
        stringLinkedList = new LinkedList<>();
        fileNameLinkedList = new LinkedList<>();
        textTabs = new LinkedList<>();
        textTabsFilenames = new LinkedList<>();
    }

    private void tabSystem() {
        JPanel queuePanel = new JPanel();
        queueTextComponent.setSize((int)(0.9 * WIDTH), (int)(0.9 * HEIGHT));
        queuePanel.add(queueTextComponent);
        tabs.add("Queue", queuePanel);
    }

    private String parseQueueAsString() throws InvalidLengthException {
        StringBuilder temp = new StringBuilder();
        if (queue.getLength() != 0) {
            for (int i = 0; i < queue.getLength(); i++) {
                temp.append(queue.getFind(i)).append(",")
                        .append(queue.getReplace(i)).append(",").append(queue.getBoolString(i)).append("\n");
            }
        }
        return temp.toString();
    }

    private JMenuBar menuSystem() {
        JMenuBar menuBar = new JMenuBar();
        createQueueMenu(menuBar);
        createTextMenu(menuBar);
        createRunMenu(menuBar);
        return menuBar;
    }

    private void createRunMenu(JMenuBar menuBar) {
        JMenu runMenu = new JMenu("Run");
        JMenuItem runOnAll = new JMenuItem("Run on all");
        runMenu.add(runOnAll);

        dynamicFileMenuAdd(runOnIndividual, "run");
        runMenu.add(runOnIndividual);

        JMenuItem manualFR = new JMenuItem("Manual Find and Replace");
        runMenu.add(manualFR);
        JMenuItem queueOnNewNext = new JMenuItem("Run queue on new text...");
        runMenu.add(queueOnNewNext);
        menuBar.add(runMenu);
    }

    private void createQueueMenu(JMenuBar menuBar) {
        JMenu queueMenu = new JMenu("Queue");
        JMenuItem queueLoad = new JMenuItem("Load");
        queueLoad.addActionListener(e -> loadQueue());
        queueMenu.add(queueLoad);
        JMenu queueSave = new JMenu("Save");
        queueMenu.add(queueSave);
        makeSaveQueueMenu(queueSave);

        menuBar.add(queueMenu);
    }

    private void loadQueue() {
        JFileChooser openMenu = new JFileChooser();
        int returnVal = openMenu.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = openMenu.getSelectedFile();
            try {
                String fileDirectory = file.getAbsolutePath();
                String ext = operator.singular(fileDirectory, ".*\\.", "", true);
                if (ext.equalsIgnoreCase("csv")) {
                    queue = new CsvReader(fileDirectory).read();
                } else if (ext.equalsIgnoreCase("tsv")) {
                    queue = new TsvReader(fileDirectory).read();
                } else if (ext.equalsIgnoreCase("json")) {
                    queue = new JsonReader(fileDirectory).read();
                } else {
                    errorDialog("Unsupported file type");
                }
                queueTextComponent.setText(parseQueueAsString());
            } catch (IOException | InvalidIntegerException | InvalidLengthException e) {
                errorDialog("Unable to load file");
            }
        }
    }

    private void makeSaveQueueMenu(JMenu queueSave) {
        JRadioButtonMenuItem saveQueueCsv = new JRadioButtonMenuItem("Save as csv", false);
        queueSave.add(saveQueueCsv);
        saveQueueCsv.addActionListener(e -> queueSaveMethod("csv"));
        JRadioButtonMenuItem saveQueueTsv = new JRadioButtonMenuItem("Save as tsv", false);
        queueSave.add(saveQueueTsv);
        saveQueueTsv.addActionListener(e -> queueSaveMethod("tsv"));
        JRadioButtonMenuItem saveQueueJson = new JRadioButtonMenuItem("Save as json", false);
        queueSave.add(saveQueueJson);
        saveQueueJson.addActionListener(e -> queueSaveMethod("json"));

    }

    private void queueSaveMethod(String type) {
        reloadQueue();
        JFileChooser openMenu = new JFileChooser();
        int returnVal = openMenu.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = openMenu.getSelectedFile();
            try {
                if (Objects.equals(type, "csv")) {
                    writeQueueCsv(file);
                } else if (Objects.equals(type, "tsv")) {
                    writeQueueTsv(file);
                } else if (Objects.equals(type, "json")) {
                    writeQueueJson(file);
                }
            } catch (FileNotFoundException | InvalidLengthException e) {
                errorDialog("Unable to save file. Queue may not be of equal length.");
            }
        }
    }

    private void writeQueueJson(File file) throws FileNotFoundException {
        JsonWriter writer = new JsonWriter(file.getAbsolutePath());
        writer.open();
        writer.write(queue);
        writer.close();
    }

    private void writeQueueTsv(File file) throws FileNotFoundException, InvalidLengthException {
        TsvWriter writer = new TsvWriter(file.getAbsolutePath());
        writer.open();
        writer.write(queue);
        writer.close();
    }

    private void writeQueueCsv(File file) throws FileNotFoundException, InvalidLengthException {
        CsvWriter writer = new CsvWriter(file.getAbsolutePath());
        writer.open();
        writer.write(queue);
        writer.close();
    }

    private void createTextMenu(JMenuBar menuBar) {
        JMenu textMenu = new JMenu("Text");
        textLoadMethod(textMenu);
        textMenu.add(textSave);
        dynamicFileMenuAdd(textSave, "save");
        JMenuItem textSaveAll = new JMenuItem("Save All");
        textSaveAll.addActionListener(e -> {
            for (String s : fileNameLinkedList) {
                saveItem(stringLinkedList.get(fileNameLinkedList.indexOf(s)), s);
            }
        });
        textMenu.add(textSaveAll);

        dynamicFileMenuAdd(textRemove, "remove");
        textMenu.add(textRemove);

        JMenuItem textNew = new JMenuItem("New...");
        textMenu.add(textNew);
        menuBar.add(textMenu);
    }

    private void textLoadMethod(JMenu textMenu) {
        JMenuItem textLoad = new JMenuItem("Load");
        // from https://www.highrankingessays.com/uncategorized/code-completion-11-103-add-listeners-to-menu-add-the-menu-items-shown-below/
        textLoad.addActionListener((ActionEvent ae) -> {
            JFileChooser openMenu = new JFileChooser();
            File fileOpen = new File("");
            openMenu.setCurrentDirectory(new File("C:\\"));
            openMenu.setDialogTitle("Open a File");
            if (openMenu.showOpenDialog(textLoad) == JFileChooser.APPROVE_OPTION) {
                fileOpen = openMenu.getSelectedFile();
                try {
                    persistence.StringReader reader = new persistence.StringReader(fileOpen.getPath());
                    if (Objects.equals(reader.read(), "")) {
                        throw new IOException();
                    } else {
                        extractedHelperForLoadingText(fileOpen, reader);
                        reloadDynamicMenus();
                    }
                } catch (IOException e) {
                    errorDialog("Error in loading file");
                }
            }
        });
        textMenu.add(textLoad);
    }

    private void extractedHelperForLoadingText(File fileOpen, StringReader reader) throws FileNotFoundException {
        stringLinkedList.add(reader.read());
        fileNameLinkedList.add(fileOpen.getPath());
        JEditorPane tempEditorPane = new JEditorPane();
        tempEditorPane.setSize((int)(0.9 * WIDTH), (int)(0.9 * HEIGHT));
        tempEditorPane.setText(reader.read());
        String fileName = fileOpen.getName();
        tabs.add(fileName, tempEditorPane);
        textTabs.add(tempEditorPane);
        textTabsFilenames.add(fileName);
    }

    private void reloadDynamicMenus() {
        dynamicFileMenuAdd(textSave, "save");
        dynamicFileMenuAdd(runOnIndividual, "run");
        dynamicFileMenuAdd(textRemove, "remove");
    }

    private void errorDialog(String errorType) {
        JOptionPane.showMessageDialog(new JFrame(), errorType);
    }

    private void dynamicFileMenuAdd(JMenu menu, String action) {
        if (fileNameLinkedList != null && fileNameLinkedList.size() != 0) {
            if (action.equalsIgnoreCase("save")) {
                for (String s : fileNameLinkedList) {
                    JRadioButtonMenuItem fitem = new JRadioButtonMenuItem(s, false);
                    menu.add(fitem);
                    fitem.addActionListener(e -> saveItem(stringLinkedList.get(fileNameLinkedList.indexOf(s)), s));
                } // TODO: FIX
            } else if (action.equalsIgnoreCase("run")) {
                extractedRunMethod(menu);
            } else if (action.equalsIgnoreCase("remove")) {
                extractedRemoveMethod(menu);
            }
        }
    }

    private void extractedRunMethod(JMenu menu) {
        for (String s : fileNameLinkedList) {
            JRadioButtonMenuItem fitem = new JRadioButtonMenuItem(s, false);
            menu.add(fitem);
            fitem.addActionListener(e -> {
                try {
                    int i = fileNameLinkedList.indexOf(s);
                    pullTabChangesToText();
                    System.out.println(parseQueueAsString());
                    String st = operator.iterator(stringLinkedList.get(i), queue);
                    System.out.println(st);
                    stringLinkedList.set(i, st);
                    pushTextChangesToTabs();
                } catch (Exception ex) {
                    errorDialog("Error in iterating through text");
                }
            });
        }
    }

    private void extractedRemoveMethod(JMenu menu) {
        for (String s : fileNameLinkedList) {
            JRadioButtonMenuItem fitem = new JRadioButtonMenuItem(s, false);
            menu.add(fitem);
            fitem.addActionListener((ActionEvent ae) -> {
                tabs.removeTabAt(fileNameLinkedList.indexOf(s) + 1);
                menu.remove(fitem);
            });
        }
    }

    private void pushTextChangesToTabs() {
        for (int i = 0; i < textTabsFilenames.size(); i++) {
            textTabs.get(i).setText(stringLinkedList.get(i));
            System.out.println(stringLinkedList);
        }
    }

    private void pullTabChangesToText() {
        for (int i = 0; i < textTabsFilenames.size(); i++) {
            stringLinkedList.set(i, textTabs.get(i).getText());
            System.out.println(stringLinkedList);
        }
    }

    private void reloadQueue() {
        try {
            for (int i = 0; i < queue.getLength(); i++) {
                queue.deleteItem(i);
            }
        } catch (Exception e) {
            errorDialog("Queue is not of the same length. Error in resetting queue");
        }
        Scanner scanner = new Scanner(queueTextComponent.getText());
        while (scanner.hasNextLine()) {
            try {
                String temp = scanner.nextLine();
                String[] tempArray = temp.split(",");
                queue.addToQueue(tempArray[0], tempArray[1], tempArray[2]);
            } catch (Exception e) {
                errorDialog("you got a weird queue, bud. try fixing it before running it again?");
            }
        }
    }

    private void saveItem(String item, String location) {
        if (location.contains("/")) {
            StringWriter writer = new StringWriter(location);
            try {
                writer.open();
                writer.write(item);
                writer.close();
            } catch (FileNotFoundException e) {
                errorDialog("Invalid file location. Please manually choose location.");
                manualSaveString(item);
            }
        } else {
            manualSaveString(item);
        }
    }

    // adapted from https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/FileChooserDemoProject/src/components/FileChooserDemo.java
    private void manualSaveString(String item) {
        JFileChooser openMenu = new JFileChooser();
        int returnVal = openMenu.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = openMenu.getSelectedFile();
            try {
                StringWriter writer = new StringWriter(file.getAbsolutePath());
                writer.open();
                writer.write(item);
                writer.close();
            } catch (FileNotFoundException e) {
                errorDialog("Unable to save file");
            }
        }
    }

    public static void main(String[] args) {
        new Rexfro();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
