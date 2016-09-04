package com.example.dellaanjeh.dootapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dellaanjeh on 9/3/16.
 */
public class DootListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Doot> dootList;

    public DootListAdapter(Context c, ArrayList<Doot> list) {
        this.context = c;
        dootList = list;
    }

    @Override
    public int getCount() {
        return dootList.size();
    }

    @Override
    public Object getItem(int position) {
        return dootList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        Doot item = dootList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tasklayout, null);

        }

        TextView tvTaskName = (TextView) convertView.findViewById(R.id.tvTaskName);
        tvTaskName.setText(item.getName());
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDate);
        tvDueDate.setText(item.getDooDate());

        if (item.getStatus().equals("Completed")) {
            tvTaskName.setPaintFlags(tvTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            tvTaskName.setPaintFlags(tvTaskName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        return convertView;
    }

    public void setTodoList(ArrayList<Doot> dootList) {
        this.dootList = dootList;
        notifyDataSetChanged();
    }
}
