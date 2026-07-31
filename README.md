# Incident Report Generator

A complete, ready-to-run **Java 17 Swing desktop application** for building,
saving, loading and exporting professional security incident reports.

No command line interaction is required to use the app — everything happens
inside the GUI window. Maven is only used to *build* the project; a build
tool is standard for any real Java project and every mainstream IDE
(IntelliJ IDEA, Eclipse, VS Code) opens it automatically.

---

## 1. Features

| Screen              | What it does                                                                 |
|---------------------|-------------------------------------------------------------------------------|
| Dashboard           | Welcome screen with session stats and a quick-start guide                    |
| Incident Details    | Core incident fields + a "Systems Affected" tab (table of impacted machines) |
| Timeline            | Add/delete/clear dated timeline events in a table                            |
| IOC                 | Add/delete/clear Indicators of Compromise (IP, domain, URL, hash, file name)  |
| Remediation         | Large text areas: immediate actions, investigation summary, root cause, remediation steps, future recommendations |
| Preview             | Full formatted preview of the report, refreshable on demand                  |
| Export              | New Report / Save Report (JSON) / Load Report (JSON) / Clear Form / Export PDF / Exit |
| About               | App info + a Help tab with usage instructions                                |

Validation dialogs are shown for empty required fields, invalid IP
addresses, invalid dates/times, and successful or failed save/export
operations.

## 2. Technology

- **Java 17**
- **Java Swing** (JFrame, JPanel, JTabbedPane, JTable, CardLayout,
  GridBagLayout, BorderLayout, JFileChooser, JOptionPane, etc. — no JavaFX)
- **Gson 2.10.1** — save/load reports as JSON
- **Apache PDFBox 2.0.30** — export reports as PDF
- **Maven** — dependency management and build

## 3. Project Structure

```
IncidentReportGenerator/
├── pom.xml
├── README.md
└── src/main/java/com/incidentreport/
    ├── Main.java                  # application entry point
    ├── gui/                       # every screen / Swing component
    │   ├── MainFrame.java
    │   ├── NavigationPanel.java
    │   ├── HeaderPanel.java
    │   ├── StatusBarPanel.java
    │   ├── RoundedButton.java
    │   ├── DashboardPanel.java
    │   ├── IncidentDetailsPanel.java
    │   ├── TimelinePanel.java
    │   ├── IOCPanel.java
    │   ├── RemediationPanel.java
    │   ├── PreviewPanel.java
    │   ├── ExportPanel.java
    │   └── AboutPanel.java
    ├── model/                     # plain data classes
    │   ├── IncidentReport.java
    │   ├── SystemAffected.java
    │   ├── TimelineEvent.java
    │   └── IOCEntry.java
    ├── service/
    │   └── ReportManager.java     # ties GUI, data and export together
    ├── data/
    │   └── ReportRepository.java  # Gson JSON save/load
    ├── export/
    │   └── PdfExporter.java       # Apache PDFBox PDF export
    └── util/
        ├── UIConstants.java       # theme colors, fonts, spacing
        ├── ValidationUtils.java   # field validation
        └── DateTimeUtils.java     # date/time formatting helpers
```

## 4. How to Open and Run

### Option A — IntelliJ IDEA
1. `File > Open` and select the `IncidentReportGenerator` folder.
2. IntelliJ detects `pom.xml` and imports it as a Maven project automatically.
3. Wait for it to download Gson and PDFBox (shown in the status bar).
4. Open `Main.java`, click the green ▶ run icon next to `main()`.

### Option B — Eclipse
1. `File > Import > Maven > Existing Maven Projects`.
2. Browse to the `IncidentReportGenerator` folder and finish the import.
3. Right-click `Main.java` → `Run As > Java Application`.

### Option C — VS Code
1. Install the **Extension Pack for Java** and **Maven for Java** extensions
   (one-time setup, if not already installed).
2. `File > Open Folder` and select `IncidentReportGenerator`.
3. Open `Main.java` and click **Run** above the `main` method.

### Option D — Command line (optional, for building only)
```bash
cd IncidentReportGenerator
mvn clean package
java -jar target/IncidentReportGenerator.jar
```
The `mvn package` step bundles Gson and PDFBox into one runnable jar
(`maven-shade-plugin`), so the jar can be double-clicked or run with a
single `java -jar` command — no extra libraries to install by hand.

> All three IDEs need an internet connection the *first* time they open the
> project, so Maven can download `gson-2.10.1.jar` and `pdfbox-2.0.30.jar`
> from Maven Central. After that first build they're cached locally and no
> connection is needed to keep working.

## 5. Using the Application

1. **Incident Details** – fill in the incident fields on the first tab, and
   any affected computers on the "Systems Affected" tab, then click
   **Save Details to Report**.
2. **Timeline** – add each event with its date (`yyyy-MM-dd`), time
   (`HH:mm`, 24-hour) and description.
3. **IOC** – record any suspicious IPs, domains, URLs, file names or hashes.
4. **Remediation** – fill in the five remediation text areas and click
   **Save Remediation to Report**.
5. **Preview** – click **Refresh Preview** any time to see the complete
   formatted report.
6. **Export** –
   - **Save Report** stores everything as a `.json` file you can reopen later.
   - **Load Report** reopens a previously saved `.json` file.
   - **Export PDF** generates the final, print-ready PDF document.
   - **Clear Form** wipes every field; **New Report** starts completely fresh.

## 6. Data Format

Reports are saved as plain, human-readable JSON, e.g.:

```json
{
  "title": "Phishing Email Targeting Finance Team",
  "incidentType": "Phishing",
  "priority": "High",
  "timeline": [
    { "date": "2026-07-29", "time": "09:15", "description": "Email reported by employee" }
  ],
  "iocs": [
    { "suspiciousIp": "203.0.113.25", "domain": "fake-invoice-portal.com" }
  ]
}
```

## 7. Notes for Beginners

- Every class has a short comment block explaining its purpose.
- The GUI is deliberately split into small, single-purpose classes (one per
  screen) so each file stays easy to read.
- `ReportManager` is the one place that "owns" the current report — every
  screen reads from and writes to it, so nothing ever gets out of sync.
