package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.DeniedByServerException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ReviewActivity extends AppCompatActivity {

    private final static int REQ_NEW_TITLE = 1234;

    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    private MissDBAdapter dbAdapter;

    private Button add;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        add = findViewById(R.id.add_review);
        listView = findViewById(R.id.list_review);

        dbAdapter = new MissDBAdapter(this);
        setListView();

        if(add == null) Log.d("Button", "null");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, AddReviewActivity.class);
                //intent.putExtra((content name), data);
                startActivityForResult(intent, REQ_NEW_TITLE);
            }
        });

    }
    private void setListView(){
        dbAdapter.openDB();
        items = new ArrayList<>();

        //obtain DB data
        String[] cols = {MissesOpenHelper.COLUMN_NAME_TITLE};
        Cursor c = dbAdapter.getDB(cols);

        if(c.moveToFirst()){
            do{
                items.add(c.getString(0));
                Log.d("Cursor:", c.getString(0));
            }while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();


        //settings of ListView
        // simple_list_item_1 は、 もともと用意されている定義済みのレイアウトファイルのID
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);//表示

        //リスト項目が選択された時
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = items.get(position);

            }
        });

        //リスト項目が長押しされた時
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = position + "番目のアイテムが長押しされました";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        switch (reqCode) {
            case REQ_NEW_TITLE:
                if (resCode == RESULT_OK) {
                    String name = data.getStringExtra(MissesOpenHelper.COLUMN_NAME_TITLE);
                    Log.d("New Title", name);
                    if (name != null && !name.isEmpty()) {
                        MissDBAdapter dbAdapter = new MissDBAdapter(this);
                        dbAdapter.openDB();
                        dbAdapter.saveDB(name, 1);
                        dbAdapter.closeDB();
                        setListView();
                        Toast.makeText(this, "新しい間違いのタイプが追加されました。", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                }
                break;
        }

    }

}
