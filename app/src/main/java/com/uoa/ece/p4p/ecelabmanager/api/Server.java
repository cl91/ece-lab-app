package com.uoa.ece.p4p.ecelabmanager.api;

/**
 * Created by chang on 30/12/14.
 */

import android.util.Log;

import com.uoa.ece.p4p.ecelabmanager.api.exception.LoginFailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static Api api;

    public static void login(String name, String pass) throws LoginFailed {
        try {
            String reply = Api.make_request_no_auth("auth", "name=" +
                    URLEncoder.encode(name, "UTF-8") + "&pass=" +
                    URLEncoder.encode(pass, "UTF-8"));
            JSONObject obj = new JSONObject(reply);
            String auth = obj.getString("auth");
            String type = obj.getString("type");
            if (auth == null || auth.isEmpty() || !type.equals("marker")) {
                throw new LoginFailed("Not authorised.");
            }
            api = new Api(auth);
        } catch (FileNotFoundException e) {
            throw new LoginFailed("Invalid username or password.");
        } catch (Exception e) {
            throw new LoginFailed(e.getMessage());
        }
    }

    public static ArrayList<Course> get_courses() throws IOException, JSONException {
        ArrayList<Course> courses = new ArrayList<Course>();
        String reply = api.make_request("course/get");
        JSONArray obj = new JSONArray(reply);
        for (int i = 0; i < obj.length(); i++) {
            Course course = new Course(obj.getJSONObject(i));
            courses.add(course);
        }
        return courses;
    }

    public static ArrayList<Student> get_student_list(String course, String lab) throws IOException, JSONException {
        ArrayList<Student> students = new ArrayList<Student>();
        String reply = api.make_request("course/" + course + "/get-student-list", "lab="+lab+"&merge=true");
        JSONArray obj = new JSONArray(reply);
        for (int i = 0; i < obj.length(); i++) {
            Student student = new Student(obj.getJSONObject(i));
            students.add(student);
        }
        return students;
    }

    public static String upload_mark(Lab lab, String uid, int[] marks) throws IOException {
        String body = "[" + marks[0];
        for (int i = 1; i < marks.length; i++) {
            body += ","+marks[i];
        }
        body += "]";
        String reply = api.make_request_with_body("mark/" + lab.course + "/upload",
                "lab="+lab.id+"&uid="+uid, body);
        return reply;
    }

    public static boolean is_marked(Lab lab, String uid) throws IOException, JSONException {
        String reply = api.make_request("mark/" + lab.course + "/get-marks", "lab=" + lab.id + "&uid=" + uid);
        JSONArray obj = new JSONArray(reply);
        return obj.length() > 0;
    }
}
