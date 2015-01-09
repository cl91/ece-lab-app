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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.uoa.ece.p4p.ecelabmanager.api.Lab;
import com.uoa.ece.p4p.ecelabmanager.api.Student;
import com.uoa.ece.p4p.ecelabmanager.utility.GlobalState;

import java.util.HashMap;
import java.util.Map;

public class MarkingDialogActivity extends Activity {
    final Context context = this;
    private Student activeStudent;
    private Lab activeLab;
    private int labTotal;
    private TextView textViewName;
    private TextView textViewAuid;
    private TextView textViewUpi;
    private Button cancelButton;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_mark);

        textViewName = (TextView) this.findViewById(R.id.studentNameLastFirst);
        textViewAuid = (TextView) this.findViewById(R.id.studentIDnumber);
        textViewUpi = (TextView) this.findViewById(R.id.studentUPI);
        cancelButton = (Button) findViewById(R.id.markDialogCancelButton);
        confirmButton = (Button) findViewById(R.id.markDialogConfirmButton);

        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            activeStudent = GlobalState.findStudentById(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
        activeLab = GlobalState.getLab();

        final String labName = activeLab.name;
        labTotal = activeLab.total_mark;

        //Name, ID number and UPI will be provided from the student database of Couchbase
        //final Document student = couchbaseSetup.getDatabaseStudents().getDocument(activeStudent);
        final String studentName = activeStudent.name;
        final String studentAuid = activeStudent.id;
        String studentUpi = activeStudent.upi;

        // The student information received will be used as text in the dialog to display.
        textViewName.setText(studentName);
        textViewAuid.setText(studentAuid);
        textViewUpi.setText(studentUpi);

        // Confirm button marks off the student.
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*)
                Log.d("Part4ProjectLabApplication",(previouslyMarked ? "true" : "false"));
                if (previouslyMarked){
                    //Check for overwrite confirmation
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setTitle("Confirm Mark Overwrite?");
                    builder1.setMessage("Are you sure you want to overwrite the previous mark?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    activeMark = String.valueOf(numberPicker.getValue());
                                    if(previousMark != activeMark || activeMark.equals("0")){
                                        HashMap<String,String> record = new HashMap<String,String>();
                                        record.put("mark", activeMark);
                                        record.put("time", Long.toString(System.currentTimeMillis()));
                                        marks.put(activeUser,record);
                                        HashMap<String,HashMap<String,HashMap<String,String>>> temp = (HashMap<String, HashMap<String, HashMap<String, String>>>) student.getProperty("labMarks");
                                        temp.put(activeLab, marks);
                                        properties.put("labMarks",temp);
                                        try {
                                            student.putProperties(properties);
                                        } catch (CouchbaseLiteException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    //Show toast message that who is marked by TA.
                                    //Toast.makeText(getApplicationContext(),"You have marked "+studentName,Toast.LENGTH_SHORT).show();
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
                                    TextView toastStudent = (TextView) layout.findViewById(R.id.toast_studentname);
                                    toastStudent.setText(studentName);
                                    TextView toastAuid = (TextView) layout.findViewById(R.id.toast_studentauid);
                                    toastAuid.setText(studentAuid);
                                    TextView toastLab = (TextView) layout.findViewById(R.id.toast_lab);
                                    toastLab.setText(labName);
                                    TextView toastMark = (TextView) layout.findViewById(R.id.toast_mark);
                                    toastMark.setText(activeMark);
                                    TextView toastMaxMark = (TextView) layout.findViewById(R.id.toast_maxmark);
                                    toastMaxMark.setText(labTotal);

                                    Toast toast = new Toast(getApplicationContext());
                                    //toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                    toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();

                                    finish();
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
                }else{
                    activeMark = String.valueOf(numberPicker.getValue());
                    if(previousMark != activeMark || activeMark.equals("0")){
                        HashMap<String,String> record = new HashMap<String,String>();
                        record.put("mark", activeMark);
                        record.put("time", Long.toString(System.currentTimeMillis()));
                        marks.put(activeUser,record);
                        HashMap<String,HashMap<String,HashMap<String,String>>> temp = (HashMap<String, HashMap<String, HashMap<String, String>>>) student.getProperty("labMarks");
                        temp.put(activeLab, marks);
                        properties.put("labMarks",temp);
                        try {
                            student.putProperties(properties);
                        } catch (CouchbaseLiteException e) {
                            e.printStackTrace();
                        }
                    }

                    //Show toast message that who is marked by TA.
                    //Toast.makeText(getApplicationContext(),"You have marked "+studentName,Toast.LENGTH_SHORT).show();
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));
                    TextView toastStudent = (TextView) layout.findViewById(R.id.toast_studentname);
                    toastStudent.setText(studentName);
                    TextView toastAuid = (TextView) layout.findViewById(R.id.toast_studentauid);
                    toastAuid.setText(studentAuid);
                    TextView toastLab = (TextView) layout.findViewById(R.id.toast_lab);
                    toastLab.setText(labName);
                    TextView toastMark = (TextView) layout.findViewById(R.id.toast_mark);
                    toastMark.setText(activeMark);
                    TextView toastMaxMark = (TextView) layout.findViewById(R.id.toast_maxmark);
                    toastMaxMark.setText(labTotal);

                    Toast toast = new Toast(getApplicationContext());
                    //toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();

                    finish();

                }
                */
                finish();
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
}