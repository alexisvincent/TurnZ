package objects.blocks;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import swing.components.AColor;
import swing.toolkit.UIToolkit;

/**
 *
 * @author alexisvincent
 */
public class BreakableBlock extends Block {

    protected int lives = 2;

    public BreakableBlock() {
        super(3);
        setType("BreakableBlock");
    }

    @Override
    public BlockAction doAction() {
        
        if (lives > 0) {
            lives--;
        }
        
        if (lives > 0) {
            return BlockAction.Stop;
        } else {
            return BlockAction.PassThrough;
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = UIToolkit.getPrettyGraphics(g);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        if (lives == 2) {
            g2d.setPaint(AColor.DarkBlue);
        } else if (lives == 1) {
            g2d.setPaint(AColor.YELLOW);
        } else {
            g2d.setPaint(AColor.DarkGrey);
        }

        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
    }

    @Override
    public char getChar() {
        return 'B';
    }

}
