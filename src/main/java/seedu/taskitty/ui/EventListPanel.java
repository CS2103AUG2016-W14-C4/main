package seedu.taskitty.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.taskitty.commons.core.LogsCenter;
import seedu.taskitty.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.taskitty.model.task.ReadOnlyTask;

// Dummy Placeholder for Deadline List Panel
// TO BE UPDATED

public class EventListPanel extends UiPart{
    private final Logger logger = LogsCenter.getLogger(EventListPanel.class);
    private static final String FXML = "EventListPanel.fxml";
    private VBox panel;
    private AnchorPane placeHolderPane;
    
    @FXML
    private ListView<ReadOnlyTask> eventListView;
    
    public EventListPanel() {
        super();
    }
    
    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
    
    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }
    
    public static EventListPanel load(Stage primaryStage, AnchorPane personListPlaceholder,
            ObservableList<ReadOnlyTask> personList) {
        EventListPanel EventListPanel =
                UiPartLoader.loadUiPart(primaryStage, personListPlaceholder, new EventListPanel());
        EventListPanel.configure(personList);
        return EventListPanel;
    }

    private void configure(ObservableList<ReadOnlyTask> personList) {
        setConnections(personList);
        addToPlaceholder();
    }
    
    private void setConnections(ObservableList<ReadOnlyTask> personList) {
        eventListView.setItems(personList);
        eventListView.setCellFactory(listView -> new PersonListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setEventHandlerForSelectionChangeEvent() {
        eventListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                raise(new TaskPanelSelectionChangedEvent(newValue));
            }
        });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            eventListView.scrollTo(index);
            eventListView.getSelectionModel().clearAndSelect(index);
        });
    }

    class PersonListViewCell extends ListCell<ReadOnlyTask> {

        public PersonListViewCell() {
        }

        @Override
        protected void updateItem(ReadOnlyTask person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(TaskCard.load(person, getIndex() + 1).getLayout());
            }
        }
    }
}

