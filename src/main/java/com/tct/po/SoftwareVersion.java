package com.tct.po;

public class SoftwareVersion {
    private Integer id;

    private String softwarename;

    private String lastversion;

    private String downloadurl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSoftwarename() {
        return softwarename;
    }

    public void setSoftwarename(String softwarename) {
        this.softwarename = softwarename == null ? null : softwarename.trim();
    }

    public String getLastversion() {
        return lastversion;
    }

    public void setLastversion(String lastversion) {
        this.lastversion = lastversion == null ? null : lastversion.trim();
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl == null ? null : downloadurl.trim();
    }
}