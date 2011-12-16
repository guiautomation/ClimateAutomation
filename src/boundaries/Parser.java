package boundaries;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;

import entities.CmdType;
import entities.Command;
import entities.Constants;
import entities.Environment;
import entities.Flow;
import entities.commands.CheckBoxCommand;
import entities.commands.ClickButtonCommand;
import entities.commands.ClickLinkCommand;
import entities.commands.LocatorType;
import entities.commands.NavigateCommand;
import entities.commands.SelectCommand;
import entities.commands.SelectRadioButton;
import entities.commands.TypeTextCommand;
import entities.commands.VerifyElementCommand;
import entities.commands.VerifyLinkCommand;
import entities.commands.VerifyTextCommand;
import entities.commands.WaitSecondsCommand;

/**
 * This is a SAX parser to parse all XML files in the framework.
 * 
 * @author gSoft Team
 * 
 */
@SuppressWarnings("deprecation")
public class Parser extends HandlerBase {
	static int id = 0;
	public static HashMap<Integer, Flow> listOfFlows = new HashMap<Integer, Flow>();
	public static HashMap<String, Integer> NameIdFlowMapping = new HashMap<String, Integer>();
	ArrayList<Command> listOfCommands = new ArrayList<Command>();
	Flow flow = new Flow();
	Command command;

	static String filePath;
	String elementType;

	private static Logger logger = Logger.getLogger(Parser.class);

	public Parser() {
		// TODO Auto-generated constructor stub
	}

	public Parser(String filePath) {
		Parser.filePath = filePath;

		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {

			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new File(Parser.filePath), new Parser());

		} catch (Throwable err) {
			err.printStackTrace();
			logger.error(err.toString());
		}
	}

	@Override
	public void startElement(String name, AttributeList attrs)
			throws SAXException {
		LocatorType locatorType = null;

		if (name.equals(Constants.TEST)) {

		} else if (name.equals(Constants.FLOW)) {
			elementType = Constants.FLOW;
			Environment environment = new Environment();
			environment.setName(attrs.getValue(Constants.ENVIRONMENT));
			flow = new Flow(id++, attrs.getValue(Constants.NAME),
					attrs.getValue(Constants.DEFECT),
					attrs.getValue(Constants.TESTCASEID), environment,
					Parser.filePath, attrs.getValue(Constants.PREFLOW), null,
					false);
		} else {

			String tempLocatorType = attrs.getValue(Constants.LOCATOR_TYPE);
			if (tempLocatorType != null
					&& tempLocatorType.equals(LocatorType.CSS_SELECTOR
							.getLocatorType()))
				locatorType = LocatorType.CSS_SELECTOR;
			else if (tempLocatorType != null
					&& tempLocatorType.equals(LocatorType.ID.getLocatorType()))
				locatorType = LocatorType.ID;
			else if (tempLocatorType != null
					&& tempLocatorType.equals(LocatorType.LINK_TEXT
							.getLocatorType()))
				locatorType = LocatorType.LINK_TEXT;
			else if (tempLocatorType != null
					&& tempLocatorType
							.equals(LocatorType.NAME.getLocatorType()))
				locatorType = LocatorType.NAME;
			else if (tempLocatorType != null
					&& tempLocatorType.equals(LocatorType.PARTIAL_LINK_TEXT
							.getLocatorType()))
				locatorType = LocatorType.PARTIAL_LINK_TEXT;
			else if (tempLocatorType != null
					&& tempLocatorType.equals(LocatorType.TAG_NAME
							.getLocatorType()))
				locatorType = LocatorType.TAG_NAME;
			else if (tempLocatorType != null
					&& tempLocatorType.equals(LocatorType.XPATH
							.getLocatorType()))
				locatorType = LocatorType.XPATH;

			if (name.equals(CmdType.CLICK_LINK.getCmdType())) {
				elementType = CmdType.CLICK_LINK.getCmdType();
				command = new ClickLinkCommand(null, locatorType);
			} else if (name.equals(CmdType.CLICK_BUTTON.getCmdType())) {
				elementType = CmdType.CLICK_BUTTON.getCmdType();
				command = new ClickButtonCommand(null, locatorType);
			} else if (name.equals(CmdType.WAIT_IN_SECONDS.getCmdType())) {
				elementType = CmdType.WAIT_IN_SECONDS.getCmdType();
				command = new WaitSecondsCommand();
			} else if (name.equals(CmdType.NAVIGATE.getCmdType())) {
				elementType = CmdType.NAVIGATE.getCmdType();
				command = new NavigateCommand();
			} else if (name.equals(CmdType.TYPE_TEXT.getCmdType())) {
				elementType = CmdType.TYPE_TEXT.getCmdType();
				command = new TypeTextCommand(
						attrs.getValue(Constants.LOCATOR), locatorType, null);
			} else if (name.equals(CmdType.SELECT.getCmdType())) {
				elementType = CmdType.SELECT.getCmdType();
				command = new SelectCommand(attrs.getValue(Constants.LOCATOR),
						locatorType, null);
			} else if (name.equals(CmdType.CHECK_BOX.getCmdType())) {
				elementType = CmdType.CHECK_BOX.getCmdType();
				command = new CheckBoxCommand(
						attrs.getValue(Constants.LOCATOR), locatorType, null);
			} else if (name.equals(CmdType.SELECT_RADIO_BUTTON.getCmdType())) {
				elementType = CmdType.SELECT_RADIO_BUTTON.getCmdType();
				command = new SelectRadioButton(
						attrs.getValue(Constants.LOCATOR), locatorType, null);
			} else if (name.equals(CmdType.VERIFY_TEXT.getCmdType())) {
				elementType = CmdType.VERIFY_TEXT.getCmdType();
				command = new VerifyTextCommand(null, (attrs
						.getValue(Constants.POSITIVE).trim()
						.equals(Constants.TRUE) ? true : false));
			} else if (name.equals(CmdType.VERIFY_ELEMENT.getCmdType())) {
				elementType = CmdType.VERIFY_ELEMENT.getCmdType();
				command = new VerifyElementCommand(null, locatorType, (attrs
						.getValue(Constants.POSITIVE).trim()
						.equals(Constants.TRUE) ? true : false));
			} else if (name.equals(CmdType.VERIFY_LINK.getCmdType())) {
				elementType = CmdType.VERIFY_LINK.getCmdType();
				command = new VerifyLinkCommand(null, locatorType, (attrs
						.getValue(Constants.POSITIVE).trim()
						.equals(Constants.TRUE) ? true : false));
			} else
				logger.error("Undefined command \"" + name + "\" in "
						+ Parser.filePath);
		}
	}

	@Override
	public void endElement(String name) throws SAXException {
		if (name.equals(Constants.FLOW)) {
			flow.setListOfCommands(listOfCommands);
			if (NameIdFlowMapping.containsKey(flow.getName()))
				logger.warn("The flow that has the name \"" + flow.getName()
						+ "\" is duplicated");
			NameIdFlowMapping.put(flow.getName(), flow.getId());
			listOfFlows.put(flow.getId(), flow);
			listOfCommands = new ArrayList<Command>();
			flow = new Flow();
			elementType = null;
		}
	}

	@Override
	public void characters(char buf[], int offset, int len) throws SAXException {
		String value = new String(buf, offset, len);

		// Temporary solution for special character "]" in Sax parser.
		value = value.replaceAll("ampbra;", "]");

		if (elementType != null && elementType.equals(Constants.FLOW)) {
		} else if (elementType != null
				&& elementType.equals(CmdType.CLICK_LINK.getCmdType())) {
			((ClickLinkCommand) command).setLocator(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.CLICK_BUTTON.getCmdType())) {
			((ClickButtonCommand) command).setLocator(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.NAVIGATE.getCmdType())) {
			((NavigateCommand) command).setValue(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.TYPE_TEXT.getCmdType())) {
			((TypeTextCommand) command).setValue(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.WAIT_IN_SECONDS.getCmdType())) {
			((WaitSecondsCommand) command).setValue(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.SELECT.getCmdType())) {
			((SelectCommand) command).setValue(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.CHECK_BOX.getCmdType())) {
			((CheckBoxCommand) command).setValue(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.SELECT_RADIO_BUTTON.getCmdType())) {
			((SelectRadioButton) command).setValue(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.VERIFY_TEXT.getCmdType())) {
			((VerifyTextCommand) command).setValue(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.VERIFY_ELEMENT.getCmdType())) {
			((VerifyElementCommand) command).setLocator(value);
		} else if (elementType != null
				&& elementType.equals(CmdType.VERIFY_LINK.getCmdType())) {
			((VerifyLinkCommand) command).setLocator(value);
		}

		if (command != null
				&& (command.getCommandType() != null && command
						.getCommandType().getCmdType() != "")) {
			listOfCommands.add(command);
			elementType = null;
			command = new Command() {

				@Override
				protected void execute() {
					// TODO Auto-generated method stub

				}
			};
		}
	}

	public static HashMap<Integer, Flow> getFlowsList() {
		return listOfFlows;
	}

	public static HashMap<String, Integer> getNameIdFlowMapping() {
		return NameIdFlowMapping;
	}

}
