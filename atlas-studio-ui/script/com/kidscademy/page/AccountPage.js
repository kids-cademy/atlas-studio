$package("com.kidscademy.page");

/**
 * AccountPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.AccountPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of AccountPage class.
	 */
	constructor() {
		super();
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.page.AccountPage";
	}
};

WinMain.createPage(com.kidscademy.page.AccountPage);
