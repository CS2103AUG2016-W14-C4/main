package seedu.taskitty.commons.events.model;

import java.time.LocalDate;
import seedu.taskitty.commons.events.BaseEvent;
import seedu.taskitty.commons.util.DateTimeUtil;
import seedu.taskitty.logic.commands.ViewCommand;

//@@author A0130853L
/**
 * Indicates the viewType being filtered in the model has changed
 */
public class ViewTypeChangedEvent extends BaseEvent {

    public final ViewCommand.ViewType viewType;
    private LocalDate date;

    public ViewTypeChangedEvent(ViewCommand.ViewType viewType, LocalDate date) {
        this.viewType = viewType;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        if (date == null) {
            return "currently viewing: " + viewType;
        } else if (DateTimeUtil.isToday(date)) {
            return "currently viewing: today";
        } else {
            return "currently viewing: " + date.toString();
        }
    }
}
