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

    public void setActionBarTitle(String title){
        getActionBar().setTitle(title);
    }

}
