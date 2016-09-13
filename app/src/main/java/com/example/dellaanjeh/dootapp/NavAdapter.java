package com.example.dellaanjeh.dootapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dellaanjeh on 9/12/16.
 */
public class NavAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<NavItem> mNavItems;

    public NavAdapter(Context context, ArrayList<NavItem> navItems) {
        mContext = context;
        mNavItems = navItems;
    }

    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.nav_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.tvNavSubtitle);
        TextView subtitleView = (TextView) view.findViewById(R.id.tvNavSubtitle);
        ImageView iconView = (ImageView) view.findViewById(R.id.ivNavIcon);

        titleView.setText( mNavItems.get(position).navTitle );
        subtitleView.setText( mNavItems.get(position).navSubtitle );
        iconView.setImageResource(mNavItems.get(position).navIcon);

        return view;
    }
}
