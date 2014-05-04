package objects.blocks;

import java.awt.Point;
import swing.components.AComponent;


/**
 *
 * @author alexisvincent
 */
public abstract class Block extends AComponent {
    
    private String type;
    private Point arrayPosition;
    
    public enum BlockAction {
        PassThrough, Stop
    }

    public Block() {
        arrayPosition = new Point(0, 0);
        type = "";
    }
    
    public String getType() {
        return type;
    }
    
    protected void setType(String type) {
        this.type = type;
    }
    
    public Point getArrayPosition() {
        return arrayPosition;
    }
    
    public void setArrayPosition(Point arrayPosition) {
        this.arrayPosition = arrayPosition;
    }

    public abstract BlockAction doAction();
}