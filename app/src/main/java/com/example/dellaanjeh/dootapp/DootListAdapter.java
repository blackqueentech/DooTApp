package com.example.dellaanjeh.dootapp;

import android.content.Context;
import android.graphics.Color;
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
            convertView = inflater.inflate(R.layout.doot_layout, null);

        }

        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        tvPriority.setText(item.getPriority());
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(item.getName());
        TextView tvDooDate = (TextView) convertView.findViewById(R.id.tvDate);
        tvDooDate.setText(item.getDooDate());

        if (item.getStatus().equals("Done")) {
            tvName.setPaintFlags(tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            tvName.setPaintFlags(tvName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        if (item.getPriority().equals("Low")) {
            tvPriority.setTextColor(Color.GREEN);
        } else if (item.getPriority().equals("Medium")) {
            tvPriority.setTextColor(Color.YELLOW);
        } else {
            tvPriority.setTextColor(Color.RED);
        }

        return convertView;
    }

    public void setDootList(ArrayList<Doot> dootList) {
        this.dootList = dootList;
        notifyDataSetChanged();
    }
}
