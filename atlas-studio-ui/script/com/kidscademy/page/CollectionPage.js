$package("com.kidscademy.page");

/**
 * Atlas collection manager. A collection contains atlas objects of the same kind.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.CollectionPage = class extends com.kidscademy.Page {
	/** Construct atlas collection instance. */
	constructor() {
		super();

		this._collectionId = Number(WinMain.url.parameters.collection);
		AtlasService.getCollection(this._collectionId, collection => this._sidebar.setObject(collection).showObject());

		this._sidebar.on("create-object", this._onCreateObject, this);
		this._sidebar.on("import-wikipedia", this._onImportWikipedia, this);
		this._sidebar.on("add-all-to-release", this._onAddAllToRelease, this);
		this._sidebar.on("edit-collection", this._onEditCollection, this);

		this._listControl = this.getByCssClass("list-control");
		this._listControl.setLayout(this.getPageAttr("list-layout"));
		this._listControl.on("click", this._onItemClick, this);
		this._listControl.on("contextmenu", this._onContextMenu, this);

		this._contextMenu = this.getByClass(com.kidscademy.ContextMenu).bind(this);
		this._itemSelect = this.getByClass(com.kidscademy.ItemSelect);

		this._filterForm = this.getByCssClass("filter-form");
		this._filterForm.setObject(this.getPageAttr("filter-form"));

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._actions.fire("load-items");

		this._loadingInfoView = this.getByCssClass("loading-info");
		this._frameView = this.getByClass(com.kidscademy.FrameView);
		this._frameView.select("list-control");

		const exportAnchor = this._sidebar.getByCssClass("export");
		exportAnchor.on("click", ev => {
			exportAnchor.setAttr("href", `export-atlas-collection.xsp?id=${this._collectionId}&state=${this._filterForm.getObject().state}`);
		});
	}

	_onUnload() {
		this.setPageAttr("filter-form", this._filterForm.getObject());
		this.setPageAttr("list-layout", this._listControl.getLayout());
	}

	// --------------------------------------------------------------------------------------------
	// FILTER ACTION HANDLERS

	_onLoadItems() {
		const filter = this._filterForm.getObject();
		const timestamp = Date.now();
		AtlasService.getCollectionItems(filter, this._collectionId, items => {
			this._loadingInfoView.setObject({
				objectsCount: items.length,
				ellapsedTime: Date.now() - timestamp
			}).show();
			this._frameView.select("list-control");
			this._frameView.setObject(items);
		});
	}

	_onResetFilter() {

	}

	_onIconsView() {
		this._frameView.select("list-control");
		this._listControl.setLayout("icons");
	}

	_onTilesView() {
		this._frameView.select("list-control");
		this._listControl.setLayout("tiles");
	}

	_onDetailsView() {
		this._frameView.select("list-control");
		this._listControl.setLayout("details");
	}

	_onCardsView() {
		this._frameView.select("list-control");
		this._listControl.setLayout("cards");
	}

	_onRelatedView() {
		const filter = this._filterForm.getObject();
		AtlasService.getCollectionRelated(filter, this._collectionId, items => {
			this._frameView.select("insight-related");
			this._frameView.setObject(items);
		});
	}

	_onLinksView() {
		const filter = this._filterForm.getObject();
		AtlasService.getCollectionLinks(filter, this._collectionId, items => {
			this._frameView.select("insight-links");
			this._frameView.setObject(items);
		});
	}

	_onImagesView() {
		const filter = this._filterForm.getObject();
		AtlasService.getCollectionImages(filter, this._collectionId, items => {
			this._frameView.select("insight-images");
			this._frameView.setObject(items);
		});
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

	// --------------------------------------------------------------------------------------------
	// CONTEXT MENU HANDLERS

	_onEditObject(objectView) {
		const object = objectView.getUserData();
		WinMain.assign("@link/object-form", { object: object.id });
	}

	_onPreviewObject(objectView) {
		const object = objectView.getUserData();
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
