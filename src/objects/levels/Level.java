package objects.levels;

import java.awt.Point;
import objects.blocks.Block;

/**
 *
 * @author alexisvincent
 */
public class Level {
    
    private Block[][] level;
    private int id;

    public Level(int id, Block[][] level) {
        this.level = level;
        this.id = id;
    }

    public Block[][] getLevel() {
        return level;
    }

    public void setLevel(Block[][] level) {
        this.level = level;
    }
    
    public void setBlock(Block block, int xIndex, int yIndex) {
        level[xIndex][yIndex] = block;
        block.setArrayPosition(new Point(xIndex, yIndex));
    }
    
    public Block getBlock(int x, int y) {
        return level[x][y];
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

    public void setID(int id) {
        this.id = id;
    }
    
}
