package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    Difficulty level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startHLGame(View view) {
        Spinner spinner = findViewById(R.id.difficultySpinner);
        String selection = spinner.getSelectedItem().toString();
        level = Difficulty.valueOf(selection.toUpperCase());

        Intent intent = new Intent(this, HigherLowerGameActivity.class);
        intent.putExtra("difficulty", level);
        startActivity(intent);
    }

    public void startMemoryGame(View view) {
        Spinner spinner = findViewById(R.id.difficultySpinner);
        String selection = spinner.getSelectedItem().toString();
        level = Difficulty.valueOf(selection.toUpperCase());

        Intent intent = new Intent(this, MemoryActivity.class);
        intent.putExtra("difficulty", level);
        startActivity(intent);
    }
}
