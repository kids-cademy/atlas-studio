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

		this._listView = this.getByCssClass("list-view");
		this._listView.on("click", this._onListClick, this);
		this._listView.on("contextmenu", this._onContextMenu, this);

		const sideMenu = this.getByCss(".side-bar .menu");
		sideMenu.on(this, {
			"&create-collection": this._onCreateCollection
		});
		const exportAnchor = sideMenu.getByCssClass("export");
		exportAnchor.setAttr("href", "export-all-atlas-collections.xsp");

		this._contentView = this.getByClass(com.kidscademy.FrameView);
		this._contentView.select("collections-list");
		this._collectionsList = this.getByClass(com.kidscademy.workspace.CollectionsList);
		this._collectionForm = this.getByClass(com.kidscademy.workspace.CollectionForm);

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
		AtlasService.createAtlasCollection(collection => {
			this._contentView.select("collection-form");
			this._collectionForm.open(collection, collection => {
				this._contentView.select("collections-list");
				if (collection != null) {
					AtlasService.saveAtlasCollection(collection, collection => this._listView.addObject(collection));
				}
			});
		});
	}

	// --------------------------------------------------------------------------------------------
	// CONTEXT MENU HANDLERS

	_onManageObjects(collectionView) {
		this._openCollectionPage(collectionView);
	}

	_onEditCollection(collectionView) {
		this._contentView.select("collection-form");
		this._collectionForm.open(collectionView.getUserData(), collection => {
			this._contentView.select("collections-list");
			if (collection != null) {
				AtlasService.saveAtlasCollection(collection, collection => collectionView.setObject(collection));
			}
		});
	}

	_onRemoveCollection(collectionView) {
		js.ua.System.confirm("@string/confirm-collection-remove", ok => {
			if (ok) {
				const collection = collectionView.getUserData();
				AtlasService.removeAtlasCollection(collection.id, () => collectionView.remove());
			}
		});
	}

	// --------------------------------------------------------------------------------------------

	_onListClick(ev) {
		const li = ev.target.getParentByTag("li");
		if (li != null) {
			this._openCollectionPage(li);
		}
	}

	_onFormClose(ev) {
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
		WinMain.assign("@link/collection", { id: collection.id });
	}

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
