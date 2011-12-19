package entities.commands;

import org.openqa.selenium.By;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 * 
 */
public class ClickButtonCommand extends Command {

	private String locator;
	private LocatorType locatorType;

	public ClickButtonCommand(String locator, LocatorType locatorType) {
		super(CmdType.CLICK_BUTTON);
		this.locator = locator;
		this.locatorType = locatorType;
	}

	public ClickButtonCommand() {
		super(CmdType.CLICK_BUTTON);
	}

	@Override
	public void execute() throws Exception {
		if (this.locatorType != null) {
			try {
				if (this.locatorType.equals(LocatorType.XPATH))
					webDriver.findElement(By.xpath(locator)).click();
				else if (this.locatorType.equals(LocatorType.ID))
					webDriver.findElement(By.id(locator)).click();
				else if (this.locatorType.equals(LocatorType.NAME))
					webDriver.findElement(By.name(locator)).click();
			} catch (Exception e) {
				throw new Exception("locator not found: " + locator);
			}
		} else {
			try {
				webDriver.findElement(By.id(locator)).click();
			} catch (Exception e) {
				try {
					webDriver.findElement(By.xpath(locator)).click();
				} catch (Exception e2) {
					try {
						webDriver.findElement(By.name(locator)).click();
					} catch (Exception e3) {
						throw new Exception("locator not found: " + locator);
					}
				}
			}
		}
	}

	public String getLocator() {
		return locator;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public void setLocatorType(LocatorType locatorType) {
		this.locatorType = locatorType;
	}

	public LocatorType getLocatorType() {
		return locatorType;
	}

	@Override
	public String toString() {
		return "ClickButtonCommand [locator=" + locator + ", locatorType="
				+ (locatorType == null ? "null" : locatorType.toString()) + "]";
	}
}
