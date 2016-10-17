package seedu.taskitty.storage;

import seedu.taskitty.commons.core.LogsCenter;
import seedu.taskitty.commons.exceptions.DataConversionException;
import seedu.taskitty.commons.util.FileUtil;
import seedu.taskitty.model.ReadOnlyEventManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A class to access TaskManager data stored as an xml file on the hard disk.
 */
public class XmlEventManagerStorage implements EventManagerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlEventManagerStorage.class);

    private String filePath;

    public XmlEventManagerStorage(String filePath){
        this.filePath = filePath;
    }

    public String getEventManagerFilePath(){
        return filePath;
    }

    /**
     * Similar to {@link #readTaskManager()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyEventManager> readEventManager(String filePath) throws DataConversionException, FileNotFoundException {
        assert filePath != null;

        File taskManagerFile = new File(filePath);

        if (!taskManagerFile.exists()) {
            logger.info("TaskManager file "  + taskManagerFile + " not found");
            return Optional.empty();
        }

        ReadOnlyEventManager taskManagerOptional = XmlFileStorage.loadEventDataFromSaveFile(new File(filePath));

        return Optional.of(taskManagerOptional);
    }

    /**
     * Similar to {@link #saveTaskManager(ReadOnlyEventManager)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveEventManager(ReadOnlyEventManager taskManager, String filePath) throws IOException {
        assert taskManager != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableEventManager(taskManager));
    }

    @Override
    public Optional<ReadOnlyEventManager> readEventManager() throws DataConversionException, IOException {
        return readEventManager(filePath);
    }

    @Override
    public void saveEventManager(ReadOnlyEventManager taskManager) throws IOException {
        saveEventManager(taskManager, filePath);
    }
}

