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

    /**
     * Constructs empty TaskList.
     */
    public UniqueTaskList() {}

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
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
        internalList.add(toAdd);
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
    	final boolean taskFoundAndMarkedAsDone = internalList.remove(toMark);
    	Task editableToMark = (Task) toMark;
    	editableToMark.markAsDone();
    	internalList.add(editableToMark);
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
        internalList.add(index, toAdd);
    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }
    
    /**
     * Removes the equivalent task from the list, specifically for the edit command.
     * @return the index of the removed task.
     */
    public int removeEdit(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        int index = internalList.indexOf(toRemove);
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return index;
    }
    
    public ObservableList<Task> getInternalList() {
  
        return internalList;
    }
    
    /**
     * Returns the internal filtered list of todo tasks.
     */
    public ObservableList<Task> getInternalTodoList() {
        ObservableList<Task> todoList = FXCollections.observableArrayList();
    	for (Task t: internalList) {
    		if (t.getTaskType().equals(TaskType.TODO)) {
    			todoList.add(t);
    		}
    	}
    	return todoList;
    }
    
    /**
     * Returns the internal filtered list of deadline tasks.
     */
    public ObservableList<Task> getInternalDeadlineList() {
        ObservableList<Task> deadlineList = FXCollections.observableArrayList();
    	for (Task t: internalList) {
    		if (t.getTaskType().equals(TaskType.DEADLINE)) {
    			deadlineList.add(t);
    		}
    	}
    	return deadlineList;
    }
    
    /**
     * Returns the internal filtered list of event tasks.
     */
    public ObservableList<Task> getInternalEventList() {
        ObservableList<Task> eventList = FXCollections.observableArrayList();
    	for (Task t: internalList) {
    		if (t.getTaskType().equals(TaskType.EVENT)) {
    			eventList.add(t);
    		}
    	}
    	return eventList;
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
