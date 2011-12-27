package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import boundaries.Email;
import boundaries.Executer;
import boundaries.GUIHandler;
import boundaries.HTMLReport;
import boundaries.Parser;
import entities.CmdType;
import entities.Config;
import entities.Constants;
import entities.Flow;

/**
 * This class represents the controller for the automation framework, it
 * contains the test life cycle:
 * 
 * - setup: Setup before the test. - test: Perform the actual test. - teardown:
 * Remove the side effects of the test.
 * 
 * @author gSoft Team
 * 
 */
public class Controller {
	private static Config config;
	private static ArrayList<String> controllerFiles = new ArrayList<String>();
	private static HTMLReport htmlReport;
	public static GUIHandler progressBar;

	private static Logger logger = Logger.getLogger(Controller.class);

	public Controller(GUIHandler guiHandler, Config config) {
		Controller.progressBar = guiHandler;
		Controller.config = config;
	}

	public Controller(Config config) {
		Controller.config = config;
	}

	/**
	 * Setup test includes: - Get configuration parameters from "config.txt"
	 * file. - Get CanonicalPaths for all files that are exist in "suite" folder
	 * and sub folders. - Initialize the Html report.
	 */
	@BeforeClass
	public static void setup() {
		try {

			logger.info("Supported Commands:");
			CmdType[] commandsSupported = CmdType.values();
			for (int i = 0; i < commandsSupported.length; i++) {
				logger.info(commandsSupported[i].getCmdType());
			}

			logger.info("");
			logger.info("------------------------------");
			logger.info("...Setup the test...");
			logger.info("------------------------------");

			getControllerFiles(config.getSuiteDir());
			htmlReport = new HTMLReport(Constants.OUT_DIR + "/"
					+ Constants.REPORT_DIR_NAME, Constants.HTML_REPORT_TITLE,
					config.getBuild());

		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	/**
	 * Test includes: - Send the CanonicalPath for each controller file in the
	 * "suite" to the "Parser" and get back a list of flows for each file. -
	 * Send the initiated Html report, config parameters, and all lists of flows
	 * to the "Executer". - Send a signal to the "Executer" to execute the
	 * already received list of flows.
	 */
	@Test
	public void test() {
		logger.info("------------------------------");
		logger.info("...Start the test...");
		logger.info("------------------------------");

		try {
			for (int i = 0; i < controllerFiles.size(); i++) {
				if (controllerFiles.get(i).contains(".xml")) {
					logger.info("Parsing the file \"" + controllerFiles.get(i)
							+ "\"");
					new Parser(controllerFiles.get(i));
				}
			}

			HashMap<Integer, Flow> listOfFlows = Parser.getFlowsList();

			if (config.isProgressBarEnable())
				progressBar.setMax(getNumberOfCommandsToExecute(listOfFlows));

			Executer executer = new Executer(htmlReport, config, listOfFlows);
			executer.executeTheListOfFlows();

			if (config.isProgressBarEnable()) {
				progressBar.getProgressBar().setValue(
						progressBar.getProgressBar().getMaximum());
			}

		} catch (Exception e) {
			logger.error(e.toString(), e);
		}

	}

	/**
	 * Get the number of all commands in the controller.xml files "for progress bar usage"
	 * @param listOfFlows
	 * @return
	 */
	private int getNumberOfCommandsToExecute(HashMap<Integer, Flow> listOfFlows) {
		int numberOfCommands = 0;
		Set<Integer> keySet = listOfFlows.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		// iterate all flows
		while (iterator.hasNext()) {
			Integer flowId = iterator.next();
			Flow flow = listOfFlows.get(flowId);
			if (flow != null && flow.getName() != null
					&& !flow.getName().equals(""))
				numberOfCommands += getNumberOfCommands(flow);

		}

		return numberOfCommands;
	}

	/**
	 * 
	 * @param flow
	 * @return
	 */
	private int getNumberOfCommands(Flow flow) {
		if (flow.getListOfCommands() == null)
			return 0;

		if (flow.getPreFlow() != null && !flow.getPreFlow().equals("")) {
			if (Parser.getNameIdFlowMapping().containsKey(flow.getPreFlow())) {
				Flow preFlow = Parser.getFlowsList().get(
						Parser.getNameIdFlowMapping().get(flow.getPreFlow()));
				return flow.getListOfCommands().size()
						+ getNumberOfCommands(preFlow);
			}
		}

		return flow.getListOfCommands().size();
	}

	/**
	 * Tear down includes" - Generate the Html report. - Send an email that
	 * contains the Html report.
	 */
	@AfterClass
	public static void tearDown() {
		logger.info("------------------------------");
		logger.info("...Tear down the test...");
		logger.info("------------------------------");
		try {
			htmlReport.generateReport();

			if (config.isEmailEnable())
				Email.sendHtmlReportInEmail(config);

			File file = new File(Constants.OUT_DIR);
			logger.info("output dir: " + file.getCanonicalPath().toString());
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	/**
	 * Get the CanonicalPaths for all files in "suite" folder and sub folders.
	 * 
	 * @param suiteDir
	 * @throws IOException
	 */
	private static void getControllerFiles(String suiteDir) throws IOException {
		File file = new File(suiteDir);
		File[] files = null;
		if (file.isDirectory() && !file.isHidden()) {

			files = file.listFiles();
			if (files == null)
				logger.warn("The directory \"" + suiteDir + "\" is empty");
			else
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory())
						getControllerFiles(files[i].getCanonicalPath());
					else
						controllerFiles.add(files[i].getCanonicalPath());
				}
		}
	}

}
