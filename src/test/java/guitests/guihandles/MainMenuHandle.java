package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import seedu.taskitty.TestApp;

import java.util.Arrays;

/**
 * Provides a handle to the main menu of the app.
 */
public class MainMenuHandle extends GuiHandle {
    public MainMenuHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public GuiHandle clickOn(String... menuText) {
        Arrays.stream(menuText).forEach((menuItem) -> guiRobot.clickOn(menuItem));
        return this;
    }

    public HelpWindowHandle openHelpWindowUsingMenu() {
        clickOn("Help", "F1");
        return new HelpWindowHandle(guiRobot, primaryStage);
    }

    public HelpWindowHandle openHelpWindowUsingAccelerator() {
        useAccelerator("F1");
        return new HelpWindowHandle(guiRobot, primaryStage);
    }
    
    public void useClearCommandUsingAccelerator() {
        useAccelerator("Ctrl", "Shift", "C");
        guiRobot.sleep(500);
    }
    
    public void useUndoCommandUsingAccelerator() {
        useAccelerator("Ctrl", "Shift", "Z");
        guiRobot.sleep(500);
    }
    
    
    private void useAccelerator(String... buttonsToPress) {
        KeyCode[] buttons = new KeyCode[buttonsToPress.length];
        for (int i = 0; i < buttonsToPress.length; i++) {
            buttons[i] = KeyCode.getKeyCode(buttonsToPress[i]);
        }   
        guiRobot.push(buttons);
        guiRobot.sleep(500);
    }  
    
}
