/*
* Name : CourseActivity.java
* Author : Tae-Woong Youn
*  This script file is the Activity file relates to the Course & lab list screen of the application.
* */




package com.uoa.ece.p4p.ecelabmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CourseActivity extends Activity {

    final Context context = this;


    // Initialize CourseActivity in onCreate method.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CourseFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course_menu, menu);
        return true;
    }

    // This method is being called when user press the back button in the CourseActivity.
    public void onBackPressed() {
        // TODO Auto-generated method stub

        // Build the dialog, set up the title, message of the dialog
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle("Quit");
        d.setMessage("Are you sure you want to logout?");

        // There are three buttons are supported in AlertDialog in Android.
        // Negative, Neutral, Positive button relatively.
        // In this case, we implemented only two, NegativeButton & PositiveButton.

        // Application will NOT be closed if the 'No' button is pressed.
        d.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Application will be closed if the 'Yes' button is pressed.
        d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = d.show(); // Displays the dialog when the method is called.

        TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
