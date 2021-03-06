package seedu.taskitty.ui;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import seedu.taskitty.commons.core.LogsCenter;
import seedu.taskitty.commons.events.model.TaskManagerChangedEvent;
import seedu.taskitty.commons.events.model.ViewTypeChangedEvent;
import seedu.taskitty.commons.util.DateTimeUtil;
import seedu.taskitty.commons.util.FxViewUtil;

import java.util.Date;
import java.util.logging.Logger;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart {
    private static final Logger logger = LogsCenter.getLogger(StatusBarFooter.class);

    //@@author A0130853L
    public static final String COMPLETED_TASKS = "completed tasks";
    public static final String ALL_TASKS = "all tasks";
    public static final String UPCOMING_TASKS = "upcoming tasks";
    private StatusBar viewStatus;
    
    //@@author
    private StatusBar syncStatus;
    private StatusBar saveLocationStatus;


    private GridPane mainPane;

    @FXML
    private AnchorPane saveLocStatusBarPane;

    @FXML
    private AnchorPane syncStatusBarPane;
    
    @FXML
    private AnchorPane viewStatusBarPane;

    private AnchorPane placeHolder;

    private static final String FXML = "StatusBarFooter.fxml";

    public static StatusBarFooter load(Stage stage, AnchorPane placeHolder, String saveLocation) {
        StatusBarFooter statusBarFooter = UiPartLoader.loadUiPart(stage, placeHolder, new StatusBarFooter());
        statusBarFooter.configure(saveLocation);
        return statusBarFooter;
    }
    //@@author A0130853L
    public void configure(String saveLocation) {
        addMainPane();
        addSyncStatus();
        setSyncStatus("Not updated yet in this session");
        addSaveLocation();
        addViewStatus();
        setViewStatus("Viewing: " + UPCOMING_TASKS);
        setSaveLocation(saveLocation);
        registerAsAnEventHandler(this);
    }
    //@@author

    private void addMainPane() {
        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(mainPane);
    }

    public void setSaveLocation(String location) {
        this.saveLocationStatus.setText(location);
    }

    private void addSaveLocation() {
        this.saveLocationStatus = new StatusBar();
        FxViewUtil.applyAnchorBoundaryParameters(saveLocationStatus, 0.0, 0.0, 0.0, 0.0);
        saveLocStatusBarPane.getChildren().add(saveLocationStatus);
    }

    private void setSyncStatus(String status) {
        this.syncStatus.setText(status);
    }

    private void addSyncStatus() {
        this.syncStatus = new StatusBar();
        FxViewUtil.applyAnchorBoundaryParameters(syncStatus, 0.0, 0.0, 0.0, 0.0);
        syncStatusBarPane.getChildren().add(syncStatus);
    }
    //@@author A0130853L
    private void setViewStatus(String status) {
        this.viewStatus.setText(status);
    }
   
    private void addViewStatus() {
        this.viewStatus = new StatusBar();
        FxViewUtil.applyAnchorBoundaryParameters(viewStatus, 0.0, 0.0, 0.0, 0.0);
        viewStatusBarPane.getChildren().add(viewStatus);
    }
    
    //@@author
    @Override
    public void setNode(Node node) {
        mainPane = (GridPane) node;
    }

    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
    
    //@@author A0130853L
    @Subscribe
    public void handleViewTypeChangedEvent(ViewTypeChangedEvent vtce) {
        String newView = vtce.viewType.toString();
        String viewStatus = "Viewing: ";
        switch(newView) {
        case("date") :
            viewStatus += DateTimeUtil.createUISpecifiedDateString(vtce.getDate());
            break;
        case("done") :
            viewStatus += COMPLETED_TASKS;
            break;
        case("all") :
            viewStatus += ALL_TASKS;
            break;
        default :
            viewStatus += UPCOMING_TASKS;
            break;
        }
        
        logger.info(LogsCenter.getEventHandlingLogMessage(vtce, "Setting filtered view to " + newView));
        setViewStatus(viewStatus); 
        
    }
    
    @Subscribe
    public void handleTaskManagerChangedEvent(TaskManagerChangedEvent tmce) {
        String lastUpdated = (new Date()).toString();
        logger.info(LogsCenter.getEventHandlingLogMessage(tmce, "Setting last updated status to " + lastUpdated));
        setSyncStatus("Last Updated: " + lastUpdated);
    }
}
