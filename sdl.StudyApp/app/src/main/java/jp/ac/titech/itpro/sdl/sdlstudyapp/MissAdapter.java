package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MissAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Miss> misses;

    public class ViewHolder{
        TextView text;
        TextView count;
    }


    public MissAdapter(Context context, ArrayList<Miss> misses) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.misses = misses;
    }

    // Listの要素数を返す
    @Override
    public int getCount() {
        return misses.size();
    }

    // indexやオブジェクトを返す
    @Override
    public Object getItem(int position) {
        return misses.get(position);
    }

    // IDを他のindexに返す
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 新しいデータが表示されるタイミングで呼び出される
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.miss_list, parent, false);
            holder = new ViewHolder();
            holder.text = view.findViewById(R.id.name_view);
            holder.count = view.findViewById(R.id.count_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.text.setText(misses.get(position).getTitle());
        holder.count.setText(String.valueOf(misses.get(position).getCount()));

        return view;
    }
}
