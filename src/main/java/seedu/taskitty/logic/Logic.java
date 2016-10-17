package seedu.taskitty.logic;

import javafx.collections.ObservableList;
import seedu.taskitty.logic.commands.CommandResult;
import seedu.taskitty.model.task.ReadOnlyTask;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     */
    CommandResult execute(String commandText);

    /** Returns the filtered list of tasks */
    ObservableList<ReadOnlyTask> getFilteredTaskList();
    /** Returns the filtered list of todo tasks */
	ObservableList<ReadOnlyTask> getFilteredTodoTaskList();
	/** Returns the filtered list of deadline tasks */
	ObservableList<ReadOnlyTask> getFilteredDeadlineTaskList();
	/** Returns the filtered list of event tasks */
	ObservableList<ReadOnlyTask> getFilteredEventTaskList();

}
