$package("com.kidscademy.page");

/**
 * DashboardPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.DashboardPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of DashboardPage class.
	 */
	constructor() {
		super();

		this._listView = this.getByCssClass("list-view");
		this._listView.on("click", this._onListClick, this);

		AtlasService.getCollections(collections => this._listView.setObject(collections));
	}

	_onListClick(ev) {
		const li = ev.target.getParentByTag("li");
		const collection = li.getUserData();
		// store selected collection on global context to make it accessible to all descendant pages
		this.setContextAttr("collection", collection);
		WinMain.assign("collection.htm");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.page.DashboardPage";
	}
};

WinMain.createPage(com.kidscademy.page.DashboardPage);
