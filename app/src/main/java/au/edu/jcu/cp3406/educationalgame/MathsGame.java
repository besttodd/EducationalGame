package au.edu.jcu.cp3406.educationalgame;

import java.util.Random;

class MathsGame {
    private String card1;
    private String card2;
    private int c1Answer;
    private int c2Answer;
    private int score;
    private Random random;

    MathsGame() {
        card1 = "";
        card2 = "";
        c1Answer = 0;
        c2Answer = 0;
        score = 0;
        random = new Random();
    }

    private String generateCard(Difficulty level) {
        int num1 = 0;
        int num2 = 0;
        int answer = 0;
        char operator = '+';
        int r;
        String card;

        switch (level) {
            case MEDIUM:
                r = random.nextInt(1);
                break;
            case HARD:
            case MASTER:
                r = random.nextInt(3);
                break;
            case EASY:
            default:
                r = 0;
        }

        switch (r) {
            case 0:
                operator = '+';
                num1 = random.nextInt(30) + 10;
                num2 = random.nextInt(30) + 10;
                answer = num1 + num2;
                break;
            case 1:
                operator = '-';
                num1 = random.nextInt(30) + 10;
                num2 = random.nextInt(num1);
                answer = num1 - num2;
                break;
            case 2:
                operator = 215;  //Multiply symbol
                num1 = random.nextInt(12);
                num2 = random.nextInt(11);
                answer = num1 * num2;
                break;
            case 3:
                operator = 247;  //Divide symbol
                num1 = 30;
                num2 = 2;
                answer = num1 / num2;
                break;
        }

        card = num1 + " " + operator + " " + num2 + " " + answer;
        return card;
    }

    void setCards(Difficulty level) {
        String[] newCard = generateCard(level).split(" ");
        card1 = newCard[0] + "\n" + newCard[1] + "  " + newCard[2];
        c1Answer = Integer.parseInt(newCard[3]);

        newCard = generateCard(level).split(" ");
        card2 = newCard[0] + "\n" + newCard[1] + "  " + newCard[2];
        c2Answer = Integer.parseInt(newCard[3]);

        //Make sure answers are not equal - except for Master difficulty
        if (level != Difficulty.MASTER) {
            while (c1Answer == c2Answer) {
                newCard = generateCard(level).split(" ");
                card2 = newCard[0] + "\n" + newCard[1] + "  " + newCard[2];
                c2Answer = Integer.parseInt(newCard[3]);
            }
        }
    }

    boolean checkCards(int selected, String task) {
        switch (task) {
            case "Higher":
                if (selected == 3) {
                    if (c1Answer == c2Answer) {
                        return true;
                    }
                } else if (selected == 2) {
                    return c2Answer > c1Answer;
                } else {
                    return c1Answer > c2Answer;
                }
            case "Lower":
                if (selected == 3) {
                    if (c1Answer == c2Answer) {
                        return true;
                    }
                } else if (selected == 2) {
                    return c2Answer < c1Answer;
                } else {
                    return c1Answer < c2Answer;
                }
        }
        return false;
    }

    void setScore(int points) {
        score = score + points;
    }

    int getScore() {
        return score;
    }

    String getCard1() {
        return card1;
    }

    String getCard2() {
        return card2;
    }
}
