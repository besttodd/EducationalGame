package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Log.d(String.valueOf(this.getClass()), "onCreate()----------------------------------------------------");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(String.valueOf(this.getClass()), "onStart()----------------------------------------------------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(String.valueOf(this.getClass()), "onStop()----------------------------------------------------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(String.valueOf(this.getClass()), "onPause()--------------------------------------------------");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(String.valueOf(this.getClass()), "onResume()--------------------------------------------------");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(String.valueOf(this.getClass()), "onRestart()-------------------------------------------------");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(String.valueOf(this.getClass()), "onBackPressed()---------------------------------------------");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(String.valueOf(this.getClass()), "onDestroy()-------------------------------------------------");
    }
}
