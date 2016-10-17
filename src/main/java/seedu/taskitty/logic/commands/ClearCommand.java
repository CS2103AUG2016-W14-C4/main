package seedu.taskitty.logic.commands;

import seedu.taskitty.model.TaskManager;
import seedu.taskitty.model.DeadlineManager;
import seedu.taskitty.model.EventManager;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Task manager has been cleared!";

    public ClearCommand() {}


    @Override
    public CommandResult execute() {
        assert model != null;
        model.resetData(TaskManager.getEmptyTaskManager());
        model.resetData(DeadlineManager.getEmptyDeadlineManager());
        model.resetData(EventManager.getEmptyEventManager());
        return new CommandResult(MESSAGE_SUCCESS);
    }


    @Override
    public void saveStateIfNeeded() {
        model.saveState();
    }
}
