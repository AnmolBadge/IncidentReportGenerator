package com.incidentreport.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.incidentreport.model.IncidentReport;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ReportRepository
 * ----------------
 * Handles persistence of an IncidentReport to and from disk as a JSON file,
 * using Gson. This is the only class in the application that talks directly
 * to the file system for saving/loading report data (PDF export is handled
 * separately by the export package).
 */
public class ReportRepository {

    private final Gson gson;

    public ReportRepository() {
        // setPrettyPrinting() makes the saved JSON file human-readable.
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Serializes the given report to JSON and writes it to the given file.
     *
     * @param report the report to save
     * @param file   destination file (should end in .json)
     * @throws IOException if the file cannot be written
     */
    public void save(IncidentReport report, File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(report, writer);
        }
    }

    /**
     * Reads a JSON file and deserializes it back into an IncidentReport.
     *
     * @param file the JSON file previously saved by save()
     * @return the reconstructed IncidentReport
     * @throws IOException if the file cannot be read or is not valid JSON
     */
    public IncidentReport load(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            IncidentReport report = gson.fromJson(reader, IncidentReport.class);
            if (report == null) {
                throw new IOException("The selected file does not contain a valid incident report.");
            }
            return report;
        }
    }
}
