package gui.map;

import animationEngine.MotionFactory;
import animationEngine.Segment;
import animationEngine.SegmentGroup;
import animationEngine.segments.AtomicFloatSegment;
import animationEngine.segments.AtomicIntegerSegment;
import animationEngine.segments.HorizontalMotionSegment;
import animationEngine.segments.VerticalMotionSegment;
import concurrency.ProcessingQueue;
import core.Config;
import core.StateMachine;
import dataTypes.AtomicFloat;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import objects.blocks.Block;
import objects.blocks.Block.BlockAction;
import objects.blocks.EmptyBlock;
import objects.blocks.PlayerBlock;
import objects.levels.InvalidLevelFileException;
import objects.levels.Level;
import objects.levels.LevelManipulator;
import swing.components.AColor;
import swing.components.AComponent;
import swing.components.ALayeredPane;
import swing.toolkit.UIToolkit;
import turnz.TurnZ;

/**
 *
 * @author alexisvincent
 */
public class Map extends AComponent {

    private Level level;
    private ArrayList<PlayerBlock> playerBlocks;

    private LayeredMap layeredMap;

    private AtomicInteger rotation;
    private AtomicFloat scale;
    private boolean rotationAnimation;
    private boolean fallAnimation;

    private int playerWin = 0;

    private Gravity gravity;

    private int lastRotationValue;

    private enum Direction {

        Clockwise, AntiClockwise
    }

    private enum Gravity {

        North, South, East, West
    }

    public Map(Level level) {
        super();
        init(level);

        this.add(layeredMap);
        enableFallAnimation(true);
        enableRotationAnimation(true);
    }

    private void init(Level level) {
        this.playerBlocks = new ArrayList<>();
        this.level = level;
        gravity = Gravity.South;

        harvestPlayers();

        playerWin = playerBlocks.size();

        this.layeredMap = new LayeredMap();

        rotation = new AtomicInteger(0);
        scale = new AtomicFloat(1);
    }

    public void doLayout() {

        int width = getWidth();
        int height = getHeight();
        int centeredX = 0;
        int centeredY = 0;

        if (getLevel().getWidth() > getLevel().getHeight()) {
            height = (int) ((1.0 * getLevel().getHeight() / (1.0 * getLevel().getWidth())) * getWidth());
            centeredY = (getHeight() - height) / 2;
        } else {
            width = (int) ((1.0 * getLevel().getWidth() / (1.0 * getLevel().getHeight())) * getWidth());
            centeredX = (getWidth() - width) / 2;
        }

        layeredMap.setBounds(centeredX, centeredY, width, height);

    }

    public Level getLevel() {
        return level;
    }

    private ArrayList<PlayerBlock> harvestPlayers() {
        playerBlocks.clear();
        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                if (level.getBlock(x, y) instanceof PlayerBlock) {
                    playerBlocks.add((PlayerBlock) level.getBlock(x, y));
                    level.setBlock(new EmptyBlock(), x, y);
                }
            }
        }
        return playerBlocks;
    }

    public void rotateClockwise() {
        rotate(Direction.Clockwise);
    }

    public void setRotation(int degrees) {
        this.rotation.set(degrees);
    }

    public void setScale(double scale) {
        this.scale.set((float) scale);
    }

    public int getRotation() {
        return rotation.get();
    }

    public double getScale() {
        return scale.get();
    }

    public boolean isRotationAnimationEnabled() {
        return rotationAnimation;
    }

    public void enableRotationAnimation(boolean rotationAnimation) {
        this.rotationAnimation = rotationAnimation;
    }

    public boolean isFallAnimationEnabled() {
        return fallAnimation;
    }

    public void enableFallAnimation(boolean fallAnimation) {
        this.fallAnimation = fallAnimation;
    }

    private synchronized void rotate(Direction direction) {

        setGravity(direction);

        ArrayList<Segment> playerSegments = new ArrayList<>();
        for (PlayerBlock playerBlock : playerBlocks) {
            Segment playerSegment = playerFall(playerBlock);
            if (playerSegment != null) {
                playerSegments.add(playerSegment);
            }
        }

        if (isVisible()) {

            final SegmentGroup rotationGroup = new SegmentGroup();
            rotationGroup.setTag("RotationGroup");

            if (isRotationAnimationEnabled()) {
                for (Segment segment : rotateMap(direction)) {
                    segment.setTag("MapRotation");
                    rotationGroup.addSegment(segment, SegmentGroup.Position.WITH_FIRST);
                }
            }

            for (Segment segment : playerSegments) {
                rotationGroup.addSegment(segment, SegmentGroup.Position.WITH_FIRST, "MapRotation");
            }

            printBoard();

            getSegmentGroup().addSegment(rotationGroup, SegmentGroup.Position.AFTER_LAST_TAG, "RotationGroup");

            TurnZ.getINSTANCE().getProcessingQueue().addJob(new ProcessingQueue.Job() {

                @Override
                public boolean doJob() {
                    getSegmentGroup().compile();
                    //TurnZ.getINSTANCE().getUI().repaint(rotationGroup.getDuration());
                    return true;
                }

                @Override
                public boolean mustBeRemoved() {
                    return true;
                }
            });

            if (playerWin == 0) {
                JOptionPane.showMessageDialog(null, "You Win!");
            StateMachine.levelSelectionPane.setVisible(true);
            }
            
            for (int i = 0; i < playerBlocks.size(); i++) {
                if (playerBlocks.get(i).isInvisible()) {
                    playerBlocks.remove(playerBlocks.get(i));
                    i--;
                }
            }

        }
    }
    
    public void saveLevel() {
        Level save = new Level(getLevel().getID(), new Block[getLevel().getWidth()][getLevel().getHeight()]);
        
        for (int y = 0; y < getLevel().getHeight(); y++) {
            for (int x = 0; x < getLevel().getWidth(); x++) {
                save.setBlock(getLevel().getBlock(x, y), x, y);
                
                for (PlayerBlock playerBlock : playerBlocks) {
                    if (playerBlock.getArrayPosition().x == x && playerBlock.getArrayPosition().y == y) {
                        save.setBlock(playerBlock, x, y);
                    }
                }
            }
        }
        
        getLevel().setSavedStates(save);
        
        try {
            LevelManipulator.outputLevel(level, LevelManipulator.getSaveLevelPath(getLevel().getID()+""));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Map.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InvalidLevelFileException ex) {
            Logger.getLogger(Map.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void printBoard() {

        for (int i = 0; i < getLevel().getWidth() * 2 + 3; i++) {
            System.out.print("-");
        }

        System.out.println();

        for (int y = 0; y < getLevel().getHeight(); y++) {
            System.out.print("| ");
            for (int x = 0; x < getLevel().getWidth(); x++) {

                char blockChar = getLevel().getBlock(x, y).getChar();

                for (PlayerBlock playerBlock : playerBlocks) {
                    if (playerBlock.getArrayPosition().x == x && playerBlock.getArrayPosition().y == y) {
                        blockChar = playerBlock.getChar();
                    }
                }

                System.out.print(blockChar + " ");
            }
            System.out.print("|\n");
        }

        for (int i = 0; i < getLevel().getWidth() * 2 + 3; i++) {
            System.out.print("-");
        }
        System.out.println("\n");

    }

    private Segment playerFall(PlayerBlock playerBlock) {

        Block nextBlock = getNextBlock(playerBlock);

        BlockAction action = null;

        if (nextBlock != null) {
            action = nextBlock.doAction();
        }

        //if we must stay where we are...
        if (nextBlock == null || action == BlockAction.Stop) {
            Segment segment;
            Block currentBlockHover = getLevel().getBlock(playerBlock.getArrayPosition().x, playerBlock.getArrayPosition().y);

            if (currentBlockHover.getLocation().x != playerBlock.getLocation().x) {
                segment = new HorizontalMotionSegment(MotionFactory.getExponential(1), 0, Config.timeToFrames(0.3), playerBlock.getLocation().x, currentBlockHover.getLocation().x, playerBlock);
            } else {
                segment = new VerticalMotionSegment(MotionFactory.getExponential(1), 0, Config.timeToFrames(0.3), playerBlock.getLocation().y, currentBlockHover.getLocation().y, playerBlock);
            }

            return segment;
        } else {
            if (action == BlockAction.PassThrough) {
                playerBlock.setArrayPosition(nextBlock.getArrayPosition());
                Segment segment = playerFall(playerBlock);
                segment.setDuration(segment.getDuration() + Config.timeToFrames(0.3));
                return segment;
                
            } else if (action == BlockAction.Finish) {
                playerBlock.setArrayPosition(nextBlock.getArrayPosition());
                Segment segment = playerFall(playerBlock);
                segment.setDuration(segment.getDuration() + Config.timeToFrames(0.3));

                playerWin--;
                playerBlock.setInvisible();
                
                return segment;
            } else {
                return null;
            }
        }
    }

    private Block getNextBlock(PlayerBlock playerBlock) {
        int xShift = 0;
        int yShift = 0;

        switch (getGravity()) {
            case North:
                yShift = -1;
                break;
            case South:
                yShift = 1;
                break;
            case East:
                xShift = 1;
                break;
            case West:
                xShift = -1;
                break;
        }

        Point nextBlockPosition = new Point(playerBlock.getArrayPosition().x + xShift, playerBlock.getArrayPosition().y + yShift);

        if (nextBlockPosition.x < 0
                || nextBlockPosition.y < 0
                || nextBlockPosition.x >= getLevel().getWidth()
                || nextBlockPosition.y >= getLevel().getHeight()) {
            return null;
        } else {
            return getLevel().getBlock(nextBlockPosition.x, nextBlockPosition.y);
        }
    }

    private Gravity getGravity() {
        return gravity;
    }

    private void setGravity(Gravity gravity) {
        this.gravity = gravity;
    }

    private void setGravity(Direction rotationDirection) {

        switch (getGravity()) {
            case North:
                if (rotationDirection == Direction.Clockwise) {
                    setGravity(Gravity.West);
                } else {
                    setGravity(Gravity.East);
                }
                break;
            case South:
                if (rotationDirection == Direction.Clockwise) {
                    setGravity(Gravity.East);
                } else {
                    setGravity(Gravity.West);
                }
                break;
            case East:
                if (rotationDirection == Direction.Clockwise) {
                    setGravity(Gravity.North);
                } else {
                    setGravity(Gravity.South);
                }
                break;
            case West:
                if (rotationDirection == Direction.Clockwise) {
                    setGravity(Gravity.South);
                } else {
                    setGravity(Gravity.North);
                }
                break;
        }
    }

    private ArrayList<Segment> rotateMap(Direction direction) {
        int coef = 1;

        if (direction == Direction.AntiClockwise) {
            coef = -1;
        }

        ArrayList<Segment> rotationSegements = new ArrayList<>();

        rotationSegements.add(new AtomicIntegerSegment(MotionFactory.getExponential(1), 0, Config.timeToFrames(0.2), lastRotationValue, lastRotationValue + coef * 90, rotation));
        rotationSegements.add(new AtomicFloatSegment(MotionFactory.getExponential(1), 0, Config.timeToFrames(0.1), 1, (float) StateMachine.rotationScale, scale));
        rotationSegements.add(new AtomicFloatSegment(MotionFactory.getExponential(1), Config.timeToFrames(0.1), Config.timeToFrames(0.1), (float) StateMachine.rotationScale, 1, scale));

        lastRotationValue = lastRotationValue + coef * 90;

        return rotationSegements;
    }

    @Override
    public void doAnimation() {
        super.doAnimation();
    }

    public void rotateAntiClockwise() {
        rotate(Direction.AntiClockwise);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = UIToolkit.getPrettyGraphics(g);

        g2d.translate(getWidth() / 2 - scale.get() * getWidth() / 2, getHeight() / 2 - scale.get() * getHeight() / 2);
        g2d.scale(scale.get(), scale.get());
        g2d.rotate(Math.toRadians(rotation.get()), this.getWidth() / 2, this.getHeight() / 2);

        super.paint(g2d);
    }

    private class LayeredMap extends ALayeredPane {

        BlockLayer blockLayer;
        PlayerLayer playerLayer;

        public LayeredMap() {
            super();

            blockLayer = new BlockLayer();
            playerLayer = new PlayerLayer();

            this.add(blockLayer, ALayeredPane.DEFAULT_LAYER);
            this.add(playerLayer, ALayeredPane.PALETTE_LAYER);
        }

        private class BlockLayer extends AComponent {

            public BlockLayer() {
                super("MapLayer");
                setBackground(AColor.DarkGreen);
                setLayout(new GridBagLayout());
                buildBlockLayer();
            }

            private void buildBlockLayer() {
                GridBagConstraints gc = UIToolkit.getDefaultGridBagConstraints();

                gc.insets = new Insets(3, 3, 3, 3);

                for (int y = 0; y < getLevel().getHeight(); y++) {
                    for (int x = 0; x < getLevel().getWidth(); x++) {
                        this.add(getLevel().getBlock(x, y), gc);
                        gc.gridx++;
                    }
                    gc.gridx = 0;
                    gc.gridy++;
                }
            }

        }

        private class PlayerLayer extends AComponent {

            public PlayerLayer() {
                super("Map.PlayerLayer");

                this.setVisible(true);

                setLayout(null);

                for (PlayerBlock playerBlock : playerBlocks) {
                    this.add(playerBlock);
                }
            }

            @Override
            public void doLayout() {

                blockLayer.doLayout();

                for (PlayerBlock playerBlock : playerBlocks) {
                    playerBlock.setSize(level.getBlock(0, 0).getWidth(), level.getBlock(0, 0).getHeight());

                    location:
                    {
                        for (int y = 0; y < getLevel().getHeight(); y++) {
                            for (int x = 0; x < getLevel().getWidth(); x++) {
                                Point blockArrayPosition = getLevel().getBlock(x, y).getArrayPosition();
                                if (blockArrayPosition.x == playerBlock.getArrayPosition().x && blockArrayPosition.y == playerBlock.getArrayPosition().y) {
                                    playerBlock.setLocation(getLevel().getBlock(x, y).getLocation());
                                    break location;
                                }
                            }
                        }
                    }

                }

            }

        }

    }

}
