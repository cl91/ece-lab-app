package com.uoa.ece.p4p.ecelabmanager.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chang on 7/01/15.
 */
public class Student {
    public String name;
    public String id;
    public String upi;
    public String email;

    public Student(JSONObject obj) throws JSONException {
        name = obj.getString("name");
        id = obj.getString("id");
        upi = obj.getString("upi");
        email = obj.getString("email");
    }
}
