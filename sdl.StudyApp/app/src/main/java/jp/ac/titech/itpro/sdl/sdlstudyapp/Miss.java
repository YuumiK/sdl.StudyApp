package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.util.Log;
import java.util.Comparator;

public class Miss {
    private int id;           // ID
    private String title;     // 名称
    private int count;        // 回数

    /**
     * MyListItem()
     *
     * @param id      int ID
     * @param title   String 名称
     * @param count   int 回数
     */
    public Miss(int id, String title, int count) {
        this.id = id;
        this.title = title;
        this.count = count;
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
     * 個数を取得
     * getCount()
     *
     * @return count int 個数
     */
    public int getCount() {
        return count;
    }

}

class MissComparatorByCount implements Comparator<Miss> {

    @Override
    public int compare(Miss m1, Miss m2) {
        if(m1.getCount() > m2.getCount()) return -1;
        else if(m1.getCount() == m2.getCount()) return 0;
        return 1;
    }
}
