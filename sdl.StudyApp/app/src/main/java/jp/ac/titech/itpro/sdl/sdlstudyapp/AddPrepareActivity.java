package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionCloudDocumentRecognizerOptions;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.IOException;
import java.util.Arrays;

public class AddPrepareActivity extends AppCompatActivity {

    protected ProgressBar progressBar;
    private Bitmap bmp;
    private Uri image_uri;
    private EditText titleEdit;
    private EditText timeEdit;
    private TextView textView;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prepare);
        progressBar = findViewById(R.id.progress_circular);
        titleEdit = findViewById(R.id.title_prepare_add);
        timeEdit = findViewById(R.id.time_prepare_add);
        textView = findViewById(R.id.text_prepare_add);

        progressBar.setVisibility(ProgressBar.VISIBLE);
        titleEdit.setVisibility(EditText.INVISIBLE);
        timeEdit.setVisibility(EditText.INVISIBLE);
        textView.setVisibility(TextView.INVISIBLE);
        add = findViewById(R.id.OK_add_prepare);
        add.setVisibility(Button.INVISIBLE);
        image_uri = Uri.parse(getIntent().getStringExtra("image_URI"));
        Log.d("Uri", image_uri.toString());
        connectWithGoogleVisionCloudApi();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEdit.getText().toString().trim();
                String text = textView.getText().toString();
                int time = Integer.valueOf(timeEdit.getText().toString().trim());
                if (!text.isEmpty() && !title.isEmpty()) {
                    Intent data = new Intent();
                    data.putExtra(TextsOpenHelper.COLUMN_NAME_TITLE, title);
                    data.putExtra("image_URI", image_uri.toString());
                    data.putExtra(TextsOpenHelper.COLUMN_NAME_TEXT, text);
                    data.putExtra(TextsOpenHelper.COLUMN_NAME_TIME, time);
                    setResult(RESULT_OK, data);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
    }
    private void connectWithGoogleVisionCloudApi(){
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
            //bmp = binarize(bmp);
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bmp);
            FirebaseVisionCloudDocumentRecognizerOptions options = new FirebaseVisionCloudDocumentRecognizerOptions.Builder()
                    .setLanguageHints(Arrays.asList("en", "hi"))
                    .build();
            FirebaseVisionDocumentTextRecognizer detector = FirebaseVision.getInstance().getCloudDocumentTextRecognizer(options);


            Task<FirebaseVisionDocumentText> result =
                    detector.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionDocumentText>() {
                                @Override
                                public void onSuccess(FirebaseVisionDocumentText firebaseVisionDocumentText) {
                                    // Task completed successfully
                                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                                    titleEdit.setVisibility(EditText.VISIBLE);
                                    timeEdit.setVisibility(EditText.VISIBLE);
                                    textView.setVisibility(TextView.VISIBLE);
                                    setParameters(firebaseVisionDocumentText);
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                                        }
                                    });

        }catch (IOException e) {
            e.printStackTrace();
            setResult(RESULT_CANCELED);
            finish();
        }
    }
    private void setParameters(FirebaseVisionDocumentText firebaseVisionDocumentText){
        String resultText = firebaseVisionDocumentText.getText();
        Log.d("result", resultText);
        //ToDo text情報、時間推定
        textView.setText(resultText);
        timeEdit.setText("0", TextView.BufferType.NORMAL);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            default:
                break;
        }
    }

    public static Bitmap binarize(Bitmap bmp) {
        Bitmap outBitMap = bmp.copy(Bitmap.Config.ARGB_8888, true);

        int width = outBitMap.getWidth();
        int height = outBitMap.getHeight();

        int i, j;
        for (j = 0; j < height; j++) {
            for (i = 0; i < width; i++) {
                int pixelColor = outBitMap.getPixel(i, j);
                int y = (int) (0.299 * Color.red(pixelColor) + 0.587 * Color.green(pixelColor) + 0.114 * Color
                        .blue(pixelColor));
                y = y > 127 ? 255 : 0;
                outBitMap.setPixel(i, j, Color.rgb(y, y, y));
            }
        }

        return outBitMap;
    }

}
