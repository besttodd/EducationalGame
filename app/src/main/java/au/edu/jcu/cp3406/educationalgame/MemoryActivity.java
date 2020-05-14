package au.edu.jcu.cp3406.educationalgame;

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

public class MemoryActivity extends BaseActivity implements StateListener {
    private StateListener listener;
    private Context context;
    private Difficulty level;
    private Tile[] tiles =
     {new Tile(R.drawable.square), new Tile(R.drawable.circle), new Tile(R.drawable.triangle), new Tile(R.drawable.hexagon), new Tile(R.drawable.plus)};
    //TileManager tileManager;
    private MemoryGame memoryGame = new MemoryGame();
    private final Handler handler = new Handler();
    private Iterator<Integer> iterator;
    private Runnable runnable;
    private TileAdapter tileAdapter;
    private GridView gridView;
    private Fragment settingsFragment;

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
        hideFragment(settingsFragment);

        gridView = findViewById(R.id.gridView);
        tileAdapter = new TileAdapter(this, tiles);
        gridView.setAdapter(tileAdapter);

        level = (Difficulty) getIntent().getSerializableExtra("difficulty");
        sequence = memoryGame.newGame(level);
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

                if (!memoryGame.checkOrder(answers)) {
                    onUpdate(State.GAME_OVER, level);
                } else if (memoryGame.isSequenceComplete()){
                    onUpdate(State.CONTINUE, level);
                }

                tileAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onUpdate(State state, Difficulty level) {
        Intent intent;

        switch (state) {
            case RESTART:
                intent = getIntent();
                handler.removeCallbacks(runnable);
                finish();
                intent.putExtra("difficulty", level);
                startActivity(intent);
                break;
            case CONTINUE:
                Toast.makeText(getBaseContext(), "YOU GOT IT\nKeep Going!", Toast.LENGTH_SHORT).show();
                //update score
                sequence = memoryGame.createSequence(answers.size());
                answers = new ArrayList<>();
                playSequence();
                break;
            case GAME_OVER:
                intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("score", memoryGame.getScore());
                intent.putExtra("difficulty", level);
                intent.putExtra("game", Game.MEMORY);
                startActivity(intent);
                break;
            case SETTINGS:
                hideFragment(settingsFragment);
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
        Log.i("Sequence", String.valueOf(sequence));
    }

    public void settingsClicked(View view) {
        showFragment(settingsFragment);
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public void hideFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
