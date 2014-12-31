/*
* Name : MarkingDialogActivity.java
* Author : Tae-Woong Youn
*  The marking dialog is shown when TAs select the student they want to mark.
*  It is created with activity rather than dialog widget in Android.
* */


package com.uoa.ece.p4p.ecelabmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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

import java.util.HashMap;
import java.util.Map;

public class MarkingDialogActivity extends Activity {


    //Variables
    final Context context = this;
    private String labTotal;
    private String activeStudent = "";
    private String activeLab = "";
    private String activeMark;
    private TextView textViewName;
    private TextView textViewAuid;
    private TextView textViewUpi;
    private Button cancelButton;
    private Button confirmButton;
    private NumberPicker numberPicker;
    private boolean previouslyMarked = false;
    private String previousMark;
    private String activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_mark);



        textViewName = (TextView) this.findViewById(R.id.studentNameLastFirst);
        textViewAuid = (TextView) this.findViewById(R.id.studentIDnumber);
        textViewUpi = (TextView) this.findViewById(R.id.studentUPI);
        cancelButton = (Button) findViewById(R.id.markDialogCancelButton);
        confirmButton = (Button) findViewById(R.id.markDialogConfirmButton);
        numberPicker = (NumberPicker)findViewById(R.id.markingNumberPicker);

        Intent intent = this.getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            activeStudent = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        if(intent != null && intent.hasExtra(Intent.EXTRA_UID)){
            activeLab = intent.getStringExtra(Intent.EXTRA_UID);
        }

/*
        // Get the total mark set up in the server from Couchbase
        //Document lab = couchbaseSetup.getDatabaseAdmin().getDocument(activeLab);
        //final String labName = (String)lab.getProperty("name");
        //labTotal = (String)lab.getProperty("totalMarks");

        //activeUser = couchbaseSetup.getActiveUser();


        //Name, ID number and UPI will be provided from the student database of Couchbase
        //final Document student = couchbaseSetup.getDatabaseStudents().getDocument(activeStudent);
        final String studentName = (String) student.getProperty("name");
        final String studentAuid = (String) student.getProperty("auid");
        String studentUpi = (String) student.getProperty("upi");
        final HashMap<String, HashMap<String,HashMap<String,String>>> labMarks = (HashMap<String, HashMap<String, HashMap<String,String>>>) student.getProperty("labMarks");

        // The student information received will be used as text in the dialog to display.
        textViewName.setText(studentName);
        textViewAuid.setText(studentAuid);
        textViewUpi.setText(studentUpi);

        //Numberpicker is one of the widget of Android, It enables TAs mark student easily with scrolling.
        // Maximum value is set as Total mark set up from the server.
        numberPicker.setMaxValue(Integer.parseInt(labTotal));
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(false);


        // The marks need to be updated.
        if(labMarks.containsKey(activeLab)){
            //Check if I marked? or get any?
            activeMark = "";
            long latestTime = 0;
            for(Map.Entry<String, HashMap<String,String>> userMap : labMarks.get(activeLab).entrySet()){
                HashMap<String,String> record = userMap.getValue();
                long timestamp = Long.parseLong(record.get("time"));
                if(timestamp > latestTime){
                    latestTime = timestamp;
                    activeMark = record.get("mark");
                }
            }
            previouslyMarked = true;
            previousMark = activeMark;
            if(activeMark.equals("-1")){
                numberPicker.setValue(Integer.parseInt(labTotal));
            }else{
                numberPicker.setValue(Integer.parseInt(previousMark));
            }

        }else{
            previouslyMarked = false;
            activeMark = labTotal;
            previousMark = "0";
            numberPicker.setValue(Integer.parseInt(labTotal));
        }

        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.putAll(student.getProperties());

        final HashMap<String, HashMap<String, String>> marks;
        //This is <Lab <Mark,Time>> for storing the marks for a lab.
        if(labMarks.containsKey(activeLab)) {
            marks = labMarks.get(activeLab);
        }else{
            marks = new HashMap<String, HashMap<String, String>>();
        }

        // Confirm button marks off the student.
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Part4ProjectLabApplication",(previouslyMarked ? "true" : "false"));
                if(previouslyMarked){
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
                //finish();
            }
        });


        // Cancel button exits the dialog.
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeMark = "-1";
                if(previouslyMarked){
                    //Do nothing
                }else if(previousMark != activeMark){
                    HashMap<String,String> record = new HashMap<String,String>();
                    record.put("mark", activeMark);
                    record.put("time", Long.toString(System.currentTimeMillis()));
                    marks.put(activeUser,record);
                    HashMap<String,HashMap<String,HashMap<String,String>>> temp = new HashMap<String, HashMap<String, HashMap<String, String>>>();
                    temp.put(activeLab, marks);
                    properties.put("labMarks",temp);
                    try {
                        student.putProperties(properties);
                    } catch (CouchbaseLiteException e) {
                        e.printStackTrace();
                    }
                }
                finish();

            }
        });
*/
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