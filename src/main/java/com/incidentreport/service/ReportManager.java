package com.incidentreport.service;

import com.incidentreport.data.ReportRepository;
import com.incidentreport.export.PdfExporter;
import com.incidentreport.model.IncidentReport;

import java.io.File;
import java.io.IOException;

/**
 * ReportManager
 * -------------
 * Central service class that the whole GUI talks to. It holds the single
 * IncidentReport currently being edited, and delegates saving/loading to
 * ReportRepository (JSON) and exporting to PdfExporter (PDF). It also keeps
 * a running count of how many reports have been generated (saved or
 * exported) during this session, shown on the Dashboard.
 */
public class ReportManager {

    private IncidentReport currentReport;
    private final ReportRepository repository;
    private final PdfExporter pdfExporter;

    private int totalReportsThisSession = 0;
    private File lastLoadedFile = null;

    public ReportManager() {
        this.currentReport = new IncidentReport();
        this.repository = new ReportRepository();
        this.pdfExporter = new PdfExporter();
    }

    public IncidentReport getCurrentReport() {
        return currentReport;
    }

    /** Discards the current report and starts a fresh, empty one. */
    public void newReport() {
        this.currentReport = new IncidentReport();
        this.lastLoadedFile = null;
    }

    /** Replaces the current report with a fully populated one (used by loadFromFile). */
    public void setCurrentReport(IncidentReport report) {
        this.currentReport = report;
    }

    /** Saves the current report as JSON to the given file. */
    public void saveToFile(File file) throws IOException {
        repository.save(currentReport, file);
        lastLoadedFile = file;
        totalReportsThisSession++;
    }

    /** Loads a report from a JSON file and makes it the current report. */
    public IncidentReport loadFromFile(File file) throws IOException {
        IncidentReport loaded = repository.load(file);
        this.currentReport = loaded;
        this.lastLoadedFile = file;
        return loaded;
    }

    /** Exports the current report as a PDF to the given file. */
    public void exportToPdf(File file) throws IOException {
        pdfExporter.export(currentReport, file);
        totalReportsThisSession++;
    }

    public int getTotalReportsThisSession() {
        return totalReportsThisSession;
    }

    public File getLastLoadedFile() {
        return lastLoadedFile;
    }
}
