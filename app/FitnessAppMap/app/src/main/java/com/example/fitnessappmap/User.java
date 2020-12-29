/**
 * This class defines the User object which stores all the information about
 * a User. The information can then be passed between different activities.
 */
package com.example.fitnessappmap;


import java.io.Serializable;


public class User implements Serializable {


    private String userName;
    private String password;
    private String name;
    private String[] friends;
    private Double[] runs;
    private String[] milestones;

    public User(String userName, String password, String name,
                String[] friends, Double[] runs, String[] milestones) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.friends = friends;
        this.runs = runs;
        this.milestones = milestones;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String[] getFriends() {
        return friends;
    }

    public Double[] getRuns() {
        return runs;
    }

    public String[] getMilestones() {
        return milestones;
    }
}
