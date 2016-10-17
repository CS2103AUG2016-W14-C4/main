package seedu.taskitty.model.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.taskitty.commons.exceptions.DuplicateDataException;
import seedu.taskitty.commons.util.CollectionUtil;

import java.util.*;

/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateTaskException extends DuplicateDataException {
        protected DuplicateTaskException() {
            super("Operation would result in duplicate tasks");
        }
    }

    /**
     * Signals that an operation targeting a specified task in the list would fail because
     * there is no such matching task in the list.
     */
    public static class TaskNotFoundException extends Exception {}
    
    /**
     * Signals that the done operation targeting a specified task in the list is a duplicate operation if the task has already been previously
     * marked as done.
     */
    public static class DuplicateMarkAsDoneException extends Exception {}

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();
    private final ObservableList<Task> internalDeadlineList = FXCollections.observableArrayList();
    private final ObservableList<Task> internalEventList = FXCollections.observableArrayList();

    /**
     * Constructs empty TaskList.
     */
    public UniqueTaskList() {}

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        ObservableList<Task> list = getListType(toCheck);
        return list.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(Task toAdd) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        ObservableList<Task> list = getListType(toAdd);
        list.add(toAdd);
    }
    
    /**
     * Gets the appropriate type of list based on the type of task
     * @param toCheck
     * @return
     */
    private ObservableList<Task> getListType(ReadOnlyTask toCheck) {
        assert toCheck.getNumArgs() == 1 || toCheck.getNumArgs() == 3 || toCheck.getNumArgs() == 5 ;
        switch (toCheck.getNumArgs()) {
        
        case 1:
            return internalList;
            
        case 3:
            return internalDeadlineList;
        
        case 5:
            return internalEventList;
            
        default:
            return internalList;
        }
    }

    /** Marks the given task as done from the list.
     * 
     * @throws TaskNotFoundException if no such task could be found in the list.
     * @throws DuplicateMarkAsDoneException if specified task in list had already been marked as done previously.
     */
    public void mark(ReadOnlyTask toMark) throws TaskNotFoundException, DuplicateMarkAsDoneException {
    	assert toMark != null;
    	if (toMark.getIsDone()) {
    		throw new DuplicateMarkAsDoneException();
    	}
    	
    	ObservableList<Task> list = getListType(toMark);
    	final boolean taskFoundAndMarkedAsDone = list.remove(toMark);
    	Task editableToMark = (Task) toMark;
    	editableToMark.markAsDone();
    	list.add(editableToMark);
    	if (!taskFoundAndMarkedAsDone) {
    		throw new TaskNotFoundException();
    	}
    }
    
    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(int index, Task toAdd) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        ObservableList<Task> list = getListType(toAdd);
        list.add(index, toAdd);
    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        ObservableList<Task> list = getListType(toRemove);
        final boolean taskFoundAndDeleted = list.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }

    public ObservableList<Task> getInternalList() {
  
        return internalList;
    }
    
    public ObservableList<Task> getInternalDeadlinesList() {
        
        return internalDeadlineList;
    }
    
    public ObservableList<Task> getInternalEventsList() {
        
        return internalEventList;
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
