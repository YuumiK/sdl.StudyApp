package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PrepareActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final static int REQ_NEW_TITLE = 1234;
    private static final int REQUEST_CHOOSER = 2000;

    private final static int REQ_PERMISSIONS = 2222;
    private final static String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ArrayList<Text> items;
    TextAdapter adapter;
    private TextDBAdapter dbAdapter;

    private GridView gridView;
    private Button add;

    //URI of photo obtained
    private Uri m_uri;
    private String filepath;


    //when the item on the grid is clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Intent intent = new Intent(getApplication(), SubActivity.class);
        //startActivity( intent );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);
        gridView = findViewById(R.id.grid_prepare);
        add = findViewById(R.id.add_prepare);

        dbAdapter = new TextDBAdapter(this);
        setGridView();

        gridView.setOnItemClickListener(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoto();
            }
        });

    }

    private void setGridView() {
        dbAdapter.openDB();
        items = new ArrayList<>();

        //obtain DB data
        String[] cols = null;
        Cursor c = dbAdapter.getDB(cols);

        if (c.moveToFirst()) {
            do {
                //Blob to bitmap
                byte[] byteArray = c.getBlob(2);
                Bitmap pict = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                Text t = new Text(
                        c.getInt(0),
                        c.getString(1),
                        pict,
                        c.getString(3),
                        c.getInt(4)
                );
                items.add(t);
            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();


        //settings of GridView
        adapter = new TextAdapter(this, items);
        gridView.setAdapter(adapter);//表示
    }

    private void getPhoto() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQ_PERMISSIONS);
                return;
            }
        }

        //camera
        File cameraFolder = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM),"Camera");

        String name =  System.currentTimeMillis() + ".jpg";
        filepath = String.format("%s/%s", cameraFolder.getPath(), name);


        File cameraFile = new File(filepath);
        m_uri = FileProvider.getUriForFile(
                PrepareActivity.this,
                getApplicationContext().getPackageName() + ".fileprovider",
                cameraFile);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, m_uri);

        Intent intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
        intentGallery.setType("image/jpeg");

        Intent intent = Intent.createChooser(intentCamera, "Select Photo");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intentGallery});
        startActivityForResult(intent, REQUEST_CHOOSER);
    }


    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        switch (reqCode) {
            case REQ_NEW_TITLE:
                if (resCode == RESULT_OK) {
                    String name = data.getStringExtra(TextsOpenHelper.COLUMN_NAME_TITLE);
                    Log.d("New Title", name);
                    if (name != null && !name.isEmpty()) {
                        TextDBAdapter dbAdapter = new TextDBAdapter(this);
                        dbAdapter.openDB();
                        //dbAdapter.saveDB();
                        dbAdapter.closeDB();
                        setGridView();
                        Toast.makeText(this, "新しいテキストが追加されました。", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                }
            case REQUEST_CHOOSER:

                //画像処理キャンセル
                if(resCode != RESULT_OK) return;


                Uri resultUri;
                //ギャラリーから画像を取得した場合
                if(data != null) resultUri = data.getData();
                //カメラで写真をとった場合 uri : m_uri
                else{
                    Log.d("Camera", "true");
                    resultUri = m_uri;
                    ContentValues contentValues = new ContentValues();
                    ContentResolver contentResolver = PrepareActivity.this.getContentResolver();
                    contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    contentValues.put("_data", filepath);
                    contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                }
                if(resultUri == null) return;

                Log.d("URI", resultUri.getEncodedPath());

                MediaScannerConnection.scanFile(
                        this,
                        new String[]{resultUri.getPath()},
                        new String[]{"image/jpeg"},
                        null
                );


                //写真を表示するインテントへ

                Intent intent = new Intent(PrepareActivity.this, AddPrepareActivity.class);
                intent.putExtra("image_URI", resultUri.toString());
                startActivityForResult(intent, REQ_NEW_TITLE);
                break;
        }

    }
}