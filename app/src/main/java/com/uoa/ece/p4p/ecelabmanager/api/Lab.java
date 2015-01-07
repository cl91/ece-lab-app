package com.uoa.ece.p4p.ecelabmanager.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by chang on 2/01/15.
 */
public class Lab {
    public String name;
    public String course;
    public int week;
    public int id;
    public boolean active;
    public int total_mark;
    public String marking_start;
    public String marking_end;

    public Lab(String course, int id, boolean active, JSONObject lab_obj) throws JSONException {
        this.course = course;
        this.id = id;
        this.active = active;
        name = lab_obj.getString("name");
        week = lab_obj.getInt("week");
        total_mark = lab_obj.getInt("total_mark");
        marking_start = lab_obj.getString("marking_start");
        marking_end = lab_obj.getString("marking_end");
    }
}
