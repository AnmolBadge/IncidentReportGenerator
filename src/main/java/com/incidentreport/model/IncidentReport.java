package com.incidentreport.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * IncidentReport
 * --------------
 * The single aggregate object that holds every piece of data entered by the
 * user across all screens of the application: incident details, affected
 * systems, timeline events, IOCs and remediation notes. This is the object
 * that gets serialized to JSON (save/load) and rendered into the PDF export.
 */
public class IncidentReport implements Serializable {

    // ---- Incident Details ----
    private String title = "";
    private String incidentType = "";
    private String reportedBy = "";
    private String department = "";
    private String incidentDate = "";
    private String incidentTime = "";
    private String priority = "Medium";
    private String description = "";

    // ---- Systems Affected ----
    private List<SystemAffected> systemsAffected = new ArrayList<>();

    // ---- Timeline ----
    private List<TimelineEvent> timeline = new ArrayList<>();

    // ---- IOC Collection ----
    private List<IOCEntry> iocs = new ArrayList<>();

    // ---- Remediation ----
    private String immediateActions = "";
    private String investigationSummary = "";
    private String rootCause = "";
    private String remediationSteps = "";
    private String futureRecommendations = "";

    public IncidentReport() {
    }

    // ------------------- Getters and Setters -------------------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public void setIncidentType(String incidentType) {
        this.incidentType = incidentType;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentTime() {
        return incidentTime;
    }

    public void setIncidentTime(String incidentTime) {
        this.incidentTime = incidentTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SystemAffected> getSystemsAffected() {
        return systemsAffected;
    }

    public void setSystemsAffected(List<SystemAffected> systemsAffected) {
        this.systemsAffected = systemsAffected;
    }

    public List<TimelineEvent> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimelineEvent> timeline) {
        this.timeline = timeline;
    }

    public List<IOCEntry> getIocs() {
        return iocs;
    }

    public void setIocs(List<IOCEntry> iocs) {
        this.iocs = iocs;
    }

    public String getImmediateActions() {
        return immediateActions;
    }

    public void setImmediateActions(String immediateActions) {
        this.immediateActions = immediateActions;
    }

    public String getInvestigationSummary() {
        return investigationSummary;
    }

    public void setInvestigationSummary(String investigationSummary) {
        this.investigationSummary = investigationSummary;
    }

    public String getRootCause() {
        return rootCause;
    }

    public void setRootCause(String rootCause) {
        this.rootCause = rootCause;
    }

    public String getRemediationSteps() {
        return remediationSteps;
    }

    public void setRemediationSteps(String remediationSteps) {
        this.remediationSteps = remediationSteps;
    }

    public String getFutureRecommendations() {
        return futureRecommendations;
    }

    public void setFutureRecommendations(String futureRecommendations) {
        this.futureRecommendations = futureRecommendations;
    }
}
