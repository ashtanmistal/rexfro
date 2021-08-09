package ui;


import model.Operator;
import model.Queue;
import model.exceptions.InvalidIntegerException;
import model.exceptions.InvalidLengthException;
import persistence.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;

public class Rexfro extends JFrame implements ActionListener {
    public static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);
    public static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
    private static final String ERRORIMAGE_JPG = "data/errorimage.jpg";
    private static final Image icon = Toolkit.getDefaultToolkit().getImage("data/Logo.png");
    private static Queue queue;

    private Operator operator;
    private LinkedList<String> stringLinkedList;
    private LinkedList<String> fileNameLinkedList;
    private LinkedList<JEditorPane> textTabs;
    private LinkedList<String> textTabsFilenames;

    // dynamically updated menus
    JMenu textSave = new JMenu("Save");
    JMenu runOnIndividual = new JMenu("Run on individual text");
    JTabbedPane tabs = new JTabbedPane();
    JTextComponent queueTextComponent = new JEditorPane();
    JMenu textRemove = new JMenu("Remove");


    public Rexfro() {
        super("Rexfro Find And Replace Operator");
        initializeFields();
        initializeGraphics();
    }

    // getter
    public static Queue getQueue() {
        return queue;
    }

    // MODIFIES: this
    // EFFECTS: Draws the JFrame window where this Rexfro instance will open, populates tools to be used
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.setJMenuBar(menuSystem());
        this.setIconImage(icon);
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
        queueTextComponent.setSize((int) (0.9 * WIDTH), (int) (0.9 * HEIGHT));
        queuePanel.add(queueTextComponent);
        tabs.add("Queue", new JScrollPane(queuePanel)); // TODO: I changed stuff here
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
        createAboutMenu(menuBar);
        return menuBar;
    }

    private void createAboutMenu(JMenuBar menuBar) {
        JMenuItem aboutMenu = new JMenuItem("About");
        StringBuilder readMe = new StringBuilder();
        aboutMenu.addActionListener(e -> readMeCreator(readMe));
        menuBar.add(aboutMenu);
    }

    private void readMeCreator(StringBuilder readMe) {
        Scanner input = null;
        try {
            input = new Scanner(new File("data/About.txt"));
        } catch (FileNotFoundException exception) {
            errorDialog("README.md file not found");
        }
        while (true) {
            assert input != null;
            if (!input.hasNextLine()) {
                break;
            }
            readMe.append(input.nextLine()).append("\n");
        }
        JTextArea textArea = new JTextArea();
        textArea.setText(readMe.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JFrame tempFrame = new JFrame();
        tempFrame.getContentPane().add(scrollPane);
        tempFrame.setSize((int) (0.7 * WIDTH), (int) (0.91 * HEIGHT));
        tempFrame.setLocationRelativeTo(this);
        tempFrame.setVisible(true);
    }

    private void createRunMenu(JMenuBar menuBar) {
        JMenu runMenu = new JMenu("Run");
        JMenuItem runOnAll = new JMenuItem("Run on all");
        runOnAll.addActionListener(e -> runOnAll());
        runMenu.add(runOnAll);
        dynamicFileMenuAdd(runOnIndividual, "run");
        runMenu.add(runOnIndividual);
        JMenuItem manualFR = new JMenuItem("Manual Find and Replace");
        runMenu.add(manualFR);
        manualFR.addActionListener(e -> runManualFROperation());
        menuBar.add(runMenu);
    }

    private void runManualFROperation() {
        String text = (String) JOptionPane.showInputDialog(this,
                "Enter the text:", "Rexfro Manual Mode - Text", JOptionPane.PLAIN_MESSAGE, null,
                null, "");
        String find = (String) JOptionPane.showInputDialog(this,
                "Enter the find operation:", "Rexfro Manual Mode - Find", JOptionPane.PLAIN_MESSAGE,
                null, null, "");
        String replace = (String) JOptionPane.showInputDialog(this,
                "Enter the Replace operation:", "Rexfro Manual Mode - Replace",
                JOptionPane.PLAIN_MESSAGE, null, null, "");
        String replaceAll = (String) JOptionPane.showInputDialog(this,
                "Do you want to replace all [Y], or just the first instance [N]?",
                "Rexfro Manual Mode - Replace All", JOptionPane.PLAIN_MESSAGE, null,
                null, "");
        if ((replaceAll != null) && (replace != null) && (find != null) && (text != null)) {
            String result = operator.singular(text, find, replace, queue.validTrue(replaceAll));
            JTextArea textArea = new JTextArea();
            textArea.setText(result);
            JFrame tempFrame = new JFrame();
            tempFrame.add(textArea);
            tempFrame.setSize((int) (0.5 * WIDTH), (int) (0.5 * HEIGHT));
            tempFrame.setVisible(true);
        }
    }

    private void runOnAll() {
        pullTabChangesToText();
        reloadQueue();
        for (int i = 0; i < textTabsFilenames.size(); i++) {
            try {
                stringLinkedList.set(i, operator.iterator(stringLinkedList.get(i), queue));
            } catch (Exception exception) {
                errorDialog("Unable to process queue on " + textTabsFilenames.get(i));
            }
        }
        pushTextChangesToTabs();
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
                saveItem(fileNameLinkedList.indexOf(s), s);
            }
        });
        textMenu.add(textSaveAll);
        dynamicFileMenuAdd(textRemove, "remove");
        textMenu.add(textRemove);
        JMenuItem textNew = new JMenuItem("New...");
        textMenu.add(textNew);
        textNew.addActionListener(e -> newTextMethod());
        menuBar.add(textMenu);
    }

    private void newTextMethod() {
        String result = (String) JOptionPane.showInputDialog(this,
                "Enter the name of the file", "New file", JOptionPane.PLAIN_MESSAGE,
                null,
                null, ".txt");
        if (result != null) {
            String path = "data\\saved\\" + result;
            stringLinkedList.add("");
            fileNameLinkedList.add(path);
            JEditorPane tempEditorPane = new JEditorPane();
            tempEditorPane.setSize((int) (0.9 * WIDTH), (int) (0.9 * HEIGHT));
            tempEditorPane.setText("");
            tabs.add(result, new JScrollPane(tempEditorPane)); // TODO: change here to add scrolling
            textTabs.add(tempEditorPane);
            textTabsFilenames.add(result);
            reloadDynamicMenus();
        }
    }

    private void textLoadMethod(JMenu textMenu) {
        JMenuItem textLoad = new JMenuItem("Load");
        // from https://www.highrankingessays.com/uncategorized/code-completion-11-103-add-listeners-to-menu-add-the-menu-items-shown-below/
        textLoad.addActionListener((ActionEvent ae) -> {
            JFileChooser openMenu = new JFileChooser();
            new File("");
            File fileOpen;
            openMenu.setCurrentDirectory(new File(""));
            openMenu.setDialogTitle("Open a File");
            if (openMenu.showOpenDialog(textLoad) == JFileChooser.APPROVE_OPTION) {
                fileOpen = openMenu.getSelectedFile();
                try {
                    persistence.StringReader reader = new persistence.StringReader(fileOpen.getPath());
                    extractedHelperForLoadingText(fileOpen, reader); // this was in a if statement before to avoid
                    reloadDynamicMenus(); // empty text files and unsupported files
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
        tempEditorPane.setSize((int) (0.9 * WIDTH), (int) (0.9 * HEIGHT));
        tempEditorPane.setText(reader.read());
        String fileName = fileOpen.getName();
        tabs.add(fileName, new JScrollPane(tempEditorPane)); // TODO: Change it here to add scrolling
        textTabs.add(tempEditorPane);
        textTabsFilenames.add(fileName);
    }

    private void reloadDynamicMenus() {
        dynamicFileMenuAdd(textSave, "save");
        dynamicFileMenuAdd(runOnIndividual, "run");
        dynamicFileMenuAdd(textRemove, "remove");
    }

    private void errorDialog(String errorType) {
        JFrame errorFrame = new JFrame();
        // from https://stackoverflow.com/questions/8333802/displaying-an-image-in-java-swing
        try {
            BufferedImage img = ImageIO.read(new File(ERRORIMAGE_JPG));
            ImageIcon icon = new ImageIcon(img);
            JLabel label = new JLabel(icon);
            JOptionPane.showMessageDialog(errorFrame, label);
        } catch (IOException e) {
            // do nothing
        }
        JOptionPane.showMessageDialog(errorFrame, errorType);
    }

    private void dynamicFileMenuAdd(JMenu menu, String action) {
        menu.removeAll();
        if (fileNameLinkedList != null && fileNameLinkedList.size() != 0) {
            if (action.equalsIgnoreCase("save")) {
                extractedSaveMethod(menu);
            } else if (action.equalsIgnoreCase("run")) {
                extractedRunMethod(menu);
            } else if (action.equalsIgnoreCase("remove")) {
                extractedRemoveMethod(menu);
            }
        }
    }

    private void extractedSaveMethod(JMenu menu) {
        for (String s : fileNameLinkedList) {
            JRadioButtonMenuItem fitem = new JRadioButtonMenuItem(s, false);
            menu.add(fitem);
            fitem.addActionListener(e -> saveItem(fileNameLinkedList.indexOf(s), s));
        }
    }

    private void extractedRemoveMethod(JMenu menu) {
        for (String s : fileNameLinkedList) {
            JRadioButtonMenuItem fitem = new JRadioButtonMenuItem(s, false);
            menu.add(fitem);
            fitem.addActionListener((ActionEvent ae) -> {
                int index = fileNameLinkedList.indexOf(s);
                tabs.removeTabAt(index + 1);
                fileNameLinkedList.remove(index);
                stringLinkedList.remove(index);
                textTabsFilenames.remove(index);
                textTabs.remove(index);
                textSave.remove(index);
                runOnIndividual.remove(index);
                menu.remove(index);
            });
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
                    reloadQueue();
                    String st = operator.iterator(stringLinkedList.get(i), queue);
                    stringLinkedList.set(i, st);
                    pushTextChangesToTabs();
                } catch (Exception ex) {
                    errorDialog("Error in iterating through text");
                }
            });
        }
    }

    private void pushTextChangesToTabs() {
        for (int i = 0; i < textTabsFilenames.size(); i++) {
            textTabs.get(i).setText(stringLinkedList.get(i));
        }
    }

    private void pullTabChangesToText() {
        for (int i = 0; i < textTabsFilenames.size(); i++) {
            stringLinkedList.set(i, textTabs.get(i).getText());
        }
    }

    private void reloadQueue() {
        queue = new Queue();
        Scanner scanner = new Scanner(queueTextComponent.getText());

        try {
            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                String[] tempArray = temp.split(",");
                queue.addToQueue(tempArray[0], tempArray[1], tempArray[2]);
            }
        } catch (Exception e) {
            errorDialog("Please fix the queue before running");
        }
    }

    private void saveItem(int index, String location) {
        pullTabChangesToText();
        String item = stringLinkedList.get(index);
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
