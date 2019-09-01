$package("com.kidscademy.atlas");

/**
 * MediaPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.atlas.MediaPage = class extends com.kidscademy.page.Page {
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
		return "com.kidscademy.atlas.MediaPage";
	}
};

WinMain.createPage(com.kidscademy.atlas.MediaPage);
