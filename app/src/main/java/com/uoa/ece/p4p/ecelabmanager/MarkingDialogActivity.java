/*
* Name : MarkingDialogActivity.java
* Author : Tae-Woong Youn
*  The marking dialog is shown when TAs select the student they want to mark.
*  It is created with activity rather than dialog widget in Android.
* */


package com.uoa.ece.p4p.ecelabmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uoa.ece.p4p.ecelabmanager.api.Lab;
import com.uoa.ece.p4p.ecelabmanager.api.Server;
import com.uoa.ece.p4p.ecelabmanager.api.Student;
import com.uoa.ece.p4p.ecelabmanager.utility.GlobalState;

import java.util.ArrayList;
import java.util.List;

public class MarkingDialogActivity extends Activity {
    final Context context = this;
    private Student stu;
    private Lab lab;
    private TextView textViewName;
    private TextView textViewId;
    private Button cancelButton;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_mark);

        textViewName = (TextView) this.findViewById(R.id.studentNameLastFirst);
        textViewId = (TextView) this.findViewById(R.id.studentIDnumber);
        cancelButton = (Button) findViewById(R.id.markDialogCancelButton);
        confirmButton = (Button) findViewById(R.id.markDialogConfirmButton);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            stu = GlobalState.findStudentById(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
        lab = GlobalState.getLab();
        final Mark mark = new Mark(lab.getNrCriteria());

        // The student information received will be used as text in the dialog to display.
        textViewName.setText(stu.name);
        textViewId.setText(stu.id);

        if (lab.marking_type.equals("number")) {        // total-mark-based marking
            ((LinearLayout)this.findViewById(R.id.number_based_layout))
                    .setVisibility(LinearLayout.VISIBLE);
            ((LinearLayout)this.findViewById(R.id.criteria_based_layout))
                    .setVisibility(LinearLayout.GONE);
            Spinner spinner = (Spinner) findViewById(R.id.total_mark_spinner);
            List<String> list = new ArrayList<String>();
            list.add(Integer.toString(0));
            for (int i = 1; i <= lab.total_mark; i++) {
                list.add(Integer.toString(i));
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            mark.setMark(0, i);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            mark.setMark(0, 0);
                        }
                    });
        } else {
            ((LinearLayout)this.findViewById(R.id.number_based_layout))
                    .setVisibility(LinearLayout.GONE);
            ((LinearLayout)this.findViewById(R.id.criteria_based_layout))
                    .setVisibility(LinearLayout.VISIBLE);
            ArrayList<String> crit_text = new ArrayList<String>();
            if (lab.marking_type.equals("criteria")) {   // criteria-based marking
                for (Lab.Criterion crit : lab.criteria) {
                    crit_text.add(crit.text);
                }
            } else if (lab.marking_type.equals("attendance")) { // attendance-based marking
                crit_text.add("Student attended the tutorial.");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_multiple_choice, crit_text);
            ListView listView = (ListView) findViewById(R.id.criteria_list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    CheckedTextView checkedTextView = ((CheckedTextView)view);
                    boolean is_checked = !checkedTextView.isChecked();
                    checkedTextView.setChecked(is_checked);
                    if (lab.marking_type.equals("criteria")) {
                        mark.setMark(i, is_checked ? lab.criteria.get(i).mark : 0);
                    } else if (lab.marking_type.equals("attendance")) {
                        mark.setMark(0, is_checked ? lab.total_mark : 0);
                    }
                }
            });
        }

        // Confirm button marks off the student.
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CheckIfMarkedOffTask(lab, stu.id, mark).execute();
            }
        });

        // Cancel button exits the dialog.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dialog_mark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class CheckIfMarkedOffTask extends AsyncTask<Void, Void, Boolean> {
        private Lab lab;
        private String uid;
        private Mark mark;
        private Throwable e = null;

        public CheckIfMarkedOffTask(Lab lab, String uid, Mark mark) {
            this.lab = lab;
            this.uid = uid;
            this.mark = mark;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                return Server.is_marked(lab, uid);
            } catch (Throwable e1) {
                e = e1;
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean is_marked) {
            if (e != null) {
                Toast.makeText(getApplicationContext(),
                        "Failed to load student's previous marks: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            } else if (is_marked) {
                // Prompt user to confirm overwriting
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("Confirm Mark Overwrite?");
                builder1.setMessage("Are you sure you want to overwrite the previous mark?");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new UploadMarkTask(lab, uid, mark).execute();
                    }
                });
                builder1.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            } else {
                new UploadMarkTask(lab, uid, mark).execute();
            }
        }
    }

    class UploadMarkTask extends AsyncTask<Void, Void, String> {
        private Lab lab;
        private String uid;
        private Mark mark;
        private Throwable e = null;

        public UploadMarkTask(Lab lab, String uid, Mark mark) {
            this.lab = lab;
            this.uid = uid;
            this.mark = mark;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return Server.upload_mark(lab, uid, mark.getMarks());
            } catch (Throwable e1) {
                e = e1;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String reply) {
            if (e != null) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Failed to update mark for " + uid + ": " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            } else if (reply != null) {
                Toast.makeText(getApplicationContext(), "Marked off student " + stu.name + ".",
                        Toast.LENGTH_LONG).show();
                GlobalState.marked_off_id = stu.id;
                GlobalState.marked_off_total_mark = mark.getTotalMarks();
            }
            finish();
        }
    }

    private class Mark {
        private int[] marks;

        public Mark(int size) {
            marks = new int[size];
            for (int i = 0; i < size; i++) {
                marks[i] = 0;
            }
        }

        public void setMark(int i, int mark) {
            marks[i] = mark;
        }

        public int[] getMarks() {
            return marks;
        }

        public int getTotalMarks() {
            int total_mark = 0;
            if (marks != null) {
                for (int i : marks) {
                    total_mark += i;
                }
            }
            return total_mark;
        }
    }
}