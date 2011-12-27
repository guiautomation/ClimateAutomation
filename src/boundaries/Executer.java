package boundaries;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import main.Controller;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import entities.Command;
import entities.Config;
import entities.Constants;
import entities.Environment;
import entities.Flow;
import entities.ReportEntry;
import entities.commands.CheckBoxCommand;
import entities.commands.ClickButtonCommand;
import entities.commands.ClickLinkCommand;
import entities.commands.NavigateCommand;
import entities.commands.SelectCommand;
import entities.commands.SelectRadioButton;
import entities.commands.TypeTextCommand;
import entities.commands.VerifyElementCommand;
import entities.commands.VerifyLinkCommand;
import entities.commands.VerifyTextCommand;
import entities.commands.WaitSecondsCommand;

/**
 * Executes the list of all flows received from the "Controller", then perform a
 * decomposition for these flows to commands, after that the execution will
 * start for each command.
 * 
 * @author gSoft Team
 * 
 */
public class Executer {

	private HTMLReport htmlReport;
	private RemoteWebDriver webDriver;
	private HashMap<Integer, Flow> listOfFlows = new HashMap<Integer, Flow>();
	private Config config;

	private SauceREST client;
	private Flow currentFlowUnderExecution = new Flow();

	private static Logger logger = Logger.getLogger(Executer.class);

	public Executer(HTMLReport htmlReport, Config config,
			HashMap<Integer, Flow> listOfFlows) {
		this.htmlReport = htmlReport;
		this.config = config;
		this.listOfFlows = listOfFlows;
	}

	/**
	 * Execute the list of all flows.
	 * 
	 * @throws URISyntaxException
	 */
	public void executeTheListOfFlows() throws Exception {
		Set<Integer> keySet = listOfFlows.keySet();
		Iterator<Integer> iterator = keySet.iterator();
		// iterate all flows
		while (iterator.hasNext()) {
			Integer flowId = iterator.next();

			// Check if the flow has a name.
			Flow flow = listOfFlows.get(flowId);
			if (flow != null
					&& (flow.getName() == null || flow.getName().equals("")))
				logger.warn("It seems there is a flow without name in the file: "
						+ flow.getFileName());
			else {

				// Update ProgressBar
				if (config.isProgressBarEnable()) {
					Controller.progressBar.setCurrentFlow(flow.getName(),
							flow.getFileName());
				}

				// Execute flow.
				long startTime = System.currentTimeMillis();
				int result = executeFlow(flow);
				long endTime = System.currentTimeMillis();

				// Update ProgressBar
				if (config.isProgressBarEnable()) {
					if (result == 1)
						Controller.progressBar.getSuccessListOfFlows().addItem(
								flow.getName());
					else if (result == 0)
						Controller.progressBar.getFailListOfFlows().addItem(
								flow.getName());
					Controller.progressBar.updateResult(result);
					if (ProgressBar.getExecuted() < ProgressBar.getExpected()) {
						ProgressBar.setCount(ProgressBar.getCount()
								+ (ProgressBar.getExpected() - ProgressBar
										.getExecuted()));
						Controller.progressBar.setValue();
					}
				}

				// Add flow result to the Html report.
				ReportEntry reportEntry = new ReportEntry();

				reportEntry.setTestName(flow.getName());
				reportEntry.setResult((result == 1 ? true : false));
				reportEntry.setFileName(flow.getFileName().substring(
						flow.getFileName().lastIndexOf("\\") + 1));
				reportEntry.setBugId(flow.getDefect() == null ? "-" : flow
						.getDefect());
				reportEntry.setTestCaseId(flow.getTestCaseId() == null ? "-"
						: flow.getTestCaseId());

				String hostName = null;
				if (flow.getEnvironment() != null
						&& flow.getEnvironment().getHostName() != null)
					if (flow.getEnvironment().getHostName().contains("sauce"))
						hostName = "Sauce Labs";
					else if (flow.getEnvironment().getHostName()
							.contains("localhost"))
						hostName = "Local Host";

				reportEntry
						.setBrowser((hostName == null ? "" : hostName + ", ")
								+ (flow.getEnvironment().getBrowserType() == null ? "-"
										: flow.getEnvironment()
												.getBrowserType())
								+ " "
								+ (flow.getEnvironment().getBrowserVersion() == null ? ""
										: flow.getEnvironment()
												.getBrowserVersion())
								+ (flow.getEnvironment().getHostPlatform() == null ? ""
										: (", " + flow.getEnvironment()
												.getHostPlatform()))
								+ (flow.getEnvironment().getName() == null ? ""
										: (", " + flow.getEnvironment()
												.getName())));
				reportEntry
						.setTimeInMillis(getTimeDuration(endTime, startTime));

				htmlReport.addReportEntry(reportEntry);
			}
		}
	}

	/**
	 * 
	 * @param endTime
	 * @param startTime
	 * @return
	 */
	private String getTimeDuration(long endTime, long startTime) {
		float duration = endTime - startTime;

		if (duration > 3600000)
			return "" + getDuration(duration / 3600000) + " hours";
		else if (duration > 60000)
			return "" + getDuration(duration / 60000) + " minutes";
		else if (duration > 1000)
			return "" + getDuration(duration / 1000) + " seconds";
		else
			return "" + getDuration(duration) + " millis";
	}

	/**
	 * 
	 * @param duration
	 * @return
	 */
	private String getDuration(float duration) {
		Float float1 = new Float(duration);
		String float2 = String.valueOf(float1);
		return (float2.length() > 4 ? float2.substring(0, 3) : float2);
	}

	/**
	 * Execute one flow.
	 * 
	 * @param flow
	 *            The flow that will be executed.
	 * @return Returns 1 if the flow executed successfully and 0 if not.
	 * @throws URISyntaxException
	 */
	private int executeFlow(Flow flow) throws Exception {

		if (config.isProgressBarEnable())
			if (flow != null && flow.getListOfCommands() != null)
				ProgressBar.setExpected(ProgressBar.getExpected()
						+ flow.getListOfCommands().size());

		if (!flow.isInPreCycle())
			currentFlowUnderExecution = flow;
		int result = 1;
		// Check the pre-flow existence.
		if (flow.getPreFlow() != null && !flow.getPreFlow().equals("")) {
			logger.info("**************************************************");
			logger.info("Execute Pre-flow \"" + flow.getPreFlow()
					+ "\" for the flow \"" + flow.getName() + "\":");
			if (!Parser.getNameIdFlowMapping().containsKey(flow.getPreFlow())) {
				logger.error("The flow \"" + flow.getPreFlow()
						+ "\" does not exist, please check that.");
				return 0;
			}

			Flow preFlow = listOfFlows.get(Parser.getNameIdFlowMapping().get(
					flow.getPreFlow()));
			preFlow.setInPreCycle(true);
			result = executeFlow(preFlow);
		}

		// Check the environment existence.
		if (flow.getEnvironment().getName() == null
				|| flow.getEnvironment().getName().equals("")) {
			logger.error("Cant execute the flow \"" + flow.getName()
					+ "\" because no environment specified for this flow.");
			return 0;
		}

		Environment environment = config.getListOfEnvironments().get(
				flow.getEnvironment().getName());
		// Check if the flow contains a correct environment that exists in the
		// confg.txt.
		if (environment == null) {
			logger.error("Cant execute the flow \""
					+ flow.getName()
					+ "\" because the environment \""
					+ flow.getEnvironment().getName()
					+ "\" specified for this flow does not exist in config.txt.");
			return 0;
		}

		flow.setEnvironment(environment);

		logger.info("");
		logger.info("**************************************************");
		logger.info("Execute flow: " + flow);
		logger.info("**************************************************");

		if (flow.getListOfCommands().isEmpty()) {
			logger.warn("No commands in the flow \"" + flow.getName() + "\"");
		} else {
			// Setup environment.
			if (flow.getPreFlow() == null || flow.getPreFlow().equals(""))
				try {
					setupEnvironment(environment);
				} catch (Exception e) {
					logger.error("Setup environment \"" + environment.getName()
							+ "\" is failed");
					return 0;
				}

			// Execute Commands.
			try {
				executeCommands(flow.getListOfCommands());

			} catch (Exception e) {
				// CatchErrorsFromGUI();

				if (environment != null && environment.getHostName() != null
						&& environment.getHostName().contains("sauce")) {
					SessionId sessionId = webDriver.getSessionId();
					try {
						client.jobFailed(sessionId.toString());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				cleanEnvironment(environment);

				logger.error(e.toString(), e);
				return 0;
			}

			// Clean Environment
			if (!flow.isInPreCycle())
				try {
					cleanEnvironment(environment);
				} catch (Exception e) {
					logger.error("Clean environment \"" + environment.getName()
							+ "\" is failed");
					return 0;
				}
			flow.setInPreCycle(false);
		}

		return result;
	}

	/**
	 * Catch all GUI elements that have the class "error".
	 */
	private void CatchErrorsFromGUI() {
		try {
			List<WebElement> errors = webDriver.findElements(By
					.xpath("//div[contains(@class, 'error')]"));
			if (errors != null && !errors.isEmpty())
				for (WebElement error : errors) {
					logger.error("Catched errors from GUI:");
					logger.error(error.getText());
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Clean environment.
	 * 
	 * @param environment
	 *            The environment that will be cleaned.
	 */
	private void cleanEnvironment(Environment environment) {
		logger.info("Clean environment: " + environment);

		if (config.isProgressBarEnable()) {
			Controller.progressBar.getProgressBar().setToolTipText(
					"Clean Env:" + environment.toString());
		}

		webDriver.quit();
		currentFlowUnderExecution = new Flow();
	}

	/**
	 * Setup environment.
	 * 
	 * @param environment
	 *            The environment against which the test will be performed.
	 * @throws Exception
	 */
	private void setupEnvironment(Environment environment) throws Exception {
		logger.info("Setup environment: " + environment);

		if (config.isProgressBarEnable()) {
			Controller.progressBar.getProgressBar().setToolTipText(
					"Setup Env:" + environment.toString());
		}

		// localhost
		if (environment.getHostName().equals("localhost")) {
			if (environment.getBrowserType()
					.equalsIgnoreCase(Constants.FIREFOX)) {
				webDriver = new FirefoxDriver();
			} else if (environment.getBrowserType().equalsIgnoreCase(
					Constants.CHROME)) {
				System.setProperty("webdriver.chrome.driver",
						"./lib/chromedriver.exe");
				webDriver = new ChromeDriver();
			} else if (environment.getBrowserType().equalsIgnoreCase(
					Constants.IE))
				webDriver = new InternetExplorerDriver();
		} else {
			DesiredCapabilities capabillities = new DesiredCapabilities();

			// Select the browser
			if (environment.getBrowserType() != null
					&& !environment.getBrowserType().equals(""))
				if (environment.getBrowserType().equalsIgnoreCase(
						Constants.FIREFOX))
					capabillities = DesiredCapabilities.firefox();
				else if (environment.getBrowserType().equalsIgnoreCase(
						Constants.IE))
					capabillities = DesiredCapabilities.internetExplorer();
				else if (environment.getBrowserType().equalsIgnoreCase(
						Constants.CHROME))
					capabillities = DesiredCapabilities.chrome();
				else if (environment.getBrowserType().equalsIgnoreCase(
						Constants.OPERA))
					capabillities = DesiredCapabilities.opera();
				else if (environment.getBrowserType().equalsIgnoreCase(
						Constants.IPHONE))
					capabillities = DesiredCapabilities.iphone();

			// Select browser version
			if (environment.getBrowserVersion() != null
					&& !environment.getBrowserVersion().equals(""))
				capabillities.setCapability("version", environment
						.getBrowserVersion().trim());

			// Select host platform
			if (environment.getHostPlatform() != null
					&& !environment.getHostPlatform().equals(""))
				if (environment.getHostPlatform().equalsIgnoreCase("xp"))
					capabillities.setCapability("platform", Platform.XP);
				else if (environment.getHostPlatform().equalsIgnoreCase("mac"))
					capabillities.setCapability("platform", Platform.MAC);
				else if (environment.getHostPlatform()
						.equalsIgnoreCase("vista"))
					capabillities.setCapability("platform", Platform.VISTA);
				else if (environment.getHostPlatform().equalsIgnoreCase("unix"))
					capabillities.setCapability("platform", Platform.UNIX);
				else if (environment.getHostPlatform()
						.equalsIgnoreCase("linux"))
					capabillities.setCapability("platform", Platform.LINUX);

			if (currentFlowUnderExecution != null
					&& currentFlowUnderExecution.getName() != null)
				capabillities.setCapability("name",
						currentFlowUnderExecution.getName());

			webDriver = new RemoteWebDriver(new URL(environment.getHostName()),
					capabillities);

			client = new SauceREST(extractUserName(environment.getHostName()),
					extractAccessKey(environment.getHostName()));
			if (environment != null && environment.getHostName() != null
					&& environment.getHostName().contains("sauce")) {
				if (config.getBuild() != null && !config.getBuild().equals(""))
					client.jobBuild(webDriver.getSessionId().toString(),
							config.getBuild());
			}
		}

		if (environment.getWebDriverTimeouts() != null
				&& !environment.getWebDriverTimeouts().equals(""))
			webDriver
					.manage()
					.timeouts()
					.implicitlyWait(
							Integer.parseInt(environment.getWebDriverTimeouts()),
							TimeUnit.SECONDS);

		URI url = new URI(environment.getBrowserUrl());
		webDriver.get(url.toString());

		if (environment.getWebDriverDeleteAllCookies())
			webDriver.manage().deleteAllCookies();
	}

	/**
	 * Extract the access key in sauce link.
	 * 
	 * @param hostName
	 * @return The access key in sauce link
	 */
	private String extractAccessKey(String hostName) {
		if (hostName != null) {
			String[] hostNameParts = hostName.split(":");
			return hostNameParts[2].substring(0,
					(hostNameParts[2].indexOf("@")));
		}

		return null;
	}

	/**
	 * Extract the user name in sauce link.
	 * 
	 * @param hostName
	 * @return The user name in sauce link
	 */
	private String extractUserName(String hostName) {
		if (hostName != null) {
			String[] hostNameParts = hostName.split(":");
			return hostNameParts[1].substring(2, hostNameParts[1].length());
		}

		return null;
	}

	/**
	 * Execute a list of commands.
	 * 
	 * @param commands
	 *            The list of commands to execute.
	 * @return
	 * @throws Exception
	 */
	private void executeCommands(ArrayList<Command> commands) throws Exception {
		for (int i = 0; i < commands.size(); i++) {
			Command command = commands.get(i);
			executeCommand(command);
		}

	}

	/**
	 * Execute one command.
	 * 
	 * @param command
	 *            The target command to execute.
	 * @throws Exception
	 */
	private void executeCommand(Command command) throws Exception {
		logger.info("Execute command: " + command);

		if (config.isProgressBarEnable()) {
			Controller.progressBar.getProgressBar().setToolTipText(
					command.toString());
			Controller.progressBar.setValue();
		}

		command.setWebDriver(webDriver);

		if (command instanceof ClickLinkCommand) {
			((ClickLinkCommand) command).execute();
		} else if (command instanceof TypeTextCommand) {
			((TypeTextCommand) command).execute();
		} else if (command instanceof WaitSecondsCommand) {
			((WaitSecondsCommand) command).execute();
		} else if (command instanceof VerifyTextCommand) {
			((VerifyTextCommand) command).execute();
		} else if (command instanceof VerifyLinkCommand) {
			((VerifyLinkCommand) command).execute();
		} else if (command instanceof VerifyElementCommand) {
			((VerifyElementCommand) command).execute();
		} else if (command instanceof CheckBoxCommand) {
			((CheckBoxCommand) command).execute();
		} else if (command instanceof ClickButtonCommand) {
			((ClickButtonCommand) command).execute();
		} else if (command instanceof NavigateCommand) {
			((NavigateCommand) command).execute();
		} else if (command instanceof SelectCommand) {
			((SelectCommand) command).execute();
		} else if (command instanceof SelectRadioButton) {
			((SelectRadioButton) command).execute();
		} else
			throw new Exception("Undefined command:" + command);
	}
}
