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

    public CourseListAdapter(Context context, ArrayList<Course> enumerator) {
        this.context = context;
        this.enumerator = enumerator;
    }

    @Override
    public int getCount() {
        return enumerator.size();
    }

    @Override
    public Object getItem(int position) {
        return enumerator.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String viewTag = "";
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.d("view", String.valueOf(position));

        Course course = (Course) getItem(position);
        Log.d("view", String.valueOf(course));

        if(convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_lab, null);
//                convertView = inflater.inflate(R.layout.list_item_course, null);
        } else {
            viewTag = (String) convertView.getTag();
        }
/*
        if(!JSONtype.equals(viewTag)) {
            if (JSONtype.equals("lab")) {
                convertView = inflater.inflate(R.layout.list_item_lab, null);
            } else if (JSONtype.equals("course")) {
                convertView = inflater.inflate(R.layout.list_item_course, null);
            }
        }
*/
//        if(JSONtype.equals("lab")){
//            String name = (String)document.getProperty("name");
/*
            String mark = (String)document.getProperty("totalMarks");
            String date = (String)document.getProperty("date");
            String date_end = (String)document.getProperty("dateEnd");
*/
            ((TextView)convertView.findViewById(R.id.list_item_lab_name)).setText("Lab Name");
            ((TextView)convertView.findViewById(R.id.list_item_lab_mark)).setText("Marks: 5");
            ((TextView)convertView.findViewById(R.id.list_item_lab_date)).setText("Data");
            ((TextView)convertView.findViewById(R.id.list_item_lab_date_end)).setText("Due Date");
/*
        }else if(JSONtype.equals("course")){
            String name = (String)document.getProperty("name");
            ((TextView)convertView.findViewById(R.id.list_item_course_name)).setText(name);
            convertView.setEnabled(false);
            convertView.setOnClickListener(null);
        }
        convertView.setTag(JSONtype);
*/
        return convertView;
    }
}
