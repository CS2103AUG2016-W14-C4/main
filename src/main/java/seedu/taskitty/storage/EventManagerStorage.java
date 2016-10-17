package seedu.taskitty.storage;

import seedu.taskitty.commons.exceptions.DataConversionException;
import seedu.taskitty.model.ReadOnlyTaskManager;
import seedu.taskitty.model.ReadOnlyDeadlineManager;
import seedu.taskitty.model.ReadOnlyEventManager;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a storage for {@link seedu.taskitty.model.DeadlineManager}.
 */
public interface EventManagerStorage {

    /**
     * Returns the file path of the data file.
     */
    String getEventManagerFilePath();

    /**
     * Returns TaskManager data as a {@link ReadOnlyTaskManager}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyEventManager> readEventManager() throws DataConversionException, IOException;

    /**
     * @see #getTaskManagerFilePath()
     */
    Optional<ReadOnlyEventManager> readEventManager(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskManager} to the storage.
     * @param taskManager cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveEventManager(ReadOnlyEventManager taskManager) throws IOException;

    /**
     * @see #saveTaskManager(ReadOnlyTaskManager)
     */
    void saveEventManager(ReadOnlyEventManager taskManager, String filePath) throws IOException;

}
