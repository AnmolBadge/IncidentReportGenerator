package com.incidentreport.model;

import java.io.Serializable;

/**
 * IOCEntry
 * --------
 * Represents a single Indicator of Compromise: a suspicious IP address,
 * domain, URL, file hash and/or file name observed during the incident.
 * A list of these is displayed in the IOC Collection JTable.
 */
public class IOCEntry implements Serializable {

    private String suspiciousIp;
    private String domain;
    private String url;
    private String fileHash;
    private String fileName;

    public IOCEntry() {
    }

    public IOCEntry(String suspiciousIp, String domain, String url, String fileHash, String fileName) {
        this.suspiciousIp = suspiciousIp;
        this.domain = domain;
        this.url = url;
        this.fileHash = fileHash;
        this.fileName = fileName;
    }

    public String getSuspiciousIp() {
        return suspiciousIp;
    }

    public void setSuspiciousIp(String suspiciousIp) {
        this.suspiciousIp = suspiciousIp;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
