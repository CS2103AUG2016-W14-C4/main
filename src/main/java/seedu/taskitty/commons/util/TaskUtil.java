package seedu.taskitty.commons.util;

import seedu.taskitty.model.task.Task;

//@@author A0139052L
/**
 * Helper functions for handling tasks related items.
 *
 */
public class TaskUtil {
    
    /**
     * Returns the specified index in the {@code command} IF a valid category character was given.
     *   else return the default index
     */
    public static int getCategoryIndex(char categoryChar) {
        
        String category = Character.toString(categoryChar).toLowerCase();
        switch(category) {
        
            case(Task.PREFIX_TODO) :
                return Task.TODO_CATEGORY_INDEX;
            
            case(Task.PREFIX_DEADLINE): 
                return Task.DEADLINE_CATEGORY_INDEX;
            
            case(Task.PREFIX_EVENT): 
                return Task.EVENT_CATEGORY_INDEX;
            
            default: 
                return getDefaultCategoryIndex();
            
        }
    }
    
    /**
     * Returns the default category index for when the user does not give any
     */
    public static int getDefaultCategoryIndex() {
        return Task.DEFAULT_CATEGORY_INDEX;
    }
    
}
