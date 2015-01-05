package com.uoa.ece.p4p.ecelabmanager.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by chang on 1/01/15.
 */
public class Course {
    public String name;
    public ArrayList<String> aliases;
    public int[] ids;
    public int[] active_ids;
    public ArrayList<Lab> labs;

    public Course(JSONObject obj) throws JSONException {
        String name = obj.getString("name");
        JSONArray a = obj.getJSONArray("aliases");
        aliases = new ArrayList<String>();
        for (int i = 0; i < a.length(); i++) {
            aliases.add(a.getString(i));
        }
        JSONObject info_obj = obj.getJSONObject("lab_info");
        ids = new int[info_obj.getJSONArray("ids").length()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = info_obj.getJSONArray("ids").getInt(i);
        }
        active_ids = new int[info_obj.getJSONArray("active_ids").length()];
        for (int i = 0; i < active_ids.length; i++) {
            active_ids[i] = info_obj.getJSONArray("active_ids").getInt(i);
        }
        labs = new ArrayList<Lab>();
        JSONArray labs_array = info_obj.getJSONArray("labs");
        for (int id : ids) {
            JSONObject lab_obj = labs_array.getJSONObject(id);
            String lab_name = lab_obj.getString("name");
            int week = lab_obj.getInt("week");
            boolean is_active = Arrays.asList(active_ids).contains(id);
            labs.add(new Lab(lab_name, week, id, is_active));
        }
    }
}
