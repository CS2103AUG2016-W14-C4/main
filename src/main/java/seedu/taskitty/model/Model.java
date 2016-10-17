package seedu.taskitty.model;

import seedu.taskitty.commons.core.UnmodifiableObservableList;
import seedu.taskitty.model.task.ReadOnlyTask;
import seedu.taskitty.model.task.Task;
import seedu.taskitty.model.task.UniqueTaskList;
import seedu.taskitty.model.task.UniqueTaskList.DuplicateMarkAsDoneException;

import java.util.Set;

import javafx.collections.ObservableList;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskManager newData);

    /** Returns the TaskManager */
    ReadOnlyTaskManager getTaskManager();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
    
    /** Edits the given task. */
    void editTask(ReadOnlyTask target, String newName) throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException;

    /** Marks the given task as done. 
     * @throws DuplicateMarkAsDoneException */
    void doneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException, DuplicateMarkAsDoneException;
    
    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;
    
    /** Undoes the previous command if there is any */
    void undo();
    
    /** Saves the current state of the TaskManager andfilteredTasks to allow for undoing */
    void saveState();
    
    /** Removes the current state saved when an invalid command is given */
    void removeUnchangedState();
    
	/** Returns the filtered todo task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    public ObservableList<ReadOnlyTask> getFilteredTodoTaskList();

    /** Returns the filtered todo task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
	public ObservableList<ReadOnlyTask> getFilteredDeadlineTaskList();

	/** Returns the filtered todo task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
	public ObservableList<ReadOnlyTask> getFilteredEventTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    
    /** Updates the filter of the filtered todo task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

    /** Updates the filter of the filtered task list to filter by the given keywords*/
	UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

}
