package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView scoreDisplay = findViewById(R.id.finalScore);
        String score = Integer.toString(Objects.requireNonNull(getIntent().getExtras()).getInt("score"));
        scoreDisplay.setText(score);
    }
}
