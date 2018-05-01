package com.linkedin.replica.recommender.models;

import com.arangodb.velocypack.ValueType;

import java.util.ArrayList;

public class JobListing {
    private String jobId;
    private String jobTitle;
    private String industryType;
    private String jobBrief;
    private String companyId;
    private ArrayList<String> requiredSkills;
    private String companyName;
    private String profilePictureUrl;

    public JobListing() {super();}

    public ArrayList<String> getRequiredSkills() {
        return requiredSkills;
    }
}
