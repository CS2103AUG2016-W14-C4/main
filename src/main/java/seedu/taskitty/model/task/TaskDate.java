package seedu.taskitty.model.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.taskitty.commons.exceptions.IllegalValueException;

//@@author A0139930B
/**
 * Represents a Task's date in the task manager.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateFormat(String)}
 */
public class TaskDate {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Task dates should be in the format dd/mm/yyyy";
    public static final String MESSAGE_DATE_INVALID =
            "Task date provided is invalid!";
    public static final String MESSAGE_DATE_MISSING =
            "Task date must be provided";
    public static final String DATE_FORMAT_STRING = "dd/MM/yyyy";
    public static final DateTimeFormatter DATE_FORMATTER_STORAGE = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING);
    public static final DateTimeFormatter DATE_FORMATTER_UI = DateTimeFormatter.ofPattern("E, dd MMM");
    
    //format: dd/mm/yyyy
    private static final String DATE_VALIDATION_REGEX = "[\\p{Digit}]{1,2}/[\\p{Digit}]{1,2}/[\\p{Digit}]{4}";

    private final LocalDate date;

    /**
     * Validates given date. The date should be parsed by Natty and
     * be in the format according to DATE_FORMAT_STRING
     *
     * @throws IllegalValueException if given date is invalid.
     */
    public TaskDate(String date) throws IllegalValueException {
        //date cannot be null after being parsed by natty
        assert date != null;
        
        String trimDate = date.trim();
        //This is not an assert because user can change the database and input wrong formats
        if (!isValidDateFormat(trimDate)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        
        try {
            this.date = LocalDate.parse(trimDate, DATE_FORMATTER_STORAGE);
        } catch (DateTimeParseException dtpe){
            throw new IllegalValueException(MESSAGE_DATE_INVALID);
        }
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidDateFormat(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }
    
    /**
     * Returns true if this TaskDate is before the given TaskDate.
     * 
     * @param date to be compared
     */
    public boolean isBefore(TaskDate date) {
        return this.date.isBefore(date.getDate());
    }

    @Override
    public String toString() {
        return date.format(DATE_FORMATTER_STORAGE);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskDate // instanceof handles nulls
                && this.date.equals(((TaskDate) other).date)); // state check
    }
    
    /**
     * This method can be used when unsure which dates are null
     */
    public static boolean isEquals(TaskDate date, TaskDate other) {

        return (date == null && other == null) //if both are null, they are equal
                || date != null && date.equals(other);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
    
    public LocalDate getDate() {
        return date;
    }
}
