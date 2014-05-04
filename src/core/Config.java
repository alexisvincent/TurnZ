package core;

import swing.resourceManagers.FontEngine;

/**
 *
 * @author alexisvincent
 */
public class Config {

    public static void setup() {
        FontEngine.registerFont("/resources/fonts/Minecraftia.ttf");
    }
    
    public static int timeToFrames(double seconds) {
        return (int)(StateMachine.frameRate*seconds);
    }
}
