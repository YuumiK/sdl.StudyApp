package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewPrepareActivity extends AppCompatActivity {
    private static final String WEBLIO_URL = "https://ejje.weblio.jp/content/";
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
        setSpannableString(getWindow().getDecorView());
    }
    private void setTimeText(int timeMin){
        time.setText(String.format("Goal:%d min.", timeMin));
    }

    public void saveFile(String filePath, String str) {
        File file = new File(filePath);
        file.getParentFile().mkdir();
        try (FileOutputStream fileOutputstream = new FileOutputStream(file,
                false)){

            fileOutputstream.write(str.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSpannableString(View view) {

        String[] part = text.getText().toString().split(" ");

        Map<String, String> map = new HashMap<>();
        for(String s: part){
            String sclens = cleanseWord(s);
            map.put(sclens, WEBLIO_URL+sclens);
        }

        SpannableString ss = createSpannableString(text.getText().toString(), map);

        TextView textView = view.findViewById(R.id.text_prepare_view);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private String cleanseWord(String s){
        return s.replace(",", "")
                .replace(".", "")
                .replace(",", "")
                .replace(":", "")
                .replace(";", "");
    }

    private SpannableString createSpannableString(String message, Map<String, String> map) {

        SpannableString ss = new SpannableString(message);

        for (final Map.Entry<String, String> entry : map.entrySet()) {
            int start = 0;
            int end = 0;

            // リンク化対象の文字列の start, end を算出する
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
                break;
            }

            // SpannableString にクリックイベント、パラメータをセットする
            ss.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    String url = entry.getValue();
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        return ss;
    }
}
