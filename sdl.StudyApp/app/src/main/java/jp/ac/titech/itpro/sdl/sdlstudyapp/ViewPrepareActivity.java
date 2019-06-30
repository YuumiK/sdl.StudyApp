package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewPrepareActivity extends AppCompatActivity {
    private TextView title;
    private TextView time;
    private Button Timer;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prepare);
        title = findViewById(R.id.title_prepare_view);
        time = findViewById(R.id.time_prepare_view);
        Timer = findViewById(R.id.TIME_view_prepare);
        text = findViewById(R.id.text_prepare_view);

        title.setText(getIntent().getStringExtra(TextsOpenHelper.COLUMN_NAME_TITLE));
        setTimeText(getIntent().getIntExtra(TextsOpenHelper.COLUMN_NAME_TIME,0));
        text.setText(getIntent().getStringExtra(TextsOpenHelper.COLUMN_NAME_TEXT));

        Timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPrepareActivity.this, PrepareTimer.class);
                intent.putExtra(TextsOpenHelper.COLUMN_NAME_TIME, Integer.valueOf(time.getText().toString()));
                startActivity(intent);
        }});

    }
    private void setTimeText(int timeMin){
        time.setText("Goal:" + timeMin+"min.");
    }
}
