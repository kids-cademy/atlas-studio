$package("com.kidscademy.page");

/**
 * Atlas collection manager. A collection contains atlas objects of the same kind.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.CollectionPage = class extends com.kidscademy.Page {
	constructor() {
		super();

		this._ATTR_FORM = "filter-form";
		this._ATTR_LAYOUT = "layout-handler";
		this._ATTR_OBJECT = "object-id";

		this._collectionId = Number(WinMain.url.parameters.collection);
		AtlasService.getCollection(this._collectionId, collection => this._sidebar.setObject(collection).showObject());

		this._filterForm = this.getByCssClass("filter-form");
		this._filterForm.setObject(this.getPageAttr(this._ATTR_FORM));

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._actions.fire("load-items");

		this._sidebar.on("create-object", this._onCreateObject, this);
		this._sidebar.on("import-wikipedia", this._onImportWikipedia, this);
		this._sidebar.on("add-all-to-release", this._onAddAllToRelease, this);
		this._sidebar.on("edit-collection", this._onEditCollection, this);
		this._sidebar.on("remove-collection", this._onRemoveCollection, this);

		this._listControl = this.getByCssClass("list-control");
		this._listControl.setLayout(this.getPageAttr("list-layout"));
		this._listControl.on("click", this._onItemClick, this);
		this._listControl.on("contextmenu", this._onContextMenu, this);

		this._contextMenu = this.getByClass(com.kidscademy.ContextMenu).bind(this);
		this._itemSelect = this.getByClass(com.kidscademy.ItemSelect);

		this._loadingInfoView = this.getByCssClass("loading-info");
		this._frameView = this.getByClass(com.kidscademy.FrameView);

		const exportAnchor = this._sidebar.getByCssClass("export");
		exportAnchor.on("click", ev => {
			exportAnchor.setAttr("href", `export-atlas-collection.xsp?id=${this._collectionId}&state=${this._filterForm.getObject().state}`);
		});
	}

	_onUnload() {
		this.setPageAttr(this._ATTR_FORM, this._filterForm.getObject());
	}

	// --------------------------------------------------------------------------------------------
	// ACTION BAR HANDLERS

	_onLoadItems() {
		const filter = this._filterForm.getObject();
		const timestamp = Date.now();
		AtlasService.getCollectionItems(filter, this._collectionId, items => {
			this._loadingInfoView.setObject({
				objectsCount: items.length,
				ellapsedTime: Date.now() - timestamp
			}).show();
			this._frameView.setObject(items);

			var layoutHandler = this.getPageAttr(this._ATTR_LAYOUT);
			if (layoutHandler == null) {
				layoutHandler = "_onIconsView";
			}
			this[layoutHandler]();
		});
	}

	_onResetFilter() {

	}

	_onIconsView() {
		this._selectListControl("_onIconsView", "icons");
	}

	_onTilesView() {
		this._selectListControl("_onTilesView", "tiles");
	}

	_onDetailsView() {
		this._selectListControl("_onDetailsView", "details");
	}

	_onCardsView() {
		this._selectListControl("_onCardsView", "cards");
	}

	_selectListControl(handler, layout) {
		this.setPageAttr(this._ATTR_LAYOUT, handler);
		this._frameView.select("list-control");
		this._listControl.setLayout(layout);
		this._onPostLoad();
	}

	_onRelatedView() {
		this._selectInsight("_onRelatedView", "insight-related", "getCollectionRelated");
	}

	_onLinksView() {
		this._selectInsight("_onLinksView", "insight-links", "getCollectionLinks");
	}

	_onImagesView() {
		this._selectInsight("_onImagesView", "insight-images", "getCollectionImages");
	}

	_selectInsight(handler, layout, method) {
		this.setPageAttr(this._ATTR_LAYOUT, handler);
		const filter = this._filterForm.getObject();
		AtlasService[method](filter, this._collectionId, items => {
			this._frameView.select(layout);
			this._frameView.setObject(items);
			this._onPostLoad();
		});
	}

	_onPostLoad() {
		const objectId = this.getPageAttr(this._ATTR_OBJECT);
		if (objectId != null) {
			this.removePageAttr(this._ATTR_OBJECT);
			this._frameView.scrollIntoView(objectId);
		}
	}

	// --------------------------------------------------------------------------------------------
	// SIDE MENU HANDLERS

	_onCreateObject() {
		WinMain.assign("@link/object-form", { collection: this._collectionId });
	}

	_onImportWikipedia() {
		js.ua.System.prompt("@string/prompt-wikipedia-url", url => {
			AtlasService.importWikipediaObject(this._collectionId, url, object => this._listControl.addObject(object));
		});
	}

	_onAddAllToRelease() {
		const childIds = this._listControl.getChildren().map(child => child.getAttr("id"));
		ReleaseService.getReleases(releases => this._itemSelect.load(releases));
		this._itemSelect.open(release => {
			ReleaseService.addReleaseChildren(release.id, childIds, () => js.ua.System.alert("@string/alert-processing-done"));
		});
	}

	_onEditCollection() {
		WinMain.assign("@link/collection-form", { collection: this._collectionId });
	}

	_onRemoveCollection() {
		js.ua.System.confirm("@string/confirm-collection-remove", ok => {
			if (ok) {
				AtlasService.removeAtlasCollection(this._collectionId, () => WinMain.assign("@link/collections"));
			}
		});
	}

	// --------------------------------------------------------------------------------------------
	// CONTEXT MENU HANDLERS

	_onEditObject(objectView) {
		const object = objectView.getUserData();
		this.setPageAttr(this._ATTR_OBJECT, object.id);
		WinMain.assign("@link/object-form", { object: object.id });
	}

	_onPreviewObject(objectView) {
		const object = objectView.getUserData();
		this.setPageAttr(this._ATTR_OBJECT, object.id);
		WinMain.assign("@link/reader", { object: object.id });
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

	_onAddToRelease(objectView) {
		ReleaseService.getReleases(releases => this._itemSelect.load(releases));
		this._itemSelect.open(release => {
			const object = objectView.getUserData();
			ReleaseService.addReleaseChild(release.id, object.id);
		});
	}

	// --------------------------------------------------------------------------------------------

	_onItemClick(ev) {
		const objectView = ev.target.getParentByTag("li");
		if (objectView == null) {
			return;
		}

		if (ev.ctrlKey) {
			this._onPreviewObject(objectView);
		}
		else {
			this._onEditObject(objectView);
		}
	}

	_onContextMenu(ev) {
		const objectView = ev.target.getParentByTag("li");
		if (objectView != null) {
			ev.halt();
			this._contextMenu.open(objectView);
		}
	}

	toString() {
		return "com.kidscademy.page.CollectionPage";
	}
};

WinMain.createPage(com.kidscademy.page.CollectionPage);
