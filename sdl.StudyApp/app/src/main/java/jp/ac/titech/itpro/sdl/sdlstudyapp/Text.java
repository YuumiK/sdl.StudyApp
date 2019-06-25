package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.graphics.Bitmap;
import android.util.Log;

public class Text {
    protected int id;           // ID
    protected String title;     // 名称
    protected Bitmap pict;     //写真
    protected String text;      // 文章
    protected int time; //時間

    /**
     * MyListItem()
     *
     * @param id      int ID
     * @param title   String 名称
     * @param pict   Bitmap 写真
     * @param text    String 文章
     * @param time    int 時間
     */
     public Text(int id, String title, Bitmap pict, String text, int time) {
        this.id = id;
        this.title = title;
        this.pict = pict;
        this.text = text;
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
     * 写真を取得
     * getPicts()
     *
     * @return pict Bitmap 写真
     */
    public Bitmap getPict(){
        return pict;
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
}