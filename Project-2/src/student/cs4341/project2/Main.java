package student.cs4341.project2;

import student.cs4341.project2.fileinterface.FileInterface;
import student.cs4341.project2.fileinterface.FileInterfaceImpl;
import student.cs4341.project2.game.Game;

public class Main {
    public static void main(final String args[]) {
        final FileInterface fileInterface = FileInterfaceImpl.newInstance(Utilities.identityMapper());
        final Game game = Game.newInstance();

        fileInterface.startPlayingWith(game);
    }
}
