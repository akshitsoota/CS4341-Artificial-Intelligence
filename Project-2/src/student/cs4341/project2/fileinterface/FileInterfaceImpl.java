package student.cs4341.project2.fileinterface;

import student.cs4341.project2.Function;
import student.cs4341.project2.Pair;
import student.cs4341.project2.game.Game;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileInterfaceImpl implements FileInterface {
    private static final String GROUP_NAME = "fdsa"; // TODO: Come up with a creative name

    private static final String FILE_MOVE = "move_file";
    private static final String FILE_GROUP_NAME = GROUP_NAME + ".go";
    private static final String FILE_END_GAME = "end_game";

    private static final int REFEREE_POLL_PERIOD = 120; // ms

    private final Function<String, String> pathTransformer;

    private FileInterfaceImpl(final Function<String, String> pathTransformer) {
        this.pathTransformer = pathTransformer;
    }

    @Override
    public void startPlayingWith(final Game game) {
        final Path moveFilePath = Paths.get(pathTransformer.apply(FileInterfaceImpl.FILE_MOVE));
        final Path groupFilePath = Paths.get(pathTransformer.apply(FileInterfaceImpl.FILE_GROUP_NAME));
        final Path endGamePath = Paths.get(pathTransformer.apply(FileInterfaceImpl.FILE_END_GAME));

        while (true) {
            // Wait for our move file
            FileUtilities.waitForFileInFS(groupFilePath);

            // See if the game ended
            if (FileUtilities.fileExists(endGamePath)) {
                break;
            }

            // Read the last move played
            final String moveString = FileUtilities.readFile(moveFilePath);
            final Pair<String, Integer> playedMoved;

            final String[] splitted = moveString.split(" ");
            if (splitted.length != 3) {
                playedMoved = game.playFirstMove();
            } else {
            	for(int i = 0; i < splitted.length; i++) {
            		System.out.println(splitted[i]);
            	}
                playedMoved = game.playWithOpponentMove(new Pair<>(splitted[1], Integer.valueOf(splitted[2].replaceAll("\r\n", ""))));
            }

            // Write this move into the move file
            final String newMoveString = new StringBuilder()
                    .append(GROUP_NAME).append(" ")
                    .append(playedMoved.first).append(" ")
                    .append(playedMoved.second)
                    .toString();

            FileUtilities.writeToFile(moveFilePath, newMoveString);

            // Sleep so that the referee picks up the move
            try {
                Thread.sleep(FileInterfaceImpl.REFEREE_POLL_PERIOD);
            } catch (final InterruptedException ignored) {
                // Hmm.
            }
        }
    }

    public static FileInterface newInstance(final Function<String, String> pathTransformer) {
        if (pathTransformer == null) {
            return null;
        }

        System.out.println("Initialized game for " + GROUP_NAME);
        return new FileInterfaceImpl(pathTransformer);
    }
}
