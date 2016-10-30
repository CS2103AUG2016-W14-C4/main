package guitests;

import org.junit.Test;

import seedu.taskitty.commons.core.Messages;
import seedu.taskitty.testutil.TestTask;

import static org.junit.Assert.assertTrue;

public class FindCommandTest extends TaskManagerGuiTest {

    //@@author A0139930B
    @Test
    public void find_nonEmptyList() {
        //find no results
        assertFindResult("find task", new TestTask[0], new TestTask[0], new TestTask[0]);
        
        //find by tag
        TestTask[] expectedTagTodoHits = { td.read };
        TestTask[] expectedTagEventHits = { td.dinner };
        assertFindResult("find #important", expectedTagTodoHits, new TestTask[0], expectedTagEventHits);
        
        //find by keyword
        TestTask[] expectedEventHits = { td.shop, td.dinner };
        assertFindResult("find xmas", new TestTask[0], new TestTask[0], expectedEventHits);

        //find after deleting one result
        commandBox.runCommand("delete e1");
        TestTask[] expectedEventHitsAfterDelete = { td.dinner };
        assertFindResult("find xmas", new TestTask[0], new TestTask[0], expectedEventHitsAfterDelete);
    }

    //@@author
    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("find todo", new TestTask[0], new TestTask[0], new TestTask[0]); //no results
    }

    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand("findevent");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertFindResult(String command, TestTask[] expectedTodoHits,
            TestTask[] expectedDeadlineHits, TestTask[] expectedEventHits) {
        commandBox.runCommand(command);
        assertTodoListSize(expectedTodoHits.length);
        assertDeadlineListSize(expectedDeadlineHits.length);
        assertEventListSize(expectedEventHits.length);
        assertResultMessage(expectedTodoHits.length + expectedDeadlineHits.length + expectedEventHits.length + " tasks listed!");
        
        assertTrue(taskListPanel.isTodoListMatching(expectedTodoHits));
        assertTrue(taskListPanel.isDeadlineListMatching(expectedDeadlineHits));
        assertTrue(taskListPanel.isEventListMatching(expectedEventHits));
    }
}
