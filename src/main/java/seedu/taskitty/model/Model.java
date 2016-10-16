package seedu.taskitty.model;

import seedu.taskitty.commons.core.UnmodifiableObservableList;
import seedu.taskitty.model.task.ReadOnlyTask;
import seedu.taskitty.model.task.Task;
import seedu.taskitty.model.task.UniqueTaskList;

import java.util.Set;

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
    void editTask(ReadOnlyTask target, Task task, int index) throws UniqueTaskList.TaskNotFoundException, UniqueTaskList.DuplicateTaskException;

    /** Marks the given task as done. */
    void doneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
    
    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;
    
    /** Undoes the previous command if there is any */
    void undo();
    
    /** Saves the current state of the TaskManager andfilteredTasks to allow for undoing */
    void saveState();
    
    /** Removes the current state saved when an invalid command is given */
    void removeUnchangedState();
    
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

}
