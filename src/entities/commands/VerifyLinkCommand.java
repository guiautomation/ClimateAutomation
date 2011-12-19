package entities.commands;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 * 
 */
public class VerifyLinkCommand extends Command {

	private String locator;
	private LocatorType locatorType;
	private boolean positive;

	public VerifyLinkCommand(String locator, LocatorType locatorType,
			boolean positive) {
		super(CmdType.VERIFY_LINK);
		this.locator = locator;
		this.locatorType = locatorType;
		this.positive = positive;
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
		return "VerifyLinkCommand [locator=" + locator + ", locatorType="
				+ (locatorType == null ? "null" : locatorType.toString())
				+ ", positive=" + positive + "]";
	}
}
