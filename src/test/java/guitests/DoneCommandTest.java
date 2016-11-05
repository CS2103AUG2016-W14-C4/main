package guitests;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import seedu.taskitty.commons.core.Messages;
import seedu.taskitty.logic.commands.Command;
import seedu.taskitty.logic.commands.DoneCommand;
import seedu.taskitty.testutil.TestTask;
import seedu.taskitty.testutil.TestTaskList;

//@@author A0130853L
import static org.junit.Assert.assertTrue;
import static seedu.taskitty.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class DoneCommandTest extends TaskManagerGuiTest {

    @Test
    public void done() {

        //mark as done using todo/default
        TestTaskList currentList = new TestTaskList(td.getTypicalTasks());
        
        int targetIndex = currentList.size();
        assertMarkAsDoneSuccess(targetIndex, currentList);

        //mark as done in deadline and event at once
        targetIndex = currentList.size('d');
        int targetIndex2 = currentList.size('e');
        assertMarkAsDoneSuccess( new int[] { targetIndex, targetIndex2 }, new char[] { 'd', 'e'} , currentList);
        
        //invalid index
        commandBox.runCommand("done t" + (currentList.size('t') + 1));
        assertResultMessage(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX + ": t2");
        
        //duplicate index provided
        commandBox.runCommand("done e1 e1");
        assertResultMessage(Messages.MESSAGE_DUPLICATE_INDEXES_PROVIDED + ": e1");
        
        //duplicate mark as done
        commandBox.runCommand("done d1");
        assertResultMessage(DoneCommand.MESSAGE_DUPLICATE_MARK_AS_DONE_ERROR_HEADER + "d1");
        
        //invalid command
        commandBox.runCommand("donee e" + (currentList.size('e')));
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
        
    }
    
    @Test 
    public void doneContinous() {
        
        TestTaskList currentList = new TestTaskList(td.getTypicalTasks());
        
        assertMarkAsDoneSuccess(1, 2, 'e', currentList);       
        
      //invalid second index larger than first
        commandBox.runCommand("done t3-1");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, 
                Command.MESSAGE_FORMAT + DoneCommand.MESSAGE_PARAMETER));
        
        //invalid second index
        commandBox.runCommand("done e1-t");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                Command.MESSAGE_FORMAT + DoneCommand.MESSAGE_PARAMETER));       
    }
    
    /**
     * Runs the done command to mark the task at specified index as done and confirms the result is correct 
     * with todo as the default category.
     * @param targetIndexOneIndexed e.g. to mark the first task in the list as done, 1 should be given as the target index.
     * @param currentList A copy of the current list of tasks (before being marked as done).     
     */
    private void assertMarkAsDoneSuccess(int targetIndexOneIndexed, final TestTaskList currentList) {
        assertMarkAsDoneSuccess(targetIndexOneIndexed, 't', currentList);
    }
    
    /**
     * Runs the done command to mark the task at specified index as done and confirms the result is correct 
     * @param targetIndexOneIndexed e.g. to mark the first task in the list as done, 1 should be given as the target index.
     * @param category the category in which to mark as done from.
     * @param currentList A copy of the current list of tasks (before being marked as done).     
     */
    private void assertMarkAsDoneSuccess(int targetIndexOneIndexed, char category, final TestTaskList currentList) {
        commandBox.runViewAllCommand();
        
        StringBuilder resultMessage = new StringBuilder(String.format(DoneCommand.MESSAGE_MARK_TASK_AS_DONE_SUCCESS_HEADER, 1));
        TestTask taskToMark = currentList.getTaskFromList(targetIndexOneIndexed - 1, category); //-1 because array uses zero indexing
        
        currentList.markTaskAsDoneInList(targetIndexOneIndexed - 1, category, taskToMark);
        commandBox.runCommand("done " + category + targetIndexOneIndexed);

        //confirm the list now contains the original list + the task marked as done
        assertTrue(currentList.isListMatching(taskListPanel));
        
        assertTaskCardMarkedAsDone(taskToMark);
        
        //confirm the result message is correct
        resultMessage.append(taskToMark.getName());
        assertResultMessage(resultMessage.toString());
    }
    
    //@@author A0139052L
    /**
     * Runs the done command to mark the tasks at specified indexes as done and confirms the result is correct 
     * @param targetIndexes the indexes in order to which to mark as done from.
     * @param categories the categories in order in which to mark as done from.
     * @param currentList A copy of the current list of tasks (before being marked as done).     
     */
    private void assertMarkAsDoneSuccess(int[] targetIndexes, char[] categories, final TestTaskList currentList) {
        commandBox.runViewAllCommand();
        
        StringBuilder commandText = new StringBuilder("done ");
        StringBuilder resultMessage = new StringBuilder(String.format(DoneCommand.MESSAGE_MARK_TASK_AS_DONE_SUCCESS_HEADER, targetIndexes.length));
        TestTask[] markedTasks = new TestTask[targetIndexes.length];
        
        for (int i = 0; i < targetIndexes.length; i++) {
            
            TestTask taskToMark = currentList.getTaskFromList(targetIndexes[i] - 1, categories[i]); //-1 because array uses zero indexing            
            currentList.markTaskAsDoneInList(targetIndexes[i] - 1, categories[i], taskToMark);
                        
            commandText.append(categories[i]);
            commandText.append(targetIndexes[i] + " ");

            resultMessage.append(taskToMark.getName() + ", ");
            
            markedTasks[i] = taskToMark;
        }
        
        commandBox.runCommand(commandText.toString());
                
        //confirm the list now contains the original list + the task marked as done
        assertTrue(currentList.isListMatching(taskListPanel));
        
        for (TestTask markedTask: markedTasks) {
            assertTaskCardMarkedAsDone(markedTask);
        }       
        
        //confirm the result message is correct
        assertResultMessage(resultMessage.substring(0, resultMessage.length() - 2));
    }
    
    /**
     * Runs the done command to mark the tasks at the category given from the first index to second index
     * @param firstIndex the index to start marking from
     * @param secondIndex the final index to finish mark at
     * @param category the category to decide the list to mark from
     * @param currentList A copy of the current list of tasks (before deletion).
     */
    private void assertMarkAsDoneSuccess(int firstIndex, int secondIndex, char category, final TestTaskList currentList) {
        commandBox.runViewAllCommand();
        
        StringBuilder resultMessage = new StringBuilder(String.format
                (DoneCommand.MESSAGE_MARK_TASK_AS_DONE_SUCCESS_HEADER, secondIndex - firstIndex + 1));
        
        for (int i = firstIndex; i <= secondIndex; i++) {
            TestTask taskToMark = currentList.getTaskFromList(firstIndex - 1, category);
            currentList.markTaskAsDoneInList(firstIndex - 1, category, taskToMark);
            
            resultMessage.append(taskToMark.getName() + ", ");
        }
        
        commandBox.runCommand("done " + category + firstIndex + "-" + secondIndex);
                
        //confirm the list now contains all previous persons except the deleted person
        assertTrue(currentList.isListMatching(taskListPanel));

        //confirm the result message is correct
        assertResultMessage(resultMessage.substring(0, resultMessage.length() - 2));
    }
    
    private void assertTaskCardMarkedAsDone(TestTask markedTask) {
        // find task card of marked task
        TaskCardHandle markedCard = taskListPanel.navigateToTask(markedTask.getName().fullName, markedTask.getPeriod().getNumArgs());
        // confirm its the correct task
        assertMatching(markedTask, markedCard);
        // confirm the task is marked
        assertMarkAsDone(markedCard);
    }
}
