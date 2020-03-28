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

		const sidebar = this.getByCss(".side-bar .header");
		this._collectionId = Number(WinMain.url.parameters.id);
		AtlasService.getCollection(this._collectionId, collection => sidebar.setObject(collection));

		const sideMenu = this.getByCss(".side-bar .menu");
		sideMenu.on(this, {
			"&create-object": this._onCreateObject,
			"&import-wikipedia": this._onImportWikipedia,
			"&add-to-release": this._onAddToRelease
		});

		this._contentView = this.getByClass(com.kidscademy.FrameView);
		this._contentView.select("list-view");

		this._infoView = this.getByCssClass("info");

		this._listView = this.getByCssClass("list-view");
		this._listView.on("click", this._onListClick, this);
		this._listView.on("contextmenu", this._onContextMenu, this);

		this._listType = new com.kidscademy.CssFlags(this._listView, "icons", "tiles", "details", "cards");
		this._listType.set(this.getPageAttr("list-type"));

		this._filterForm = this.getByCssClass("filter-form");
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._contextMenu = this.getByClass(com.kidscademy.ContextMenu).bind(this);

		this._itemSelect = this.getByClass(com.kidscademy.ItemSelect);

		this._filterForm.setObject(this.getPageAttr("filter-form"));
		this._actions.fire("load-items");

		const exportAnchor = sideMenu.getByCssClass("export");
		exportAnchor.on("click", ev => {
			exportAnchor.setAttr("href", `export-atlas-collection.xsp?id=${this._collectionId}&state=${this._filterForm.getObject().state}`);
		});
	}

	_onUnload() {
		this.setPageAttr("filter-form", this._filterForm.getObject());
		this.setPageAttr("list-type", this._listType.get());
	}

	// --------------------------------------------------------------------------------------------
	// FILTER ACTION HANDLERS

	_onLoadItems() {
		const filter = this._filterForm.getObject();
		const timestamp = Date.now();
		AtlasService.getCollectionItems(filter, this._collectionId, items => {
			this._infoView.setObject({
				objectsCount: items.length,
				ellapsedTime: Date.now() - timestamp
			}).show();
			this._contentView.select("list-view");
			this._contentView.setObject(items);
			this._revealItem();
		});
	}

	_onResetFilter() {

	}

	_onIconsView() {
		this._contentView.select("list-view");
		this._listType.set("icons");
		this._revealItem();
	}

	_onTilesView() {
		this._contentView.select("list-view");
		this._listType.set("tiles");
		this._revealItem();
	}

	_onDetailsView() {
		this._contentView.select("list-view");
		this._listType.set("details");
		this._revealItem();
	}

	_onCardsView() {
		this._contentView.select("list-view");
		this._listType.set("cards");
		this._revealItem();
	}

	_onRelatedView() {
		const filter = this._filterForm.getObject();
		AtlasService.getCollectionRelated(filter, this._collectionId, items => {
			this._contentView.select("insight-related");
			this._contentView.setObject(items);
			this._revealItem();
		});
	}

	_onLinksView() {
		const filter = this._filterForm.getObject();
		AtlasService.getCollectionLinks(filter, this._collectionId, items => {
			this._contentView.select("insight-links");
			this._contentView.setObject(items);
			this._revealItem();
		});
	}

	_onImagesView() {
		const filter = this._filterForm.getObject();
		AtlasService.getCollectionImages(filter, this._collectionId, items => {
			this._contentView.select("insight-images");
			this._contentView.setObject(items);
			this._revealItem();
		});
	}

	// --------------------------------------------------------------------------------------------
	// SIDE MENU HANDLERS

	_onCreateObject() {
		this._moveToPage("@link/form", "0");
	}

	_onImportWikipedia() {
		js.ua.System.prompt("@string/prompt-wikipedia-url", url => {
			AtlasService.importWikipediaObject(this._collectionId, url, object => {
				this._listView.addObject(object);
				this._revealItem(object.id);
			});
		});
	}

	_onAddToRelease() {
		const childIds = this._listView.getChildren().map(child => child.getAttr("id"));
		ReleaseService.getReleases(releases => this._itemSelect.load(releases));
		this._itemSelect.open(release => {
			ReleaseService.addReleaseChildren(release.id, childIds);
		});
	}

	// --------------------------------------------------------------------------------------------
	// CONTEXT MENU HANDLERS

	_onEditObject(objectView) {
		const object = objectView.getUserData();
		this._moveToPage("@link/form", object.id);
	}

	_onPreviewObject(objectView) {
		const object = objectView.getUserData();
		WinMain.assign("@link/reader", { id: object.id });
	}

	_onRemoveObject(objectView) {
		js.ua.System.confirm("@string/confirm-object-remove", (ok) => {
			if (ok) {
				const object = objectView.getUserData();
				AtlasService.removeAtlasObject(object.id, () => objectView.remove());
			}
		});
	}

	_onMoveObject(objectView) {
		AtlasService.getCollections(collections => this._itemSelect.load(collections));
		this._itemSelect.open(collection => {
			const object = objectView.getUserData();
			AtlasService.moveAtlasObject(object, collection.id, () => objectView.remove());
		});
	}

	// --------------------------------------------------------------------------------------------

	_onListClick(ev) {
		const li = ev.target.getParentByTag("li");
		if (li == null) {
			return;
		}
		const objectId = li.getAttr("id");
		this.setPageAttr("object-id", objectId);

		if (ev.ctrlKey) {
			this._onPreviewObject(li);
		}
		else {
			this._moveToPage("@link/form", objectId);
		}
	}

	_onContextMenu(ev) {
		const li = ev.target.getParentByTag("li");
		if (li == null) {
			return;
		}
		ev.halt();

		if (ev.ctrlKey) {
			const objectId = li.getAttr("id");
			this._onPreviewObject(li);
			return;
		}

		this._contextMenu.open(li);
	}

	// --------------------------------------------------------------------------------------------

	_moveToPage(pageName, objectId) {
		// store selected object ID on global context to make it available to next pages
		this.setContextAttr("objectId", objectId);
		WinMain.assign(pageName, { id: objectId });
	}

	_revealItem(objectId) {
		if (typeof objectId === "undefined") {
			objectId = this.getPageAttr("object-id");
		}
		if (objectId == null) {
			return;
		}
		var itemView = this.getById(objectId);
		if (itemView != null) {
			itemView.scrollIntoView();
		}
		this.removePageAttr("object-id");
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
