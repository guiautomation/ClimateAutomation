package entities.commands;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 * 
 */
public class SelectRadioButton extends Command {

	private String locator;
	private LocatorType locatorType;
	private String value;

	public SelectRadioButton(String locator, LocatorType locatorType,
			String value) {
		super(CmdType.SELECT_RADIO_BUTTON);
		this.locator = locator;
		this.locatorType = locatorType;
		this.value = value;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

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
		return "SelectRadioButton [locator=" + locator + ", locatorType="
				+ (locatorType == null ? "null" : locatorType.toString())
				+ ", value=" + value + "]";
	}
}
