package seedu.taskitty.logic.commands;

import seedu.taskitty.model.TaskManager;

/**
 * Clears the taskManager.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Task manager has been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(TaskManager.getEmptyTaskManager());
        return new CommandResult(MESSAGE_SUCCESS);
    }


    @Override
    public void saveStateIfNeeded() {
        model.saveState();
    }
}
