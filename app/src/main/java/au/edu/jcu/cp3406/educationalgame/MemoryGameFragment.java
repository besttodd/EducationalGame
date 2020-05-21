package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class MemoryGameFragment extends Fragment {
    private static final int POINTS = 10;
    private Difficulty level;
    private SoundManager soundManager;
    private Context context;
    private StateListener listener;
    private MemoryGame memoryGame;
    private StatusFragment statusFragment;
    private Tile[] tiles;
    private TileAdapter tileAdapter;
    private GridView gridView;
    private List<Integer> sequence;
    private List<Integer> answers;
    private Integer[] previous = {0};
    private Iterator<Integer> iterator;
    private int rounds;
    private final Handler handler = new Handler();
    private Runnable runnable;

    public MemoryGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        listener = (StateListener) context;
        memoryGame = new MemoryGame();
    }

    @Override
    @SuppressWarnings("unchecked")  //getting sequence from MemoryActivity
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memory_game, container, false);

        level = (Difficulty) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("difficulty");
        assert level != null;
        soundManager = (SoundManager) context.getApplicationContext();
        TileManager tileManager = new TileManager(context.getAssets(), "Shapes");

        if (savedInstanceState == null) {
            sequence = memoryGame.newGame(level);
            rounds = 0;
        } else {
            sequence = (List<Integer>) savedInstanceState.getSerializable("sequence");
            memoryGame.setSequence(sequence);
            rounds = savedInstanceState.getInt("rounds");
            memoryGame.setScore(savedInstanceState.getInt("score"));
        }
        tiles = tileManager.getTiles(memoryGame.getNumTiles());
        gridView = view.findViewById(R.id.gridView);
        String screen = getResources().getString(R.string.screen_type);
        if (screen.equals("tablet")) {
            gridView.setColumnWidth(200);
        }
        tileAdapter = new TileAdapter(context, tiles);
        gridView.setAdapter(tileAdapter);
        answers = new ArrayList<>();

        playSequence();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tile tile = tiles[position];
                tiles[previous[0]].lightOff();

                tile.lightUp();
                soundManager.playSound(3);
                answers.add(position);
                previous[0] = position;

                if (!memoryGame.checkOrder(answers)) {
                    listener.onUpdate(State.GAME_OVER, level);
                } else if (memoryGame.isSequenceComplete()) {
                    Toast.makeText(context, "YOU GOT IT\nKeep Going!", Toast.LENGTH_SHORT).show();
                    memoryGame.setScore(POINTS);
                    rounds++;
                    statusFragment.setScore(memoryGame.getScore(), rounds);
                    sequence = memoryGame.createSequence(answers.size());
                    answers = new ArrayList<>();
                    playSequence();
                    listener.onUpdate(State.CONTINUE, level);
                }
                tileAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        statusFragment = (StatusFragment) Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.statusFragmentM);
        assert statusFragment != null;
        statusFragment.setScore(memoryGame.getScore(), rounds);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("sequence", (Serializable) sequence);
        outState.putInt("rounds", rounds);
        outState.putInt("score", memoryGame.getScore());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    private void playSequence() {
        iterator = sequence.iterator();

        handler.post(runnable = new Runnable() {
            @Override
            public void run() {
                if (iterator.hasNext()) {
                    tiles[previous[0]].lightOff();
                    previous[0] = iterator.next();
                    gridView.setAdapter(tileAdapter);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tiles[previous[0]].lightUp();
                            soundManager.playSound(3);
                            gridView.setAdapter(tileAdapter);
                        }
                    }, 1000);
                    handler.postDelayed(this, memoryGame.getSpeed());
                } else {
                    tiles[previous[0]].lightOff();
                    gridView.setAdapter(tileAdapter);
                    Toast.makeText(context, "YOUR TURN", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.i("Sequence", String.valueOf(sequence));
    }

    int getScore() {
        return memoryGame.getScore();
    }
}
