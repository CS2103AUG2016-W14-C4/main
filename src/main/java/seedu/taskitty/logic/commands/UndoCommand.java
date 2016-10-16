package seedu.taskitty.logic.commands;

import java.util.EmptyStackException;

/**
 * Undoes previous command given
 * @author tan
 *
 */
public class UndoCommand extends Command {
    
    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undos the previous action\n"
            + "Example: " + COMMAND_WORD;

    private static final String MESSAGE_UNDO_TASK_SUCCESS = "Undoed previous action";
    
    private static final String MESSAGE_NO_PREVIOUS_COMMANDS = "There is no more previous command in this session.";
    
    public UndoCommand() {}
    
    @Override
    public CommandResult execute() {
        try {
            model.undo();
            return new CommandResult(MESSAGE_UNDO_TASK_SUCCESS);
        } catch (EmptyStackException e) {
            return new CommandResult(MESSAGE_NO_PREVIOUS_COMMANDS);
        }       
    }

    @Override
    public void saveStateIfNeeded() {}

}
