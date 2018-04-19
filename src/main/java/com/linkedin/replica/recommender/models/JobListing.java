package com.linkedin.replica.recommender.models;

public class JobListing {
    private String jobId;
    private String jobTitle;
    private String companyName;
    private String companyId;
    private String companyPicture;
    private String [] requiredSkills;
    private String industryType;

    public JobListing() {super();}

    public JobListing(String jobId, String jobTitle, String companyName, String companyId, String companyPicture, String industryType, String [] requiredSkills) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyId = companyId;
        this.companyPicture = companyPicture;
        this.companyName = companyName;
        this.industryType = industryType;
        this.requiredSkills = requiredSkills;
    }

    public String getJobID() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCompanyPicture() {
        return companyPicture;
    }

    public String getIndustryType() {
        return industryType;
    }

    public String[] getRequiredSkills() { return requiredSkills; }

    @Override
    public boolean equals(Object obj) {
        JobListing job = (JobListing) obj;
        return this.jobId.equals(job.jobId);
    }

    @Override
    public String toString() {
        return this.jobId;
    }
}
