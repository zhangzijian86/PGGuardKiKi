package com.pg.pgguardkiki.bean;

import java.io.Serializable;

/**
 * Created by zzj on 16-11-10.
 */
public class Nick implements Serializable {
    private String Roster_Nick;

    public String getRoster_Nick() {
        return Roster_Nick;
    }

    public void setRoster_Nick(String roster_Nick) {
        Roster_Nick = roster_Nick;
    }
}
