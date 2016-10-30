package seedu.taskitty.logic.commands;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import seedu.taskitty.commons.core.Messages;
import seedu.taskitty.commons.core.UnmodifiableObservableList;
import seedu.taskitty.commons.util.AppUtil;
import seedu.taskitty.model.task.ReadOnlyTask;
import seedu.taskitty.model.task.Task;
import seedu.taskitty.model.task.UniqueTaskList.DuplicateMarkAsDoneException;
import seedu.taskitty.model.task.UniqueTaskList.TaskNotFoundException;

//@@author A0130853L
/**
 * Marks a task as done identified using it's last displayed index from the task manager.
 */
public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_PARAMETER = COMMAND_WORD + " [index] [more indexes]...";
    public static final String MESSAGE_USAGE = "This command marks tasks in TasKitty as done, Meow!"
            + "\n[index] is the index eg. t1, d1, e1.";

    public static final String MESSAGE_MARK_TASK_AS_DONE_SUCCESS_HEADER = "%1$s" + " Tasks marked as done: ";
    public static final String MESSAGE_DUPLICATE_MARK_AS_DONE_ERROR_HEADER = "These tasks has already been marked as done: ";
    
    private boolean hasInvalidIndex;
    
    private boolean hasDuplicateMarkAsDoneTask;
    
    private boolean hasDuplicateIndexesProvided;
    
    private final List<Pair<Integer, Integer>> listOfIndexes;
    
    private final String commandText;
    
    public DoneCommand(List<Pair<Integer, Integer>> listOfIndexes, String commandText) {
        assert listOfIndexes != null;
        this.listOfIndexes = listOfIndexes;
        this.hasInvalidIndex = false;
        this.hasDuplicateMarkAsDoneTask = false;
        this.hasDuplicateIndexesProvided = false;
        this.commandText = commandText;
    }

    @Override
    public CommandResult execute() {
        
        int categoryIndex;
        int targetIndex;
        ArrayList<ReadOnlyTask> listOfTaskToMarkDone = new ArrayList<ReadOnlyTask>();
        StringBuilder invalidIndexMessageBuilder = new StringBuilder(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX + ": ");
        StringBuilder duplicateMarkAsDoneMessageBuilder = new StringBuilder(MESSAGE_DUPLICATE_MARK_AS_DONE_ERROR_HEADER);
        StringBuilder duplicateIndexesProvidedMessageBuilder = new StringBuilder(Messages.MESSAGE_DUPLICATE_INDEXES_PROVIDED + ": ");
        StringBuilder resultMessageBuilder = new StringBuilder(String.format(MESSAGE_MARK_TASK_AS_DONE_SUCCESS_HEADER, listOfIndexes.size()));
        
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
            
            ReadOnlyTask taskToBeMarkedDone = lastShownList.get(targetIndex - 1);
            
            if (taskToBeMarkedDone.getIsDone()) {
                hasDuplicateMarkAsDoneTask = true;
                duplicateMarkAsDoneMessageBuilder.append(currentTaskIndex);
                continue;
            }
            
            if (!listOfTaskToMarkDone.contains(taskToBeMarkedDone)) {
                listOfTaskToMarkDone.add(taskToBeMarkedDone);
                resultMessageBuilder.append(currentTaskIndex);
            } else {
                hasDuplicateIndexesProvided = true;
                duplicateIndexesProvidedMessageBuilder.append(currentTaskIndex);
            }
        }
        
        if (hasInvalidIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            String invalidIndexMessage = invalidIndexMessageBuilder.toString();
            return new CommandResult(invalidIndexMessage);
        }
        
        if (hasDuplicateIndexesProvided) {
            indicateAttemptToExecuteIncorrectCommand();
            String duplicateIndexesProvidedMessage = duplicateIndexesProvidedMessageBuilder.toString();
            return new CommandResult(duplicateIndexesProvidedMessage);
        }
        
        if (hasDuplicateMarkAsDoneTask) {
            String duplicateMarkAsDoneMessage = duplicateMarkAsDoneMessageBuilder.toString();
            return new CommandResult(duplicateMarkAsDoneMessage);
        }
                        
        try {
             model.markTasksAsDone(listOfTaskToMarkDone);
             model.storeDoneCommandInfo(listOfTaskToMarkDone, commandText);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        } catch (DuplicateMarkAsDoneException e) {
            assert false: "The target task should not be marked done";
        }
        
        String resultMessage = resultMessageBuilder.toString();
        return new CommandResult(resultMessage);
    }
}
