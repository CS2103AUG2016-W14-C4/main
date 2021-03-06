package seedu.taskitty.logic.commands;

import java.time.LocalDate;
import seedu.taskitty.model.task.TaskDate;

//@@author A0130853L
/**
 * This command has 4 types of functionalities, depending on the following keyword that is entered.
 * Type 1: view DATE/today
 * Lists all events for the specified date, deadlines up to the specified date, and all todo tasks.
 * Type 2: view done
 * Lists all tasks that have been completed.
 * Type 3: view
 * Lists all upcoming and uncompleted tasks in the task manager.
 * Type 4: view all
 * Lists all tasks in the task manager.
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_PARAMETER = COMMAND_WORD + " [date] | done | all";
    public static final String MESSAGE_USAGE = "This command shows upcoming tasks, Meow!"
            + "\nUse \"view [date]\" for dated tasks, \"view done\" for done tasks, \"view all\" for all tasks!";
    public static final String VIEW_ALL_MESSAGE_SUCCESS = "All tasks are listed, Meow!";
    private LocalDate date;

    public enum ViewType {

        done("done"), // to differentiate between 4 types of command functionalities
        date("date"), all("all"), normal("default");

        private String value;

        ViewType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }

    private ViewType viewType;

    /**
     * Constructor for view done and view date command functionalities.
     * 
     * @param parameter
     *            must not be empty, and will definitely be "done", "all", or a
     *            valid date guaranteed by the command parser.
     */
    public ViewCommand(String parameter) {
        assert parameter != null;
        switch (parameter) {
        case "done": // view done tasks
            viewType = ViewType.done;
            break;
        case "all":
            viewType = ViewType.all;
            break;
        default: // view tasks based on date
            this.date = LocalDate.parse(parameter, TaskDate.DATE_FORMATTER_STORAGE);
            viewType = ViewType.date;
            break;
        }
    }

    /**
     * Views uncompleted and upcoming tasks, events and deadlines.
     */
    public ViewCommand() {
        this.viewType = ViewType.normal;
    }

    @Override
    public CommandResult execute() {
        switch (viewType) {
        case normal: // view uncompleted and upcoming tasks
            model.updateToDefaultList();
            return new CommandResult(getMessageForTaskListShownSummary(model.getTaskList().size()));
        case done: // view done
            model.updateFilteredDoneList();
            return new CommandResult(getMessageForTaskListShownSummary(model.getTaskList().size()));
        case all: // view all
            model.updateFilteredListToShowAll();
            return new CommandResult(VIEW_ALL_MESSAGE_SUCCESS);
        default: // view date
            model.updateFilteredDateTaskList(date);
            return new CommandResult(getMessageForTaskListShownSummary(model.getTaskList().size()));
        }
    }

}
