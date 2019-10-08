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

		const actions = this.getByCss(".side-bar .actions");
		actions.on(this, {
			"&new-collection": this._onNewCollection,
			"&edit-collection": this._onEditCollection,
			"&remove-collection": this._onRemoveCollection
		});
		const exportAnchor = actions.getByCssClass("export");
		exportAnchor.setAttr("href", "export-all-atlas-collections.xsp");
	}

	_onListClick(ev) {
		const li = ev.target.getParentByTag("li");
		const collection = li.getUserData();
		// store selected collection on global context to make it accessible to all descendant pages
		this.setContextAttr("collection", collection);
		WinMain.assign("collection.htm");
	}

	_onNewCollection() {

	}

	_onEditCollection() {

	}

	_onRemoveCollection() {

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
