package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;

public class Text {
    protected int id;           // ID
    protected String title;     // 名称
    protected Uri pict_uri;     //写真
    protected String text;      // 文章
    protected int time; //時間
    protected Rect range;
    /**
     * MyListItem()
     *
     * @param id      int ID
     * @param title   String 名称
     * @param pict_uri Uri 写真
     * @param text    String 文章
     * @param time    int 時間
     */
     public Text(int id, String title, Uri pict_uri, String text,  Rect range, int time) {
        this.id = id;
        this.title = title;
        this.pict_uri = pict_uri;
        this.text = text;
        this.range = range;
        this.time = time;
     }

    /**
     * IDを取得
     * getId()
     *
     * @return id int ID
     */
    public int getId() {
        Log.d("取得したID：", String.valueOf(id));
        return id;
    }

    /**
     * 名称を取得
     * getTitle()
     *
     * @return title String 名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 写真のURIを取得
     * getPictsUri()
     *
     * @return pict_uri Uri 写真
     */
    public Uri getPictsUri(){
        return pict_uri;
    }

    /**
     * 文章を取得
     * getTitle()
     *
     * @return text String 文章
     */
    public String getText(){
        return text;
    }

    /**
     * 時間を取得
     * getTime()
     *
     * @return time int 時間
     */
    public int getTime() {
        return time;
    }

    public Rect getRect()
    {
        return range;
    }
}
