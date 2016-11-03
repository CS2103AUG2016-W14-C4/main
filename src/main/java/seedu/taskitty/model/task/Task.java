package seedu.taskitty.model.task;

import seedu.taskitty.commons.util.CollectionUtil;
import seedu.taskitty.model.tag.UniqueTagList;

import java.util.Objects;

//@@author A0139930B
/**
 * Represents a Task in the taskManager.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask, Comparable<Task> {
    
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
    
    public static final String PREFIX_TODO = "T";
    public static final String PREFIX_DEADLINE = "D";
    public static final String PREFIX_EVENT = "E";
    public static final String[] CATEGORIES = {
            PREFIX_TODO, PREFIX_DEADLINE, PREFIX_EVENT };

    public static final int DEFAULT_CATEGORY_INDEX = 0;
    public static final int TODO_CATEGORY_INDEX = 0;
    public static final int DEADLINE_CATEGORY_INDEX = 1;
    public static final int EVENT_CATEGORY_INDEX = 2;
    
    private Name name;
    private TaskPeriod period;
    private boolean isDone;
    private boolean isOverdue;

    private UniqueTagList tags;

    /**
     * Constructor for a Task.
     * The type of Task it is is dependent on the period given.
     * 
     * Every field must be present and not null.
     */
    public Task(Name name, TaskPeriod period, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(name, period, tags);
        
        this.name = name;
        this.period = period;
        this.tags = new UniqueTagList(tags);
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getPeriod(), source.getTags());
        this.isDone = source.getIsDone();
        this.isOverdue = source.isOverdue();
    }

    @Override
    public Name getName() {
        return name;
    }

    //@@author
    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this person's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }
    
    /** 
     * Marks task as done.
     */
    public void unmarkAsDone() {
        if (isDone) {
            this.isDone = false;
        }
    }
    
    //@@author A0130853L
    /** 
     * Marks task as done or event as over.
     */
    public void markAsDone() {
    	if (!isDone) {
    		this.isDone = true;
    		this.isOverdue = false;
    	}
    }
    
    /**
     * Marks a deadline as overdue.
     */
    public void markAsOverdue() {
    	if (!isDone && !isOverdue) {
    		this.isOverdue = true;
    	}
    }
    
    
    //@@author
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
	public TaskPeriod getPeriod() {
	    return period;
	}
	//@@author A0130853L
	@Override
	public boolean getIsDone() {
		return isDone;
	}
	
	@Override
	public boolean isTodo() {
		return period.isTodo();
	}
	
	@Override
	public boolean isDeadline() {
		return period.isDeadline();
	}
	
	@Override
	public boolean isEvent() {
		return period.isEvent();
	}
	
	// only for deadlines
	@Override
	public boolean isOverdue() {
		return isOverdue;
	}
	
	
	//@@author A0139052L
    @Override
    public int compareTo(Task taskToCompare) {
        // sort all tasks that are done to the back of the list
	    if (this.getIsDone() && !taskToCompare.getIsDone()) {
	        return 1;
	    } else if (!this.getIsDone() && taskToCompare.getIsDone()) {
	        return -1;
       } else {           
       	   int periodCompare = this.getPeriod().compareTo(taskToCompare.period);
       	   if (this.getIsDone()) {
       	       periodCompare = -periodCompare; // sort done tasks in the opposite order
       	   }
           //If no difference in date and time is found in period, compare using name
           if (periodCompare == 0) {
               return this.getName().fullName.toLowerCase().compareTo(taskToCompare.getName().fullName.toLowerCase());
           } else {
               return periodCompare;
           }
       }       
    }
	
}
