package com.uoa.ece.p4p.ecelabmanager.utility;

import com.uoa.ece.p4p.ecelabmanager.api.Lab;
import com.uoa.ece.p4p.ecelabmanager.api.Student;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chang on 7/01/15.
 */
public class GlobalState {
    private static Lab lab;
    private static ArrayList<Student> students;
    private static HashMap<String, Student> student_id_map = new HashMap<String, Student>();;
    private static HashMap<String, Student> student_name_map = new HashMap<String, Student>();;

    public static Lab getLab() {
        return lab;
    }

    public static void setLab(Lab l) {
        lab = l;
    }

    public static ArrayList<Student> getStudents() {
        return students;
    }

    public static HashMap<String, Student> getStudentIDMap() {
        return student_id_map;
    }

    public static HashMap<String, Student> getStudentNameMap() {
        return student_name_map;
    }

    public static void setStudents(ArrayList<Student> studentArrayList) {
        students = studentArrayList;
        for (Student s : studentArrayList) {
            student_id_map.put(s.id, s);
            student_name_map.put(s.name, s);
        }
    }
}