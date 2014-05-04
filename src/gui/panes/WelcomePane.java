package gui.panes;

import core.StateMachine;
import gui.components.TButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import swing.components.AColor;
import swing.components.AComponent;
import swing.toolkit.UIToolkit;
import turnz.TurnZ;

/**
 *
 * @author alexisvincent
 */
public class WelcomePane extends AComponent {

    private TButton playButton;
    private TButton statsButton;
    private TButton quitButton;
    private TButton preferencesButton;

    private TButton[] buttons;

    public WelcomePane() {
        super("WelcomePane");
        setBackground(AColor.DarkGrey);
        setLayout(new GridBagLayout());

        init();
        
        
        final KeyAdapter controls = new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        TurnZ.getINSTANCE().quit();
                        break;
                }
            }
        };
        
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                randomise();
                TurnZ.getINSTANCE().getUI().getFrame().addKeyListener(controls);
            }
            @Override
            public void componentHidden(ComponentEvent e) {
                randomise();
                TurnZ.getINSTANCE().getUI().getFrame().removeKeyListener(controls);
            }
        });
    }

    private void init() {

        buttons = new TButton[4];

        playButton = new TButton("Play");
        statsButton = new TButton("Stats");
        quitButton = new TButton("Quit");
        preferencesButton = new TButton("Settings");

        playButton.setFont(playButton.getFont().deriveFont(48f));
        statsButton.setFont(statsButton.getFont().deriveFont(48f));
        quitButton.setFont(statsButton.getFont().deriveFont(48f));
        preferencesButton.setFont(preferencesButton.getFont().deriveFont(48f));

        playButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                StateMachine.levelSelectionPane.setVisible(true);
            }
        });
        
        preferencesButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                StateMachine.preferencePane.setVisible(true);
            }
        });

        quitButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                TurnZ.getINSTANCE().quit();
            }
        });

        buttons[0] = playButton;
        buttons[1] = statsButton;
        buttons[2] = quitButton;
        buttons[3] = preferencesButton;
    }
    
    private void randomise() {
        for (int i = 0; i < buttons.length; i++) {
            double random1 = Math.random();
            double random2 = Math.random();
            TButton temp = buttons[i];
            buttons[i] = buttons[(int) (buttons.length * random1)];
            buttons[(int) (buttons.length * random1)] = temp;
        }
        
        this.removeAll();
        
        GridBagConstraints gc = UIToolkit.getDefaultGridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        
        for (int i = 0; i < buttons.length; i++) {
            if (i == 2) {
                gc.gridx = 0;
                gc.gridy++;
            }
            this.add(buttons[i], gc);
            gc.gridx++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = UIToolkit.getPrettyGraphics(g);

        g2d.setPaint(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

}
