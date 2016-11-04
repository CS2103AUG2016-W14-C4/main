package seedu.taskitty.model.task;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.taskitty.commons.exceptions.IllegalValueException;

//@@author A0139930B
/**
 * Represents a Task's time in the task manager.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class TaskTime {

    public static final String MESSAGE_TIME_CONSTRAINTS =
            "Task time should be in the format hh:mm";
    public static final String MESSAGE_TIME_INVALID =
            "Task time provided is invalid!";
    public static final String MESSAGE_TIME_MISSING =
            "Task time must be provided";
    public static final String TIME_FORMAT_STRING = "HH:mm";
    public static final DateTimeFormatter TIME_FORMATTER_STORAGE = DateTimeFormatter.ofPattern(TIME_FORMAT_STRING);
    public static final DateTimeFormatter TIME_FORMATTER_UI = DateTimeFormatter.ofPattern("hh:mma");
    
    //format: hh:mm
    private static final String TIME_VALIDATION_FORMAT = "[\\p{Digit}]{1,2}:[\\p{Digit}]{2}";

    private final LocalTime time;

    /**
     * Validates given time. The time should be parsed by Natty and
     * be in the format according to TIME_FORMAT_STRING
     *
     * @throws IllegalValueException if given time is invalid or null.
     */
    public TaskTime(String time) throws IllegalValueException {
        if (time == null) {
            throw new IllegalValueException(MESSAGE_TIME_MISSING);
        }
        
        String trimTime = time.trim();
        //This is not an assert because user can change the database and input wrong formats
        if (!isValidTimeFormat(trimTime)) {
            throw new IllegalValueException(MESSAGE_TIME_CONSTRAINTS);
        }
        
        try {
            this.time = LocalTime.parse(trimTime, TIME_FORMATTER_STORAGE);
        } catch (DateTimeParseException dtpe){
            throw new IllegalValueException(MESSAGE_TIME_INVALID);
        }
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidTimeFormat(String test) {
        return test.matches(TIME_VALIDATION_FORMAT);
    }
    
    /**
     * Returns true if this TaskTime is before the given TaskTime.
     * 
     * @param time to be compared
     */
    public boolean isBefore(TaskTime time) {
        return this.time.isBefore(time.getTime());
    }

    @Override
    public String toString() {
        return time.format(TIME_FORMATTER_STORAGE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskTime // instanceof handles nulls
                && this.time.equals(((TaskTime) other).time)); // state check
    }
    
    /**
     * This method can be used when unsure which times are null
     */
    public static boolean isEquals(TaskTime time, TaskTime other) {
        if (time == other) {
            return true;
        }
        
        //if either one is null, they are not equal
        if (time == null || other == null) {
            return false;
        }
        
        return time.equals(other);
    }
    
    public LocalTime getTime() {
        return time;
    }

}
