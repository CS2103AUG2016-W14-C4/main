package seedu.taskitty.commons.events.model;

import seedu.taskitty.commons.events.BaseEvent;
import seedu.taskitty.model.ReadOnlyDeadlineManager;

/** Indicates the TaskManager in the model has changed*/
public class DeadlineManagerChangedEvent extends BaseEvent {

    public final ReadOnlyDeadlineManager data;

    public DeadlineManagerChangedEvent(ReadOnlyDeadlineManager data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}

