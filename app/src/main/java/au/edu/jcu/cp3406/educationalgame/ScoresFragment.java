package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScoresFragment extends Fragment {
    private Context context;
    private String game;
    private SQLiteDatabase db;
    private Cursor cursor;

    private ArrayList<String> dateList = new ArrayList<>();
    private ArrayList<String> difficultyList = new ArrayList<>();
    private ArrayList<Integer> scoreList = new ArrayList<>();

    public ScoresFragment() {
        game = "Maths";
    }

    ScoresFragment(CharSequence pageTitle) {
        String[] text = pageTitle.toString().split(" ");
        game = text[0].toUpperCase();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores, container, false);
        ListView listScores = view.findViewById(R.id.highScoresList);

        DBHelper dbHelper = new DBHelper(context);
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query("HIGHSCORES", new String[]{"DATE", "DIFFICULTY", "SCORE", "GAME"},
                    "GAME = ?", new String[]{game}, null, null, "SCORE" + " DESC, DIFFICULTY" + " DESC");

            if (cursor.moveToFirst()) {
                do {
                    dateList.add(cursor.getString(cursor.getColumnIndex("DATE")));
                    difficultyList.add(convert(cursor.getInt(cursor.getColumnIndex("DIFFICULTY"))));
                    scoreList.add(cursor.getInt(cursor.getColumnIndex("SCORE")));
                } while (cursor.moveToNext());
            }

            ScoresAdapter scoresAdapter = new ScoresAdapter(context, dateList, difficultyList, scoreList);
            listScores.setAdapter(scoresAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    private String convert(int level) {
        String newLevel = "";

        switch (level) {
            case 0:
                newLevel = "EASY";
                break;
            case 1:
                newLevel = "MEDIUM";
                break;
            case 2:
                newLevel = "HARD";
                break;
            case 3:
                newLevel = "MASTER";
                break;
        }
        return newLevel;
    }
}
