package au.edu.jcu.cp3406.educationalgame;

class Background {
    static void run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
