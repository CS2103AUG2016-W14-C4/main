package seedu.address.storage;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.events.model.TaskManagerChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.TaskManager;
import seedu.address.model.ReadOnlyTaskManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalTestPersons;
import seedu.address.testutil.EventsCollector;

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
        storageManager = new StorageManager(getTempFilePath("ab"), getTempFilePath("prefs"));
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
        TaskManager original = new TypicalTestPersons().getTypicalAddressBook();
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
        Storage storage = new StorageManager(new XmlAddressBookStorageExceptionThrowingStub("dummy"), new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleAddressBookChangedEvent(new TaskManagerChangedEvent(new TaskManager()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlAddressBookStorageExceptionThrowingStub extends XmlTaskManagerStorage{

        public XmlAddressBookStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveTaskManager(ReadOnlyTaskManager taskManager, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
