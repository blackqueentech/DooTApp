package com.example.dellaanjeh.dootapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sackcentury.shinebuttonlib.ShineButton;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    Integer id, dootId;
    ListView lvNavList;
    RelativeLayout navPane;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    FragmentManager fm = getSupportFragmentManager();
    ArrayList<NavItem> navList = new ArrayList<NavItem>();
    ShineButton sbtnSave;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("EXTRA_NAME");
            priority = extras.getString("EXTRA_PRIORITY");
            dooDate = extras.getString("EXTRA_DOO_DATE");
            notes = extras.getString("EXTRA_NOTES");
            status = extras.getString("EXTRA_STATUS");
            id = extras.getInt("EXTRA_ID");
            dootId = id;
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

        sbtnSave = (ShineButton) findViewById(R.id.sbtnSave);
        if (sbtnSave != null) sbtnSave.init(this);
        sbtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String notes = etNotes.getText().toString();
                String status = String.valueOf(spStatus.getSelectedItem());
                String doodate = etDooDate.getText().toString();
                String priority = String.valueOf(spPriority.getSelectedItem());
                dbHelper.editDoot(id, name, doodate, notes, status, priority);
                setResult(Activity.RESULT_OK);
                EditActivity.this.finish();
                Toast.makeText(getBaseContext(), "Doot has been updated!", Toast.LENGTH_SHORT).show();
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navPane = (RelativeLayout) findViewById(R.id.navPane);
        navList.add(new NavItem("Save", "New Doot, new details!", R.drawable.save));
        navList.add(new NavItem("Delete", "Good-bye!", R.drawable.delete));
        navList.add(new NavItem("Cancel", "When you don't wanna edit anymore", R.drawable.cancel));
        lvNavList = (ListView) findViewById(R.id.lvNavList);
        final NavAdapter navAdapter = new NavAdapter(EditActivity.this, navList);
        lvNavList.setAdapter(navAdapter);
        lvNavList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvNavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NavItem item = (NavItem) lvNavList.getItemAtPosition(position);
                if (item.getNavTitle().equals("Save")) {
                    String name = etName.getText().toString();
                    String notes = etNotes.getText().toString();
                    String status = String.valueOf(spStatus.getSelectedItem());
                    String doodate = etDooDate.getText().toString();
                    String priority = String.valueOf(spPriority.getSelectedItem());
                    dbHelper.editDoot(dootId, name, doodate, notes, status, priority);
                    setResult(Activity.RESULT_OK);
                    EditActivity.this.finish();
                    Toast.makeText(getBaseContext(), "Doot has been updated!", Toast.LENGTH_SHORT).show();
                } else if (item.getNavTitle().equals("Delete")) {
                    showDeleteDialog();
                } else if (item.getNavTitle().equals("Cancel")) {
                    setResult(Activity.RESULT_CANCELED);
                    EditActivity.this.finish();
                }
                drawerLayout.closeDrawer(navPane);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("something", "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);

    }

    private void selectItemFromDrawer(int position) {
        Fragment fragment = new PreferencesFragment();

        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();

        lvNavList.setItemChecked(position, true);
        setTitle(navList.get(position).navTitle);

        // Close the drawer
        drawerLayout.closeDrawer(navPane);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void showDeleteDialog() {
        new LovelyStandardDialog(this)
                .setTopColorRes(R.color.delete)
                .setButtonsColorRes(R.color.primary_dark)
                .setIcon(R.drawable.white_trash)
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

}
