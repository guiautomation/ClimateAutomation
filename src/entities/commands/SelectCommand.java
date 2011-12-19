package entities.commands;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 * 
 */
public class SelectCommand extends Command {

	private String locator;
	private LocatorType locatorType;
	private String value;

	public SelectCommand(String locator, LocatorType locatorType, String value) {
		super(CmdType.SELECT);
		this.locator = locator;
		this.locatorType = locatorType;
		this.value = value;
	}

	@Override
	public void execute() throws Exception {
		WebElement select = null;
		try {
			if (this.locatorType != null) {
				if (this.locatorType.equals(LocatorType.ID))
					select = webDriver.findElement(By.id(locator));
				else if (this.locatorType.equals(LocatorType.XPATH))
					select = webDriver.findElement(By.xpath(locator));
				else if (this.locatorType.equals(LocatorType.CSS_SELECTOR))
					select = webDriver.findElement(By.cssSelector(locator));
				else if (this.locatorType.equals(LocatorType.TAG_NAME))
					select = webDriver.findElement(By.tagName(locator));
				else if (this.locatorType.equals(LocatorType.NAME))
					select = webDriver.findElement(By.name(locator));
			} else {
				select = webDriver.findElement(By.id(locator));
			}

			List<WebElement> options = select
					.findElements(By.tagName("option"));
			for (WebElement option : options) {
				if (option.getText().equals(value)) {
					option.click();
				}
			}
		} catch (Exception e) {
			throw new Exception("locator not found: " + locator);
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
		return "SelectCommand [locator=" + locator + ", locatorType="
				+ (locatorType == null ? "null" : locatorType.toString())
				+ ", value=" + value + "]";
	}

}
