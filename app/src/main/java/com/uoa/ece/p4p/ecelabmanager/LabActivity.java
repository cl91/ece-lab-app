/*
* Name : LabActivity.java
* Author : Tae-Woong Youn
*  This script file is the Activity file relates to the Student list screen (of its lab) of the application.
* */

package com.uoa.ece.p4p.ecelabmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.uoa.ece.p4p.ecelabmanager.api.Lab;


public class LabActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new LabListFragment()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lab_menu, menu);
        return true;
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

        // This 'if' statement present the function of recent button at the title bar.
        if (id == R.id.action_recent){

            //Build the dialog for recent, which makes TAs can switch between recently visited labs.
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Recent Labs");
/*
            final CharSequence[] recentLabs = couchbaseSetup.getRecent();
            final CharSequence[] recentLabsName = couchbaseSetup.getRecentNames();
            builder.setItems(recentLabsName,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Do something with the selection
                    Intent intent = new Intent(LabActivity.this, LabActivity.class).putExtra(Intent.EXTRA_TEXT, recentLabs[i]);
                    startActivity(intent);
                    finish();
                }
            });
*/
            AlertDialog alert = builder.create();
            alert.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title){
        getActionBar().setTitle(title);
    }

}
