package seedu.taskitty.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.taskitty.commons.core.ComponentManager;
import seedu.taskitty.commons.core.LogsCenter;
import seedu.taskitty.commons.core.UnmodifiableObservableList;
import seedu.taskitty.commons.events.model.TaskManagerChangedEvent;
import seedu.taskitty.commons.util.StringUtil;
import seedu.taskitty.model.task.ReadOnlyTask;
import seedu.taskitty.model.task.Task;
import seedu.taskitty.model.task.TaskType;
import seedu.taskitty.model.task.UniqueTaskList;
import seedu.taskitty.model.task.UniqueTaskList.DuplicateMarkAsDoneException;
import seedu.taskitty.model.task.UniqueTaskList.TaskNotFoundException;

import java.awt.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the task manager data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskManager taskManager;
    private final FilteredList<Task> filteredTasks;
    private final Stack<ReadOnlyTaskManager> historyCommands;
    private final Stack<Predicate> historyPredicates;

    /**
     * Initializes a ModelManager with the given TaskManager
     * TaskManager and its variables should not be null
     */
    public ModelManager(TaskManager src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with task manager: " + src + " and user prefs " + userPrefs);

        taskManager = new TaskManager(src);
        filteredTasks = new FilteredList<>(taskManager.getTasks());
        historyCommands = new Stack<ReadOnlyTaskManager>();
        historyPredicates = new Stack<Predicate>();
    }

    public ModelManager() {
        this(new TaskManager(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskManager initialData, UserPrefs userPrefs) {
        taskManager = new TaskManager(initialData);
        filteredTasks = new FilteredList<>(taskManager.getTasks());
        historyCommands = new Stack<ReadOnlyTaskManager>();
        historyPredicates = new Stack<Predicate>();
    }

    @Override
    public void resetData(ReadOnlyTaskManager newData) {
        taskManager.resetData(newData);
        indicateTaskManagerChanged();
    }

    @Override
    public ReadOnlyTaskManager getTaskManager() {
        return taskManager;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskManagerChanged() {
        raise(new TaskManagerChangedEvent(taskManager));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskManager.removeTask(target);
        updateFilteredListToShowAll();
           
    	indicateTaskManagerChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        taskManager.addTask(task);
    	updateFilteredListToShowAll();
 
        indicateTaskManagerChanged();
    }
    
    public synchronized void undo() {
        resetData(historyCommands.pop());
        updateFilteredTaskList(historyPredicates.pop());
        
    }
    
    public synchronized void saveState() {
        historyCommands.push(new TaskManager(taskManager));
        historyPredicates.push(filteredTasks.getPredicate());
    }
    
    public synchronized void removeUnchangedState() {
        historyCommands.pop();
        historyPredicates.pop();
    }
    
    @Override
    public synchronized void doneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException, DuplicateMarkAsDoneException{
    	taskManager.doneTask(target);
        
        updateFilteredListToShowAll();
            
    	indicateTaskManagerChanged();
    }
   	@Override
    public synchronized void editTask(ReadOnlyTask target, String newName) throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException {
        int index = taskManager.removeTaskForEdit(target);
        Task taskToEdit = (Task) target;
        taskToEdit.setNewName(newName);
        indicateTaskManagerChanged();
        taskManager.addTask(taskToEdit, index);
        updateFilteredListToShowAll();
        indicateTaskManagerChanged();
    }

    //=========== Filtered Task List Accessors ===============================================================
   	
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }
    @Override
    public ObservableList<ReadOnlyTask> getFilteredTodoTaskList() {
    	ObservableList<ReadOnlyTask> todos = FXCollections.observableArrayList();
    	for (Task t: filteredTasks) {
    		if (t.getTaskType().equals(TaskType.TODO)) {
    			todos.add(t);
    		}
    	}
        return todos;
    }
    
    @Override
    public ObservableList<ReadOnlyTask> getFilteredDeadlineTaskList() {
    	ObservableList<ReadOnlyTask> deadlines = FXCollections.observableArrayList();
    	for (Task t: filteredTasks) {
    		if (t.getTaskType().equals(TaskType.DEADLINE)) {
    			deadlines.add(t);
    		}
    	}
        return deadlines;
    }
    
    @Override
    public ObservableList<ReadOnlyTask> getFilteredEventTaskList() {
    	ObservableList<ReadOnlyTask> events = FXCollections.observableArrayList();
    	for (Task t: filteredTasks) {
    		if (t.getTaskType().equals(TaskType.EVENT)) {
    			events.add(t);
    		}
    	}
        return events;
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }
    

    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }
    

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }
    
 
    
    private void updateFilteredTaskList(Predicate previousPredicate) {
        filteredTasks.setPredicate(previousPredicate);
    }
    

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
