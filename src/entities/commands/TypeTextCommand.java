package entities.commands;

import org.openqa.selenium.By;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 *
 */
public class TypeTextCommand extends Command {

	private String locator;
	private LocatorType locatorType;
	private String value;

	public TypeTextCommand(String locator, LocatorType locatorType, String value) {
		super(CmdType.TYPE_TEXT);
		this.locator = locator;
		this.locatorType = locatorType;
		this.value = value;
	}

	@Override
	public void execute() throws Exception {
		if (this.locatorType != null) {
			try {
				if (this.locatorType.equals(LocatorType.XPATH)) {
					webDriver.findElement(By.xpath(locator)).clear();
					webDriver.findElement(By.xpath(locator)).sendKeys(value);
				} else if (this.locatorType.equals(LocatorType.ID)) {
					webDriver.findElement(By.id(locator)).clear();
					webDriver.findElement(By.id(locator)).sendKeys(value);
				} else if (this.locatorType.equals(LocatorType.NAME)) {
					webDriver.findElement(By.name(locator)).clear();
					webDriver.findElement(By.name(locator)).sendKeys(value);
				}
			} catch (Exception e) {
				throw new Exception("locator not found: " + locator);
			}
		} else {
			try {
				webDriver.findElement(By.id(locator)).clear();
				webDriver.findElement(By.id(locator)).sendKeys(value);
			} catch (Exception e) {
				try {
					webDriver.findElement(By.xpath(locator)).clear();
					webDriver.findElement(By.xpath(locator)).sendKeys(value);
				} catch (Exception e2) {
					try {
						webDriver.findElement(By.name(locator)).clear();
						webDriver.findElement(By.name(locator)).sendKeys(value);
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setLocatorType(LocatorType locatorType) {
		this.locatorType = locatorType;
	}

	public LocatorType getLocatorType() {
		return locatorType;
	}

	@Override
	public String toString() {
		return "TypeTextCommand [locator=" + locator + ", locatorType="
				+ (locatorType == null ? "null" : locatorType.toString())
				+ ", value=" + value + "]";
	}
}
