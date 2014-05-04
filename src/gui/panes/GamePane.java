package gui.panes;

import core.StateMachine;
import gui.map.Map;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import objects.levels.Level;
import objects.levels.TestLevel;
import swing.components.AColor;
import swing.components.AComponent;
import swing.toolkit.UIToolkit;
import turnz.TurnZ;

/**
 *
 * @author alexisvincent
 */
public class GamePane extends AComponent {

    private Map map;
    private final LeftSideBar leftSideBar;
    private final RightSideBar rightSideBar;

    public GamePane() {
        super("GamePane");

        setLayout(new GridBagLayout());
        
        leftSideBar = new LeftSideBar();
        rightSideBar = new RightSideBar();
        setLevel(new TestLevel());

        final KeyAdapter controls = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        map.rotateAntiClockwise();
                        break;
                    case KeyEvent.VK_RIGHT:
                        map.rotateClockwise();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        StateMachine.levelSelectionPane.setVisible(true);
                        break;
                }
            }
        };

        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentHidden(ComponentEvent e) {
                TurnZ.getINSTANCE().getUI().getFrame().removeKeyListener(controls);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                TurnZ.getINSTANCE().getUI().getFrame().addKeyListener(controls);
            }
        });
        
    }

    public void setLevel(Level level) {
        map = new Map(level);
        buildPane();
    }
    
    private void buildPane() {
        
        this.removeAll();
        
        GridBagConstraints gc = UIToolkit.getDefaultGridBagConstraints();
        
        gc.weightx = 0;
        gc.fill = GridBagConstraints.VERTICAL;
        gc.insets = new Insets(10, 10, 10, 0);
        this.add(leftSideBar, gc);
        
        gc.weightx = 1;
        gc.gridx ++;
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(10, 10, 10, 10);
        this.add(map, gc);
        
        gc.weightx = 0;
        gc.gridx ++;
        gc.fill = GridBagConstraints.VERTICAL;
        gc.insets = new Insets(10, 0, 10, 10);
        this.add(rightSideBar, gc);
    }

    @Override
    public void doLayout() {
        
        int width = 0;
        
        if (getWidth() > getHeight()) {
            width = (getWidth()-getHeight()-20)/2;
        } else {
            width = (getHeight()-getWidth()-20)/2;
        }
        
        leftSideBar.setPreferredSize(new Dimension(width, 0));
        rightSideBar.setPreferredSize(new Dimension(width, 0));
        
        super.doLayout();
    }
    
    private class RightSideBar extends AComponent {

        public RightSideBar() {
            super("GamePane.RightSideBar");
            this.setPreferredSize(new Dimension(300, 0));
            
            setBackground(AColor.DarkGreen);
        }

//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2d = UIToolkit.getPrettyGraphics(g);
//            
//            g2d.setPaint(getBackground());
//            g2d.fillRect(0, 0, getWidth(), getHeight());
//        }
    }
    
    private class LeftSideBar extends AComponent {

        public LeftSideBar() {
            super("GamePane.LeftSideBar");
            this.setPreferredSize(new Dimension(300, 0));
            
            setBackground(AColor.DarkGreen);
        }

//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2d = UIToolkit.getPrettyGraphics(g);
//            
//            g2d.setPaint(getBackground());
//            g2d.fillRect(0, 0, getWidth(), getHeight());
//        }
    }
}
