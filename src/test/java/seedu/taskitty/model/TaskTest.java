package seedu.taskitty.model;

import seedu.taskitty.commons.exceptions.IllegalValueException;
import seedu.taskitty.model.tag.UniqueTagList;
import seedu.taskitty.model.task.Name;
import seedu.taskitty.model.task.Task;
import seedu.taskitty.model.task.TaskDate;
import seedu.taskitty.model.task.TaskTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TaskTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void invalidDateFormat_exceptionThrown() throws Exception {
        thrown.expect(IllegalValueException.class);
        TaskDate date = new TaskDate("25-12-2016");
    }
    
    @Test
    public void invalidDate_exceptionThrown() throws Exception {
        thrown.expect(IllegalValueException.class);
        TaskDate date = new TaskDate("29/2/2015");
    }
    
    @Test
    public void invalidTimeFormat_exceptionThrown() throws Exception {
        thrown.expect(IllegalValueException.class);
        TaskTime date = new TaskTime("3pm");
    }
    
    @Test
    public void invalidTime_exceptionThrown() throws Exception {
        thrown.expect(IllegalValueException.class);
        TaskDate date = new TaskDate("25:00");
    }
}