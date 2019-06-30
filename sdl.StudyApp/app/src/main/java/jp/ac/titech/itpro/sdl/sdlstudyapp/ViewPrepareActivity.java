package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

public class ViewPrepareActivity extends AppCompatActivity {
    private TextView title;
    private TextView time;
    private Button Timer;
    private Button print;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prepare);
        title = findViewById(R.id.title_prepare_view);
        time = findViewById(R.id.time_prepare_view);
        Timer = findViewById(R.id.TIME_view_prepare);
        print = findViewById(R.id.print_view_prepare);
        text = findViewById(R.id.text_prepare_view);

        title.setText(getIntent().getStringExtra(TextsOpenHelper.COLUMN_NAME_TITLE));
        setTimeText(getIntent().getIntExtra(TextsOpenHelper.COLUMN_NAME_TIME,0));
        text.setText(getIntent().getStringExtra(TextsOpenHelper.COLUMN_NAME_TEXT));

        Timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewPrepareActivity.this, PrepareTimer.class);
                intent.putExtra(TextsOpenHelper.COLUMN_NAME_TIME, getIntent().getIntExtra(TextsOpenHelper.COLUMN_NAME_TIME,0));
                startActivity(intent);
        }});
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = Environment.getExternalStorageDirectory() + "/"+title.getText().toString()+".txt";
                saveFile(filename, text.getText().toString());

                File documentDir = new File(filename);
                Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", documentDir);

                Log.d("Uri", uri.toString());
                Intent printIntent = new Intent(ViewPrepareActivity.this, PrintDialogActivity.class);
                printIntent.setDataAndType(uri, "text/plain");
                printIntent.putExtra("title", title.getText().toString());
                startActivity(printIntent);
            }});


    }
    private void setTimeText(int timeMin){
        time.setText(String.format("Goal:%d min.", timeMin));
    }
    public void saveFile(String filePath, String str) {
        File file = new File(filePath);
        file.getParentFile().mkdir();
        try (FileOutputStream fileOutputstream = new FileOutputStream(file,
                true);){

            fileOutputstream.write(str.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
