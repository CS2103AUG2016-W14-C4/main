package seedu.taskitty.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.taskitty.commons.util.FxViewUtil;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart {
    public static final String RESULT_DISPLAY_ID = "resultDisplay";
    //@@author A0130853L
    private static final String WELCOME_MESSAGE = "Welcome! Here is your agenda for today:";
    private static final String WELCOME_MESSAGE_WITH_OVERDUE_DEADLINES = "Welcome! You have overdue tasks.";
    private static boolean hasOverdue;
    
    //@@author
    public static final String IMAGE_CAT_NORMAL = "/images/cat_normal.png";
    public static final String IMAGE_CAT_HAPPY = "/images/cat_happy.png";
    public static final String IMAGE_CAT_SAD = "/images/cat_sad.png";
    
    private static final String FXML = "ResultDisplay.fxml";

    private AnchorPane placeHolder;

    private AnchorPane mainPane;
    
    @FXML
    private AnchorPane resultDisplayArea;
    
    @FXML
    private Label toolTipLabel;
    
    @FXML
    private Label descriptionLabel;

    public static ResultDisplay load(Stage primaryStage, AnchorPane placeHolder) {
        ResultDisplay statusBar = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ResultDisplay());
        statusBar.configure();
        return statusBar;
    }
    
    //@@author A0130853L
    public void configure() {
        postCorrectWelcomeMessage();
        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(mainPane);
        FxViewUtil.applyAnchorBoundaryParameters(resultDisplayArea, 0.0, 0.0, 0.0, 0.0);
    }
    
    //@@author
    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    //@@author A0139930B
    /**
     * Prints the given message onto the Result Display without description
     */
    public void postMessage(String message) {
        toolTipLabel.setText(message);
        descriptionLabel.setText("");
    }
    
    /**
     * Prints the given message onto the Result Display with description
     */
    public void postMessage(String message, String description) {
        toolTipLabel.setText(message);
        descriptionLabel.setText(description);
    }
    
    //@@author A0130853L
    private void displayOverdueWelcomeMessage() {
        postMessage(WELCOME_MESSAGE_WITH_OVERDUE_DEADLINES);
    }
    
    public static void setOverdue() {
        hasOverdue = true;
    }
    
    private void postCorrectWelcomeMessage() {
        if (!hasOverdue) {
            postMessage(WELCOME_MESSAGE);
        } else {
            displayOverdueWelcomeMessage();
        }
    }

}
