package seedu.taskitty.model.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.taskitty.commons.exceptions.DuplicateDataException;
import seedu.taskitty.commons.util.CollectionUtil;
import seedu.taskitty.commons.util.DateTimeUtil;
import seedu.taskitty.ui.ResultDisplay;
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
    
	//@@author A0130853L
    /**
     * Signals that the done operation targeting a specified task in the list is a duplicate operation if the task has already been previously
     * marked as done.
     */
    public static class DuplicateMarkAsDoneException extends Exception {}

	//@@author
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

    //@@author A0139052L
    /**
     * Adds a task into the list in a sorted fashion.
     *
     * @throws DuplicateTaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void addSorted(Task toAdd) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        
        for (int i = 0; i < internalList.size(); i++) {
            if (toAdd.compareTo(internalList.get(i)) < 0) {
                internalList.add(i, toAdd);
                return;
            }
        }
        internalList.add(toAdd);
    }
    
    /**
     * Adds tasks to the list in an unsorted fashion. Used in initialization of TaskManager
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
    
    /** 
     * Unmarks the given task as done from the list. Used only in redo function 
     */
    public void unmark(ReadOnlyTask toUnmark) {
        assert toUnmark != null && toUnmark.getIsDone();
        final boolean taskFoundAndUnmarkedAsDone = internalList.remove(toUnmark);
        assert taskFoundAndUnmarkedAsDone;
        
        Task editableToUnmark = (Task) toUnmark;
        editableToUnmark.unmarkAsDone();
        
        try {
            addSorted(editableToUnmark);
        } catch (DuplicateTaskException e) {
            assert false: "Should not have duplicate task";
        }
    }
    
    //@@author A0130853L
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
    	try {
            addSorted(editableToMark);
        } catch (DuplicateTaskException e) {
            assert false: "Should not have duplicate task";
        }
    	if (!taskFoundAndMarkedAsDone) {
    		throw new TaskNotFoundException();
    	}
    }   
    
    //@@author
    /**
     * Sorts the task list according to compareTo method in Task
     */
    public void sort() {
        internalList.sort(null);
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
    //@@author A0130853L

    public ObservableList<Task> getInternalList() {
    	checkAndSetOverdue();
    	checkAndSetIsOverToday();
        return internalList;
    }
    
    /**
     * Returns the internal list, filtered to view only the specified type of Task
     * 
     * @param filter according to Task.
     */
    private void checkAndSetOverdue() {
    	boolean hasOverdue = false;    	
    	for (Task t: internalList) {
    		if (t.isDeadline() && !t.getIsDone()) {
    			if (DateTimeUtil.isOverdue(t)) {
    				t.markAsOverdue();
    				hasOverdue = true;
    			}
    		}
    	}
    	if (hasOverdue) {
    		ResultDisplay.setOverdue();
    	}
    }
    
    /**
     * When an event is over, automatically mark it as done.
     */
    private void checkAndSetIsOverToday() {
        final ArrayList<Task> eventsToSetOver = new ArrayList<Task>();
    	for (Task t: internalList) {
    	    if (t.isEvent() && DateTimeUtil.isOverdue(t)) {
    	        eventsToSetOver.add(t);
    	    }
    	}
    	for (Task overEvents: eventsToSetOver) {
    	    try {
                mark(overEvents);
            } catch (TaskNotFoundException e) {
                assert false: "Task should not be missing";
            } catch (DuplicateMarkAsDoneException e) {
                assert false: "Task should not be marked done";
            }
    	}
    }
    
    
    //@@author A0139930B
    public FilteredList<Task> getFilteredTaskList(int filter) {
        return internalList.filtered(p -> p.getPeriod().getNumArgs() == filter);
    }
    
    //@@author A0130853L
    public FilteredList<Task> getFilteredTaskList() {
        return internalList.filtered(null);
    }
    //@@author
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTaskList) other).internalList));
    }
}
