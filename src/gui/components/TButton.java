package gui.components;

import java.awt.Color;
import swing.components.AButton;
import swing.resourceManagers.FontEngine;

/**
 *
 * @author alexisvincent
 */
public class TButton extends AButton {

    public TButton(String name) {
        super(name);

        double random = Math.random();

        setBackdropNormal(new Color(0, 70 + (int) (random * 100), 100 + (int) (random * 100), 180));
        setBackdropMouseOver(new Color(0, 70 + (int) (random * 100), 100 + (int) (random * 100), 120));
        setBackdrop(getBackdropNormal());

        setFontNormal(getBackdropMouseOver().brighter().brighter());
        setFontMouseOver(new Color(255, 255, 255, 200));
        setFont(getFontNormal());
        
        setBackdropMouseOver(new Color(0, 100, 150, 120));

        this.setFont(FontEngine.getFont("Minecraftia Regular").deriveFont(24f));
    }

}
