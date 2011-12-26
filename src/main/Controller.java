package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import boundaries.HTMLReport;
import boundaries.Parser;
import boundaries.ProgressBar;
import entities.CmdType;
import entities.Config;
import entities.Constants;
import entities.Environment;
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

	/**
	 * Pre-execution configurations.
	 */
	static {

		File file = new File(Constants.OUT_DIR);
		file.deleteOnExit();
		file.mkdir();
	}

	private static Logger logger = Logger.getLogger(Controller.class);

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

			config = getConfigParams();
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

			if(config.isProgressBarEnable())
				ProgressBar.ini();
			
			HashMap<Integer, Flow> listOfFlows = Parser.getFlowsList();
			
			if(config.isProgressBarEnable())
				ProgressBar.setMax(getNumberOfCommandsToExecute(listOfFlows));
			
			Executer executer = new Executer(htmlReport, config, listOfFlows);
			executer.executeTheListOfFlows();
			
			if(config.isProgressBarEnable()){
					ProgressBar.progressBar.setValue(ProgressBar.progressBar.getMaximum());
			}
			
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}

	}

	/**
	 * 
	 * @param listOfFlows
	 * @return
	 */
	private int getNumberOfCommandsToExecute(HashMap<Integer, Flow> listOfFlows) {
		int numberOfCommands=0;
		Set<Integer> keySet = listOfFlows.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		// iterate all flows
		while (iterator.hasNext()) {
			Integer flowId = iterator.next();
			Flow flow = listOfFlows.get(flowId);
			if (flow != null && flow.getName() != null && !flow.getName().equals(""))
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
		if(flow.getListOfCommands()==null)
			return 0;
		
		if (flow.getPreFlow() != null && !flow.getPreFlow().equals("")) {
			if (Parser.getNameIdFlowMapping().containsKey(flow.getPreFlow())) {
				Flow preFlow = Parser.getFlowsList().get(Parser.getNameIdFlowMapping().get(
						flow.getPreFlow()));
				return flow.getListOfCommands().size()+getNumberOfCommands(preFlow);
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
	 * Get all configuration parameters that are exist in "config.txt" file.
	 * 
	 * @return Config object that contains all parameters in the "config.txt"
	 *         file.
	 * @throws IOException
	 */
	private static Config getConfigParams() throws IOException {

		Config config = new Config();
		Environment environment = new Environment();
		HashMap<String, Environment> listOfEnvironments = new HashMap<String, Environment>();

		String line = "";

		FileReader fstream = new FileReader(Constants.CONFIG_PATH);
		BufferedReader in = new BufferedReader(fstream);

		while ((line = in.readLine()) != null) {
			line.trim();
			if (line == null || line.startsWith("/") || line.equals("")
					|| !line.contains("=")) {
				continue;
			}
			String[] attribute = line.split("=");

			if (attribute != null && attribute.length == 2) {
				if (attribute[0].equals(Constants.ENVIRONMENT_CONFIG)) {
					environment = new Environment();
					environment.setName(attribute[1]);

					if (listOfEnvironments.containsKey(environment.getName()))
						logger.warn("The environment that has name \""
								+ environment.getName() + "\" is duplicated");
					listOfEnvironments.put(environment.getName(), environment);
				}

				if (attribute[0].equals(Constants.HOST_NAME))
					environment.setHostname(attribute[1]);
				else if (attribute[0].equals(Constants.HOST_PORT))
					environment.setHostPort(attribute[1]);
				else if (attribute[0].equals(Constants.HOST_PROTOCOL))
					environment.setHostProtocol(attribute[1]);
				else if (attribute[0].equals(Constants.HOST_PLATFORM))
					environment.setHostPlatform(attribute[1]);

				else if (attribute[0].equals(Constants.BROWSER_TYPE))
					environment.setBrowserType(attribute[1]);
				else if (attribute[0].equals(Constants.BROWSER_URL))
					environment.setBrowserUrl(attribute[1]);
				else if (attribute[0].equals(Constants.BROWSER_VERSION))
					environment.setBrowserVersion(attribute[1]);

				else if (attribute[0]
						.equals(Constants.WEBDRIVER_TIMEOUTS_SECONDS))
					environment.setWebDriverTimeouts(attribute[1]);
				else if (attribute[0]
						.equals(Constants.WEBDRIVER_DELETE_ALL_COOKIES))
					environment.setWebDriverDeleteAllCookies(attribute[1]
							.trim().equals("true") ? true : false);

				else if (attribute[0].equals(Constants.SUITE_DIR))
					config.setSuiteDir(attribute[1]);
				else if (attribute[0].equals(Constants.REPORT_DIR))
					config.setReportDir(attribute[1]);
				else if (attribute[0].equals(Constants.BUILD))
					config.setBuild(attribute[1]);

				else if (attribute[0].equals(Constants.PROGRESS_BAR_ENABLE))
					config.setProgressBarEnable(attribute[1].trim().equals("true") ? true
							: false);
				
				else if (attribute[0].equals(Constants.EMAIL_ENABLE))
					config.setEmailEnable(attribute[1].trim().equals("true") ? true
							: false);
				else if (attribute[0].equals(Constants.EMAIL_TO))
					config.setEmailTo(attribute[1].trim());
				else if (attribute[0].equals(Constants.EMAIL_HOST))
					config.setEmailHost(attribute[1].trim());
				else if (attribute[0].equals(Constants.EMAIL_FROM))
					config.setEmailFrom(attribute[1].trim());
				else if (attribute[0].equals(Constants.EMAIL_PASSWORD))
					config.setEmailPassword(attribute[1].trim());
				else if (attribute[0].equals(Constants.EMAIL_ATTACH_LOG))
					config.setEmailAttachedLog(attribute[1].trim().equals(
							"true") ? true : false);
			} else
				logger.error("Some environment params are not exist, please check the config.text");
		}

		config.setListOfEnvironments(listOfEnvironments);

		in.close();
		fstream.close();

		/*
		 * .....................................................................
		 * Check if all environments are completed.
		 * ..............................
		 * ........................................
		 */

		if (config.getSuiteDir() == null || config.getSuiteDir().equals(""))
			logger.error("Please add \"suiteDir\" in config.txt file");

		HashMap<String, Environment> listOfEnv = config.getListOfEnvironments();
		Set<String> keys = listOfEnv.keySet();
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			String envName = iterator.next();
			Environment env = listOfEnv.get(envName);

			if (env.getName() == null || env.getName().equals(""))
				logger.error("Please add \"environment\" name for all environments");
			if (env.getHostName() == null || env.getHostName().equals(""))
				logger.error("Please add \"host.name\" for all environments");
			if (env.getBrowserType() == null || env.getBrowserType().equals(""))
				logger.error("Please add \"browser.type\" for all environments");
			if (env.getBrowserUrl() == null || env.getBrowserUrl().equals(""))
				logger.error("Please add \"browser.url\" for all environments");
			if (env.getBrowserVersion() == null
					|| env.getBrowserVersion().equals(""))
				logger.error("Please add \"browser.version\" for all environments");
		}
		// ......................................................................

		logger.info("Loading the config file \"./suite/config.txt\" is completed with the following params:");
		logger.info(config);
		return config;
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
