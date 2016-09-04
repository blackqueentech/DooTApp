package com.example.dellaanjeh.dootapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DootDetailsActivity extends AppCompatActivity {

    private static final int EDIT_REQUEST = 123;
    // labels
    @BindView(R.id.tvNameLabel) TextView tvNameLabel;
    @BindView(R.id.tvDooDateLabel) TextView tvDooDateLabel;
    @BindView(R.id.tvStatusLabel) TextView tvStatusLabel;
    @BindView(R.id.tvNotesLabel) TextView tvNotesLabel;
    @BindView(R.id.tvPriorityLabel) TextView tvPriorityLabel;
    // values
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvDooDate) TextView tvDooDate;
    @BindView(R.id.tvNotes) TextView tvNotes;
    @BindView(R.id.tvStatus) TextView tvStatus;
    @BindView(R.id.tvPriority) TextView tvPriority;
    String name, dooDate, status, notes, priority;
    DBHelper helper;
    DootListAdapter adapter;
    ArrayList<Doot> dootList;
    Integer id;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doot_details);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Doot doot = helper.getDoot(id);
        tvName.setText(doot.getName());
        tvPriority.setText(doot.getPriority());
        tvDooDate.setText(doot.getDooDate());
        tvNotes.setText(doot.getNotes());
        tvStatus.setText(doot.getStatus());
    }
}
