package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    // Context of the app under test.
    private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    @Test
    public void useAppContext() {
        assertEquals("au.edu.jcu.cp3406.educationalgame", appContext.getPackageName());
    }

    @Test
    public void databaseReadandWrite() {
        SQLiteDatabase db;
        DBHelper dbhelper;
        Cursor cursor;
        int newScore = 100;

        ArrayList<String> idList = new ArrayList<String>();
        ArrayList<String> dateList = new ArrayList<String>();
        ArrayList<String> difficultyList = new ArrayList<String>();
        ArrayList<Integer> scoreList = new ArrayList<Integer>();

        int level = 3;

        dbhelper = new DBHelper(appContext);
        db = dbhelper.getWritableDatabase();
        cursor = db.query("HIGHSCORES", new String[] {"_id", "SCORE"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            int existingScore = cursor.getInt(1);
            if (newScore > existingScore) {
                dbhelper.insertScore(db, "08-05-20", level, newScore, "MATHS");
                Log.i("ExInstTest","New high score added!");
                break;
            }
        }

        try {
            db = dbhelper.getReadableDatabase();
            cursor = db.query("HIGHSCORES", new String[] {"_id", "DATE", "DIFFICULTY", "SCORE"},
                    null, null, null, null, "SCORE"+ " DESC");

            if (cursor.moveToFirst()) {
                do { idList.add(cursor.getString(cursor.getColumnIndex("_id")));
                    dateList.add(cursor.getString(cursor.getColumnIndex("DATE")));
                    difficultyList.add(cursor.getString(cursor.getColumnIndex("DIFFICULTY")));
                    scoreList.add(cursor.getInt(cursor.getColumnIndex("SCORE")));
                } while (cursor.moveToNext());
            }
        } catch(SQLiteException e) {
            Log.i("ExInstTest","Database unavailable");
        }

        assert(dateList.contains("08-05-20"));
        assert(difficultyList.contains("MASTER"));
        assert(scoreList.contains(newScore));

        cursor.close();
        db.close();
    }

    @Test
    public void getTiles() {
        int NUM_TILES = 3;   //Depending on difficulty level
        TileManager tileManager = new TileManager(appContext.getAssets(), "Shapes");   //Read images in from asset folder
        List<Tile> list = new ArrayList<>();
        Bitmap[] selected = new Bitmap[2];

        //Get the shape pairs and store them as a tile
        for (int i = 0; i < NUM_TILES * 2; i += 2) {
            selected[0] = tileManager.getTileImage(i);
            selected[1] = tileManager.getTileImage(i + 1);
            list.add(new Tile(selected));
        }
        Tile[] tiles = new Tile[list.size()];
        list.toArray(tiles);

        //tiles should be a Tile arrary with the dark image active
        assertEquals(tileManager.getTileImage(0), tiles[0].getActive());
        assertEquals(tileManager.getTileImage(2), tiles[1].getActive());
        assertEquals(tileManager.getTileImage(4), tiles[2].getActive());
    }
}
