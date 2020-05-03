package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MemoryActivity extends AppCompatActivity implements StateListener {
    private StateListener listener;
    private Context context;
    private Tile[] tiles =
     {new Tile(R.drawable.square), new Tile(R.drawable.circle), new Tile(R.drawable.square), new Tile(R.drawable.circle), new Tile(R.drawable.square), new Tile(R.drawable.circle)};
    //TileManager tileManager;
    private MemoryGame game = new MemoryGame();
    final Handler handler = new Handler();
    Iterator<Integer> iterator;
    boolean correct;
    Runnable runnable;
    TileAdapter tileAdapter;
    GridView gridView;
    Fragment settingsFragment;

    List<Integer> sequence;
    List<Integer> answers;
    Integer[] previous = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        //tileManager = new TileManager(this.getAssets(), "Shapes");
        //tiles = tileManager.getTiles();

        listener = (StateListener) context;

        FragmentManager fm = getSupportFragmentManager();
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);
        showHideFragment(settingsFragment);

        gridView = findViewById(R.id.gridView);
        tileAdapter = new TileAdapter(this, tiles);
        gridView.setAdapter(tileAdapter);

        final Difficulty level = (Difficulty) getIntent().getSerializableExtra("difficulty");
        sequence = game.newGame(level);
        answers = new ArrayList<>();
        playSequence();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tile tile = tiles[position];
                tiles[previous[0]].lightOff();
                tile.lightUp();
                answers.add(position);
                previous[0] = position;

                if (!game.checkOrder(answers)) {
                    onUpdate(State.GAME_OVER, level);
                } else if (game.isSequenceComplete()){
                    onUpdate(State.CONTINUE, level);
                }

                tileAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onUpdate(State state, Difficulty level) {
        Intent intent;

        switch (state) {
            case SOUND:
                //soundManager.muteUnMuteSound();
                break;
            case MUSIC:
                break;
            case RESTART:
                intent = getIntent();
                handler.removeCallbacks(runnable);
                finish();
                intent.putExtra("difficulty", level);
                startActivity(intent);
                break;
            case CONTINUE:
                game.createSequence(answers.size() + 1);
                playSequence();
                break;
            case GAME_OVER:
                intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("score", game.getScore());
                startActivity(intent);
                break;
            case SETTINGS:
                showHideFragment(settingsFragment);
                break;
        }
    }

    public void playSequence() {
        iterator = sequence.iterator();

        handler.post(runnable = new Runnable() {
            @Override
            public void run() {
                if (iterator.hasNext()) {
                    tiles[previous[0]].lightOff();
                    previous[0] = iterator.next();
                    tiles[previous[0]].lightUp();
                    gridView.setAdapter(tileAdapter);
                    handler.postDelayed(this, 1200);
                } else {
                    tiles[previous[0]].lightOff();
                    gridView.setAdapter(tileAdapter);
                    Toast.makeText(getBaseContext(), "YOUR TURN", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.i("playSequence", String.valueOf(sequence));
    }

    public void settingsClicked(View view) {
        showHideFragment(settingsFragment);
    }

    public void showHideFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (settingsFragment.isHidden()) {
            ft.show(fragment);
            Log.d("hidden","Show");
        } else {
            ft.hide(fragment);
            Log.d("Shown","Hide");
        }
        ft.commit();
    }
}
