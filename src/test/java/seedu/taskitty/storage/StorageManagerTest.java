package seedu.taskitty.storage;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.taskitty.commons.events.model.DeadlineManagerChangedEvent;
import seedu.taskitty.commons.events.model.EventManagerChangedEvent;
import seedu.taskitty.commons.events.model.TaskManagerChangedEvent;
import seedu.taskitty.commons.events.storage.DataSavingExceptionEvent;
import seedu.taskitty.model.DeadlineManager;
import seedu.taskitty.model.EventManager;
import seedu.taskitty.model.ReadOnlyDeadlineManager;
import seedu.taskitty.model.ReadOnlyEventManager;
import seedu.taskitty.model.ReadOnlyTaskManager;
import seedu.taskitty.model.TaskManager;
import seedu.taskitty.model.UserPrefs;
import seedu.taskitty.storage.JsonUserPrefsStorage;
import seedu.taskitty.storage.Storage;
import seedu.taskitty.storage.StorageManager;
import seedu.taskitty.storage.XmlTaskManagerStorage;
import seedu.taskitty.testutil.EventsCollector;
import seedu.taskitty.testutil.TypicalTestTask;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StorageManagerTest {

    private StorageManager storageManager;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Before
    public void setup() {
        storageManager = new StorageManager(getTempFilePath("ab"), 
                getTempFilePath("ab"), getTempFilePath("ab"), getTempFilePath("prefs"));
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefsStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
     */

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        TaskManager original = new TypicalTestTask().getTypicalTaskManager();
        storageManager.saveTaskManager(original);
        ReadOnlyTaskManager retrieved = storageManager.readTaskManager().get();
        assertEquals(original, new TaskManager(retrieved));
        //More extensive testing of AddressBook saving/reading is done in XmlAddressBookStorageTest
    }

    @Test
    public void getAddressBookFilePath(){
        assertNotNull(storageManager.getTaskManagerFilePath());
    }

    @Test
    public void handleAddressBookChangedEvent_exceptionThrown_eventRaised() throws IOException {
        //Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlTaskManagerStorageExceptionThrowingStub("dummy"), 
                new XmlDeadlineManagerStorageExceptionThrowingStub("dummy"), 
                new XmlEventManagerStorageExceptionThrowingStub("dummy"),
                new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleTaskManagerChangedEvent(new TaskManagerChangedEvent(new TaskManager()));
        storage.handleDeadlineManagerChangedEvent(new DeadlineManagerChangedEvent(new DeadlineManager()));
        storage.handleEventManagerChangedEvent(new EventManagerChangedEvent(new EventManager()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlTaskManagerStorageExceptionThrowingStub extends XmlTaskManagerStorage{

        public XmlTaskManagerStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }
    
    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlDeadlineManagerStorageExceptionThrowingStub extends XmlDeadlineManagerStorage{

        public XmlDeadlineManagerStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveDeadlineManager(ReadOnlyDeadlineManager taskManager, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }
    
    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlEventManagerStorageExceptionThrowingStub extends XmlEventManagerStorage{

        public XmlEventManagerStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveEventManager(ReadOnlyEventManager taskManager, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
