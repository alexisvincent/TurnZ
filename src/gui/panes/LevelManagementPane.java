package gui.panes;

import concurrency.ProcessingQueue;
import core.StateMachine;
import gui.components.TButton;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Logger;
import objects.levels.Level;
import objects.levels.LevelManipulator;
import swing.components.AComponent;
import swing.toolkit.UIToolkit;
import turnz.TurnZ;

/**
 *
 * @author alexisvincent
 */
public class LevelManagementPane extends AComponent {

    private ArrayList<Level> levels;

    private final TButton backButton;
    private final TButton randomButton;
    private final TButton newLevelButton;

    public LevelManagementPane() {
        super("LevelSelectionPane");

        levels = new ArrayList<>();

        setLayout(new GridBagLayout());

        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                refresh();
            }
        });

        backButton = new TButton("Back");
        backButton.setFont(backButton.getFont().deriveFont(24f));
        backButton.setPreferredSize(new Dimension(0, 50));
        backButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                StateMachine.welcomePane.setVisible(true);
            }
        });

        randomButton = new TButton("Rnd");
        randomButton.setFont(randomButton.getFont().deriveFont(48f));
        randomButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                //TODO
            }
        });

        newLevelButton = new TButton("+");
        newLevelButton.setFont(newLevelButton.getFont().deriveFont(48f));
        newLevelButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                //TODO
            }
        });

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
    }

    public void refresh() {
        levels = LevelManipulator.getAllLevels();
        int levelsPerLine = (int)((2.0/5.0) * (levels.size()+2));

        levels.sort(new Comparator<Level>() {

            @Override
            public int compare(Level o1, Level o2) {
                return ((Integer) o1.getID()).compareTo(o2.getID());
            }
        });

        final ArrayList<TButton> buttons = new ArrayList<>();

        if (!levels.isEmpty()) {
            for (final Level level : levels) {
                TButton levelButton = new TButton("" + level.getID());
                levelButton.setFont(levelButton.getFont().deriveFont(48f));
                levelButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        StateMachine.gamePane.setLevel(level);
                        StateMachine.gamePane.setVisible(true);
                    }
                });
                levelButton.setVisible(false);
                buttons.add(levelButton);
            }
        }

        randomButton.setVisible(false);
        newLevelButton.setVisible(false);

        removeAll();

        GridBagConstraints gc = UIToolkit.getDefaultGridBagConstraints();

        gc.insets = new Insets(0, 0, 2, 0);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weighty = 0;
        gc.gridwidth = levelsPerLine;
        this.add(backButton, gc);

        gc.insets = new Insets(0, 0, 0, 0);
        gc.gridwidth = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.gridy++;

        buttons.add(randomButton);
        buttons.add(newLevelButton);

        for (TButton tButton : buttons) {
            this.add(tButton, gc);
            gc.gridx++;

            if (gc.gridx == levelsPerLine) {
                gc.gridx = 0;
                gc.gridy++;
            }
        }

        TurnZ.getINSTANCE().getProcessingQueue().addJob(new ProcessingQueue.Job() {

            @Override
            public boolean doJob() {
                for (TButton tButton : buttons) {
                    tButton.setVisible(true);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(LevelManagementPane.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
                return true;
            }

            @Override
            public boolean mustBeRemoved() {
                return true;
            }
        });

    }

}
