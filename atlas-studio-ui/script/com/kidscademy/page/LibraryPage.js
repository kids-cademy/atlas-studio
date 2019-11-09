$package("com.kidscademy.page");

/**
 * LibraryPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.LibraryPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of LibraryPage class.
	 */
	constructor() {
		super();

		this._listView = this.getByCssClass("list-view");
		this._listView.on("click", this._onListClick, this);
		this._listView.on("contextmenu", this._onContextMenu, this);

		const sideMenu = this.getByCss(".side-bar .menu");
		sideMenu.on(this, {
			"&create-collection": this._onCreateCollection
		});
		const exportAnchor = sideMenu.getByCssClass("export");
		exportAnchor.setAttr("href", "export-all-atlas-collections.xsp");

		this._listType = new com.kidscademy.CssFlags(this._listView, "icons", "cards");
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._contextMenu = this.getByClass(com.kidscademy.ContextMenu).bind(this);

		this._listType.set(this.getPageAttr("list-type"));
		this._actions.fire("load-items");
	}

	_onUnload() {
		this.setPageAttr("list-type", this._listType.get());
	}

	// --------------------------------------------------------------------------------------------
	// FILTER ACTION HANDLERS

	_onLoadItems() {
		AtlasService.getCollections(collections => this._listView.setObject(collections).show());
	}

	_onResetFilter() {

	}

	_onIconsView() {
		this._listType.set("icons");
	}

	_onCardsView() {
		this._listType.set("cards");
	}

	// --------------------------------------------------------------------------------------------
	// SIDE MENU HANDLERS

	_onCreateCollection() {

	}

	// --------------------------------------------------------------------------------------------
	// CONTEXT MENU HANDLERS

	_onManageObjects(collectionView) {
		this._openCollectionPage(collectionView);
	}

	_onEditCollection(collectionView) {
		const collection = collectionView.getUserData();
		alert(`edit ${collection.display}`)
	}

	_onRemoveCollection(collectionView) {
		const collection = collectionView.getUserData();
		alert(`remove ${collection.display}`)
	}

	// --------------------------------------------------------------------------------------------

	_onListClick(ev) {
		const li = ev.target.getParentByTag("li");
		if (li != null) {
			this._openCollectionPage(li);
		}
	}

	_onContextMenu(ev) {
		const li = ev.target.getParentByTag("li");
		if (li == null) {
			return;
		}
		ev.halt();
		this._contextMenu.open(li);
	}

	_openCollectionPage(collectionView) {
		const collection = collectionView.getUserData();
		// store selected collection on global context to make it accessible to all descendant pages
		this.setContextAttr("collection", collection);
		WinMain.assign("@link/collection");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.page.LibraryPage";
	}
};

WinMain.createPage(com.kidscademy.page.LibraryPage);
