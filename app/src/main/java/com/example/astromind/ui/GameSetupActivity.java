package com.example.astromind.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.astromind.R;

public class GameSetupActivity extends AppCompatActivity {
    Button setUpGameButton;
    TextView gameDurationEditView;
    TextView gameMaxScoreEditView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        initViews();

        setUpGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!settingsAreNull()) {
                    startGame();
                } else tellUserToFillInFields();
            }
        });

    }

    private void initViews() {
        setUpGameButton = findViewById(R.id.set_up_game_btn);
        gameDurationEditView = findViewById(R.id.time_et);
        gameMaxScoreEditView = findViewById(R.id.points_et);
    }

    private void startGame() {
        int gameDuration = Integer.parseInt(String.valueOf(gameDurationEditView.getText()));
        int maxGameScore = Integer.parseInt(String.valueOf(gameMaxScoreEditView.getText()));
        //Launch game activity
        Intent startGameIntent =
                new Intent(GameSetupActivity.this, GameActivity.class);

        startGameIntent.putExtra("maxGameScore", maxGameScore);
        startGameIntent.putExtra("gameDuration", gameDuration);
        startActivity(startGameIntent);
        finish();
    }

    private boolean settingsAreNull() {
        if (TextUtils.isEmpty(gameDurationEditView.getText().toString())){
            gameDurationEditView.setError(getString(R.string.field_is_empty));
            return true;
        }
        if (TextUtils.isEmpty(gameMaxScoreEditView.getText().toString())){
            gameMaxScoreEditView.setError(getString(R.string.field_is_empty));
            return true;
        } else return false;
    }

    private void tellUserToFillInFields() {
        Toast.makeText(GameSetupActivity.this, R.string.message_about_null_settings, Toast.LENGTH_LONG).show();
    }

    //Appbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity_game_setup, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.info_item) {
            startActivity(new Intent(GameSetupActivity.this, InfoActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


}