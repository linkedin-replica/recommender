package models;

public class JobListing {
    private int jobID;
    private String positionName;
    private String companyName;
    private String companyID;
    private String companyProfilePictureURL;
    private String [] requiredSkills;

    public JobListing() {}

    public int getJobID() {
        return jobID;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getCompanyProfilePictureURL() {
        return companyProfilePictureURL;
    }

    public String[] getRequiredSkills() { return requiredSkills; }
}
