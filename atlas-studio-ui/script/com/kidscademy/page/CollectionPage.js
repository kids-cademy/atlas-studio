$package("com.kidscademy.page");

/**
 * CollectionPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.CollectionPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of CollectionPage class.
	 */
	constructor() {
		super();
		// current selected collection is stored on global context
		const collection = this._getContextAttr("collection");

		this._listView = this.getByCssClass("list-view");
		this._listView.on("click", this._onListClick, this);

		this._sidebar = this.getByCss(".side-bar .header");
		this._sidebar.setObject(collection);

		const actions = this.getByCss(".side-bar .actions");
		actions.on(this, {
			"&edit-collection": this._onEditCollection,
			"&new-object": this._onNewObject,
			"&export-collection": this._onExportCollection,
			"&remove-collection": this._onRemoveCollection
		});

		AtlasService.getCollectionItems(collection.id, items => this._listView.setObject(items));
	}

	_onEditCollection() {

	}

	_onExportCollection() {

	 }
	
	_onRemoveCollection() { 

	}

	_onNewObject() {
		this._openObjectForm("0");
	}

	_onListClick(ev) {
		const li = ev.target.getParentByTag("li");
		this._openObjectForm(li.getAttr("id"));
	}

	_openObjectForm(objectId) {
		// store selected object ID on global context to make it available to form page
		this._setContextAttr("objectId", objectId);
		WinMain.assign("form.htm");
	}

	_onBack() {
		WinMain.assign("dashboard.htm");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.page.CollectionPage";
	}
};

WinMain.createPage(com.kidscademy.page.CollectionPage);
