package seedu.taskitty.model;

import javafx.collections.transformation.FilteredList;
import seedu.taskitty.commons.core.ComponentManager;
import seedu.taskitty.commons.core.LogsCenter;
import seedu.taskitty.commons.core.UnmodifiableObservableList;
import seedu.taskitty.commons.events.model.DeadlineManagerChangedEvent;
import seedu.taskitty.commons.events.model.EventManagerChangedEvent;
import seedu.taskitty.commons.events.model.TaskManagerChangedEvent;
import seedu.taskitty.commons.util.StringUtil;
import seedu.taskitty.model.task.ReadOnlyTask;
import seedu.taskitty.model.task.Task;
import seedu.taskitty.model.task.UniqueTaskList;
import seedu.taskitty.model.task.UniqueTaskList.DuplicateMarkAsDoneException;
import seedu.taskitty.model.task.UniqueTaskList.TaskNotFoundException;

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
    private final DeadlineManager deadlineManager;
    private final EventManager eventManager;
    private final FilteredList<Task> filteredTasks;
    private final FilteredList<Task> filteredDeadlines;
    private final FilteredList<Task> filteredEvents;
    private final Stack<ReadOnlyTaskManager> historyCommands;
    private final Stack<ReadOnlyDeadlineManager> historyCommandsDeadline;
    private final Stack<ReadOnlyEventManager> historyCommandsEvent;
    private final Stack<Predicate> historyPredicates;

    /**
     * Initializes a ModelManager with the given TaskManager
     * TaskManager and its variables should not be null
     */
    public ModelManager(TaskManager src, DeadlineManager src1, EventManager src2, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with task manager: " + src + " and user prefs " + userPrefs);

        taskManager = new TaskManager(src);
        deadlineManager = new DeadlineManager(src1);
        eventManager = new EventManager(src2);
        filteredTasks = new FilteredList<>(taskManager.getTasks());
        filteredDeadlines = new FilteredList<>(deadlineManager.getTasks());
        filteredEvents = new FilteredList<>(eventManager.getTasks());
        historyCommands = new Stack<ReadOnlyTaskManager>();
        historyCommandsDeadline = new Stack<ReadOnlyDeadlineManager>();
        historyCommandsEvent = new Stack<ReadOnlyEventManager>();
        historyPredicates = new Stack<Predicate>();
    }

    public ModelManager() {
        this(new TaskManager(), new DeadlineManager(), new EventManager(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskManager initialData, ReadOnlyDeadlineManager initialData1, ReadOnlyEventManager initialData2, UserPrefs userPrefs) {
        taskManager = new TaskManager(initialData);
        deadlineManager = new DeadlineManager(initialData1);
        eventManager = new EventManager(initialData2);
        filteredTasks = new FilteredList<>(taskManager.getTasks());
        filteredDeadlines = new FilteredList<>(deadlineManager.getTasks());
        filteredEvents = new FilteredList<>(eventManager.getTasks());
        historyCommands = new Stack<ReadOnlyTaskManager>();
        historyCommandsDeadline = new Stack<ReadOnlyDeadlineManager>();
        historyCommandsEvent = new Stack<ReadOnlyEventManager>();
        historyPredicates = new Stack<Predicate>();
    }
    
    @Override
    public void resetData(ReadOnlyTaskManager newData) {
        taskManager.resetData(newData);
        indicateTaskManagerChanged();
    }
    
    @Override
    public void resetData(ReadOnlyDeadlineManager newData) {
        deadlineManager.resetData(newData);
        indicateDeadlineManagerChanged();
    }
    
    @Override
    public void resetData(ReadOnlyEventManager newData) {
        eventManager.resetData(newData);
        indicateEventManagerChanged();
    }

    @Override
    public ReadOnlyTaskManager getTaskManager() {
        return taskManager;
    }
    
    @Override
    public ReadOnlyDeadlineManager getDeadlineManager() {
        return deadlineManager;
    }
    
    @Override
    public ReadOnlyEventManager getEventManager() {
        return eventManager;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskManagerChanged() {
        raise(new TaskManagerChangedEvent(taskManager));
    }
    
    /** Raises an event to indicate the model has changed */
    private void indicateDeadlineManagerChanged() {
        raise(new DeadlineManagerChangedEvent(deadlineManager));
    }
    
    /** Raises an event to indicate the model has changed */
    private void indicateEventManagerChanged() {
        raise(new EventManagerChangedEvent(eventManager));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        switch (target.getNumArgs()) {
            case 1:
                taskManager.removeTask(target);
                indicateTaskManagerChanged();
                
            case 3:
                deadlineManager.removeTask(target);
                indicateDeadlineManagerChanged();
                
            case 5:
                eventManager.removeTask(target);
                indicateEventManagerChanged();
        }
        
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        switch (task.getNumArgs()) {
        case 1:
            taskManager.addTask(task);
            updateFilteredListToShowAll();
            indicateTaskManagerChanged();
        
        case 3:
            deadlineManager.addTask(task);
            updateFilteredDeadlineListToShowAll();
            indicateDeadlineManagerChanged();
          
        case 5:
            eventManager.addTask(task);
            updateFilteredEventListToShowAll();
            indicateEventManagerChanged();
        }
    }
    
    public synchronized void undo() {
        resetData(historyCommands.pop());
        resetData(historyCommandsDeadline.pop());
        resetData(historyCommandsEvent.pop());
        updateFilteredEventList(historyPredicates.pop());
        updateFilteredDeadlineList(historyPredicates.pop());
        updateFilteredTaskList(historyPredicates.pop());
    }
    
    public synchronized void saveState() {
        historyCommands.push(new TaskManager(taskManager));
        historyCommandsDeadline.push(new DeadlineManager(deadlineManager));
        historyCommandsEvent.push(new EventManager(eventManager));
        historyPredicates.push(filteredTasks.getPredicate());
        historyPredicates.push(filteredDeadlines.getPredicate());
        historyPredicates.push(filteredEvents.getPredicate());
    }
    
    public synchronized void removeUnchangedState() {
        historyCommands.pop();
        historyCommandsDeadline.pop();
        historyCommandsEvent.pop();
        for (int i=0; i<3; i++) {
            historyPredicates.pop();
        }
    }
    
    @Override
    public synchronized void doneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException, DuplicateMarkAsDoneException{
        switch (target.getNumArgs()) {
        case 1:
            taskManager.doneTask(target);
            updateFilteredListToShowAll();
            indicateTaskManagerChanged();
        
        case 3:
            deadlineManager.doneTask(target);
            updateFilteredDeadlineListToShowAll();
            indicateDeadlineManagerChanged();
          
        case 5:
            eventManager.doneTask(target);
            updateFilteredEventListToShowAll();
            indicateEventManagerChanged();
        }
    }
   	@Override
    public synchronized void editTask(ReadOnlyTask target, Task task, int index) throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException {
        switch (target.getNumArgs()) {
        case 1:
            taskManager.removeTask(target);
            indicateTaskManagerChanged();
            taskManager.addTask(task, index);
            updateFilteredListToShowAll();
            indicateTaskManagerChanged();
        
        case 3:
            deadlineManager.removeTask(target);
            indicateDeadlineManagerChanged();
            deadlineManager.addTask(task, index);
            updateFilteredDeadlineListToShowAll();
            indicateDeadlineManagerChanged();
          
        case 5:
            eventManager.removeTask(target);
            indicateEventManagerChanged();
            eventManager.addTask(task, index);
            updateFilteredEventListToShowAll();
            indicateEventManagerChanged();
        }
    }

    //=========== Filtered Task List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }
    
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredDeadlineList() {
        return new UnmodifiableObservableList<>(filteredDeadlines);
    }
    
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredEventList() {
        return new UnmodifiableObservableList<>(filteredEvents);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }
    
    @Override
    public void updateFilteredDeadlineListToShowAll() {
        filteredDeadlines.setPredicate(null);
    }
    
    @Override
    public void updateFilteredEventListToShowAll() {
        filteredEvents.setPredicate(null);
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }
    
    @Override
    public void updateFilteredDeadlineList(Set<String> keywords){
        updateFilteredDeadlineList(new PredicateExpression(new NameQualifier(keywords)));
    }
    
    @Override
    public void updateFilteredEventList(Set<String> keywords){
        updateFilteredEventList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }
    
    private void updateFilteredDeadlineList(Expression expression) {
        filteredDeadlines.setPredicate(expression::satisfies);
    }
    
    private void updateFilteredEventList(Expression expression) {
        filteredEvents.setPredicate(expression::satisfies);
    }
    
    private void updateFilteredTaskList(Predicate previousPredicate) {
        filteredTasks.setPredicate(previousPredicate);
    }
    
    private void updateFilteredDeadlineList(Predicate previousPredicate) {
        filteredDeadlines.setPredicate(previousPredicate);
    }
    
    private void updateFilteredEventList(Predicate previousPredicate) {
        filteredEvents.setPredicate(previousPredicate);
    }
    
    

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask person);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask person) {
            return qualifier.run(person);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask person);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask person) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(person.getName().fullName, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
