package turnz;

import applicationFramework.core.Application;
import applicationFramework.ui.GUI;
import core.Config;
import core.StateMachine;
import java.awt.Dimension;
import javax.swing.SwingUtilities;
import swing.components.ALayeredPane;

/**
 *
 * @author alexisvincent
 */
public class TurnZ extends Application {

    private TurnZ() {
        super();
    }

    public static Application getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new TurnZ();
        }
        return INSTANCE;
    }

    @Override
    protected void initGUI() {
        //Init Gui
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                gui = new GUI("TurnZ").setDimensions(new Dimension(900, 600)).setGoodDefaults();
                gui.getFrame().getLayeredPane().add(StateMachine.applicationPane,  ALayeredPane.DEFAULT_LAYER);
                //gui.setFullScreen(true);
                gui.show();
                //gui.enableDoubleBuffering(true);
                gui.startPainting(StateMachine.frameRate);
            }
        });
    }

    public static void main(String[] args) {
        Config.setup();
        getINSTANCE();
    }
}
