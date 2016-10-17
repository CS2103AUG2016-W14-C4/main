package seedu.taskitty.storage;

import seedu.taskitty.commons.events.model.TaskManagerChangedEvent;
import seedu.taskitty.commons.events.storage.DataSavingExceptionEvent;
import seedu.taskitty.commons.exceptions.DataConversionException;
import seedu.taskitty.model.ReadOnlyTaskManager;
import seedu.taskitty.model.ReadOnlyDeadlineManager;
import seedu.taskitty.model.ReadOnlyEventManager;
import seedu.taskitty.model.UserPrefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

/**
 * API of the Storage component
 */
public interface Storage extends TaskManagerStorage, DeadlineManagerStorage, EventManagerStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getTaskManagerFilePath();
    
    @Override
    String getDeadlineManagerFilePath();
    
    @Override
    String getEventManagerFilePath();

    @Override
    Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException;
    
    @Override
    Optional<ReadOnlyDeadlineManager> readDeadlineManager() throws DataConversionException, IOException;
    
    @Override
    Optional<ReadOnlyEventManager> readEventManager() throws DataConversionException, IOException;

    @Override
    void saveTaskManager(ReadOnlyTaskManager taskManager) throws IOException;
    
    @Override
    void saveDeadlineManager(ReadOnlyDeadlineManager taskManager) throws IOException;
    
    @Override
    void saveEventManager(ReadOnlyEventManager taskManager) throws IOException;

    /**
     * Saves the current version of the Task Manager to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleAddressBookChangedEvent(TaskManagerChangedEvent abce);
}
