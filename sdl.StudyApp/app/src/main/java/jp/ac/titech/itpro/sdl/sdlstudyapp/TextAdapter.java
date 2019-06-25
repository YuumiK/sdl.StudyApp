package jp.ac.titech.itpro.sdl.sdlstudyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TextAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ArrayList<Text> texts;

    public class ViewHolder{
        ImageView imageView;
        TextView textView;
    }

    TextAdapter(Context context){
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    public TextAdapter(Context context, ArrayList<Text> texts) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.texts = texts;
    }

    // Listの要素数を返す
    @Override
    public int getCount() {
        return texts.size();
    }

    // indexやオブジェクトを返す
    @Override
    public Object getItem(int position) {
        return texts.get(position);
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
        TextAdapter.ViewHolder holder;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.text_list, parent, false);
            holder = new TextAdapter.ViewHolder();
            holder.imageView = view.findViewById(R.id.image_view_prepare);
            holder.textView = view.findViewById(R.id.text_view_prepare);
            view.setTag(holder);
        } else {
            holder = (TextAdapter.ViewHolder) view.getTag();
        }

        holder.imageView.setImageBitmap(texts.get(position).getPict());
        holder.textView.setText(texts.get(position).getTitle());

        return view;
    }
}
