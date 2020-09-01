package com.example.android.portfolio.helpers;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               Job.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This is a helper class used to create and define
 *                                                  a user.
 * ===============================================================================================*/
import com.google.firebase.auth.FirebaseAuth;

public class User {
    private String userID;
    private String username;
    private String jobType;
    private String portfolio;
    private String linkedIn;

    public User() {
        setUserID();
        setUsername("");
        setJobType("");
        setPortfolio("");
        setLinkedIn("");
    }
    public User(String username, String jobType, String portfolio, String linkedIn) {
        setUserID();
        setUsername(username);
        setJobType(jobType);
        setPortfolio(portfolio);
        setLinkedIn(linkedIn);
    }



    public String getUserID() {
        return userID;
    }

    public void setUserID() {
        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if(username.equals("")) {
            this.username = "Update Name";
        } else {
            this.username = username;
        }

    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        if(jobType.equals("")) {
            this.jobType = "Update Job Type!";
        } else {
            this.jobType = jobType;
        }

    }

    public String getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(String portfolio) {
        if(portfolio.equals("")) {
            this.portfolio = "Update Portfolio";
        } else {
            this.portfolio = portfolio;
        }
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        if(linkedIn.equals("")) {
            this.linkedIn = "Update LinkedIn";
        } else {
            this.linkedIn = linkedIn;
        }
    }

}
