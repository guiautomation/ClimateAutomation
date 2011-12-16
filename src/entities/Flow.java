package entities;

import java.util.ArrayList;

/**
 * 
 * @author gSoft Team
 * 
 */

public class Flow {

	private Integer id; // must be unique.
	private String name; // must be unique.
	private String defect;
	private String testCaseId;
	private Environment environment;
	private String fileName;
	private String preFlow;
	private ArrayList<Command> listOfCommands = new ArrayList<Command>();

	private boolean inPreCycle;

	public Flow(Integer id, String name, String defect, String testCaseId,
			Environment environment, String fileName, String preFlow,
			ArrayList<Command> listOfCommands, boolean inPreCycle) {
		super();
		this.id = id;
		this.name = name;
		this.defect = defect;
		this.testCaseId = testCaseId;
		this.environment = environment;
		this.fileName = fileName;
		this.preFlow = preFlow;
		this.listOfCommands = listOfCommands;
		this.inPreCycle = inPreCycle;
	}

	public Flow() {
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefect() {
		return defect;
	}

	public void setDefect(String defect) {
		this.defect = defect;
	}

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	public ArrayList<Command> getListOfCommands() {
		return listOfCommands;
	}

	public void setListOfCommands(ArrayList<Command> listOfCommands) {
		this.listOfCommands = listOfCommands;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPreFlow() {
		return preFlow;
	}

	public void setPreFlow(String preFlow) {
		this.preFlow = preFlow;
	}

	public void setInPreCycle(boolean inPreCycle) {
		this.inPreCycle = inPreCycle;
	}

	public boolean isInPreCycle() {
		return inPreCycle;
	}

	@Override
	public String toString() {
		return "Flow [id=" + id + ", name=" + name + ", defect=" + defect
				+ ", testCaseId=" + testCaseId + ", environment=" + environment
				+ ", fileName=" + fileName + ", preFlow=" + preFlow
				+ ", listOfCommands=" + listOfCommands.toString()
				+ ", inPreCycle=" + inPreCycle + "]";
	}
}
