# A0130853Lreused
###### /java/seedu/taskitty/ui/TaskListPanel.java
``` java
/**
 * Base class for the 3 panels containing the list of tasks.
 */
public abstract class TaskListPanel extends UiPart {
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
    protected VBox panel;
    protected AnchorPane placeHolderPane;

    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }
    
    public abstract void configure(ObservableList<ReadOnlyTask> taskList);
    
    public abstract int getTaskCardID();
    
    public static <T extends TaskListPanel> T load(Stage primaryStage, AnchorPane taskListPlaceholder,
                                       ObservableList<ReadOnlyTask> taskList, T listPanel) {
        T taskListPanel =  UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, listPanel);
        taskListPanel.configure(taskList);
        return taskListPanel;
    }    
    
    protected void setConnections(ListView<ReadOnlyTask> taskListView, ObservableList<ReadOnlyTask> taskList) {
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new TaskListViewCell(getTaskCardID()));
        setEventHandlerForSelectionChangeEvent(taskListView);
    }

    protected void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(panel);
    }

    private void setEventHandlerForSelectionChangeEvent(ListView<ReadOnlyTask> taskListView) {
        taskListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                raise(new TaskPanelSelectionChangedEvent(newValue));
            }
        });
    }

    class TaskListViewCell extends ListCell<ReadOnlyTask> {
        
        private final int taskCardID;
        
        public TaskListViewCell(int taskCardID) {
            this.taskCardID = taskCardID;
        }

        @Override
        protected void updateItem(ReadOnlyTask task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                switch (taskCardID) {
                
                case (TodoListPanel.TODO_CARD_ID):
                    setGraphic(TodoCard.load(task, getIndex() + 1).getLayout());
                    break;
                
                case (DeadlineListPanel.DEADLINE_CARD_ID):
                    setGraphic(DeadlineCard.load(task, getIndex() + 1).getLayout());
                    break;
                    
                case (EventListPanel.EVENT_CARD_ID):
                    setGraphic(EventCard.load(task, getIndex() + 1).getLayout());
                    break;
                
                default:
                    setGraphic(TodoCard.load(task, getIndex() + 1).getLayout());
                    break;
                }                
            }
        }
    }

}
```
