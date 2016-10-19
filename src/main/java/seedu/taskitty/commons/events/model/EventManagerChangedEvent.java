package seedu.taskitty.commons.events.model;

import seedu.taskitty.commons.events.BaseEvent;
import seedu.taskitty.model.ReadOnlyEventManager;

/** Indicates the TaskManager in the model has changed*/
public class EventManagerChangedEvent extends BaseEvent {

    public final ReadOnlyEventManager data;

    public EventManagerChangedEvent(ReadOnlyEventManager data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}


