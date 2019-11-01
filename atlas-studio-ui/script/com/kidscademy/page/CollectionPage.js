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
		this._collection = this.getContextAttr("collection");

		this._listView = this.getByCssClass("list-view");
		this._listView.on("click", this._onListClick, this);

		this._sidebar = this.getByCss(".side-bar .header");
		this._sidebar.setObject(this._collection);

		const actions = this.getByCss(".side-bar .actions");
		actions.on(this, {
			"&edit-collection": this._onEditCollection,
			"&new-object": this._onNewObject,
			"&remove-collection": this._onRemoveCollection
		});
		const exportAnchor = actions.getByCssClass("export");
		exportAnchor.setAttr("href", `export-atlas-collection.xsp?id=${this._collection.id}`);

		AtlasService.getCollectionItems(this._collection.id, items => this._listView.setObject(items));
	}

	_onEditCollection() {

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
			this._moveToPage("@link/reader", objectId);
		}
		else {
			this._moveToPage("@link/form", objectId);
		}
	}

	_moveToPage(pageName, objectId) {
		// store selected object ID on global context to make it available to next pages
		this.setContextAttr("objectId", objectId);
		WinMain.assign(pageName);
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
