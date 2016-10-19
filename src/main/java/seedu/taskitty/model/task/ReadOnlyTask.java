package seedu.taskitty.model.task;

import seedu.taskitty.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Person in the taskmanager.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    Name getName();
    boolean getIsDone();
    TaskDate getStartDate();
    TaskDate getEndDate();
    TaskTime getStartTime();
    TaskTime getEndTime();

    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName())); // state checks here onwards
    }

    /**
     * Formats the task as text, showing all dates and times.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName());
        if (getStartDate() != null) {
            builder.append("From: " + getStartDate().toString());
        }
        if (getStartTime() != null) {
            builder.append(" " + getStartTime().toString());
        }
        if (getEndDate() != null) {
            builder.append("Till: " + getEndDate().toString());
        }
        if (getEndTime() != null) {
            builder.append(" " + getEndTime().toString());
        }
        builder.append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
    
    /**
     * Formats the task as a String representing all arguments it has.
     */
    default String getAllKeyArguments() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName());
        if (getStartDate() != null) {
            builder.append(" " + getStartDate().toString());
        }
        if (getStartTime() != null) {
            builder.append(" " + getStartTime().toString());
        }
        if (getEndDate() != null) {
            builder.append(" " + getEndDate().toString());
        }
        if (getEndTime() != null) {
            builder.append(" " + getEndTime().toString());
        }
        getTags().forEach(builder::append);
        return builder.toString();
    }
    
    /**
     * Returns a string representation of this Task's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }

}
