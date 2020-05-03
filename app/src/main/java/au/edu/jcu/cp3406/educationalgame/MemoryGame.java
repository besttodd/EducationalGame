package au.edu.jcu.cp3406.educationalgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class MemoryGame {
    private List<Integer> sequence;
    private Random r;
    private int numTiles;
    private boolean complete;
    private boolean gameOver;
    private int score;

    MemoryGame() {
        sequence = new ArrayList<>();
        r = new Random();
        numTiles = 2;
        complete = false;
        gameOver = false;
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
                numTiles = 6;
                break;
        }
        createSequence(2);
        return sequence;
    }

    public void createSequence(int numSteps) {
        sequence.add(r.nextInt(numTiles));
        for (int i = 0; i < numSteps; i++) {
            sequence.add(r.nextInt(numTiles));
        }
    }

    boolean checkOrder(List<Integer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            if (!answers.get(i).equals(sequence.get(i))) {
                gameOver = true;
                return false;
            }
        }
        if (answers.size() == sequence.size()) {
            complete = true;
        }
        System.out.println("S=" + sequence + "-----------------------------------------------------");
        System.out.println("A=" + answers + "------------------------------------------------------");
        return true;
    }

    boolean isGameOver() {
        return gameOver;
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
