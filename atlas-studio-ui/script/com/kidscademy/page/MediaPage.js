$package("com.kidscademy.page");

/**
 * MediaPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.MediaPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of MediaPage class.
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
		return "com.kidscademy.page.MediaPage";
	}
};

WinMain.createPage(com.kidscademy.page.MediaPage);
