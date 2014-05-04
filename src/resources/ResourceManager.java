package resources;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author alexisvincent
 */
public class ResourceManager {
    
    public static Image background;
    
    public static Image getBackground() {
        if (background == null) {
            try {
                background = ImageIO.read(ResourceManager.class.getResource("/resources/images/background.jpg"));
            } catch (IOException ex) {
                Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return background;
    }

}
