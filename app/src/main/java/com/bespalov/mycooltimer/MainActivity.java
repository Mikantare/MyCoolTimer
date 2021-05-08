package com.bespalov.mycooltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBarTimer;
    private Button buttonStart;
    private TextView textViewTimer;
    private boolean startTimer = false;
    private CountDownTimer countDownTimer;

    private MediaPlayer mediaPlayer;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent openSetting = new Intent(this, SettingsActivity.class);
            startActivity(openSetting);
            return true;
        } else
        if (id == R.id.action_about) {
            Intent openAbout = new Intent(this, AboutActivity.class);
            startActivity(openAbout);
            return true;
    }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        seekBarTimer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int mlseconds = seekBarTimer.getProgress();
                setTextViewTimer(mlseconds);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void init() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.konec);
        seekBarTimer = findViewById(R.id.seekBarTimer);
        buttonStart = findViewById(R.id.buttonStart);
        textViewTimer = findViewById(R.id.textViewTimer);
        seekBarTimer.setMax(600000);
        seekBarTimer.setProgress(30000);
        int mlseconds = seekBarTimer.getProgress();
        setTextViewTimer(mlseconds);
    }

    private void setTextViewTimer(int seconds) {
        int minutes = seconds / 60000;
        int secs = (seconds % 60000) / 1000;
        String time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
        textViewTimer.setText(time);
    }

    private void    startTimer (int mlSecs) {
        countDownTimer = new CountDownTimer(mlSecs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setTextViewTimer((int) millisUntilFinished);
            }

            @Override
            public void onFinish() {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (sharedPreferences.getBoolean("enable_sound", true)) {
                    mediaPlayer.start();
                }
                textViewTimer.setText("00:00");
            
            }
        };
        countDownTimer.start();
    }

    public void clickTimer(View view) {
        if (!startTimer) {
            buttonStart.setText("Stop");
            seekBarTimer.setEnabled(startTimer);
            int mlseconds = seekBarTimer.getProgress();
            startTimer(mlseconds);
            startTimer = true;
        } else if (startTimer) {
            seekBarTimer.setEnabled(startTimer);
            countDownTimer.cancel();
            startTimer = false;
            buttonStart.setText("Start");
            init();
        }
    }
}