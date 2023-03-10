package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button fast_rewind_btn,play_btn,pause_btn,fast_forward_btn;
    TextView time_txt,title_txt;
    SeekBar seekBar;

    MediaPlayer mediaPlayer;
    Handler handler=new Handler();

    double startTime=0;
    double finalTime=0;
    int forwardTime =10000;
    int backwardTime=10000;
    static int oneTimeOnly=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        fast_rewind_btn=findViewById(R.id.fast_rewind_btn);
        fast_forward_btn=findViewById(R.id.fast_forward_btn);
        play_btn=findViewById(R.id.play_btn);
        pause_btn=findViewById(R.id.pause_btn);
        time_txt=findViewById(R.id.time_txt);
        title_txt=findViewById(R.id.title_txt);
        seekBar=findViewById(R.id.seekBar);
        mediaPlayer=MediaPlayer.create(this, R.raw.song);
        title_txt.setText(getResources().getIdentifier("song",
                "raw",getPackageName()));
        seekBar.setClickable(false);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });

        fast_forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int) startTime;
                if((temp + forwardTime) <= finalTime){
                    startTime =startTime + forwardTime;
                    mediaPlayer.seekTo((int)startTime);
                }
                else{
                    Toast.makeText(MainActivity.this, "Can't Jump to Forward!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fast_rewind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp=(int) startTime;
                if((temp - backwardTime) > 0){
                    startTime =startTime - backwardTime;
                    mediaPlayer.seekTo((int)startTime);
                }
                else{
                    Toast.makeText(MainActivity.this, "Can't Jump to Backward!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public  void PlayMusic() {
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        time_txt.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes
                        ((long) finalTime))
        ));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);
    }

        private Runnable UpdateSongTime=new Runnable() {
            @Override
            public void run() {
                startTime=mediaPlayer.getCurrentPosition();
                time_txt.setText(String.format("%d:%d",TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
                ));

                seekBar.setProgress((int) startTime);
                handler.postDelayed(this,100);
            }
        };

}