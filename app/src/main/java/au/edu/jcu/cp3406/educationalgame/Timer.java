package au.edu.jcu.cp3406.educationalgame;

public class Timer {
    private Boolean isRunning;
    private long seconds;
    private long minutes;

    Timer() {
        isRunning = false;
        minutes = 0;
        seconds = 0;
    }

    Timer(String current) {
        isRunning = false;
        seconds = Integer.parseInt(current.substring(current.length()-2));
        minutes = Integer.parseInt(current.substring(1, 2));
    }

    void tick() {
        if (seconds > 0) { seconds--; }
        else if (minutes > 0) {
            seconds = 59;
            minutes--;
        }else {
            stopTimer();
        }
    }

    void startTimer() { isRunning = true; }

    private void stopTimer() { isRunning = false; }

    boolean isRunning() { return isRunning; }

    public String toString(){
        return String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds);
    }
}
