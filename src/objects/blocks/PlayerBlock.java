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
public class PlayerBlock extends Block {

    boolean invisible = false;

    public PlayerBlock() {
        super(6);
        setType("PlayerBlock");
    }

    @Override
    public BlockAction doAction() {
        return BlockAction.Stop;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = UIToolkit.getPrettyGraphics(g);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (!invisible) {
            g2d.setPaint(AColor.RED);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        }

    }

    public void setInvisible() {
        invisible = true;
    }

    public boolean isInvisible() {
        return invisible;
    }

    @Override
    public char getChar() {
        return 'P';
    }

}
