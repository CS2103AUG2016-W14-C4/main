package seedu.taskitty.logic.commands;

import java.util.HashSet;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.taskitty.commons.core.Messages;
import seedu.taskitty.commons.core.UnmodifiableObservableList;
import seedu.taskitty.commons.exceptions.IllegalValueException;
import seedu.taskitty.model.tag.Tag;
import seedu.taskitty.model.tag.UniqueTagList;
import seedu.taskitty.model.task.Name;
import seedu.taskitty.model.task.ReadOnlyTask;
import seedu.taskitty.model.task.Task;
import seedu.taskitty.model.task.TaskDate;
import seedu.taskitty.model.task.TaskTime;
import seedu.taskitty.model.task.UniqueTaskList;
import seedu.taskitty.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Edits a task identified using it's last displayed index from the task manager.
 */

public class EditCommand extends Command{
    
    public static final String COMMAND_WORD = "edit";
    
    public static final String CATEGORY_CHARS = "t|d|e";
    
    public static final int DEFAULT_INDEX = 0;

    public static final String MESSAGE_USAGE = COMMAND_WORD 
            + ": Edits the floating task identified by the category character and index number used in the last task listing.\n "
            + "Parameters: CATEGORY (default to 't' if not given or incorrect) INDEX (must be a positive integer)\n NEW_NAME (must be alphanumeric)"
            + "Example: " + COMMAND_WORD + " " + CATEGORY_CHARS + " 1 finish CS2103T";

    public static final String MESSAGE_SUCCESS = "Task edited: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager";

    public final int categoryIndex;
    public final int targetIndex;

    private final String newName;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(String name, int targetIndex, int categoryIndex)
            throws IllegalValueException {
        this.newName = name;
    	this.targetIndex = targetIndex;
        this.categoryIndex = categoryIndex;
     
    }
    
    /**
     * @throws IllegalValueException 
     * 
     */
    public EditCommand(String name, int targetIndex) throws IllegalValueException {
        // default to Todo category if no given category
        this(name, targetIndex, DEFAULT_INDEX);
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
        
        if (!newName.matches(Name.NAME_VALIDATION_REGEX_FORMAT)) {
        	model.removeUnchangedState();
        	indicateAttemptToExecuteIncorrectCommand();
        	return new CommandResult(Name.MESSAGE_NAME_CONSTRAINTS);
        }

        ReadOnlyTask taskToEdit = lastShownList.get(targetIndex - 1);

        try {
            model.editTask(taskToEdit, newName);
        } catch (UniqueTaskList.DuplicateTaskException e) {
            model.removeUnchangedState();
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        } catch (TaskNotFoundException pnfe) {
            model.removeUnchangedState();
            assert false : "The target task cannot be missing";
        }
        
        return new CommandResult(String.format(MESSAGE_SUCCESS, taskToEdit));
    }

    @Override
    public void saveStateIfNeeded() {
        model.saveState();
    }
}

