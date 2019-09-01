$package("com.kidscademy.atlas");

/**
 * AccountPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.atlas.AccountPage = class extends com.kidscademy.page.Page {
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
		return "com.kidscademy.atlas.AccountPage";
	}
};

WinMain.createPage(com.kidscademy.atlas.AccountPage);
