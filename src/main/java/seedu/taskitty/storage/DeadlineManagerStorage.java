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
public interface DeadlineManagerStorage {

    /**
     * Returns the file path of the data file.
     */
    String getDeadlineManagerFilePath();

    /**
     * Returns TaskManager data as a {@link ReadOnlyTaskManager}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyDeadlineManager> readDeadlineManager() throws DataConversionException, IOException;
    
    /**
     * @see #getDeadlineManagerFilePath()
     */
    Optional<ReadOnlyDeadlineManager> readDeadlineManager(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyTaskManager} to the storage.
     * @param taskManager cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveDeadlineManager(ReadOnlyDeadlineManager taskManager) throws IOException;

    /**
     * @see #saveTaskManager(ReadOnlyTaskManager)
     */
    void saveDeadlineManager(ReadOnlyDeadlineManager taskManager, String filePath) throws IOException;

}

