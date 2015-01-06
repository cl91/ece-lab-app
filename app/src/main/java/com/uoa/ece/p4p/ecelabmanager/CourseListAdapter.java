/*
* Name : CourseListAdapter.java
* Author : Tae-Woong Youn & Henry Lee
*  This class populates the list view with Course list data.
* */

package com.uoa.ece.p4p.ecelabmanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uoa.ece.p4p.ecelabmanager.api.Course;
import com.uoa.ece.p4p.ecelabmanager.api.Lab;

import java.util.ArrayList;

/**
 * Created by wellPlayed on 29/07/2014.
 */
public class CourseListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Object> enumerator;

    public CourseListAdapter(Context context) {
        this.context = context;
    }

    public void updateData(ArrayList<Course> courses) {
        if (courses != null) {
            enumerator = new ArrayList<Object>();
            for (Course c : courses) {
                if (c.active_ids.length > 0) {
                    enumerator.add(c.getNameWithAliases());
                    for (int active : c.active_ids) {
                        enumerator.add(c.getLab(active));
                    }
                }
            }
            notifyDataSetChanged();
        }
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
        Object obj = enumerator.get(position);

        if (obj instanceof Lab) {       // Item is a lab
            if (convertView == null || convertView.getTag() != "lab") {
                convertView = inflater.inflate(R.layout.list_item_lab, null);
            }
            Lab l = (Lab) obj;
            ((TextView) convertView.findViewById(R.id.list_item_lab_name)).setText("Lab Name: " + l.name);
            ((TextView) convertView.findViewById(R.id.list_item_lab_mark)).setText("Marks: " + l.total_mark);
            ((TextView) convertView.findViewById(R.id.list_item_lab_date)).setText("Date :" + l.marking_start);
            ((TextView) convertView.findViewById(R.id.list_item_lab_date_end)).setText("Due Date: " + l.marking_end);
            convertView.setTag("lab");
        } else {
            if (convertView == null || convertView.getTag() != "course") {
                convertView = inflater.inflate(R.layout.list_item_course, null);
            }
            String name = (String) obj;
            ((TextView) convertView.findViewById(R.id.list_item_course_name)).setText(name);
            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
            convertView.setTag("course");
        }
        return convertView;
    }
}
