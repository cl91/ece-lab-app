/*
* Name : CourseFragment.java
* Author : Tae-Woong Youn
*  A Fragment represents a behavior or a portion of user interface in an Activity.
* This script file gives a behavior of user interface in an CourseActivity.
* */



package com.uoa.ece.p4p.ecelabmanager;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class CourseFragment extends Fragment {

    private String activeUser = "";
    private CourseListAdapter mCourseListAdapter;

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            activeUser = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            activeUser = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        mCourseListAdapter = new CourseListAdapter(getActivity());

        if(!activeUser.equals("")){
            initQuery();
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "activeUser not set.", Toast.LENGTH_LONG);
        }

    }

    private void initQuery(){
    }

    private void runQueries() {
//        if(!queryRows.isEmpty()){
//            updateCourseLabAdapter();
//        }
    }

    private void updateCourseLabAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCourseListAdapter.update();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_course_menu, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_course);
        listView.setAdapter(mCourseListAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Document document = (Document) mCourseListAdapter.getItem(position);
//                if(document.getProperty("JSONtype").equals("lab")){
//                    Intent intent = new Intent(getActivity(), LabActivity.class)
//                            .putExtra(Intent.EXTRA_TEXT, (String)document.getId());
//                    startActivity(intent);
//                }
//            }
//        });

        return rootView;
    }

    // This class is setting up the validation date of each lab.
    // Within the range of the startDate & endDate, teacher assistant can mark the corresponding lab.
    // Used the local date for this validation.
    public boolean checkDateValidation (String startDate, String endDate){

        try {

            startDate = startDate.substring(4,startDate.length());
            endDate = endDate.substring(4,endDate.length());
            Log.i("Part4ProjectLabApplication",startDate);

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+12:00"));
            Date currentDate = new Date();

            String CurrentDateString = dateFormat.format(currentDate);

            Date LabStartDate = dateFormat.parse(startDate);
            Date LabEndDate = dateFormat.parse(endDate);
            Date LabCurrentDate = dateFormat.parse(CurrentDateString);

            Calendar calLabStart = Calendar.getInstance();
            Calendar calLabEnd = Calendar.getInstance();
            Calendar calLabCurrent = Calendar.getInstance();

            calLabStart.setTime(LabStartDate);
            calLabEnd.setTime(LabEndDate);

            calLabCurrent.setTime(LabCurrentDate);

            // Set up the validation rule in terms of schedule.
            // Starting date must before the Current date or can be equal.
            // End date must after the current date or can be equal.
            // Otherwise, it's not valid
            if (calLabStart.before(calLabCurrent) || calLabStart.equals(calLabCurrent)) {
                if (calLabEnd.after(calLabCurrent) || calLabEnd.equals(calLabCurrent)) {
                    return true;
//
                }
            }
            return false;

        } catch (ParseException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}