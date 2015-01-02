package com.uoa.ece.p4p.ecelabmanager.api;

/**
 * Created by chang on 2/01/15.
 */
public class Lab {
    public String name;
    public int week;
    public int id;
    public boolean active;

    public Lab(String name, int week, int id, boolean active) {
        this.name = name;
        this.week = week;
        this.id = id;
        this.active = active;
    }
}
