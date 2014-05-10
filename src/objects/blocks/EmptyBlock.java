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
public class EmptyBlock extends Block {

    public EmptyBlock() {
        super(1);
        setType("EmptyBlock");
    }

    @Override
    public BlockAction doAction() {
        return BlockAction.PassThrough;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = UIToolkit.getPrettyGraphics(g);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setPaint(AColor.DarkBlue);
        //g2d.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
    }

    @Override
    public char getChar() {
        return ' ';
    }

}
