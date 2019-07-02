package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class ReviewActivity extends AppCompatActivity {

    private final static int REQ_NEW_TITLE = 1234;

    private ArrayList<Miss> items;
    private BaseAdapter adapter;
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, AddReviewActivity.class);
                //intent.putExtra((content name), data);
                startActivityForResult(intent, REQ_NEW_TITLE);
            }
        });

        //リスト項目が選択された時
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = items.get(position).getTitle();
                int count = items.get(position).getCount() + 1;
                dbAdapter.openDB();
                dbAdapter.update(name, count);
                dbAdapter.closeDB();
                setListView();
            }
        });

        //リスト項目が長押しされた時
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder
                        = new AlertDialog.Builder(ReviewActivity.this);
                final NumberPicker numberPicker = new NumberPicker(ReviewActivity.this);
                numberPicker.setMaxValue(Integer.MAX_VALUE);
                numberPicker.setMinValue(0);

                final int pos = position;
                numberPicker.setValue(items.get(position).getCount());
                builder.setMessage("間違い数を調整します。0の場合、項目が消滅します")
                        .setTitle(items.get(position).getTitle())
                        .setView(numberPicker)
                        .setPositiveButton("OK" , new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                int num = numberPicker.getValue();
                                dbAdapter.openDB();
                                if(num == 0){
                                    Log.d("number picker", "0");
                                    dbAdapter.selectDelete(items.get(pos).getTitle());
                                }
                                else{
                                    dbAdapter.update(items.get(pos).getTitle(), num);
                                }
                                dbAdapter.closeDB();
                                setListView();
                            }
                        })
                        .setNegativeButton("CANCEL", null)
                        .show();

                return false;
            }
        });

    }
    private void setListView(){
        dbAdapter.openDB();
        items = new ArrayList<>();

        //obtain DB data
        String[] cols = null;
        Cursor c = dbAdapter.getDB(cols);

        if(c.moveToFirst()){
            do{
                Miss m = new Miss(
                        c.getInt(0),
                        c.getString(1),
                        c.getInt(2)
                        );
                items.add(m);
            }while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();
        sortListView();

        //settings of ListView
        adapter = new MissAdapter(this, items);
        listView.setAdapter(adapter);//表示
    }
    public void sortListView(){
        Collections.sort(items, new MissComparatorByCount());
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
                break;
        }

    }

}
