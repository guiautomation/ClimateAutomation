package entities.commands;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 *
 */
public class WaitSecondsCommand extends Command {

	private String value;

	public WaitSecondsCommand(String value) {
		super(CmdType.WAIT_IN_SECONDS);
		this.value = value;
	}

	public WaitSecondsCommand() {
		super(CmdType.WAIT_IN_SECONDS);
	}

	@Override
	public void execute() throws Exception {
		int waitPeriod = 0;
		try {
			waitPeriod = Integer.parseInt(value);
		} catch (Exception e) {
			throw new Exception("Please enter a valid number of seconds");
		}
		for (int i = 0; i < waitPeriod; i++) {
			Thread.sleep(1000);
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "WaitSecondsCommand [value=" + value + ", commandType="
				+ commandType + "]";
	}
}
