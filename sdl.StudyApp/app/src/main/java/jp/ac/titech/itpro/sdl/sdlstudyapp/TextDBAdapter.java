package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TextDBAdapter {

    private SQLiteDatabase db = null;           // SQLiteDatabase
    private TextsOpenHelper dbHelper;   // DBHelper
    protected Context context;                  // Context

    // コンストラクタ
    public TextDBAdapter(Context context) {
        this.context = context;
        dbHelper = new TextsOpenHelper(this.context);
    }

    /**
     * DBの読み書き
     * openDB()
     *
     * @return this 自身のオブジェクト
     */
    public TextDBAdapter openDB() {
        db = dbHelper.getWritableDatabase();        // DBの読み書き
        return this;
    }

    /**
     * DBの読み込み
     * readDB()
     *
     * @return this 自身のオブジェクト
     */
    public TextDBAdapter readDB() {
        db = dbHelper.getReadableDatabase();        // DBの読み込み
        return this;
    }

    /**
     * DBを閉じる
     * closeDB()
     */
    public void closeDB() {
        db.close();     // DBを閉じる
        db = null;
    }

    /**
     * DBのレコードへ登録
     * saveDB()
     *
     * @param title 名称
     * @param text 文章
     * @param time 時間
     */
    public void saveDB(String title, String text, int time) {


        db.beginTransaction();          // トランザクション開始

        try {
            ContentValues values = new ContentValues();     // ContentValuesでデータを設定していく
            values.put(TextsOpenHelper.COLUMN_NAME_TITLE, title);
            Log.d("saveDB", title);
            values.put(TextsOpenHelper.COLUMN_NAME_TEXT, text);
            Log.d("saveDB", text);
            values.put(TextsOpenHelper.COLUMN_NAME_TIME, time);
            Log.d("saveDB", Integer.toString(time));
            //重複がないか検索
            Cursor c = searchDB(null, TextsOpenHelper.COLUMN_NAME_TITLE, new String[]{title});
            if(!c.moveToFirst()) {
                // insertメソッド データ登録
                // 第1引数：DBのテーブル名
                // 第2引数：更新する条件式
                // 第3引数：ContentValues
                db.insert(TextsOpenHelper.TABLE_NAME, null, values);      // レコードへ登録
            }
            else throw new Exception("Overlapping");
            db.setTransactionSuccessful();      // トランザクションへコミット
            Log.d("saveDB", "successful");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                // トランザクションの終了
        }
    }

    /**
     * DBのデータを取得
     * getDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    public Cursor getDB(String[] columns) {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(TextsOpenHelper.TABLE_NAME, columns, null, null, null, null, null);
    }

    /**
     * DBの検索したデータを取得
     * searchDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @param column  String 選択条件に使うカラム名
     * @param name    String[]
     * @return DBの検索したデータ
     */
    public Cursor searchDB(String[] columns, String column, String[] name) {
        return db.query(TextsOpenHelper.TABLE_NAME, columns, column + " like ?", name, null, null, null);
    }

    /**
     * DBのレコードを全削除
     * allDelete()
     */
    public void allDelete() {

        db.beginTransaction();                      // トランザクション開始
        try {
            // deleteメソッド DBのレコードを削除
            // 第1引数：テーブル名
            // 第2引数：削除する条件式 nullの場合は全レコードを削除
            // 第3引数：第2引数で?を使用した場合に使用
            db.delete(TextsOpenHelper.TABLE_NAME, null, null);        // DBのレコードを全削除
            db.setTransactionSuccessful();          // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                    // トランザクションの終了
        }
    }



    /**
     * DBのレコードの単一削除
     * selectDelete()
     *
     * @param position String
     */
    public void selectDelete(String position) {

        db.beginTransaction();                      // トランザクション開始
        try {
            db.delete(TextsOpenHelper.TABLE_NAME, TextsOpenHelper._ID + "=?", new String[]{position});
            db.setTransactionSuccessful();          // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                    // トランザクションの終了
        }
    }
}