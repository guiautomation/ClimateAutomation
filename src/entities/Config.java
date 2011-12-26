package entities;

import java.util.HashMap;

/**
 * 
 * @author gSoft Team
 * 
 */

public class Config {

	private String build;
	private String suiteDir;
	private String reportDir;

	private boolean progressBarEnable;
	
	private boolean emailEnable;
	private String emailTo;
	private String emailHost;
	private String emailFrom;
	private String emailPassword;
	private boolean emailAttachedLog;

	private HashMap<String, Environment> listOfEnvironments = new HashMap<String, Environment>();

	
	public Config(String build, String suiteDir, String reportDir,
			boolean progressBarEnable, boolean emailEnable, String emailTo,
			String emailHost, String emailFrom, String emailPassword,
			boolean emailAttachedLog,
			HashMap<String, Environment> listOfEnvironments) {
		this.build = build;
		this.suiteDir = suiteDir;
		this.reportDir = reportDir;
		this.progressBarEnable = progressBarEnable;
		this.emailEnable = emailEnable;
		this.emailTo = emailTo;
		this.emailHost = emailHost;
		this.emailFrom = emailFrom;
		this.emailPassword = emailPassword;
		this.emailAttachedLog = emailAttachedLog;
		this.listOfEnvironments = listOfEnvironments;
	}

	public Config() {
		// TODO Auto-generated constructor stub
	}

	public String getSuiteDir() {
		return suiteDir;
	}

	public void setSuiteDir(String suiteDir) {
		this.suiteDir = suiteDir;
	}

	public String getReportDir() {
		return reportDir;
	}

	public void setReportDir(String reportDir) {
		this.reportDir = reportDir;
	}

	public boolean isEmailEnable() {
		return emailEnable;
	}

	public void setEmailEnable(boolean emailEnable) {
		this.emailEnable = emailEnable;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public boolean isEmailAttachedLog() {
		return emailAttachedLog;
	}

	public void setEmailAttachedLog(boolean emailAttachedLog) {
		this.emailAttachedLog = emailAttachedLog;
	}

	public HashMap<String, Environment> getListOfEnvironments() {
		return listOfEnvironments;
	}

	public void setListOfEnvironments(
			HashMap<String, Environment> listOfEnvironments) {
		this.listOfEnvironments = listOfEnvironments;
	}

	public void setEmailHost(String emailHost) {
		this.emailHost = emailHost;
	}

	public String getEmailHost() {
		return emailHost;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getBuild() {
		return build;
	}
	
	public void setProgressBarEnable(boolean progressBarEnable) {
		this.progressBarEnable = progressBarEnable;
	}

	public boolean isProgressBarEnable() {
		return progressBarEnable;
	}

	@Override
	public String toString() {
		return "Config [build=" + build + ", suiteDir=" + suiteDir
				+ ", reportDir=" + reportDir + ", progressBarEnable="
				+ progressBarEnable + ", emailEnable=" + emailEnable
				+ ", emailTo=" + emailTo + ", emailHost=" + emailHost
				+ ", emailFrom=" + emailFrom + ", emailPassword="
				+ emailPassword + ", emailAttachedLog=" + emailAttachedLog
				+ ", listOfEnvironments=" + listOfEnvironments.toString() + "]";
	}

}
