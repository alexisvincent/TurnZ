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
import swing.components.AColor;
import swing.components.AComponent;
import swing.toolkit.UIToolkit;
import turnz.TurnZ;

/**
 *
 * @author alexisvincent
 */
public class PreferencePane extends AComponent {

    private TButton frameRateButton;
    private TButton rotationScaleButton;

    private TButton[] buttons;

    public PreferencePane() {
        super("PreferencePane");
        setBackground(AColor.DarkGrey);
        setLayout(new GridBagLayout());

        init();

        final KeyAdapter controls = new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        StateMachine.welcomePane.setVisible(true);
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

        randomise();
    }

    private void init() {

        buttons = new TButton[2];

        frameRateButton = new TButton("Frame Rate: " + StateMachine.frameRate);
        frameRateButton.setFont(frameRateButton.getFont().deriveFont(24f));

        frameRateButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                switch (StateMachine.frameRate) {
                    case 15:
                        StateMachine.frameRate = 30;
                        break;
                    case 30:
                        StateMachine.frameRate = 60;
                        break;
                    case 60:
                        StateMachine.frameRate = 120;
                        break;
                    case 120:
                        StateMachine.frameRate = 15;
                        break;
                }

                frameRateButton.setName("Frame Rate: " + StateMachine.frameRate);
                TurnZ.getINSTANCE().getUI().stopPainting();
                TurnZ.getINSTANCE().getUI().startPainting(StateMachine.frameRate);
            }
        });

        rotationScaleButton = new TButton("Rotation Scale: " + StateMachine.rotationScale);
        rotationScaleButton.setFont(frameRateButton.getFont().deriveFont(24f));

        rotationScaleButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                switch ((int)(StateMachine.rotationScale*10)) {
                    case 6:
                        StateMachine.rotationScale = 0.8;
                        break;
                    case 8:
                        StateMachine.rotationScale = 1;
                        break;
                    case 10:
                        StateMachine.rotationScale = 0.6;
                        break;
                }

                rotationScaleButton.setName("Rotation Scale: " + StateMachine.rotationScale);
            }
        });

        buttons[0] = frameRateButton;
        buttons[1] = rotationScaleButton;
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
