package entities.commands;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 * 
 */
public class VerifyTextCommand extends Command {

	private String value;
	private boolean positive;

	public VerifyTextCommand(String value, boolean positive) {
		super(CmdType.VERIFY_TEXT);
		this.value = value;
		this.positive = positive;
	}

	@Override
	public void execute() throws Exception {
		try {
			boolean result = webDriver.getPageSource().contains(value);
			if (positive) {
				if (result == false)
					throw new Exception(value + " is not found");
			} else {
				if (result == true)
					throw new Exception(value + " is found");
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

	@Override
	public String toString() {
		return "VerifyTextCommand [value=" + value + ", positive=" + positive
				+ ", commandType=" + commandType + "]";
	}
}
