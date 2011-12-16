package entities;

/**
 * 
 * @author gSoft Team
 * 
 */
public class Environment {

	private String name;
	private String hostName;
	private String hostPort;
	private String hostProtocol;
	private String hostPlatform;

	private String browserType;
	private String browserUrl;
	private String browserVersion;

	private String webDriverTimeouts;
	private boolean webDriverDeleteAllCookies;

	public Environment(String name, String hostName, String hostPort,
			String hostProtocol, String hostPlatform, String browserType,
			String browserUrl, String browserVersion, String webDriverTimeouts,
			boolean webDriverDeleteAllCookies) {

		this.name = name;
		this.hostName = hostName;
		this.hostPort = hostPort;
		this.hostProtocol = hostProtocol;
		this.hostPlatform = hostPlatform;
		this.browserType = browserType;
		this.browserUrl = browserUrl;
		this.browserVersion = browserVersion;
		this.webDriverTimeouts = webDriverTimeouts;
		this.webDriverDeleteAllCookies = webDriverDeleteAllCookies;
	}

	public Environment() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostname(String hostName) {
		this.hostName = hostName;
	}

	public String getHostPort() {
		return hostPort;
	}

	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}

	public String getHostProtocol() {
		return hostProtocol;
	}

	public void setHostProtocol(String hostProtocol) {
		this.hostProtocol = hostProtocol;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getBrowserUrl() {
		return browserUrl;
	}

	public void setBrowserUrl(String browserUrl) {
		this.browserUrl = browserUrl;
	}

	public void setHostPlatform(String hostPlatform) {
		this.hostPlatform = hostPlatform;
	}

	public String getHostPlatform() {
		return hostPlatform;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setWebDriverTimeouts(String webDriverTimeouts) {
		this.webDriverTimeouts = webDriverTimeouts;
	}

	public String getWebDriverTimeouts() {
		return webDriverTimeouts;
	}

	public void setWebDriverDeleteAllCookies(boolean webDriverDeleteAllCookies) {
		this.webDriverDeleteAllCookies = webDriverDeleteAllCookies;
	}

	public boolean getWebDriverDeleteAllCookies() {
		return webDriverDeleteAllCookies;
	}

	@Override
	public String toString() {
		return "Environment [name=" + name + ", hostName=" + hostName
				+ ", hostPort=" + hostPort + ", hostProtocol=" + hostProtocol
				+ ", hostPlatform=" + hostPlatform + ", browserType="
				+ browserType + ", browserUrl=" + browserUrl
				+ ", browserVersion=" + browserVersion + ", webDriverTimeouts="
				+ webDriverTimeouts + ", webDriverDeleteAllCookies="
				+ webDriverDeleteAllCookies + "]";
	}
}
