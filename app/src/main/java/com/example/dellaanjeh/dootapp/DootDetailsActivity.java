package com.example.dellaanjeh.dootapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DootDetailsActivity extends AppCompatActivity {

    private static final int EDIT_REQUEST = 123;
    // values
    TextView tvName;
    TextView tvDooDate;
    TextView tvNotes;
    TextView tvStatus;
    TextView tvPriority;
    String name, dooDate, status, notes, priority;
    DBHelper helper;
    Button btnDone;
    DootListAdapter adapter;
    ArrayList<Doot> dootList;
    Integer id;
    ListView lvNavList;
    RelativeLayout navPane;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    ArrayList<NavItem> navList = new ArrayList<NavItem>();
    final Context context = this;
    FragmentManager fm = getSupportFragmentManager();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doot_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPriority = (TextView) findViewById(R.id.tvPriority);
        tvDooDate = (TextView) findViewById(R.id.tvDooDate);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvNotes = (TextView) findViewById(R.id.tvNotes);
        btnDone = (Button) findViewById(R.id.btnDone);
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

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status.equals("Done")) {
                    helper.changeDootStatus(id, "Done");
                    DootDetailsActivity.this.finish();
                    Toast.makeText(getBaseContext(), "Nice, you finished your doot!", Toast.LENGTH_SHORT).show();
                } else {
                    helper.changeDootStatus(id, "To Do");
                    DootDetailsActivity.this.finish();
                    Toast.makeText(getBaseContext(), "Doot updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navPane = (RelativeLayout) findViewById(R.id.navPane);
        navList.add(new NavItem("Edit", "Change up your doot", R.drawable.edit));
        navList.add(new NavItem("Delete", "Good-bye!", R.drawable.done_all));
        navList.add(new NavItem("Back to List", "See all your doots", R.drawable.list));
        lvNavList = (ListView) findViewById(R.id.lvNavList);
        final NavAdapter navAdapter = new NavAdapter(DootDetailsActivity.this, navList);
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
                if (item.getNavTitle().equals("Edit")) {
                    Intent intent = new Intent(DootDetailsActivity.this, EditActivity.class);
                    intent.putExtra("EXTRA_ID", id);
                    intent.putExtra("EXTRA_NAME", name);
                    intent.putExtra("EXTRA_DOO_DATE", dooDate);
                    intent.putExtra("EXTRA_PRIORITY", priority);
                    intent.putExtra("EXTRA_STATUS", status);
                    intent.putExtra("EXTRA_NOTES", notes);
                    startActivityForResult(intent,EDIT_REQUEST);
                } else if (item.getNavTitle().equals("Delete")) {
                    showDeleteDialog();
                } else if (item.getNavTitle().equals("Back to List")) {
                    DootDetailsActivity.this.finish();
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
                .setIcon(R.drawable.trash)
                .setTitle("Are you sure you want to delete your doot?")
                .setMessage("It will be gone. Forever.")
                .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(Integer.toString(id), "doot ID");
                        helper.deleteDoot(id);
                        DootDetailsActivity.this.finish();
                        Toast.makeText(getBaseContext(), "Doot deleted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.action_delete){
//            showDeleteDialog();
//        } else if(item.getItemId() == R.id.action_edit) {
//            Intent intent = new Intent(DootDetailsActivity.this, EditActivity.class);
//            intent.putExtra("EXTRA_ID", id);
//            intent.putExtra("EXTRA_NAME", name);
//            intent.putExtra("EXTRA_DOO_DATE", dooDate);
//            intent.putExtra("EXTRA_PRIORITY", priority);
//            intent.putExtra("EXTRA_STATUS", status);
//            intent.putExtra("EXTRA_NOTES", notes);
//            startActivityForResult(intent,EDIT_REQUEST);
//        } else if(item.getItemId() == R.id.action_back) {
//            DootDetailsActivity.this.finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.details_menu, menu);
//        return true;
//    }
}
