package objects.levels;

import objects.blocks.Block;
import objects.blocks.BreakableBlock;
import objects.blocks.EmptyBlock;
import objects.blocks.GoalBlock;
import objects.blocks.ObstacleBlock;
import objects.blocks.PlayerBlock;

/**
 *
 * @author alexisvincent
 */
public class TestLevel extends Level {

    public TestLevel() {
        super(0, new Block[6][6]);

        setBlock(new ObstacleBlock(), 0, 0);
        setBlock(new PlayerBlock(), 0, 1);
        setBlock(new EmptyBlock(), 0, 2);
        setBlock(new PlayerBlock(), 0, 3);
        setBlock(new EmptyBlock(), 0, 4);
        setBlock(new ObstacleBlock(), 0, 5);

        setBlock(new ObstacleBlock(), 1, 5);
        setBlock(new ObstacleBlock(), 1, 0);
        setBlock(new PlayerBlock(), 1, 1);
        setBlock(new EmptyBlock(), 1, 2);
        setBlock(new PlayerBlock(), 1, 3);
        setBlock(new EmptyBlock(), 1, 4);
        
        setBlock(new ObstacleBlock(), 2, 0);
        setBlock(new PlayerBlock(), 2, 1);
        setBlock(new EmptyBlock(), 2, 2);
        setBlock(new PlayerBlock(), 2, 3);
        setBlock(new EmptyBlock(), 2, 4);
        setBlock(new ObstacleBlock(), 2, 5);
        
        setBlock(new ObstacleBlock(), 3, 0);
        setBlock(new PlayerBlock(), 3, 1);
        setBlock(new EmptyBlock(), 3, 2);
        setBlock(new PlayerBlock(), 3, 3);
        setBlock(new EmptyBlock(), 3, 4);
        setBlock(new ObstacleBlock(), 3, 5);
        
        setBlock(new ObstacleBlock(), 4, 0);
        setBlock(new PlayerBlock(), 4, 1);
        setBlock(new EmptyBlock(), 4, 2);
        setBlock(new PlayerBlock(), 4, 3);
        setBlock(new EmptyBlock(), 4, 4);
        setBlock(new ObstacleBlock(), 4, 5);
        
        setBlock(new ObstacleBlock(), 5, 0);
        setBlock(new PlayerBlock(), 5, 1);
        setBlock(new EmptyBlock(), 5, 2);
        setBlock(new PlayerBlock(), 5, 3);
        setBlock(new EmptyBlock(), 5, 4);
        setBlock(new ObstacleBlock(), 5, 5);
    }

}
