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
    private int id;
    
    public enum BlockAction {
        PassThrough, Stop, Finish
    }

    public Block(int id) {
        arrayPosition = new Point(0, 0);
        type = "";
        this.id = id;
    }
    
    public String getType() {
        return type;
    }
    
    protected void setType(String type) {
        this.type = type;
    }
    
    public abstract char getChar();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Point getArrayPosition() {
        return arrayPosition;
    }
    
    public void setArrayPosition(Point arrayPosition) {
        this.arrayPosition = arrayPosition;
    }

    public abstract BlockAction doAction();
}