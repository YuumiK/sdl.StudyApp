package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.util.Log;

public class Text {
    private int id;           // ID
    private String title;     // 名称
    private String text;      // 文章
    private int time; //時間
    /**
     * MyListItem()
     *
     * @param id      int ID
     * @param title   String 名称
     * @param text    String 文章
     * @param time    int 時間
     */
     public Text(int id, String title, String text, int time) {
        this.id = id;
        this.title = title;
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
