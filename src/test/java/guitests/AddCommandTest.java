package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;

import seedu.taskitty.commons.core.Messages;
import seedu.taskitty.logic.commands.AddCommand;
import seedu.taskitty.logic.commands.Command;
import seedu.taskitty.testutil.TestTask;
import seedu.taskitty.testutil.TestTaskList;

import static org.junit.Assert.assertTrue;
import static seedu.taskitty.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class AddCommandTest extends TaskManagerGuiTest {

    @Test
    public void add() {
        //add one task
        TestTaskList currentList = new TestTaskList(td.getTypicalTasks());
        TestTask taskToAdd = td.addTodo;
        assertAddSuccess(taskToAdd, td.addTodo.getPeriod().getNumArgs(), currentList);

        //add another task
        taskToAdd = td.addDeadline;
        assertAddSuccess(taskToAdd, td.addDeadline.getPeriod().getNumArgs(), currentList);

        //add duplicate task
        commandBox.runCommand(td.addDeadline.getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
        assertTrue(currentList.isListMatching(taskListPanel));

        //add to empty list
        commandBox.runCommand("clear");
        currentList.clear();
        assertAddSuccess(td.addDeadline, td.addDeadline.getPeriod().getNumArgs(), currentList);

        //invalid command
        commandBox.runCommand("adds party");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
        
      //natty read too many parameters
        commandBox.runCommand("add party 6pm oct 3 or 4 to oct 5 or 6 7pm");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                Command.MESSAGE_FORMAT + AddCommand.MESSAGE_PARAMETER));
    }

    private void assertAddSuccess(TestTask taskToAdd, int type, TestTaskList currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getName().fullName, type);
        assertMatching(taskToAdd, addedCard);

        //confirm the list now contains all previous tasks plus the new task
        currentList.addTaskToList(taskToAdd);
        assertTrue(currentList.isListMatching(taskListPanel));
    }

}
