package gui.map;

import animationEngine.MotionFactory;
import animationEngine.Segment;
import animationEngine.TimeLine;
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
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import objects.blocks.Block;
import objects.blocks.Block.BlockAction;
import objects.blocks.EmptyBlock;
import objects.blocks.PlayerBlock;
import objects.levels.Level;
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

        //ADD Components
        this.add(layeredMap);
        //firstFall();
    }
    
    private void firstFall() {
        ArrayList<Segment> playerSegments = new ArrayList<>();
        for (PlayerBlock playerBlock : playerBlocks) {
            Segment playerSegment = playerFall(playerBlock);
            if (playerSegment != null) {
                playerSegments.add(playerSegment);
            }
        }

        if (isVisible()) {

            final TimeLine timeLine = new TimeLine();

            for (Segment segment : playerSegments) {
                timeLine.addSegment(segment);
            }

            this.addTimeline(timeLine);
            TurnZ.getINSTANCE().getProcessingQueue().addJob(new ProcessingQueue.Job() {

                @Override
                public boolean doJob() {
                    timeLine.compile();
                    return true;
                }

                @Override
                public boolean mustBeRemoved() {
                    return true;
                }
            });
        }
    }

    private void init(Level level) {
        this.playerBlocks = new ArrayList<>();
        this.level = level;
        gravity = Gravity.South;

        harvestPlayers();

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
        for (int i = 0; i < level.getHeight(); i++) {
            for (int j = 0; j < level.getWidth(); j++) {
                if (level.getLevel()[i][j] instanceof PlayerBlock) {
                    playerBlocks.add((PlayerBlock) level.getLevel()[i][j]);
                    level.setBlock(new EmptyBlock(), i, j);
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

    private synchronized void rotate(Direction direction) {

//        setGravity(direction);
//
//        ArrayList<Segment> playerSegments = new ArrayList<>();
//        for (PlayerBlock playerBlock : playerBlocks) {
//            Segment playerSegment = playerFall(playerBlock);
//            if (playerSegment != null) {
//                playerSegments.add(playerSegment);
//            }
//        }

        if (isVisible()) {

            final TimeLine timeLine = new TimeLine();
//
//            for (Segment segment : playerSegments) {
//                timeLine.addSegment(segment);
//            }
//
            for (Segment segment : rotateMap(direction)) {
                timeLine.addSegment(segment);
            }

            this.addTimeline(timeLine);
            
            TurnZ.getINSTANCE().getProcessingQueue().addJob(new ProcessingQueue.Job() {

                @Override
                public boolean doJob() {
                    timeLine.compile();
                    return true;
                }

                @Override
                public boolean mustBeRemoved() {
                    return true;
                }
            });
        }
    }

    private Segment playerFall(PlayerBlock playerBlock) {

        Block nextBlock = getNextBlock(playerBlock);

        BlockAction action = null;

        if (nextBlock != null) {
            action = nextBlock.doAction();
            System.out.println(nextBlock.getArrayPosition());
        }

        //if we must stay where we are...
        if (nextBlock == null || action == BlockAction.Stop) {
            System.out.println("stop");
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
                segment.setTotalFrames(segment.getTotalFrames()+Config.timeToFrames(0.3));
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
                yShift = -11;
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

//        @Override
//        protected void paintComponent(Graphics g) {
//            Graphics2D g2d = UIToolkit.getPrettyGraphics(g);
//            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//            g2d.setPaint(AColor.DarkGrey.darker());
//            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
//        }
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

                for (int i = 0; i < getLevel().getHeight(); i++) {
                    for (int j = 0; j < getLevel().getWidth(); j++) {
                        this.add(getLevel().getBlock(i, j), gc);
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
                        for (int i = 0; i < getLevel().getHeight(); i++) {
                            for (int j = 0; j < getLevel().getWidth(); j++) {
                                Point blockArrayPosition = getLevel().getBlock(i, j).getArrayPosition();
                                if (blockArrayPosition.x == playerBlock.getArrayPosition().x && blockArrayPosition.y == playerBlock.getArrayPosition().y) {
                                    playerBlock.setLocation(getLevel().getBlock(i, j).getLocation());
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
