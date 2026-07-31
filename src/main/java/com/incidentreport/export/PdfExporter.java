package com.incidentreport.export;

import com.incidentreport.model.IOCEntry;
import com.incidentreport.model.IncidentReport;
import com.incidentreport.model.SystemAffected;
import com.incidentreport.model.TimelineEvent;
import com.incidentreport.util.DateTimeUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * PdfExporter
 * -----------
 * Builds a clean, professional, multi-page PDF version of an IncidentReport
 * using Apache PDFBox. Handles automatic page breaks and word-wrapping so
 * long descriptions never run off the page.
 */
public class PdfExporter {

    private static final float PAGE_WIDTH = PDRectangle.A4.getWidth();
    private static final float PAGE_HEIGHT = PDRectangle.A4.getHeight();
    private static final float MARGIN = 50f;
    private static final float LINE_HEIGHT = 15f;

   private final PDFont fontBold = PDType1Font.HELVETICA_BOLD;
private final PDFont fontRegular = PDType1Font.HELVETICA;
private final PDFont fontTitle = PDType1Font.HELVETICA_BOLD;

    // Mutable cursor state used while writing the document.
    private PDDocument document;
    private PDPage currentPage;
    private PDPageContentStream stream;
    private float cursorY;

    /**
     * Exports the given report to a PDF file at the given location.
     *
     * @param report the report to export
     * @param file   destination file (should end in .pdf)
     * @throws IOException if the PDF cannot be created or written
     */
    public void export(IncidentReport report, File file) throws IOException {
        document = new PDDocument();
        startNewPage();

        writeTitle("INCIDENT REPORT");
        writeSubtitle(nonEmpty(report.getTitle(), "Untitled Incident"));
        writeGeneratedOn();
        addSpacer(10);

        writeSectionHeader("1. Incident Details");
        writeField("Incident Type", report.getIncidentType());
        writeField("Reported By", report.getReportedBy());
        writeField("Department", report.getDepartment());
        writeField("Incident Date", report.getIncidentDate());
        writeField("Incident Time", report.getIncidentTime());
        writeField("Priority", report.getPriority());
        writeWrappedField("Description", report.getDescription());
        addSpacer(8);

        writeSectionHeader("2. Systems Affected");
        List<SystemAffected> systems = report.getSystemsAffected();
        if (systems.isEmpty()) {
            writeParagraph("No systems recorded.");
        } else {
            for (SystemAffected s : systems) {
                writeParagraph("- " + nonEmpty(s.getComputerName(), "Unknown Computer")
                        + " | IP: " + nonEmpty(s.getIpAddress(), "N/A")
                        + " | OS: " + nonEmpty(s.getOperatingSystem(), "N/A")
                        + " | Dept: " + nonEmpty(s.getDepartment(), "N/A"));
                if (s.getDescription() != null && !s.getDescription().isEmpty()) {
                    writeParagraph("  " + s.getDescription());
                }
            }
        }
        addSpacer(8);

        writeSectionHeader("3. Timeline of Events");
        List<TimelineEvent> events = report.getTimeline();
        if (events.isEmpty()) {
            writeParagraph("No timeline events recorded.");
        } else {
            for (TimelineEvent e : events) {
                writeParagraph("- [" + e.getDate() + " " + e.getTime() + "] " + e.getDescription());
            }
        }
        addSpacer(8);

        writeSectionHeader("4. Indicators of Compromise (IOC)");
        List<IOCEntry> iocs = report.getIocs();
        if (iocs.isEmpty()) {
            writeParagraph("No IOCs recorded.");
        } else {
            for (IOCEntry ioc : iocs) {
                writeParagraph("- IP: " + nonEmpty(ioc.getSuspiciousIp(), "N/A")
                        + " | Domain: " + nonEmpty(ioc.getDomain(), "N/A"));
                writeParagraph("  URL: " + nonEmpty(ioc.getUrl(), "N/A"));
                writeParagraph("  File: " + nonEmpty(ioc.getFileName(), "N/A")
                        + " | Hash: " + nonEmpty(ioc.getFileHash(), "N/A"));
            }
        }
        addSpacer(8);

        writeSectionHeader("5. Remediation");
        writeWrappedField("Immediate Actions", report.getImmediateActions());
        writeWrappedField("Investigation Summary", report.getInvestigationSummary());
        writeWrappedField("Root Cause", report.getRootCause());
        writeWrappedField("Remediation Steps", report.getRemediationSteps());
        writeWrappedField("Future Recommendations", report.getFutureRecommendations());

        stream.close();
        document.save(file);
        document.close();
    }

    // ------------------------------------------------------------------
    // Low level page/content helpers
    // ------------------------------------------------------------------

    private void startNewPage() throws IOException {
        currentPage = new PDPage(PDRectangle.A4);
        document.addPage(currentPage);
        stream = new PDPageContentStream(document, currentPage);
        cursorY = PAGE_HEIGHT - MARGIN;
    }

    private void ensureSpace(float needed) throws IOException {
        if (cursorY - needed < MARGIN) {
            stream.close();
            startNewPage();
        }
    }

    private void writeTitle(String text) throws IOException {
        ensureSpace(30);
        stream.beginText();
        stream.setFont(fontTitle, 20);
        stream.newLineAtOffset(MARGIN, cursorY);
        stream.showText(text);
        stream.endText();
        cursorY -= 26;
    }

    private void writeSubtitle(String text) throws IOException {
        ensureSpace(20);
        stream.beginText();
        stream.setFont(fontBold, 14);
        stream.newLineAtOffset(MARGIN, cursorY);
        stream.showText(text);
        stream.endText();
        cursorY -= 20;
    }

    private void writeGeneratedOn() throws IOException {
        ensureSpace(15);
        stream.beginText();
        stream.setFont(fontRegular, 9);
        stream.newLineAtOffset(MARGIN, cursorY);
        stream.showText("Generated on " + DateTimeUtils.currentDisplayDate()
                + " at " + DateTimeUtils.currentDisplayTime());
        stream.endText();
        cursorY -= 16;
    }

    private void writeSectionHeader(String text) throws IOException {
        ensureSpace(24);
        cursorY -= 4;
        stream.beginText();
        stream.setFont(fontBold, 13);
        stream.newLineAtOffset(MARGIN, cursorY);
        stream.showText(text);
        stream.endText();
        cursorY -= 18;
    }

    private void writeField(String label, String value) throws IOException {
        writeParagraph(label + ": " + nonEmpty(value, "N/A"));
    }

    private void writeWrappedField(String label, String value) throws IOException {
        writeParagraph(label + ":");
        String content = nonEmpty(value, "N/A");
        for (String line : wrap(content, fontRegular, 10, PAGE_WIDTH - 2 * MARGIN - 10)) {
            writeParagraph("   " + line);
        }
        cursorY -= 4;
    }

    private void writeParagraph(String text) throws IOException {
        for (String line : wrap(text, fontRegular, 10, PAGE_WIDTH - 2 * MARGIN)) {
            ensureSpace(LINE_HEIGHT);
            stream.beginText();
            stream.setFont(fontRegular, 10);
            stream.newLineAtOffset(MARGIN, cursorY);
            stream.showText(line);
            stream.endText();
            cursorY -= LINE_HEIGHT;
        }
    }

    private void addSpacer(float amount) throws IOException {
        ensureSpace(amount);
        cursorY -= amount;
    }

    /** Word-wraps text so it never overflows the printable page width. */
    private java.util.List<String> wrap(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        java.util.List<String> lines = new java.util.ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }
        for (String paragraph : text.split("\n", -1)) {
            StringBuilder current = new StringBuilder();
            for (String word : paragraph.split(" ")) {
                String candidate = current.isEmpty() ? word : current + " " + word;
                float width = font.getStringWidth(candidate) / 1000 * fontSize;
                if (width > maxWidth && !current.isEmpty()) {
                    lines.add(current.toString());
                    current = new StringBuilder(word);
                } else {
                    current = new StringBuilder(candidate);
                }
            }
            lines.add(current.toString());
        }
        return lines;
    }

    private String nonEmpty(String value, String fallback) {
        return (value == null || value.trim().isEmpty()) ? fallback : value;
    }
}
