package entities;

/**
 * 
 * @author gSoft Team
 * 
 */
public class ReportEntry {

	private String testName;
	private boolean result;
	private String fileName;
	private String bugId;
	private String testCaseId;
	private String browser;
	private String timeInMillis;

	public ReportEntry(String testName, boolean result, String fileName,
			String bugId, String testCaseId, String browser, String timeInMillis) {
		this.testName = testName;
		this.fileName = fileName;
		this.result = result;
		this.bugId = bugId;
		this.testCaseId = testCaseId;
		this.browser = browser;
		this.timeInMillis = timeInMillis;
	}

	public ReportEntry() {
		// TODO Auto-generated constructor stub
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getBugId() {
		return bugId;
	}

	public void setBugId(String bugId) {
		this.bugId = bugId;
	}

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getTimeInMillis() {
		return timeInMillis;
	}

	public void setTimeInMillis(String timeInMillis) {
		this.timeInMillis = timeInMillis;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "ReportEntry [testName=" + testName + ", result=" + result
				+ ", fileName=" + fileName + ", bugId=" + bugId
				+ ", testCaseId=" + testCaseId + ", browser=" + browser
				+ ", timeInMillis=" + timeInMillis + "]";
	}
}
