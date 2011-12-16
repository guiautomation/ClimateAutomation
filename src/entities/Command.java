package entities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * 
 * @author gSoft Team
 * 
 */
public abstract class Command {

	protected CmdType commandType;

	protected RemoteWebDriver webDriver;

	public Command(CmdType clickLink) {
		this.commandType = clickLink;
	}

	public Command() {
		// TODO Auto-generated constructor stub
	}

	protected abstract void execute() throws Exception;

	public CmdType getCommandType() {
		return commandType;
	}

	public void setCommandType(CmdType commandType) {
		this.commandType = commandType;
	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(RemoteWebDriver webDriver) {
		this.webDriver = webDriver;
	}
}
