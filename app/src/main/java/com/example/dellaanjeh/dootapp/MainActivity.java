package com.example.dellaanjeh.dootapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AddToListDialog.AddToListListener {

    private static final int DETAILS_ACTIVITY = 321;
    SQLHandler sqlHandler;
    ListView lvDoots;
    ArrayList<Doot> list;
    DootListAdapter adapter;
    TextView tvEmptyList;
    DBHelper dh;
    Button btnAdd;
    FloatingActionButton fab;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View emptyView = findViewById(R.id.empty);
        lvDoots = (ListView) findViewById(R.id.lvDoots);
        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        sqlHandler = new SQLHandler(this);
        dh = new DBHelper(this);
        list = dh.getAllDoots();
        adapter = new DootListAdapter(MainActivity.this, list);
        lvDoots.setAdapter(adapter);
        lvDoots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DootDetailsActivity.class);
                Doot doot = (Doot) adapter.getItem(position);
                intent.putExtra("EXTRA_ID", doot.getId());
                intent.putExtra("EXTRA_NAME", doot.getName());
                intent.putExtra("EXTRA_DOO_DATE", doot.getDooDate());
                intent.putExtra("EXTRA_STATUS", doot.getStatus());
                intent.putExtra("EXTRA_NOTES", doot.getNotes());
                intent.putExtra("EXTRA_PRIORITY", doot.getPriority());
                startActivityForResult(intent,DETAILS_ACTIVITY);
            }
        });

        tvEmptyList = (TextView) emptyView.findViewById(R.id.tvEmptyList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToListDialog dialog = new AddToListDialog();
                dialog.show(fm, "New Doot");
            }
        });

        lvDoots.setEmptyView(tvEmptyList);
    }

    @Override
    public void onItemAdded() {
        adapter.setDootList(dh.getAllDoots());
        Toast.makeText(this, "Doot added!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.setDootList(dh.getAllDoots());
    }
}
