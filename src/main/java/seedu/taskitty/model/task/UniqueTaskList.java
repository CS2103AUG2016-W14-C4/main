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
    
    //@@author
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
    
    //@@author A0139052L
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
            assert false : "Should not have duplicate task";
        }
    }
    
    //@@author A0130853L
    /** Marks the given task as done from the list.
     * 
     */
    public void mark(ReadOnlyTask toMark) {
    	assert toMark != null && !toMark.getIsDone();

    	final boolean taskFoundAndMarkedAsDone = internalList.remove(toMark);
    	assert taskFoundAndMarkedAsDone;
    	
    	Task editableToMark = (Task) toMark;
    	editableToMark.markAsDone();
    	
    	try {
            addSorted(editableToMark);
        } catch (DuplicateTaskException e) {
            assert false : "Should not have duplicate task";
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
     */
    public void remove(ReadOnlyTask toRemove){
        assert toRemove != null;
        
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        assert taskFoundAndDeleted;
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
            if (t.isDeadline() && !t.getIsDone() && DateTimeUtil.isOverdue(t)) {
                t.markAsOverdue();
                hasOverdue = true;
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
    	    if (t.isEvent() && DateTimeUtil.isOverdue(t) && !t.getIsDone()) {
    	        eventsToSetOver.add(t);
    	    }
    	}
    	for (Task overEvents: eventsToSetOver) {
    	    mark(overEvents);     
    	}
    }
    
    
    //@@author A0139930B
    /**
     * Filters the list based on the number of arguments the TaskPeriod has
     *   TODOS      : numArgs = Task.TASK_COMPONENT_COUNT
     *   DEADLINES  : numArgs = Task.DEADLINE_COMPONENT_COUNT
     *   EVENTS     : numArgs = Task.EVENT_COMPONENT_COUNT
     */
    public FilteredList<Task> getFilteredTaskList(int filter) {
        return internalList.filtered(p -> p.isNumArgsMatch(filter));
    }
    
    //@@author
    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
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

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
