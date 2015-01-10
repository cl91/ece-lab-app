package com.uoa.ece.p4p.ecelabmanager.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chang on 2/01/15.
 */
public class Lab {
    public final String name;
    public final String course;
    public final int week;
    public final int id;
    public final boolean active;
    public final int total_mark;
    public final String marking_start;
    public final String marking_end;
    public final String marking_type;
    public final ArrayList<Criterion> criteria = new ArrayList<Criterion>();

    public Lab(String course, int id, boolean active, JSONObject lab_obj) throws JSONException {
        this.course = course;
        this.id = id;
        this.active = active;
        name = lab_obj.getString("name");
        week = lab_obj.getInt("week");
        total_mark = lab_obj.getInt("total_mark");
        marking_start = lab_obj.getString("marking_start");
        marking_end = lab_obj.getString("marking_end");
        marking_type = lab_obj.getString("marking");
        if (marking_type.equals("criteria")) {
            JSONArray crit_arr = lab_obj.getJSONArray("criteria");
            for (int i = 0; i < crit_arr.length(); i++) {
                JSONObject crit_obj = crit_arr.getJSONObject(i);
                int mark = crit_obj.getInt("mark");
                String text = crit_obj.getString("text");
                criteria.add(new Criterion(mark, text));
            }
        }
    }

    public int getNrCriteria() {
        if (marking_type.equals("criteria")) {
            return criteria.size();
        } else {
            return 1;
        }
    }

    public class Criterion {
        public int mark;    // submark of this criterion
        public String text; // criterion text
        public Criterion(int mark, String text) {
            this.mark = mark;
            this.text = text;
        }
    }
}
