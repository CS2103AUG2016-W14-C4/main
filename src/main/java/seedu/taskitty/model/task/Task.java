package seedu.taskitty.model.task;

import seedu.taskitty.commons.exceptions.IllegalValueException;
import seedu.taskitty.commons.util.CollectionUtil;
import seedu.taskitty.model.tag.UniqueTagList;


import java.util.Objects;

/**
 * Represents a Task in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {
    
    public static final int TASK_COMPONENT_INDEX_NAME = 0;
    public static final int TASK_COMPONENT_COUNT = 1;
    
    public static final int DEADLINE_COMPONENT_INDEX_NAME = 0;
    public static final int DEADLINE_COMPONENT_INDEX_END_DATE = 1;
    public static final int DEADLINE_COMPONENT_INDEX_END_TIME = 2;
    public static final int DEADLINE_COMPONENT_COUNT = 3;
    
    public static final int EVENT_COMPONENT_INDEX_NAME = 0;
    public static final int EVENT_COMPONENT_INDEX_START_DATE = 1;
    public static final int EVENT_COMPONENT_INDEX_START_TIME = 2;
    public static final int EVENT_COMPONENT_INDEX_END_DATE = 3;
    public static final int EVENT_COMPONENT_INDEX_END_TIME = 4;
    public static final int EVENT_COMPONENT_COUNT = 5;

    private Name name;
    private TaskDate startDate;
    private TaskTime startTime;
    private TaskDate endDate;
    private TaskTime endTime;
    private boolean isDone;

    private TaskType taskType;

    private UniqueTagList tags;

    /**
     * Constructor for a "todo" Task.
     * "todo" is a Task only has name
     * Every field must be present and not null.
     */
    public Task(Name name, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(name, tags);
        
        this.name = name;
        this.tags = new UniqueTagList(tags);
        this.taskType = TaskType.TODO;
    }
    
    /**
     * Constructor for a "deadline" Task.
     * "deadline" is a Task only has name, endDate and endTime
     * Every field must be present and not null.
     */
    public Task(Name name, TaskDate endDate, TaskTime endTime, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(name, endDate, endTime, tags);
        
        this.name = name;
        this.endDate = endDate;
        this.endTime = endTime;
        this.tags = new UniqueTagList(tags);
        this.taskType = TaskType.DEADLINE;
    }
    
    /**
     * Constructor for a "event" Task.
     * "event" is a Task with all fields.
     * This constructor allows nulls and can be used when unsure which values are null
     */
    public Task(Name name, TaskDate startDate, TaskTime startTime,
            TaskDate endDate, TaskTime endTime, UniqueTagList tags) {
        this.name = name;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
        this.taskType = TaskType.EVENT;
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getStartDate(), source.getStartTime(),
                source.getEndDate(), source.getEndTime(), source.getTags());
    }

    @Override
    public Name getName() {
        return name;
    }
    
    @Override
    public TaskDate getStartDate() {
        return startDate;
    }
    
    @Override
    public TaskDate getEndDate() {
        return endDate;
    }

    @Override
    public TaskTime getStartTime() {
        return startTime;
    }

    @Override
    public TaskTime getEndTime() {
        return endTime;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }
    
    @Override
    public TaskType getTaskType() {
    	return taskType;
    }
    

    /**
     * Replaces this person's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }
    
    /** 
     * Renames task with new name. For edit command.
     */
    public void setNewName(String newName) {
    	try {	
    		this.name = new Name(newName);
    	} catch (IllegalValueException e) {
    		e.printStackTrace();
    	}
    }
    /** 
     * Marks task as done.
     */
    public void markAsDone() {
    	if (!isDone) {
    		this.isDone = true;
    	}
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

	@Override
	public boolean getIsDone() {
		return isDone;
	}
}
