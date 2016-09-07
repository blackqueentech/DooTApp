package com.example.dellaanjeh.dootapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DootDetailsActivity extends AppCompatActivity {

    private static final int EDIT_REQUEST = 123;
    // labels
    TextView tvNameLabel;
    TextView tvDooDateLabel;
    TextView tvStatusLabel;
    TextView tvNotesLabel;
    TextView tvPriorityLabel;
    // values
    TextView tvName;
    TextView tvDooDate;
    TextView tvNotes;
    TextView tvStatus;
    TextView tvPriority;
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

        tvNameLabel = (TextView) findViewById(R.id.tvNameLabel);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPriorityLabel = (TextView) findViewById(R.id.tvPriorityLabel);
        tvPriority = (TextView) findViewById(R.id.tvPriority);
        tvDooDateLabel = (TextView) findViewById(R.id.tvDooDateLabel);
        tvDooDate = (TextView) findViewById(R.id.tvDooDate);
        tvStatusLabel = (TextView) findViewById(R.id.tvStatusLabel);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvNotesLabel = (TextView) findViewById(R.id.tvNotesLabel);
        tvNotes = (TextView) findViewById(R.id.tvNotes);
        adapter = new DootListAdapter(DootDetailsActivity.this, dootList);
        helper = new DBHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("EXTRA_NAME");
            priority = extras.getString("EXTRA_PRIORITY");
            dooDate = extras.getString("EXTRA_DOO_DATE");
            notes = extras.getString("EXTRA_NOTES");
            status = extras.getString("EXTRA_STATUS");
            id = extras.getInt("EXTRA_ID");
        }

        tvName.setText(name);
        tvPriority.setText(priority);
        tvDooDate.setText(dooDate);
        tvNotes.setText(notes);
        tvStatus.setText(status);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete){
//            LayoutInflater li = LayoutInflater.from(context);
//            View view = li.inflate(R.layout.delete_dialog, null);
//
//
//            ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.ToDonutDialog);
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
//
//            final TextView message = (TextView) view
//                    .findViewById(R.id.tvDeleteMessage);
//
//            alertDialogBuilder
//                    .setCancelable(false)
//                    .setPositiveButton("Yes",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,int id) {
//                                    Log.d(Integer.toString(taskId), "task ID");
//                                    helper.deleteTask(taskId);
//                                    TaskDetailsActivity.this.finish();
//                                    Toast.makeText(getBaseContext(), "Task deleted!", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                    .setNegativeButton("No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.setTitle("Are you sure you want to delete this task?");
//            alertDialog.show();
        } else if(item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(DootDetailsActivity.this, EditActivity.class);
            intent.putExtra("EXTRA_ID", id);
            intent.putExtra("EXTRA_NAME", name);
            intent.putExtra("EXTRA_DOO_DATE", dooDate);
            intent.putExtra("EXTRA_PRIORITY", priority);
            intent.putExtra("EXTRA_STATUS", status);
            intent.putExtra("EXTRA_NOTES", notes);
            startActivityForResult(intent,EDIT_REQUEST);
        } else if(item.getItemId() == R.id.action_back) {
            DootDetailsActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Doot doot = helper.getDoot(id);
        tvName.setText(doot.getName());
        tvDooDate.setText(doot.getDooDate());
        tvNotes.setText(doot.getNotes());
        tvStatus.setText(doot.getStatus());
        tvPriority.setText(doot.getPriority());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
    }
}
