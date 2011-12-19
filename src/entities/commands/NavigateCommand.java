package entities.commands;

import entities.CmdType;
import entities.Command;

/**
 * 
 * @author gSoft Team
 * 
 */
public class NavigateCommand extends Command {

	private String value;

	public NavigateCommand(String value) {
		super(CmdType.NAVIGATE);
		this.value = value;
	}

	public NavigateCommand() {
		super(CmdType.NAVIGATE);
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "NavigateCommand [value=" + value + ", commandType="
				+ commandType + "]";
	}
}
