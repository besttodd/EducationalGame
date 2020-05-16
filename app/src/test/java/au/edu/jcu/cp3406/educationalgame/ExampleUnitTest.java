package au.edu.jcu.cp3406.educationalgame;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    public void getTiles() {
        int NUM_TILES = 3;
        String[] shapes = {"square", "squareLitUp", "triangle", "triangleLitUp", "circle", "circleLitUp"};

        Tile[] tiles = new Tile[NUM_TILES];
        List<Tile> list = new ArrayList<>();
        String[] selected = new String[2];

        for (int i = 0; i < NUM_TILES * 2; i += 2) {
            selected[0] = shapes[i];
            selected[1] = shapes[i + 1];
            list.add(new Tile(selected));
        }
        list.toArray(tiles);
    }

    public void checkHigher() {
        String task = "Higher";
        int selected = 2;  //3=EQUALS Button, 2=Card 2, 1=Card 1
        int c1Answer = 5;
        int c2Answer = 3;
        boolean correct = false;

        switch (task) {
            case "Higher":
                if (selected == 3) {
                    if (c1Answer == c2Answer) {
                        correct = true;
                    }
                } else if (selected == 2) {
                    correct = c2Answer > c1Answer;
                } else correct = c1Answer > c2Answer;
            case "Lower":
                if (selected == 3) {
                    if (c1Answer == c2Answer) {
                        correct = true;
                    }
                } else if (selected == 2) {
                    correct = c2Answer < c1Answer;
                } else correct = c1Answer < c2Answer;
        }
        correct = false;

        assertEquals(false, correct);
    }
}