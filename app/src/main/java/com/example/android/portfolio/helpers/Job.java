package com.example.android.portfolio.helpers;
/* =================================================================================================
 *              Project             :               Kaira
 *              Filename            :               Job.java
 *              Programmer          :               Austin Kempker
 *              Date                :               08/31/2020
 *              Description         :               This is a helper class used to create and define
 *                                                  a job.
 * ===============================================================================================*/

import java.util.UUID;

public class Job {
    private String jobID;
    private String companyName;
    private String jobName;
    private String website;
    private String contactName;
    private String contactEmail;
    private String applicationLink;
    private String jobState;
    private int dropdownChoice;

    public Job() {

    }

    public Job(String companyNewName, String jobNewName, String newWebsite, String contactNewName, String contactNewEmail,
               String applicationNewLink, int dropdownNewChoice) {
        this.companyName = companyNewName;
        this.jobName = jobNewName;
        this.website = newWebsite;
        this.contactName = contactNewName;
        this.contactEmail = contactNewEmail;
        this.applicationLink = applicationNewLink;
        this.dropdownChoice = dropdownNewChoice;
        jobID = UUID.randomUUID().toString();
        jobState = "Applied";
    }

    public String getJobID() {
        return jobID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getApplicationLink() {
        return applicationLink;
    }

    public void setApplicationLink(String applicationLink) {
        this.applicationLink = applicationLink;
    }

    public int getDropdownChoice() {
        return dropdownChoice;
    }

    public void setDropdownChoice(int dropdownChoice) {
        this.dropdownChoice = dropdownChoice;
    }

    public String getJobState() {
        return jobState;
    }

    public void setJobState(String state) {
        this.jobState = state;
    }
}
