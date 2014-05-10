package objects.levels;

import java.awt.Point;
import objects.blocks.Block;

/**
 *
 * @author alexisvincent
 */
public class Level {
    
    private Level save;
    
    private Block[][] level;
    private int id;

    public Level(int id, Block[][] level) {
        this(id, level, null);
    }
    
    public Level(int id, Block[][] level, Level save) {
        this.level = level;
        this.id = id;
        this.save = save;
    }

    public void setLevel(Block[][] level) {
        this.level = level;
    }
    
    public void setBlock(Block block, int x, int y) {
        block.setArrayPosition(new Point(x, y));
        level[y][x] = block;
    }
    
    public Block getBlock(int x, int y) {
        return level[y][x];
    }
    
    public int getHeight() {
        return level.length;
    }
    
    public int getWidth() {
        return level[0].length;
    }

    public int getID() {
        return id;
    }

    public Level getSavedStates() {
        return save;
    }

    public void setSavedStates(Level save) {
        this.save = save;
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
}
