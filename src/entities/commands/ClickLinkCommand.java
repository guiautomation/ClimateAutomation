package entities.commands;

import org.openqa.selenium.By;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 *
 */
public class ClickLinkCommand extends Command {

	private String locator;
	private LocatorType locatorType;

	public ClickLinkCommand(String locator, LocatorType locatorType) {
		super(CmdType.CLICK_LINK);
		this.locator = locator;
		this.locatorType = locatorType;
	}

	public ClickLinkCommand() {
		super(CmdType.CLICK_LINK);
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
				else if (this.locatorType.equals(LocatorType.LINK_TEXT))
					webDriver.findElement(By.linkText(locator)).click();
				else if (this.locatorType.equals(LocatorType.PARTIAL_LINK_TEXT))
					webDriver.findElement(By.partialLinkText(locator)).click();
				webDriver.switchTo().defaultContent();
			} catch (Exception e) {
				throw new Exception("locator not found: " + locator);
			}
		} else {
			try {
				webDriver.findElement(By.id(locator)).click();
				webDriver.switchTo().defaultContent();
			} catch (Exception e) {
				try {
					webDriver.findElement(By.xpath(locator)).click();
					webDriver.switchTo().defaultContent();
				} catch (Exception e2) {
					try {
						webDriver.findElement(By.linkText(locator)).click();
						webDriver.switchTo().defaultContent();
					} catch (Exception e3) {
						try {
							webDriver.findElement(By.name(locator)).click();
							webDriver.switchTo().defaultContent();
						} catch (Exception e4) {
							try {
								webDriver.findElement(
										By.partialLinkText(locator)).click();
								webDriver.switchTo().defaultContent();
							} catch (Exception e5) {
								throw new Exception("locator not found: "
										+ locator);
							}
						}
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
		return "ClickLinkCommand [locator=" + locator + ", locatorType="
				+ (locatorType == null ? "null" : locatorType.toString()) + "]";
	}
}
