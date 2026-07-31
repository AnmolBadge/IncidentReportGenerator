package com.incidentreport.model;

import java.io.Serializable;

/**
 * SystemAffected
 * --------------
 * Represents one computer/system that was affected by the incident.
 */
public class SystemAffected implements Serializable {

    private String computerName;
    private String ipAddress;
    private String operatingSystem;
    private String department;
    private String description;

    public SystemAffected() {
    }

    public SystemAffected(String computerName, String ipAddress, String operatingSystem,
                           String department, String description) {
        this.computerName = computerName;
        this.ipAddress = ipAddress;
        this.operatingSystem = operatingSystem;
        this.department = department;
        this.description = description;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
