package core;

import gui.panes.ApplicationPane;
import gui.panes.GamePane;
import gui.panes.LevelManagementPane;
import gui.panes.ModalPane;
import gui.panes.PreferencePane;
import gui.panes.WelcomePane;

/**
 *
 * @author alexisvincent
 */
public class StateMachine {

    public static WelcomePane welcomePane;
    public static GamePane gamePane;
    public static LevelManagementPane levelSelectionPane;
    public static PreferencePane preferencePane;
    
    public static ApplicationPane applicationPane;
    public static ModalPane modalPane;
    
    public static int frameRate;
    public static double rotationScale;
    
    static {
        
        frameRate = 60;
        rotationScale = 0.8;
        
        welcomePane = new WelcomePane();
        gamePane = new GamePane();
        levelSelectionPane = new LevelManagementPane();
        preferencePane = new PreferencePane();
        
        applicationPane = new ApplicationPane();
        
//        modalPane = new ModalPane();
    }
}
