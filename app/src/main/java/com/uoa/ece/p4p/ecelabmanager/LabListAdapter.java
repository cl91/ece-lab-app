/*
* Name : LabListAdapter.java
* Author : Tae-Woong Youn & Henry Lee
*  This class populates the list view with Lab list data.
* */



package com.uoa.ece.p4p.ecelabmanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uoa.ece.p4p.ecelabmanager.api.Student;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rawr on 29/07/2014.
 */
public class LabListAdapter extends BaseAdapter {

    private List rows;
    private Context context;

    public LabListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (rows != null) {
            return rows.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        if (rows != null) {
            return rows.get(i);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Section){
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (getItemViewType(position) == 0){
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.lab_list_item, parent, false);
            }
            Item item = (Item) getItem(position);
            TextView nameTextView = (TextView) view.findViewById(R.id.lab_list_item_name);
            nameTextView.setText(item.student.name);
            TextView auidTextView = (TextView) view.findViewById(R.id.lab_list_item_auid);
            auidTextView.setText(item.student.id);

            // Show light green background if the student is marked off
            // TODO: Implement mark off
            if (item != null){
                view.setBackgroundColor(Color.parseColor("#D9E9C2")); //HEX code of visible light green.
            }else{
                view.setBackgroundColor(0x00000000);
            }
        } else { // Section
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.lab_list_section, parent, false);
                view.setEnabled(false);
                view.setOnClickListener(null);
            }
            Section section = (Section) getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.lab_list_section_textView);
            textView.setText(section.text);
        }
        return view;
    }

    // Update student list.
    public void update(final List rows){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LabListAdapter.this.rows = rows;
                notifyDataSetChanged();
            }
        });
    }

    /*
    Row data classes
     */

    public static abstract class Row {}

    public static final class Section extends Row {
        public final String text;
        public Section(String text) {
            this.text = text;
        }
    }

    public static final class Item extends Row {
        public final Student student;
        public Item(Student student) {
            this.student = student;
        }
    }

}



