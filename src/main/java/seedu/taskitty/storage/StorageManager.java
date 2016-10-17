package seedu.taskitty.storage;

import com.google.common.eventbus.Subscribe;

import seedu.taskitty.commons.core.ComponentManager;
import seedu.taskitty.commons.core.LogsCenter;
import seedu.taskitty.commons.events.model.DeadlineManagerChangedEvent;
import seedu.taskitty.commons.events.model.EventManagerChangedEvent;
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
import java.util.logging.Logger;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskManagerStorage taskManagerStorage;
    private DeadlineManagerStorage deadlineManagerStorage;
    private EventManagerStorage eventManagerStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TaskManagerStorage taskManagerStorage, DeadlineManagerStorage deadlineManagerStorage, EventManagerStorage eventManagerStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.taskManagerStorage = taskManagerStorage;
        this.deadlineManagerStorage = deadlineManagerStorage;
        this.eventManagerStorage = eventManagerStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String taskManagerFilePath, String deadlineManagerFilePath, String eventManagerFilePath, String userPrefsFilePath) {
        this(new XmlTaskManagerStorage(taskManagerFilePath), new XmlDeadlineManagerStorage(deadlineManagerFilePath), new XmlEventManagerStorage(eventManagerFilePath),
                new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public String getTaskManagerFilePath() {
        return taskManagerStorage.getTaskManagerFilePath();
    }
    
    @Override
    public String getDeadlineManagerFilePath() {
        return deadlineManagerStorage.getDeadlineManagerFilePath();
    }
    
    @Override
    public String getEventManagerFilePath() {
        return eventManagerStorage.getEventManagerFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException {
        return readTaskManager(taskManagerStorage.getTaskManagerFilePath());
    }
    
    @Override
    public Optional<ReadOnlyDeadlineManager> readDeadlineManager() throws DataConversionException, IOException {
        return readDeadlineManager(deadlineManagerStorage.getDeadlineManagerFilePath());
    }
    
    @Override
    public Optional<ReadOnlyEventManager> readEventManager() throws DataConversionException, IOException {
        return readEventManager(eventManagerStorage.getEventManagerFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskManagerStorage.readTaskManager(filePath);
    }
    
    @Override
    public Optional<ReadOnlyDeadlineManager> readDeadlineManager(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return deadlineManagerStorage.readDeadlineManager(filePath);
    }
    
    @Override
    public Optional<ReadOnlyEventManager> readEventManager(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return eventManagerStorage.readEventManager(filePath);
    }

    @Override
    public void saveTaskManager(ReadOnlyTaskManager taskManager) throws IOException {
        saveTaskManager(taskManager, taskManagerStorage.getTaskManagerFilePath());
    }
    
    @Override
    public void saveDeadlineManager(ReadOnlyDeadlineManager taskManager) throws IOException {
        saveDeadlineManager(taskManager, deadlineManagerStorage.getDeadlineManagerFilePath());
    }
    
    @Override
    public void saveEventManager(ReadOnlyEventManager taskManager) throws IOException {
        saveEventManager(taskManager, eventManagerStorage.getEventManagerFilePath());
    }

    @Override
    public void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskManagerStorage.saveTaskManager(taskManager, filePath);
    }
    
    @Override
    public void saveDeadlineManager(ReadOnlyDeadlineManager taskManager, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        deadlineManagerStorage.saveDeadlineManager(taskManager, filePath);
    }
    
    @Override
    public void saveEventManager(ReadOnlyEventManager taskManager, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        eventManagerStorage.saveEventManager(taskManager, filePath);
    }


    @Override
    @Subscribe
    public void handleTaskManagerChangedEvent(TaskManagerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskManager(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
    
    @Override
    @Subscribe
    public void handleDeadlineManagerChangedEvent(DeadlineManagerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveDeadlineManager(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
    
    @Override
    @Subscribe
    public void handleEventManagerChangedEvent(EventManagerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveEventManager(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
