package models;

public class JobListing {
    private int jobID;
    private String positionName;
    private String companyName;
    private String companyID;
    private String companyProfilePictureURL;

    public JobListing(int jobID, String positionName, String companyName, String companyID, String companyProfilePictureURL) {
        this.jobID = jobID;
        this.positionName = positionName;
        this.companyName = companyName;
        this.companyID = companyID;
        this.companyProfilePictureURL = companyProfilePictureURL;
    }

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
}
