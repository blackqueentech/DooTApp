package com.example.dellaanjeh.dootapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditDootActivity extends AppCompatActivity {

    TextView tvTaskName, tvTaskNotes, tvStatus, tvDooDate, tvPriority;
    EditText etTaskName, etTaskNotes, etDooDate;
    String taskName, dooDate, status, notes, priority;
    Long id;
    DBHelper dbHelper;
    Spinner spStatus,spPriority;
    String[] statuses, priorities;
    DatePickerDialog dooDatePicker;
    SimpleDateFormat dateFormat;
    ArrayAdapter<String> statusAdapter, priorityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doot);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            taskName = extras.getString("EXTRA_NAME");
            dooDate = extras.getString("EXTRA_DUE_DATE");
            notes = extras.getString("EXTRA_NOTES");
            status = extras.getString("EXTRA_STATUS");
            priority = extras.getString("EXTRA_PRIORITY");
            id = extras.getLong("EXTRA_ID");
        }
        tvTaskName = (TextView) findViewById(R.id.tvName);
        etTaskName = (EditText) findViewById(R.id.etName);
        etTaskName.setText(taskName, TextView.BufferType.EDITABLE);
        tvDooDate = (TextView) findViewById(R.id.tvDooDate);
        etDooDate = (EditText) findViewById(R.id.etDooDate);
        etDooDate.setText(dooDate, TextView.BufferType.EDITABLE);
        etDooDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                dooDatePicker = new DatePickerDialog(EditDootActivity.this, new DatePickerDialog.OnDateSetListener() {

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

        tvTaskNotes = (TextView) findViewById(R.id.tvNotes);
        etTaskNotes = (EditText) findViewById(R.id.etNotes);
        etTaskNotes.setText(notes, TextView.BufferType.EDITABLE);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        spStatus = (Spinner) findViewById(R.id.spStatus);
        statuses = new String[]{"To Do", "Done"};
        statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setSelection(getIndex(spStatus, status));
        spStatus.setAdapter(statusAdapter);

        tvPriority = (TextView) findViewById(R.id.tvPriority);
        spPriority = (Spinner) findViewById(R.id.spPriority);
        priorities = new String[]{"Low", "Medium", "High"};
        priorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setSelection(getIndex(spPriority, priority));
        spPriority.setAdapter(priorityAdapter);
        dbHelper = new DBHelper(this);
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
            String name = etTaskName.getText().toString();
            String notes = etTaskNotes.getText().toString();
            String status = String.valueOf(spStatus.getSelectedItem());
            String doodate = etDooDate.getText().toString();
            String priority = String.valueOf(spPriority.getSelectedItem());
            dbHelper.editDoot(id, name, doodate, notes, status, priority);
            setResult(Activity.RESULT_OK);
            EditDootActivity.this.finish();
            Toast.makeText(getBaseContext(), "Doot has been updated!", Toast.LENGTH_SHORT).show();
        } else if(item.getItemId() == R.id.action_back) {
            setResult(Activity.RESULT_CANCELED);
            EditDootActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
