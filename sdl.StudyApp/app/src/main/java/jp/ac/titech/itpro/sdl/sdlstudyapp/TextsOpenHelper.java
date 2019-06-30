package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TextsOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TextsData.db";
    protected static final String TABLE_NAME = "TextsDatadb";
    protected static final String _ID = "_id";
    protected static final String COLUMN_NAME_TITLE = "title";
    protected static final String COLUMN_NAME_PICTURE = "picture";
    protected static final String COLUMN_NAME_TEXT = "text";
    protected static final String COLUMN_NAME_TIME = "time";
    protected static final String COLUMN_NAME_STARTX = "startx";
    protected static final String COLUMN_NAME_STARTY = "starty";
    protected static final String COLUMN_NAME_ENDX = "endx";
    protected static final String COLUMN_NAME_ENDY = "endy";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_PICTURE + " TEXT," +
                    COLUMN_NAME_TEXT + " TEXT," +
                    COLUMN_NAME_STARTX + " INTEGER,"  +
                    COLUMN_NAME_STARTY + " INTEGER," +
                    COLUMN_NAME_ENDX+ " INTEGER," +
                    COLUMN_NAME_ENDY + " INTEGER," +
                    COLUMN_NAME_TIME + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    TextsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                SQL_CREATE_ENTRIES
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // アップデートの判別
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
