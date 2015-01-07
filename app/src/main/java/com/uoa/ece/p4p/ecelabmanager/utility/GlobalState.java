package com.uoa.ece.p4p.ecelabmanager.utility;

import com.uoa.ece.p4p.ecelabmanager.api.Lab;

/**
 * Created by chang on 7/01/15.
 */
public class GlobalState {
    private static Lab lab;

    public static Lab getLab() {
        return lab;
    }

    public static void setLab(Lab l) {
        lab = l;
    }
}
