package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URI;

public class AddPrepareActivity extends AppCompatActivity {

    protected ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prepare);
        imageView = findViewById(R.id.tmp_pict);

        Uri image_uri = Uri.parse(getIntent().getStringExtra("image_URI"));
        Log.d("Uri", image_uri.toString());
        Bitmap bmp;
        try {
            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uri);
        }catch (IOException e) {
            e.printStackTrace();
            bmp = null;
        }

        imageView.setImageBitmap(bmp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            default:
                break;
        }
    }
}
