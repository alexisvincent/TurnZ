package objects.levels;

import io.files.Secretary;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
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
public class LevelLoader {

    public static Level getLevel(String levelName) throws FileNotFoundException, InvalidLevelFileException {
        Path levelFile = Secretary.getSubFile(Secretary.getSubFile(Secretary.getWorkingDirectory(), "Levels"), levelName);
        return parseLevelFile(levelFile);
    }

    private static Level parseLevelFile(Path levelFile) throws InvalidLevelFileException {

        Level level;

        try {
            Scanner fileScanner = new Scanner(levelFile);

            Scanner configScanner = new Scanner(fileScanner.nextLine());

            String filename = levelFile.getFileName().toString();
            filename = filename.substring(0, filename.length() - 6);

            level = new Level(Integer.parseInt(filename), new Block[configScanner.nextInt()][configScanner.nextInt()]);

            for (int i = 0; i < level.getHeight(); i++) {

                Scanner lineScanner = new Scanner(fileScanner.nextLine());

                for (int j = 0; j < level.getWidth(); j++) {
                    int blockID = lineScanner.nextInt();

                    switch (blockID) {
                        case 1:
                            level.setBlock(new EmptyBlock(), i, j);
                            break;
                        case 2:
                            level.setBlock(new ObstacleBlock(), i, j);
                            break;
                        case 3:
                            level.setBlock(new BreakableBlock(), i, j);
                            break;
                        case 4:
                            level.setBlock(new PartiallyBrokenBlock(), i, j);
                            break;
                        case 5:
                            level.setBlock(new GoalBlock(), i, j);
                            break;
                        case 6:
                            level.setBlock(new PlayerBlock(), i, j);
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
