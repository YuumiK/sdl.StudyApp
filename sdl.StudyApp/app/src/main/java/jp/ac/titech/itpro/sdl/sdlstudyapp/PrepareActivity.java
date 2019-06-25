package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class PrepareActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private final static int REQ_NEW_TITLE = 1234;

    ArrayList<Text> items;
    TextAdapter adapter;
    private TextDBAdapter dbAdapter;

    private GridView gridView;
    private Button add;


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
                Intent intent = new Intent(PrepareActivity.this, AddPrepareActivity.class);
                //intent.putExtra((content name), data);
                startActivityForResult(intent, REQ_NEW_TITLE);
            }
        });

    }

    private void setGridView(){
        dbAdapter.openDB();
        items = new ArrayList<>();

        //obtain DB data
        String[] cols = null;
        Cursor c = dbAdapter.getDB(cols);

        if(c.moveToFirst()){
            do{
                //Blob to bitmap
                byte[] byteArray = c.getBlob(2);
                Bitmap pict = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);

                Text t = new Text(
                        c.getInt(0),
                        c.getString(1),
                        pict,
                        c.getString(3),
                        c.getInt(4)
                );
                items.add(t);
            }while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();


        //settings of GridView
        adapter = new TextAdapter(this, items);
        gridView.setAdapter(adapter);//表示
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
                        dbAdapter.saveDB(name, 1);
                        dbAdapter.closeDB();
                        setView();
                        Toast.makeText(this, "新しいテキストが追加されました。", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                }
                break;
        }

    }
}
