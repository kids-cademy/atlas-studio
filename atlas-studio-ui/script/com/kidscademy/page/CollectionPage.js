$package("com.kidscademy.page");

/**
 * Atlas collection manager. A collection contains atlas objects of the same kind.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.CollectionPage = class extends com.kidscademy.page.Page {
	/** Construct atlas collection instance. */
	constructor() {
		super();
		// current selected collection is stored on global context by dashboard
		const collection = this.getContextAttr("collection");

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
		this._moveToPage("form.htm", "0");
	}

	_onListClick(ev) {
		const li = ev.target.getParentByTag("li");
		const objectId = li.getAttr("id");

		if (ev.ctrlKey) {
			this._moveToPage("reader.htm", objectId);
		}
		else {
			this._moveToPage("form.htm", objectId);
		}
	}

	_moveToPage(pageName, objectId) {
		// store selected object ID on global context to make it available to next pages
		this.setContextAttr("objectId", objectId);
		WinMain.assign(pageName);
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
