import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import main.Controller;

import org.apache.log4j.Logger;

import boundaries.GUIHandler;
import boundaries.ProgressBar;
import entities.Config;
import entities.Constants;
import entities.Environment;

/**
 * 
 * @author gSoft Team
 * 
 */
public class Main {

	/**
	 * Pre-execution configurations.
	 */
	static {

		File file = new File(Constants.OUT_DIR);
		file.deleteOnExit();
		file.mkdir();
	}

	private static Logger logger = Logger.getLogger(Main.class);

	/**
	 * @param args
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {

		Config config = getConfigParams();

		if (config.isProgressBarEnable()) {
			GUIHandler guiHandler = new ProgressBar();
			Thread thread = new Thread(guiHandler);
			thread.start();
			Thread.sleep(1000);

			Controller controller = new Controller(guiHandler, config);
			Controller.setup();
			controller.test();
			Controller.tearDown();

			thread.stop();
			guiHandler.getFrame().dispose();
		} else {
			Controller controller = new Controller(config);
			Controller.setup();
			controller.test();
			Controller.tearDown();
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
					config.setProgressBarEnable(attribute[1].trim().equals(
							"true") ? true : false);

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
}
