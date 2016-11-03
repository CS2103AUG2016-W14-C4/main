package seedu.taskitty.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import seedu.taskitty.commons.util.DateUtil;
import seedu.taskitty.model.task.ReadOnlyTask;

//@@author A0130853L

/**
 * Panel containing the list of event tasks.
 */
public class EventListPanel extends TaskListPanel {
    private static final String FXML = "EventListPanel.fxml";
    
    @FXML
    private Label date;
    
    @FXML
    private Label header;
    
    @FXML
    private ListView<ReadOnlyTask> eventListView;
    
    public static final int EVENT_CARD_ID = 2;
    
    public EventListPanel() {
        super();
    }
    
    @Override
    public String getFxmlPath() {
        return FXML;
    }
    
    @Override
    public int getTaskCardID() {
        return EVENT_CARD_ID;
    }
    
    @Override
    public void configure(ObservableList<ReadOnlyTask> eventList) {
    	setDefaultDate();
        setConnections(eventListView, eventList);
        addToPlaceholder();
    }
    
    /**
     * For when no view command is executed.
     */
    public void setEmptyDate() {
    	date.setText("");
    }
    
    /** 
     * For when `view today` is executed, and today's date with the "(today)" label will be shown.
     */
    public void setDefaultDate() {
    	
    	date.setText(DateUtil.createDefaultDateString());
    	date.setStyle("-fx-text-fill: black");
    	date.setStyle("-fx-background-color: white");
    }
    
    /**
     * For when `view date` is executed, and that date will be shown.
     * @param newDate
     */
    public void updateDate(LocalDate newDate) {
    	DateFormat df = new SimpleDateFormat("dd MMM yyyy");
    	date.setText(df.format(newDate));
    }
}
