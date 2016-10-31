package seedu.taskitty.logic.commands;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import seedu.taskitty.commons.core.Messages;
import seedu.taskitty.commons.core.UnmodifiableObservableList;
import seedu.taskitty.commons.util.AppUtil;
import seedu.taskitty.model.task.ReadOnlyTask;
import seedu.taskitty.model.task.Task;
import seedu.taskitty.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0139052L
/**
 * Deletes a task identified using it's last displayed index from the task manager.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_PARAMETER = COMMAND_WORD + " [index] [more indexes]...";
    public static final String MESSAGE_USAGE = "This command deletes tasks from TasKitty, Meow!"
            + "\n[index] is the index eg. t1, d1, e1.";

    public static final String MESSAGE_DELETE_TASK_SUCCESS_HEADER = " %1$s Tasks Deleted: ";
    
    private boolean hasInvalidIndex;
    
    private boolean hasDuplicateIndexesProvided;
    
    private final List<Pair<Integer, Integer>> listOfIndexes;
    
    private final String commandText;
    
    public DeleteCommand(List<Pair<Integer, Integer>> listOfIndexes, String commandText) {
        assert listOfIndexes != null;
        this.listOfIndexes = listOfIndexes;
        this.hasInvalidIndex = false;
        this.hasDuplicateIndexesProvided = false;
        this.commandText = commandText;
    }

    @Override
    public CommandResult execute() {
        
        int categoryIndex;
        int targetIndex;
        ArrayList<ReadOnlyTask> listOfTaskToDelete = new ArrayList<ReadOnlyTask>();
        StringBuilder invalidIndexMessageBuilder = new StringBuilder(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX + ": ");
        StringBuilder resultMessageBuilder = new StringBuilder(String.format(MESSAGE_DELETE_TASK_SUCCESS_HEADER, listOfIndexes.size()));
        StringBuilder duplicateIndexesProvidedMessageBuilder = new StringBuilder(Messages.MESSAGE_DUPLICATE_INDEXES_PROVIDED + ": ");
        
        for (Pair<Integer, Integer> indexPair: listOfIndexes) {
            categoryIndex = indexPair.getKey();
            targetIndex = indexPair.getValue();
            assert categoryIndex >= 0 && categoryIndex < 3;
            
            String currentTaskIndex = Task.CATEGORIES[categoryIndex] + targetIndex + " ";
            
            UnmodifiableObservableList<ReadOnlyTask> lastShownList = AppUtil.getCorrectListBasedOnCategoryIndex(model, categoryIndex); 
            
            if (lastShownList.size() < targetIndex) {
                hasInvalidIndex = true;
                invalidIndexMessageBuilder.append(currentTaskIndex);
                continue;
            }
            
            ReadOnlyTask taskToDelete = lastShownList.get(targetIndex - 1);
            
            if (!listOfTaskToDelete.contains(taskToDelete)) {
                listOfTaskToDelete.add(taskToDelete);
                resultMessageBuilder.append(taskToDelete.getName() + ", ");
            } else {
                hasDuplicateIndexesProvided = true;
                duplicateIndexesProvidedMessageBuilder.append(currentTaskIndex);
            }                       
        }
        
        if (hasInvalidIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            String invalidIndexMessage = invalidIndexMessageBuilder.toString().trim();
            return new CommandResult(invalidIndexMessage);
        }
        
        if (hasDuplicateIndexesProvided) {
            indicateAttemptToExecuteIncorrectCommand();
            String duplicateIndexesProvidedMessage = duplicateIndexesProvidedMessageBuilder.toString().trim();
            return new CommandResult(duplicateIndexesProvidedMessage);
        } 
        
        try {
             model.deleteTasks(listOfTaskToDelete);
             model.storeCommandInfo(COMMAND_WORD, commandText, listOfTaskToDelete);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }
        
        resultMessageBuilder.delete(resultMessageBuilder.length() - 2, resultMessageBuilder.length());// remove the extra separator at the end
        String resultMessage = resultMessageBuilder.toString();
        return new CommandResult(resultMessage);
    }

}
