package seedu.taskitty.logic.parser;

import seedu.taskitty.commons.exceptions.IllegalValueException;
import seedu.taskitty.commons.util.StringUtil;
import seedu.taskitty.logic.commands.*;
import seedu.taskitty.model.tag.Tag;
import seedu.taskitty.model.task.TaskDate;
import seedu.taskitty.model.task.TaskTime;

import static seedu.taskitty.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.taskitty.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;


import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

/**
 * Parses user input.
 */
public class CommandParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern TASK_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");
    
      
    //Used for checking for number date formats in arguments
    private static final Pattern LOCAL_DATE_FORMAT =  Pattern.compile(".* (?<arguments>\\d(\\d)?[/-]\\d(\\d)?).*");
    
    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern TASK_DATA_ARGS_FORMAT = //Tags must be at the end
            Pattern.compile("(?<arguments>[\\p{Graph} ]+)"); // \p{Graph} is \p{Alnum} or \p{Punct}
    
    private static final Pattern EDIT_TASK_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<targetIndex>.)"
                    + "(?<name>[^/]+)"
                    + "(?<tagArguments>(?: t/[^/]+)*)"); // variable number of tags

    public CommandParser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);
        
        case EditCommand.COMMAND_WORD:
            return prepareEdit(arguments);
            
        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
        
        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();
            
        case DoneCommand.COMMAND_WORD:
        	return prepareDone(arguments);
        	
        case ViewDoneCommand.COMMAND_WORD:
        	return new ViewDoneCommand();
        
        case ViewCommand.COMMAND_WORD:
        	if (userInput.equals("view")) {
        		return prepareView(null);
        	}
        	return prepareView(arguments);
        
        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }
    
    /**
     * Parses arguments in the context of the view command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareView(String arguments) {
    	if (arguments == null) {
			return new ViewCommand(); // view events today, and all deadlines and todos
		}
    	if (arguments.trim().equals("done")) {
    		return new ViewDoneCommand(); // defaults to viewDone command
    	}
		String[] details = extractTaskDetailsNatty(arguments);
		if (details.length!= 3) { // no date was successfully extracted
			return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
	    } else {
	        assert details[1] != null; // contains date
	        return new ViewCommand(details[1]);
	    }
	}

    //@@author A0139930B
	/**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
        final Matcher matcher = TASK_DATA_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }
        try {
            String arguments = matcher.group("arguments");
            String taskDetailArguments = getTaskDetailArguments(arguments);
            String tagArguments = getTagArguments(arguments);
            
            return new AddCommand(
                    extractTaskDetailsNatty(taskDetailArguments),
                    getTagsFromArgs(tagArguments)
            );
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    
    /**
     * Parses the argument to get a string of all the relevant details of the task
     * 
     * @param arguments command args string without command word
     */
    private String getTaskDetailArguments(String arguments) {
        //Have 2 magic numbers.. where should I put them..
        //-1 is NOT_FOUND
        //0 is START_OF_ARGUMENT
        int detailLastIndex = arguments.indexOf(Tag.TAG_VALIDATION_REGEX_PREFIX);
        if (detailLastIndex == -1) {
            detailLastIndex = arguments.length();
        }
        
        return arguments.substring(0, detailLastIndex).trim();
    }
    
    /**
     * Parses the argument to get a string of all tags, including the Tag prefix
     * 
     * @param arguments command args string without command word
     */
    private String getTagArguments(String arguments) {
        //This line is exactly the same as the 1st line of getTaskDetailArguments.. how?
        int tagStartIndex = arguments.indexOf(Tag.TAG_VALIDATION_REGEX_PREFIX);
        if (tagStartIndex == -1) {
            tagStartIndex = arguments.length();
        }
        
        return arguments.substring(tagStartIndex);
    }
    
    /**
     * Extracts the task details into a String array representing the name, date, time.
     * Details are arranged according to index shown in Task
     * 
     * @param dataArguments command args string with only name, date, time arguments
     */
    private String[] extractTaskDetailsNatty(String dataArguments) {
        dataArguments = convertToNattyDateFormat(dataArguments);
        Parser dateTimeParser = new Parser();
        List<DateGroup> dateGroups = dateTimeParser.parse(dataArguments);
        
        int nameEndIndex = dataArguments.length();
        ArrayList<String> details = new ArrayList<String>();
        for (DateGroup group : dateGroups) {
            List<Date> dates = group.getDates();
            nameEndIndex = Math.min(nameEndIndex, group.getPosition() - 1);
            for (Date date : dates) {
                details.add(extractLocalDate(date));
                details.add(extractLocalTime(date));
            }
        }
        details.add(0, dataArguments.substring(0, nameEndIndex).trim());
        
        String[] returnDetails = new String[details.size()];
        details.toArray(returnDetails);
        return returnDetails;
        
    }
    
    //@@author
    /**
     * Converts any number formats of date from the local format to one which can be parsed by natty
     * @param arguments
     * @return arguments with converted dates if any
     */
    private String convertToNattyDateFormat(String arguments) {
        Matcher matchDate = LOCAL_DATE_FORMAT.matcher(arguments);
        if (matchDate.matches()) {
            String localDateString = matchDate.group("arguments");
            String dateSeparator = getDateSeparator(localDateString);
            return convertToNattyFormat(arguments, localDateString, dateSeparator);
        } else {
            return arguments;
        }       
    }
    
    /**
     * Get the separator between day month and year in a date
     * @param localDateString the string representing the date
     * @return the separator character used in localDateString
     */
    private String getDateSeparator(String localDateString) {
        // if 2nd char in string is an integer, then the 3rd char must be the separator
        // else 2nd char is the separator
        if (StringUtil.isInteger(localDateString.substring(1,2))) {
            return localDateString.substring(2, 3);
        } else {
            return localDateString.substring(1, 2);
        }
    }
    
    /**
     * Convert the local date format inside arguments into a format
     * which can be parsed by natty
     * @param arguments the full argument string
     * @param localDateString the localDate extracted out from arguments
     * @param dateSeparator the separator for the date extracted out
     * @return converted string where the date format has been converted from local to natty format
     */
    private String convertToNattyFormat(String arguments, String localDateString, String dateSeparator) {
        String[] dateComponents = localDateString.split(dateSeparator);
        int indexOfDate = arguments.indexOf(localDateString);
        StringBuilder nattyDateStringBuilder =  new StringBuilder();
        nattyDateStringBuilder.append(dateComponents[1]);
        nattyDateStringBuilder.append(dateSeparator);
        nattyDateStringBuilder.append(dateComponents[0]);
        StringBuilder convertDateStringBuilder = new StringBuilder(arguments);
        convertDateStringBuilder.replace(indexOfDate, indexOfDate + localDateString.length(), nattyDateStringBuilder.toString());
        String stringFromConvertedDate = convertDateStringBuilder.substring(indexOfDate);
        String stringUpToConvertedDate = convertDateStringBuilder.substring(0, indexOfDate);
        return convertToNattyDateFormat(stringUpToConvertedDate) + stringFromConvertedDate;
    }
    
    //@@author A0139930B
    /**
     * Takes in a date from Natty and converts it into a string representing date
     * Format of date returned is according to TaskDate
     * 
     * @param date retrieved using Natty
     */
    private String extractLocalDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TaskDate.DATE_FORMAT_STRING);
        return dateFormat.format(date);
    }
    
    /**
     * Takes in a date from Natty and converts it into a string representing time
     * Format of time returned is according to TaskTime
     * 
     * @param date retrieved using Natty
     */
    private String extractLocalTime(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(TaskTime.TIME_FORMAT_STRING);
        String currentTime = timeFormat.format(new Date());
        String inputTime = timeFormat.format(date);
        
        if (currentTime.equals(inputTime)) {
            //Natty parses the current time if string does not include time.
            //We want to ignore input when current time equal input time
            return null;
        }
        return inputTime;
        
    }

    //@@author
    /**
     * Extracts the new person's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Parses arguments in the context of the delete person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {
        String[] splitArgs = args.trim().split(" ");
        if (splitArgs.length == 0 || splitArgs.length > 2) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        //takes the last argument given for parsing index
        Optional<Integer> index = parseIndex(splitArgs[splitArgs.length - 1]);
        
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        if (splitArgs.length == 1) {
            return new DeleteCommand(index.get());
        } else {
            return new DeleteCommand(index.get(), StringUtil.getCategoryIndex(splitArgs[0]));
        }
    }
    
    /**
     * Parses arguments in the context of the mark as done command.
     * 
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDone(String args) {
    	
        String[] splitArgs = args.trim().split(" ");
        if (splitArgs.length == 0 || splitArgs.length > 2) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
        }
        //takes the last argument given for parsing index
        Optional<Integer> index = parseIndex(splitArgs[splitArgs.length - 1]);
        
        if (!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
        }
        
        if (splitArgs.length == 1) {
            return new DoneCommand(index.get());
        } else {
            return new DoneCommand(index.get(), StringUtil.getCategoryIndex(splitArgs[0]));
        }
    }
    
    /**
     * Parses arguments in the context of the edit task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareEdit(String args) {
        String[] splitArgs = args.trim().split(" ");
        if (splitArgs.length <3) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        
        
        Optional<Integer> index = parseIndex(splitArgs[1]);
        int categoryIndex = StringUtil.getCategoryIndex(splitArgs[0]);

        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        
        try {
            String arguments = "";
            for (int i=2; i<splitArgs.length; i++){
                arguments = arguments + splitArgs[i] + " ";
            }
            arguments.substring(0, arguments.length()-1);
            String taskDetailArguments = getTaskDetailArguments(arguments);
            String tagArguments = getTagArguments(arguments);
            
            return new EditCommand(
                    extractTaskDetailsNatty(taskDetailArguments),
                    getTagsFromArgs(tagArguments),
                    index.get(),
                    categoryIndex
            );
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Parses arguments in the context of the select person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = TASK_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }    
    
    /**
     * Parses arguments in the context of the find person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }
        
        //check if the keyword given is a single date, if so give a view command
        String[] date = extractTaskDetailsNatty(args);
        if (containsOnlyOneDate(date)) {
            return new ViewCommand(date[1]);
        }
        
        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }
    
    /**
     * checks if the array obtained from extractTaskDetailsNatty obtains only a single date
     * @param args the string array obtain from extractTaskDetailsNatty
     * @return true if arguments contains only 1 date in it
     */
    private boolean containsOnlyOneDate(String[] args) {
        return args.length == 3 && args[2] == null && args[0].equals("");
    }
}