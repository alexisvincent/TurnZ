package gui.panes;

import core.StateMachine;
import java.awt.Graphics;
import java.awt.Graphics2D;
import swing.components.AColor;
import swing.components.ASwopPane;
import swing.toolkit.UIToolkit;

/**
 *
 * @author alexisvincent
 */
public class ApplicationPane extends ASwopPane {

        public ApplicationPane() {
            super();
            setName("ApplicationPane");
            setBackground(AColor.DarkGrey);
            
            addSwopable(StateMachine.gamePane);
            addSwopable(StateMachine.welcomePane);
            addSwopable(StateMachine.levelSelectionPane);
            addSwopable(StateMachine.preferencePane);
            
            StateMachine.welcomePane.setVisible(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = UIToolkit.getPrettyGraphics(g);

            g2d.setPaint(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

    }