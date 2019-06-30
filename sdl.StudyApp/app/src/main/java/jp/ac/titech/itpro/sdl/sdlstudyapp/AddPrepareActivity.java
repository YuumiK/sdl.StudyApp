package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddPrepareActivity extends AppCompatActivity {

    protected ProgressBar progressBar;
    private Bitmap bmp;
    private Uri image_uri;
    private EditText titleEdit;
    private EditText timeEdit;
    private EditText textEdit;
    private Button add;
    private Rect textRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prepare);
        progressBar = findViewById(R.id.progress_circular);
        titleEdit = findViewById(R.id.title_prepare_add);
        timeEdit = findViewById(R.id.time_prepare_add);
        textEdit = findViewById(R.id.text_prepare_add);

        progressBar.setVisibility(ProgressBar.VISIBLE);
        titleEdit.setVisibility(EditText.INVISIBLE);
        timeEdit.setVisibility(EditText.INVISIBLE);
        textEdit.setVisibility(TextView.INVISIBLE);
        add = findViewById(R.id.OK_add_prepare);
        add.setVisibility(Button.INVISIBLE);
        image_uri = Uri.parse(getIntent().getStringExtra("image_URI"));
        Log.d("Uri", image_uri.toString());
        connectWithGoogleVisionCloudApi();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEdit.getText().toString().trim();
                String text = textEdit.getText().toString();
                int time = Integer.valueOf(timeEdit.getText().toString().trim());
                if (!text.isEmpty() && !title.isEmpty()) {
                    Intent data = new Intent();
                    data.putExtra(TextsOpenHelper.COLUMN_NAME_TITLE, title);
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
                                    textEdit.setVisibility(TextView.VISIBLE);
                                    add.setVisibility(Button.VISIBLE);
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
    private void setParameters(FirebaseVisionDocumentText result){
        String resultText = result.getText();
        Log.d("result", resultText);
        //ToDo text情報、時間推定
        final ArrayList<String> textBlocks = new ArrayList<String>();
        final ArrayList<Rect> textRects = new ArrayList<Rect>();
        for (FirebaseVisionDocumentText.Block block: result.getBlocks()) {
            String blockText = block.getText();
            Float blockConfidence = block.getConfidence();
            List<RecognizedLanguage> blockRecognizedLanguages = block.getRecognizedLanguages();
            Rect blockFrame = block.getBoundingBox();
            Log.d("BlockText", blockText);
            Log.d("BlockFrame", blockFrame.toString());
            if (getStringLine(blockText)>2){
                textBlocks.add(blockText);
                textRects.add(blockFrame);
            }
            for (FirebaseVisionDocumentText.Paragraph paragraph: block.getParagraphs()) {
                String paragraphText = paragraph.getText();
                Float paragraphConfidence = paragraph.getConfidence();
                List<RecognizedLanguage> paragraphRecognizedLanguages = paragraph.getRecognizedLanguages();
                Rect paragraphFrame = paragraph.getBoundingBox();
                for (FirebaseVisionDocumentText.Word word: paragraph.getWords()) {
                    String wordText = word.getText();
                    Float wordConfidence = word.getConfidence();
                    List<RecognizedLanguage> wordRecognizedLanguages = word.getRecognizedLanguages();
                    Rect wordFrame = word.getBoundingBox();
                    for (FirebaseVisionDocumentText.Symbol symbol: word.getSymbols()) {
                        String symbolText = symbol.getText();
                        Float symbolConfidence = symbol.getConfidence();
                        List<RecognizedLanguage> symbolRecognizedLanguages = symbol.getRecognizedLanguages();
                        Rect symbolFrame = symbol.getBoundingBox();
                    }
                }
            }
        }

        if(textBlocks.size() == 1)
        {
            textEdit.setText(textBlocks.get(0));
            timeEdit.setText(String.valueOf(getStringLine(textBlocks.get(0))+5), TextView.BufferType.NORMAL);
            textRect = textRects.get(0);
        }
        else{
            List<String> list = new ArrayList<>();
            for (String text:textBlocks) list.add(text.substring(0,9));
            final String[] items = list.toArray(new String[0]);
            new AlertDialog.Builder(this)
                    .setTitle("Selector")
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            textEdit.setText(textBlocks.get(which));
                            timeEdit.setText(String.valueOf(getStringLine(textBlocks.get(which))+5), TextView.BufferType.NORMAL);
                            textRect = textRects.get(which);
                        }
                    })
                    .show();
        }

    }

    protected int getStringLine(String text){
        String separator = System.getProperty("line.separator");
        Log.d("Separator", separator);
        String[] data = text.split(separator);
        return data.length;
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
