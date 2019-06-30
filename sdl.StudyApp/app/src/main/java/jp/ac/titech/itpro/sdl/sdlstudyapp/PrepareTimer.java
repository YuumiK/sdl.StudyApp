package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PrepareTimer extends AppCompatActivity {

    TextView time;
    Button cancel;
    Button start;
    private final String TIMER_START = "START";
    private final String TIMER_PAUSE = "PAUSE";
    private final String TIMER_RESUME = "RESUME";
    CountDown countDown;
    private SimpleDateFormat dataFormat =
            new SimpleDateFormat("mm:ss", Locale.US);

    //音声
    private SoundPool mSoundPool;
    private int mSoundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);
        time = findViewById(R.id.text_timer);
        cancel = findViewById(R.id.cancel_button_timer);
        start = findViewById(R.id.start_button_timer);

        countDown = getTimer(getIntent().getIntExtra(TextsOpenHelper.COLUMN_NAME_TIME, 0)*60*1000);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }});

        start.setText(TIMER_START);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(start.getText().equals(TIMER_START)) {
                    countDown = getTimer(getIntent().getIntExtra(TextsOpenHelper.COLUMN_NAME_TIME, 0)*60*1000);
                    countDown.start();
                    start.setText(TIMER_PAUSE);
                }
                else if(start.getText().equals(TIMER_PAUSE)){
                    countDown.cancel();
                    start.setText(TIMER_RESUME);
                }
                else{
                    String[] parts = time.getText().toString().split(":");
                    int min = Integer.parseInt(parts[0]);
                    int sec = Integer.parseInt(parts[1]);
                    countDown = getTimer((min*60+sec)*1000);
                    countDown.start();
                    start.setText(TIMER_PAUSE);
                }
            }
        });

        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPool.load(getApplicationContext(),R.raw.wolveshowling, 0);
    }

    private CountDown getTimer(long millisec){
        //millisecond
        long interval = 1000;
        time.setText(dataFormat.format(millisec));
        return new CountDown(millisec, interval);
    }

    class CountDown extends CountDownTimer {

        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            time.setText(dataFormat.format(0));
            Toast.makeText(PrepareTimer.this, "Time is up!", Toast.LENGTH_SHORT).show();
            mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
            start.setText(TIMER_START);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            time.setText(dataFormat.format(millisUntilFinished));

        }
    }
}
