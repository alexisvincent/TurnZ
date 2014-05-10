package objects.blocks;

/**
 *
 * @author alexisvincent
 */
public class PartiallyBrokenBlock extends BreakableBlock {

    public PartiallyBrokenBlock() {
        super();
        setType("PartiallyBrokenBlock");
        setId(4);
        lives = 1;
    }

}
