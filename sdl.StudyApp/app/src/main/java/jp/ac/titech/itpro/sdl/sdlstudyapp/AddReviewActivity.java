package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddReviewActivity extends AppCompatActivity {

    private static final String TITECH = "東工大";
    private static final String PASS = "合格!!!";

    private EditText editText;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        editText = findViewById(R.id.entry_add);
        add = findViewById(R.id.OK_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if (!name.isEmpty()) {
                    if(name.equals(TITECH)){
                        name = PASS;
                    }
                    Intent data = new Intent();
                    data.putExtra(MissesOpenHelper.COLUMN_NAME_TITLE, name);
                    setResult(RESULT_OK, data);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });

    }
}
