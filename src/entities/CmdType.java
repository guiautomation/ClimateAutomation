package entities;

/**
 * This class contains all commands types that are supported in this framework.
 * 
 * @author gSoft Team
 * 
 */
public enum CmdType {

	WAIT_IN_SECONDS("waitseconds"), CLICK_LINK("clicklink"), CLICK_BUTTON(
			"clickbutton"), TYPE_TEXT("typetext"), NAVIGATE("navigate"), VERIFY_TEXT(
			"verifytext"), SELECT("select"), CHECK_BOX("checkbox"), SELECT_RADIO_BUTTON(
			"selectradiobutton"), VERIFY_ELEMENT("verifyelement"), VERIFY_LINK(
			"verifylink");

	private String cmdType;

	CmdType(String cmdType) {
		this.cmdType = cmdType;
	}

	/**
	 * 
	 * @return
	 */
	public String getCmdType() {
		return cmdType;
	}
}
