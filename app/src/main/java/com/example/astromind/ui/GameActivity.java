package com.example.astromind.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ankushgrover.hourglass.Hourglass;
import com.example.astromind.R;

public class GameActivity extends AppCompatActivity {
    //Game values
    int maxGameScore;
    int gameDuration; // in seconds
    int currentGameScore;
    int currentTimeLeft;

    //Game objects
    MediaPlayer gameMusicPlayer;
    ProgressBar gameProgressBar;
    TextView playerStatsTV;
    ImageView gameObject;
    TextView timerTextView;
    Hourglass gameCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    private void setValuesFromSettings(Intent gameSettings) {
        maxGameScore = gameSettings.getIntExtra("maxGameScore", 100);
        gameDuration = gameSettings.getIntExtra("gameDuration", 60);
    }

    private void initViews() {
        gameProgressBar = findViewById(R.id.gameProgressBar);
        playerStatsTV = findViewById(R.id.player_stats);
        gameObject = findViewById(R.id.space_ship);
        timerTextView = findViewById(R.id.timerTV);
    }

    private void startGame() {
        setValuesFromSettings(getIntent());

        initViews();

        currentTimeLeft = gameDuration;
        initTimer(gameDuration);

        initGameMusicPlayer();
        startPlayingMusic();

        currentGameScore = 0;//set default starting score value
        gameProgressBar.setMax(maxGameScore); // set max value of progress bar to max score from settings
        displayGameScore(playerStatsTV, gameProgressBar);//Showing game score on ProgressBar and TextView

        //OnClick for game object
        gameObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentGameScore++; //one click gives player +1 score
                displayGameScore(playerStatsTV, gameProgressBar);
                checkScoreAndUnlockAchievements();
            }
        });
    }

    private void resumeGame() {
        startPlayingMusic();
        gameCountDownTimer.resumeTimer();
    }

    public void pauseGame() {
        pauseMusic();
        gameCountDownTimer.pauseTimer();
    }

    private void finishGame() {
        pauseMusic();
        /*gameCountDownTimer.stopTimer();*/
        new AlertDialog.Builder(GameActivity.this)
                .setTitle(getString(R.string.dialog_title) + currentGameScore + getString(R.string.points_for) + gameDuration + getString(R.string.seconds))
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(GameActivity.this, GameSetupActivity.class));
                    }
                })
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finishGame();
                    }
                })
                .create().show();
    }

    //Getting current score and showing achievements if current score matches the score needed
    private void checkScoreAndUnlockAchievements() {
        boolean newbieRankAchieved = (currentGameScore >= maxGameScore * 0.2);
        boolean expertRankAchieved = (currentGameScore >= maxGameScore * 0.4);
        boolean masterRankAchieved = (currentGameScore >= maxGameScore * 0.6);
        boolean grandmasterRankAchieved = (currentGameScore >= maxGameScore * 0.8);
        boolean legendRankAchieved = (currentGameScore >= maxGameScore);

        if (newbieRankAchieved) {
            findViewById(R.id.newbie_chip).setVisibility(View.VISIBLE);
            findViewById(R.id.placeholder_chip).setVisibility(View.GONE);
        }
        if (expertRankAchieved) {
            findViewById(R.id.expert_chip).setVisibility(View.VISIBLE);
        }
        if (masterRankAchieved) {
            findViewById(R.id.master_chip).setVisibility(View.VISIBLE);
        }
        if (grandmasterRankAchieved) {
            findViewById(R.id.grandmaster_chip).setVisibility(View.VISIBLE);
        }
        if (legendRankAchieved) {
            findViewById(R.id.legend_chip).setVisibility(View.VISIBLE);
        }
    }

    //Timer
    private void initTimer(int timeInSeconds) {
        gameCountDownTimer = new Hourglass(timeInSeconds * 1000, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {
                timerTextView.setText(String.valueOf(currentTimeLeft));
                currentTimeLeft--;
            }

            @Override
            public void onTimerFinish() {
                finishGame();
            }
        };
    }

    //Music player
    private void initGameMusicPlayer() {
        gameMusicPlayer = MediaPlayer.create(this, R.raw.main_game_music);
    }

    private void startPlayingMusic() {
        gameMusicPlayer.start();
    }

    private void pauseMusic() {
        gameMusicPlayer.pause();
    }

    private void stopMusic() {
        gameMusicPlayer.stop();
    }

    @SuppressLint("SetTextI18n")
    private void displayGameScore(TextView playerStatsTV, ProgressBar playerPB) {
        playerStatsTV.setText(getText(R.string.points) + ": " + currentGameScore);
        playerPB.setProgress(currentGameScore);
    }
}