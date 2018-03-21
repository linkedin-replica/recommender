package com.linkedin.replica.recommender.models;

public class JobListing {
    private String jobId;
    private String positionName;
    private String companyName;
    private String companyId;
    private String [] requiredSkills;

    public JobListing() {super();}

    public JobListing(String jobId, String positionName, String companyName, String companyId, String [] requiredSkills) {
        this.jobId = jobId;
        this.positionName = positionName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.requiredSkills = requiredSkills;
    }

    public String getJobID() {
        return jobId;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyId() {
        return companyId;
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
