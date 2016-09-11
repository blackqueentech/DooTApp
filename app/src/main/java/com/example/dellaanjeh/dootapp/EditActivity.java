package com.example.dellaanjeh.dootapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;
import com.michaldrabik.tapbarmenulib.TapBarMenu;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    TextView tvName, tvPriority, tvNotes, tvStatus, tvDooDate;
    EditText etName, etNotes, etDooDate;
    DBHelper dbHelper;
    Spinner spStatus, spPriority;
    String[] statuses, priorities;
    DatePickerDialog dooDatePicker;
    SimpleDateFormat dateFormat;
    ArrayAdapter<String> statusAdapter, priorityAdapter;
    String name, priority, dooDate, status, notes;
    Integer id;
    BottomNavigationView navbar;

    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("EXTRA_NAME");
            priority = extras.getString("EXTRA_PRIORITY");
            dooDate = extras.getString("EXTRA_DOO_DATE");
            notes = extras.getString("EXTRA_NOTES");
            status = extras.getString("EXTRA_STATUS");
            id = extras.getInt("EXTRA_ID");
        }
        tvName = (TextView) findViewById(R.id.tvName);
        etName = (EditText) findViewById(R.id.etName);
        etName.setText(name, TextView.BufferType.EDITABLE);
        tvDooDate = (TextView) findViewById(R.id.tvDooDate);
        etDooDate = (EditText) findViewById(R.id.etDooDate);
        etDooDate.setText(dooDate, TextView.BufferType.EDITABLE);
        etDooDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                dooDatePicker = new DatePickerDialog(EditActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDooDate.setText(dateFormat.format(newDate.getTime()));
                    }

                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dooDatePicker.show();
            }
        });
        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

        tvNotes = (TextView) findViewById(R.id.tvNotes);
        etNotes = (EditText) findViewById(R.id.etNotes);
        etNotes.setText(notes, TextView.BufferType.EDITABLE);

        tvPriority = (TextView) findViewById(R.id.tvPriority);
        spPriority = (Spinner) findViewById(R.id.spPriority);
        priorities = new String[]{"Low", "Medium", "High"};
        priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setSelection(getIndex(spPriority, priority));
        spPriority.setAdapter(priorityAdapter);

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        spStatus = (Spinner) findViewById(R.id.spStatus);
        statuses = new String[]{"To Do", "Done"};
        statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setSelection(getIndex(spStatus, status));
        spStatus.setAdapter(statusAdapter);
        dbHelper = new DBHelper(this);

        navbar = (BottomNavigationView) findViewById(R.id.navbar);
        BottomNavigationItem save = new BottomNavigationItem
                ("Save", ContextCompat.getColor(this, R.color.primary), R.drawable.save);
        BottomNavigationItem delete = new BottomNavigationItem
                ("Delete", ContextCompat.getColor(this, R.color.primary), R.drawable.trash);
        navbar.addTab(save);
        navbar.addTab(delete);


        navbar.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                switch (index) {
                    case 0:
                        String name = etName.getText().toString();
                        String notes = etNotes.getText().toString();
                        String status = String.valueOf(spStatus.getSelectedItem());
                        String doodate = etDooDate.getText().toString();
                        String priority = String.valueOf(spPriority.getSelectedItem());
                        dbHelper.editDoot(id, name, doodate, notes, status, priority);
                        setResult(Activity.RESULT_OK);
                        EditActivity.this.finish();
                        Toast.makeText(getBaseContext(), "Doot has been updated!", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        showDeleteDialog();
                        break;
                }
            }
        });

    }

    private void showDeleteDialog() {
        new LovelyStandardDialog(this)
                .setTopColorRes(R.color.delete)
                .setButtonsColorRes(R.color.primary_dark)
                .setIcon(R.drawable.trash)
                .setTitle("Are you sure you want to delete your doot?")
                .setMessage("This is a strong edit.")
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(Integer.toString(id), "doot ID");
                        helper.deleteDoot(id);
                        EditActivity.this.finish();
                        Toast.makeText(getBaseContext(), "Doot deleted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            String name = etName.getText().toString();
            String notes = etNotes.getText().toString();
            String status = String.valueOf(spStatus.getSelectedItem());
            String doodate = etDooDate.getText().toString();
            String priority = String.valueOf(spPriority.getSelectedItem());
            dbHelper.editDoot(id, name, doodate, notes, status, priority);
            setResult(Activity.RESULT_OK);
            EditActivity.this.finish();
            Toast.makeText(getBaseContext(), "Doot has been updated!", Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.action_back) {
            setResult(Activity.RESULT_CANCELED);
            EditActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
