package entities.commands;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 *
 */
public class VerifyElementCommand extends Command {

	private String locator;
	private LocatorType locatorType;
	private boolean positive;

	private static Logger logger = Logger.getLogger(VerifyElementCommand.class);

	public VerifyElementCommand(String locator, LocatorType locatorType,
			boolean positive) {
		super(CmdType.VERIFY_ELEMENT);
		this.locator = locator;
		this.locatorType = locatorType;
		this.positive = positive;
	}

	@Override
	public void execute() throws Exception {
		boolean result = false;
		if (positive) {
			try {
				if (this.locatorType != null) {
					if (this.locatorType.equals(LocatorType.ID))
						result = webDriver.findElement(By.id(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.XPATH))
						result = webDriver.findElement(By.xpath(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.CSS_SELECTOR))
						result = webDriver.findElement(By.cssSelector(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.LINK_TEXT))
						result = webDriver.findElement(By.linkText(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.NAME))
						result = webDriver.findElement(By.name(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.TAG_NAME))
						result = webDriver.findElement(By.tagName(locator))
								.isDisplayed();
					else if (this.locatorType
							.equals(LocatorType.PARTIAL_LINK_TEXT))
						result = webDriver.findElement(
								By.partialLinkText(locator)).isDisplayed();
				} else {
					logger.error("There is no locator type for this command");
				}
				if (result == false)
					throw new Exception(locator + " is not found");
			} catch (Exception e) {
				throw new Exception(locator + " is not found");
			}
		} else {
			try {

				if (this.locatorType != null) {
					if (this.locatorType.equals(LocatorType.ID))
						result = webDriver.findElement(By.id(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.XPATH))
						result = webDriver.findElement(By.xpath(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.CSS_SELECTOR))
						result = webDriver.findElement(By.cssSelector(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.LINK_TEXT))
						result = webDriver.findElement(By.linkText(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.NAME))
						result = webDriver.findElement(By.name(locator))
								.isDisplayed();
					else if (this.locatorType.equals(LocatorType.TAG_NAME))
						result = webDriver.findElement(By.tagName(locator))
								.isDisplayed();
					else if (this.locatorType
							.equals(LocatorType.PARTIAL_LINK_TEXT))
						result = webDriver.findElement(
								By.partialLinkText(locator)).isDisplayed();
				} else {
					logger.error("There is no locator type for this command");
				}

				if (result == true)
					throw new Exception(locator + " is found");
			} catch (Exception e) {
				if (result == true)
					throw new Exception(locator + " is found");
			}
		}
	}

	public String getLocator() {
		return locator;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

	public void setLocatorType(LocatorType locatorType) {
		this.locatorType = locatorType;
	}

	public LocatorType getLocatorType() {
		return locatorType;
	}

	@Override
	public String toString() {
		return "VerifyElementCommand [locator=" + locator + ", locatorType="
				+ (locatorType == null ? "null" : locatorType.toString())
				+ ", positive=" + positive + "]";
	}
}
