package seedu.taskitty.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import seedu.taskitty.model.task.ReadOnlyTask;


/**
 * Panel containing the list of todo tasks.
 */
public class TodoListPanel extends TaskListPanel {
    private static final String FXML = "TodoListPanel.fxml";

    @FXML
    private Label header;
    @FXML
    private ListView<ReadOnlyTask> todoListView;

    public TodoListPanel() {
        super();
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    protected void configure(ObservableList<ReadOnlyTask> taskList) {
    	header.setText("TODOS [t]");
    	header.setStyle("-fx-text-fill: white");
        setConnections(todoListView, taskList);
        addToPlaceholder();
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            todoListView.scrollTo(index);
            todoListView.getSelectionModel().clearAndSelect(index);
        });
    }

}
