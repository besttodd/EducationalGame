package au.edu.jcu.cp3406.educationalgame;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class MemoryGame {
    private List<Integer> sequence;
    private Random r;
    private int numTiles;
    private boolean complete;
    private int score;

    MemoryGame() {
        sequence = new ArrayList<>();
        r = new Random();
        numTiles = 2;
        complete = false;
        score = 0;
    }

    List<Integer> newGame(Difficulty level) {
        switch (level) {
            case EASY:
                numTiles = 2;
                break;
            case MEDIUM:
                numTiles = 3;
                break;
            case HARD:
                numTiles = 4;
                break;
            case MASTER:
                numTiles = 5;
                break;
        }
        return createSequence(2);
    }

    List<Integer> createSequence(int numSteps) {
        complete = false;
        sequence = new ArrayList<>();
        sequence.add(r.nextInt(numTiles));
        for (int i = 0; i < numSteps; i++) {
            sequence.add(r.nextInt(numTiles));
        }
        return sequence;
    }

    boolean checkOrder(List<Integer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            if (!answers.get(i).equals(sequence.get(i))) {
                return false;
            }
        }
        if (answers.size() == sequence.size()) {
            complete = true;
        }
        Log.i("S=", sequence + "-----------------------------------------------------");
        Log.i("A=", answers + "------------------------------------------------------");
        return true;
    }

    boolean isSequenceComplete() {
        return complete;
    }

    void setScore(int points) {
        score = score + points;
    }

    int getScore() {
        return score;
    }
}
