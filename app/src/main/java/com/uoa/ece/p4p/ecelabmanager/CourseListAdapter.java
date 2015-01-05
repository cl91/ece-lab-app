/*
* Name : CourseListAdapter.java
* Author : Tae-Woong Youn & Henry Lee
*  This class populates the list view with Course list data.
* */

package com.uoa.ece.p4p.ecelabmanager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uoa.ece.p4p.ecelabmanager.api.Course;

import java.util.ArrayList;

/**
 * Created by wellPlayed on 29/07/2014.
 */
public class CourseListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Course> enumerator;

    public CourseListAdapter(Context context) {
        this.context = context;
    }

    public void updateData(ArrayList<Course> courses) {
        enumerator = courses;
    }

    @Override
    public int getCount() {
        if (enumerator != null) {
            return enumerator.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (enumerator != null) {
            return enumerator.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item_lab, null);

        ((TextView)convertView.findViewById(R.id.list_item_lab_name)).setText("Lab Name");
        ((TextView)convertView.findViewById(R.id.list_item_lab_mark)).setText("Marks: 5");
        ((TextView)convertView.findViewById(R.id.list_item_lab_date)).setText("Data");
        ((TextView)convertView.findViewById(R.id.list_item_lab_date_end)).setText("Due Date");

        return convertView;
    }
}
