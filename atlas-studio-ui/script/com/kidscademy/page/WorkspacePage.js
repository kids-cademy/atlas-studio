$package("com.kidscademy.page");

/**
 * WorkspacePage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.WorkspacePage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of WorkspacePage class.
	 */
	constructor() {
		super();

		this._contentView = this.getByClass(com.kidscademy.FrameView);
	}

	selectView(viewName) {
		this._contentView.select(viewName);
	}

	// --------------------------------------------------------------------------------------------

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.page.WorkspacePage";
	}
};

WinMain.createPage(com.kidscademy.page.WorkspacePage);
