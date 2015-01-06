/*
* Name : CourseFragment.java
* Author : Tae-Woong Youn
*  A Fragment represents a behavior or a portion of user interface in an Activity.
* This script file gives a behavior of user interface in an CourseActivity.
* */



package com.uoa.ece.p4p.ecelabmanager;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.uoa.ece.p4p.ecelabmanager.api.Course;
import com.uoa.ece.p4p.ecelabmanager.api.Lab;
import com.uoa.ece.p4p.ecelabmanager.api.Server;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

public class CourseFragment extends Fragment {
    private CourseListAdapter mCourseListAdapter;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCourseListAdapter = new CourseListAdapter(getActivity());
        new GetCoursesTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_menu, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_course);
        listView.setAdapter(mCourseListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = mCourseListAdapter.getItem(position);
                if (obj instanceof Lab) {
                    Intent intent = new Intent(getActivity(), LabActivity.class);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    class GetCoursesTask extends AsyncTask<Void, Void, ArrayList<Course>> {
        private Throwable e;
        @Override
        protected ArrayList<Course> doInBackground(Void... voids) {
            try {
                return Server.get_courses();
            } catch (Throwable e) {
                this.e = e;
                return null;
            }
        }

        protected void onPostExecute(ArrayList<Course> courses) {
            if (courses == null) {
                e.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Failed to get courses: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                mCourseListAdapter.updateData(courses);
            }
        }
    }
}