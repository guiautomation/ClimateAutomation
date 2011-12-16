package entities.commands;

/**
 * 
 * @author gSoft Team
 * 
 */
public enum LocatorType {

	XPATH("xpath"), ID("id"), NAME("name"), LINK_TEXT("linkText"), PARTIAL_LINK_TEXT(
			"partialLinkText"), CSS_SELECTOR("cssSelector"), TAG_NAME("tagName");

	private String locatorType;

	LocatorType(String locatorType) {
		this.locatorType = locatorType;
	}

	/**
	 * 
	 * @return
	 */
	public String getLocatorType() {
		return locatorType;
	}
}