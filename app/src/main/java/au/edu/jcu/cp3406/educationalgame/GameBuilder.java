package au.edu.jcu.cp3406.educationalgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBuilder {
    List<Integer> sequence;
    Random r;
    boolean complete;

    public GameBuilder() {
        sequence = new ArrayList<>();
        r = new Random();
        complete = true;
    }

    public List<Integer> newGame(Difficulty level) {
        switch (level) {
            case EASY:
                createSequence(2);
                break;
            case MEDIUM:
                createSequence(3);
                break;
            case HARD:
                createSequence(4);
                break;
            case MASTER:
                createSequence(6);
                break;
        }
        return sequence;
    }

    public void createSequence(int numTiles) {
        for (int i = 0; i < 10; i++) {
            sequence.add(r.nextInt(numTiles));
        }
    }

    public void sequenceComplete() {
        complete = true;
    }

    public boolean sequenceRunning() {
        return complete;
    }

    public boolean checkOrder(Tile tile) {
        //if (tile)
        System.out.println(sequence.get(0) +"=="+ tile.getImg()+"CHECKING------------------------------------------------------------");
        return true;
    }
}
