/*
* Name : CourseListAdapter.java
* Author : Tae-Woong Youn & Henry Lee
*  This class populates the list view with Course list data.
* */

package com.uoa.ece.p4p.ecelabmanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by wellPlayed on 29/07/2014.
 */
public class CourseListAdapter extends BaseAdapter {

    Context context;

    public CourseListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String viewTag = "";
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
        }else{
            viewTag = (String) convertView.getTag();
        }

//        if(JSONtype.equals("lab")){
//            String name = (String)document.getProperty("name");
/*
            String mark = (String)document.getProperty("totalMarks");
            String date = (String)document.getProperty("date");
            String date_end = (String)document.getProperty("dateEnd");

            ((TextView)convertView.findViewById(R.id.list_item_lab_name)).setText(name);
            ((TextView)convertView.findViewById(R.id.list_item_lab_mark)).setText("Marks: "+mark);
            ((TextView)convertView.findViewById(R.id.list_item_lab_date)).setText(date);
            ((TextView)convertView.findViewById(R.id.list_item_lab_date_end)).setText("Due "+date_end.substring(4,date_end.length()));

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

    public void update(){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
     //           CourseListAdapter.this.enumerator = enumerator;
                notifyDataSetChanged();
            }
        });
    }
}
