package objects.levels;

import io.files.Secretary;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import objects.blocks.Block;
import objects.blocks.BreakableBlock;
import objects.blocks.EmptyBlock;
import objects.blocks.GoalBlock;
import objects.blocks.ObstacleBlock;
import objects.blocks.PartiallyBrokenBlock;
import objects.blocks.PlayerBlock;

/**
 *
 * @author alexisvincent
 */
public class LevelManipulator {

    public static Level getLevel(String levelName) throws FileNotFoundException, InvalidLevelFileException {
        Path levelFile = Secretary.getSubFile(Secretary.getSubFile(Secretary.getWorkingDirectory(), "Levels"), levelName);
        Level level = parseLevelFile(levelFile);
        
        try {
            Level save = getLevelSave(levelName);
            level.setSavedStates(save);
        } catch (FileNotFoundException | InvalidLevelFileException ex) {
            System.out.println("No Level Save for this file");
        }

        return level;
    }
    
    public static Path getSaveLevelPath(String levelName) throws FileNotFoundException, InvalidLevelFileException {
        Path levelFile = Secretary.getSubFile(Secretary.getSubFile(Secretary.getWorkingDirectory(), "Levels"), levelName + ".level.save");
        return levelFile;
    }

    private static Level parseLevelFile(Path levelFile) throws InvalidLevelFileException {

        Level level;

        try {
            Scanner fileScanner = new Scanner(levelFile);

            Scanner configScanner = new Scanner(fileScanner.nextLine());

            String filename = levelFile.getFileName().toString();
            filename = filename.substring(0, filename.length() - 6);

            level = new Level(Integer.parseInt(filename), new Block[configScanner.nextInt()][configScanner.nextInt()]);

            for (int y = 0; y < level.getHeight(); y++) {

                Scanner lineScanner = new Scanner(fileScanner.nextLine());

                for (int x = 0; x < level.getWidth(); x++) {
                    int blockID = lineScanner.nextInt();

                    switch (blockID) {
                        case 1:
                            level.setBlock(new EmptyBlock(), x, y);
                            break;
                        case 2:
                            level.setBlock(new ObstacleBlock(), x, y);
                            break;
                        case 3:
                            level.setBlock(new BreakableBlock(), x, y);
                            break;
                        case 4:
                            level.setBlock(new PartiallyBrokenBlock(), x, y);
                            break;
                        case 5:
                            level.setBlock(new GoalBlock(), x, y);
                            break;
                        case 6:
                            level.setBlock(new PlayerBlock(), x, y);
                            break;
                        default:
                            throw new InvalidLevelFileException();
                    }
                }
            }
        } catch (Exception e) {
            throw new InvalidLevelFileException();
        }

        return level;
    }
    
    public static void outputLevel(Level level, Path levelPath) {
        if (!levelPath.toFile().exists()) {
            Secretary.touch(levelPath);
        }

        PrintWriter printWriter = null;

        try {
            printWriter = Secretary.getFileInputStream(levelPath, false);
        } catch (IOException ex) {
            Logger.getLogger(LevelManipulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        printWriter.println(level.getWidth() + " " + level.getHeight());

        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                printWriter.print(level.getBlock(x, y).getId() + " ");
            }
            printWriter.println();
        }
    }

    public static Level getLevelSave(String levelName) throws FileNotFoundException, InvalidLevelFileException {
        Path levelFile = Secretary.getSubFile(Secretary.getSubFile(Secretary.getWorkingDirectory(), "Levels"), levelName + ".save");
        return parseLevelFile(levelFile);
    }

    public static ArrayList<Level> getAllLevels() {

        ArrayList<Level> levels = new ArrayList<>();

        ArrayList<Path> levelFiles;
        try {
            levelFiles = Secretary.getDirectoryFiles(Secretary.getSubFile(Secretary.getWorkingDirectory(), "Levels").toAbsolutePath());

            for (Path path : levelFiles) {

                if (path.toString().endsWith(".level")) {
                    try {
                        levels.add(getLevel(path.getFileName().toString()));
                    } catch (FileNotFoundException | InvalidLevelFileException ex) {
                        System.out.println("Bad Level File Found");
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("No Level Directory Found");
        }

        return levels;

    }

}
