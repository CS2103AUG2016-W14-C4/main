package seedu.taskitty.storage;

import seedu.taskitty.commons.core.LogsCenter;
import seedu.taskitty.commons.exceptions.DataConversionException;
import seedu.taskitty.commons.util.FileUtil;
import seedu.taskitty.model.ReadOnlyDeadlineManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A class to access TaskManager data stored as an xml file on the hard disk.
 */
public class XmlDeadlineManagerStorage implements DeadlineManagerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlDeadlineManagerStorage.class);

    private String filePath;

    public XmlDeadlineManagerStorage(String filePath){
        this.filePath = filePath;
    }

    public String getDeadlineManagerFilePath(){
        return filePath;
    }

    /**
     * Similar to {@link #readTaskManager()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyDeadlineManager> readDeadlineManager(String filePath) throws DataConversionException, FileNotFoundException {
        assert filePath != null;

        File taskManagerFile = new File(filePath);

        if (!taskManagerFile.exists()) {
            logger.info("TaskManager file "  + taskManagerFile + " not found");
            return Optional.empty();
        }

        ReadOnlyDeadlineManager taskManagerOptional = XmlFileStorage.loadDeadlineDataFromSaveFile(new File(filePath));

        return Optional.of(taskManagerOptional);
    }

    /**
     * Similar to {@link #saveTaskManager(ReadOnlyDeadlineManager)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveDeadlineManager(ReadOnlyDeadlineManager taskManager, String filePath) throws IOException {
        assert taskManager != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableDeadlineManager(taskManager));
    }

    @Override
    public Optional<ReadOnlyDeadlineManager> readDeadlineManager() throws DataConversionException, IOException {
        return readDeadlineManager(filePath);
    }

    @Override
    public void saveDeadlineManager(ReadOnlyDeadlineManager taskManager) throws IOException {
        saveDeadlineManager(taskManager, filePath);
    }
}
