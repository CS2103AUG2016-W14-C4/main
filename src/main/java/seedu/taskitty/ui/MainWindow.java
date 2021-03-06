package seedu.taskitty.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.taskitty.commons.core.Config;
import seedu.taskitty.commons.core.GuiSettings;
import seedu.taskitty.commons.events.ui.ExitAppRequestEvent;
import seedu.taskitty.logic.Logic;
import seedu.taskitty.model.UserPrefs;
import seedu.taskitty.model.task.Task;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart {

    private static final String ICON = "/images/Tasks-icon.png";
    private static final String FXML = "MainWindow.fxml";
    public static final int MIN_HEIGHT = 650;
    public static final int MIN_WIDTH = 750;

    private Logic logic;

    // Independent Ui parts residing in this Ui container

    private ResultDisplay resultDisplay;
    private static StatusBarFooter statusBarFooter;
    private CommandBox commandBox;
    private Config config;

    // Handles to elements of this Ui container
    private VBox rootLayout;
    private Scene scene;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;
    
    @FXML
    private MenuItem undoMenuItem;
    
    @FXML
    private MenuItem redoMenuItem;
    
    @FXML
    private MenuItem viewMenuItem;
    
    @FXML
    private MenuItem viewAllMenuItem;
    
    @FXML
    private MenuItem viewDoneMenuItem;
    
    @FXML
    private MenuItem exitMenuItem;
    
    @FXML
    private MenuItem clearMenuItem;
    
    //@@author A0130853L
    @FXML
    private AnchorPane taskListPanelPlaceholder;
    
    @FXML
    private AnchorPane deadlineListPanelPlaceholder;

    @FXML
    private AnchorPane eventListPanelPlaceholder;

    //@@author 
    @FXML
    private ImageView catImage;
    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private AnchorPane statusbarPlaceholder;


    @Override
    public void setNode(Node node) {
        rootLayout = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {

        MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
        mainWindow.configure(config.getAppTitle(), config.getTaskManagerName(), config, prefs, logic);
        return mainWindow;
    }

    private void configure(String appTitle, String taskManagerName, Config config, UserPrefs prefs,
                           Logic logic) {

        //Set dependencies
        this.logic = logic;
        this.config = config;

        //Configure the UI
        setTitle(appTitle);
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        scene = new Scene(rootLayout);
        primaryStage.setScene(scene);

        setAccelerators();
    }
    
    //@@author A0139052L
    private void setAccelerators() {
        helpMenuItem.setAccelerator(KeyCombination.valueOf("F1"));
        exitMenuItem.setAccelerator(KeyCombination.valueOf("Esc"));
        undoMenuItem.setAccelerator(KeyCombination.valueOf("Ctrl + Shift + U"));
        redoMenuItem.setAccelerator(KeyCombination.valueOf("Ctrl + Shift + Y"));
        viewMenuItem.setAccelerator(KeyCombination.valueOf("Ctrl + Shift + T"));
        viewAllMenuItem.setAccelerator(KeyCombination.valueOf("Ctrl + Shift + L"));
        viewDoneMenuItem.setAccelerator(KeyCombination.valueOf("Ctrl + Shift + D"));
        clearMenuItem.setAccelerator(KeyCombination.valueOf("Ctrl + Shift + C"));        
    }
    
    //@@author
    public void fillInnerParts() {

        TaskListPanel.load(primaryStage, getTaskListPlaceholder(),
                logic.getFilteredTaskList(), new TaskListPanel(), Task.TASK_COMPONENT_COUNT);
        TaskListPanel.load(primaryStage, getDeadlineListPlaceholder(),
                logic.getFilteredDeadlineList(), new TaskListPanel(), Task.DEADLINE_COMPONENT_COUNT);
        TaskListPanel.load(primaryStage, getEventListPlaceholder(),
                logic.getFilteredEventList(), new TaskListPanel(), Task.EVENT_COMPONENT_COUNT);
        resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
        statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(),
                config.getTaskManagerFilePath());
        commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, this, logic);
    }
    
    //@@author A0135793W
    public static StatusBarFooter getStatusBarFooter() {
        return statusBarFooter;
    }
    
    //@@author
    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }
    
    public ImageView getCatImage() {
        return catImage;
    }

    //@@author A0130853L
    private AnchorPane getTaskListPlaceholder() {
        return taskListPanelPlaceholder;
    }
    
    private AnchorPane getDeadlineListPlaceholder() {
        return deadlineListPanelPlaceholder;
    }
    
    private AnchorPane getEventListPlaceholder() {
        return eventListPanelPlaceholder;
    }

    //@@author
    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    protected void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    public GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = HelpWindow.load(primaryStage);
        helpWindow.show();
    }

    public void show() {
        primaryStage.show();
    }
    
    //@@author A0139052L
    @FXML
    public void handleUndo() {
        commandBox.handleCommands("undo");
    }
    
    @FXML
    public void handleRedo() {
        commandBox.handleCommands("redo");
    }
    
    @FXML
    public void handleView() {
        commandBox.handleCommands("view");
    }
    
    @FXML
    public void handleViewAll() {
        commandBox.handleCommands("view all");
    }
            
    @FXML
    public void handleViewDone() {
        commandBox.handleCommands("view done");
    }
    
    @FXML
    public void handleClear() {
        commandBox.handleCommands("clear");
    }   
    
    //@@author A0130853L
    /**
     *  Initialises the list to show today's events upon UI initialisation
     * */
    public void initialiseList() {
        logic.initialiseList();
    }
    
    //@@author
    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

}
