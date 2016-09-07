package com.example.dellaanjeh.dootapp;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddToListDialog extends DialogFragment {

    TextView tvName;
    TextView tvPriority;
    TextView tvNotes;
    TextView tvStatus;
    TextView tvDooDate;
    EditText etName;
    EditText etNotes;
    EditText etDooDate;
    Button btnAdd;
    DBHelper dbHelper;
    ListView lvDoots;
    Spinner spStatus;
    Spinner spPriority;
    String[] statuses, priorities;
    DatePickerDialog dooDatePicker;
    SimpleDateFormat dateFormat;
    ArrayAdapter<String> statusAdapter, priorityAdapter;
    AddToListListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.add_to_list_dialog, container,
                false);
        View mainView = inflater.inflate(R.layout.activity_main, container, false);
        getDialog().setTitle("Add to your Doots");

        lvDoots = (ListView) mainView.findViewById(R.id.lvDoots);
        tvName = (TextView) rootView.findViewById(R.id.tvName);
        etName = (EditText) rootView.findViewById(R.id.etName);

        tvDooDate = (TextView) rootView.findViewById(R.id.tvDooDate);
        etDooDate = (EditText) rootView.findViewById(R.id.etDooDate);
        lvDoots = (ListView) mainView.findViewById(R.id.lvDoots);
        dbHelper = new DBHelper(getActivity());
        etDooDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                dooDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        etDooDate.setText(dateFormat.format(newDate.getTime()));
                    }

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dooDatePicker.show();
            }
        });

        dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        tvNotes = (TextView) rootView.findViewById(R.id.tvNotes);
        etNotes = (EditText) rootView.findViewById(R.id.etNotes);

        tvStatus = (TextView) rootView.findViewById(R.id.tvStatus);
        spStatus = (Spinner) rootView.findViewById(R.id.spStatus);
        statuses = new String[]{"To Do", "Done"};
        statusAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, statuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(statusAdapter);

        tvPriority = (TextView) rootView.findViewById(R.id.tvPriority);
        spPriority = (Spinner) rootView.findViewById(R.id.spPriority);
        priorities = new String[]{"Low", "Medium", "High"};
        priorityAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, priorities);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPriority.setAdapter(priorityAdapter);

        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String notes = etNotes.getText().toString();
                String status = String.valueOf(spStatus.getSelectedItem());
                String doodate = etDooDate.getText().toString();
                String priority = String.valueOf(spPriority.getSelectedItem());
                addListenerOnSpinnerItemSelection();
                dbHelper.addDoot(name, doodate, notes, status, priority);
                listener.onItemAdded();
                AddToListDialog.this.dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listener = (MainActivity)getActivity();
    }

    public void addListenerOnSpinnerItemSelection(){
        spStatus.setOnItemSelectedListener(new CustomOnStatusSelectedListener());
        spPriority.setOnItemSelectedListener(new CustomOnStatusSelectedListener());
    }

    interface AddToListListener{
        public void onItemAdded();
    }

}
