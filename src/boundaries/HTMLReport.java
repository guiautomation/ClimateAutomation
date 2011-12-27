package boundaries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import entities.ReportEntry;

/**
 * This is the handler for the Html report.
 * 
 * @author gSoft Team
 * 
 */
public class HTMLReport {
	private float count;
	private float numberOfSuccess;
	private long startTime;
	private long endTime;

	String reportTitle;
	String reportDir;

	static String reportContent = "";

	private static Logger logger = Logger.getLogger(HTMLReport.class);

	/**
	 * Constructor to set all necessary params for the html report.
	 * 
	 * @param reportDir
	 *            The directory path that will contain this report.
	 * @param reportTitle
	 *            The main title of the html report.
	 */
	public HTMLReport(String reportDir, String reportTitle, String build) {
		logger.info("Initialize an Html report with title \"" + reportTitle
				+ "\"");
		this.reportDir = reportDir;
		this.reportTitle = reportTitle;
		this.startTime = System.currentTimeMillis();
		this.count = 0;
		this.numberOfSuccess = 0;

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		HTMLReport.reportContent +=

		"<html>"
				+ "<head>"
				+ "<title>Report</title>"
				+ "</head>"
				+ "<body>"
				+ "<div style=\"font-weight: bold;background-color: #fffccc;padding:12px\"><td>Date :"
				+ dateFormat.format(date)
				+ "</td><br></br>"
				+ "<td>Build :"
				+ build
				+ "</td></div>"
				+ "<br/>"
				+ "<div style=\"font-weight: bold;text-align:center;background-color: #177523;font-size: 25px;padding:15px\" >"
				+

				"</style><td>"
				+ reportTitle
				+ "</td></div>"
				+

				"<div  >"
				+

				"<center><font SIZE=\"+4\"><img SRC=\"./logo.png\" X-SAS-UseImageWidth X-SAS-UseImageHeight ALIGN=\"bottom\" WIDTH=\"400\" HEIGHT=\"100\"></font></center></p><h3><center>"
				+

				"</div>"
				+

				"<table style=\"border-style:solid; border-width:5px; ;width : 100%\">"
				+ "<tr style=\"background-color:#BFA8AE;font-weight: bold\">"
				+ "<td style=\"font-weight: bold\">Test name</td><td style=\"font-weight: bold\">Pass/Fail</td><td style=\"font-weight: bold\">File Name</td><td style=\"font-weight: bold\">Bug ID</td><td style=\"font-weight: bold\">TC ID</td><td style=\"font-weight: bold\">HostName/Browser/Platform/Ref</td><td style=\"font-weight: bold\">Time</td>"
				+ "</tr>";

	}

	/**
	 * Add new row to the html report.
	 * 
	 * @param entry
	 *            Contains the values for the new row.
	 */
	public void addReportEntry(ReportEntry entry) {
		logger.info("Add report entry: " + entry);
		HTMLReport.reportContent +=

		"<tr>"
				+ "<td>"
				+ entry.getTestName()
				+ "</td>"
				+ (entry.isResult() ? "<td style=\"font-weight: bold;color:green\">Pass</td>"
						: "<td style=\"font-weight: bold;color:red\">Fail</td>")
				+ "<td>" + entry.getFileName() + "</td>" + "<td>"
				+ entry.getBugId() + "</td>" + "<td>" + entry.getTestCaseId()
				+ "</td>" + "<td>" + entry.getBrowser() + "</td>" + "<td>"
				+ entry.getTimeInMillis() + "</td>" + "</tr>";

		if (entry.isResult())
			this.numberOfSuccess++;
		this.count++;

	}

	/**
	 * Finish up the report.
	 * 
	 * @throws IOException
	 */
	public void generateReport() throws IOException {
		File file = new File(this.reportDir);
		if (file.exists())
			file.delete();
		file.mkdirs();
		String destination = this.reportDir + "/report.html";
		logger.info("Generate the HTML report in \"" + file.getCanonicalPath()
				+ "\"");
		this.endTime = System.currentTimeMillis();
		HTMLReport.reportContent +=

		"</table>"
				+ "<br/>"
				+ "<br/>"
				+ "<br/><br/><table border=1px width=100%><tr style=\"background-color:#CC6666;font-weight: bold;font-size: 18px\"><td>Number Of Tests : "
				+ this.count + "</td><td>Number Of Success Tests : "
				+ this.numberOfSuccess + "</td><td>Success Rate : "
				+ (this.numberOfSuccess / this.count) * 100
				+ "%</td><td>Duration : " + getTimeDuration() + "</td></tr>" +

				"</table>" +

				"</body>" +

				"</html>";

		FileWriter fstream = new FileWriter(destination);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(HTMLReport.reportContent);
		out.close();

		// Copy logo.png image
		if (this.reportDir != null && !this.reportDir.equals(".")
				&& !this.reportDir.equals("./")) {
			File inputFile = new File("./logo.png");
			File outputFile = new File(this.reportDir + "/logo.png");
			outputFile.createNewFile();
			FileInputStream in = new FileInputStream(inputFile);
			FileOutputStream out1 = new FileOutputStream(outputFile);

			int c;
			while ((c = in.read()) != -1)
				out1.write(c);
			in.close();
			out1.close();
		}
	}

	public static String getReportContent() {
		return reportContent;
	}

	/**
	 * Get duration time since report initialization until report generation.
	 * 
	 * @return
	 */
	private String getTimeDuration() {

		float duration = this.endTime - this.startTime;

		if (duration > 3600000)
			return "" + getDuration(duration / 3600000) + " hours";
		else if (duration > 60000)
			return "" + getDuration(duration / 60000) + " minutes";
		else if (duration > 1000)
			return "" + getDuration(duration / 1000) + " seconds";
		else
			return "" + getDuration(duration) + " millis";
	}

	private String getDuration(float duration) {
		Float float1 = new Float(duration);
		String float2 = String.valueOf(float1);
		return (float2.length() > 4 ? float2.substring(0, 4) : float2);
	}

}
