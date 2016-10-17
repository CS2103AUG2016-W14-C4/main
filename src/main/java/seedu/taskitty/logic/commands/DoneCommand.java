package seedu.taskitty.logic.commands;

import javafx.collections.ObservableList;
import seedu.taskitty.commons.core.Messages;
import seedu.taskitty.commons.core.UnmodifiableObservableList;
import seedu.taskitty.model.task.ReadOnlyTask;
import seedu.taskitty.model.task.UniqueTaskList.DuplicateMarkAsDoneException;
import seedu.taskitty.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Marks a task as done identified using it's last displayed index from the task Manager under the specified category.
 */
public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";
    
    public static final String CATEGORY_CHARS = "t|d|e";
    
    public static final int DEFAULT_INDEX = 0;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the task identified by the category character and index number used in the last task listing as done.\n"
            + "Parameters: CATEGORY(default to 't' if not given or incorrect) INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " " + CATEGORY_CHARS + " 1";

    public static final String MESSAGE_MARK_TASK_AS_DONE_SUCCESS = "Task done: %1$s";
    public static final String MESSAGE_DUPLICATE_MARK_AS_DONE_ERROR = "The task \"%1$s\" has already been marked as done.";

    public final int categoryIndex;
    
    public final int targetIndex;
    
    public DoneCommand(int targetIndex) {
        // default to Todo category if no given category
        this(targetIndex, DEFAULT_INDEX);
    }
    
    public DoneCommand(int targetIndex, int categoryIndex) {
        this.targetIndex = targetIndex;
        this.categoryIndex = categoryIndex;
    }


    @Override
    public CommandResult execute() {
    	assert categoryIndex >= 0 && categoryIndex < 3;
        ObservableList<ReadOnlyTask> lastShownList;
        switch (categoryIndex) {
        case 0:
        	lastShownList = model.getFilteredTodoTaskList();
        	break;
        case 1:
        	lastShownList = model.getFilteredDeadlineTaskList();
        	break;
        case 2:
        	lastShownList = model.getFilteredEventTaskList();
        default:
        	lastShownList = model.getFilteredTodoTaskList();
        	break;
        }
        if (lastShownList.size() < targetIndex) {
        	model.removeUnchangedState();
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX); 
        }
        

        ReadOnlyTask taskToBeMarkedDone = lastShownList.get(targetIndex - 1);

        try {
            model.doneTask(taskToBeMarkedDone);
        } catch (DuplicateMarkAsDoneException dmade) {
        	model.removeUnchangedState();
        	return new CommandResult(String.format(MESSAGE_DUPLICATE_MARK_AS_DONE_ERROR, taskToBeMarkedDone));
        } catch (TaskNotFoundException pnfe) {
            model.removeUnchangedState();
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_MARK_TASK_AS_DONE_SUCCESS, taskToBeMarkedDone));
    }


    @Override
    public void saveStateIfNeeded() {
        model.saveState();
    }

}
