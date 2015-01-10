package com.uoa.ece.p4p.ecelabmanager.api;

import org.json.JSONException;
import org.json.JSONObject;

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
    }

    public int getNrCriteria() {
        return 1;
    }
}
