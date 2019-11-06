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
		this._listView.hide();

		this._sidebar = this.getByCss(".side-bar .header");
		this._sidebar.setObject(this._collection);

		const sidebarActions = this.getByCss(".side-bar .actions");
		sidebarActions.on(this, {
			"&edit-collection": this._onEditCollection,
			"&new-object": this._onNewObject,
			"&remove-collection": this._onRemoveCollection
		});
		const exportAnchor = sidebarActions.getByCssClass("export");
		exportAnchor.setAttr("href", `export-atlas-collection.xsp?id=${this._collection.id}`);

		this._filterForm = this.getByCssClass("form-bar");
		this._listType = new com.kidscademy.CssFlags(this._listView, "icons", "cards", "links");
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);

		this._filterForm.setObject(this.getPageAttr("filter-form"));
		this._listType.set(this.getPageAttr("list-type"));
		this._actions.fire("load-items");
	}

	_onUnload() {
		this.setPageAttr("filter-form", this._filterForm.getObject());
		this.setPageAttr("list-type", this._listType.get());
	}

	// --------------------------------------------------------------------------------------------
	// FILTER ACTION HANDLERS

	_onLoadItems() {
		const filter = this._filterForm.getObject();
		AtlasService.getCollectionItems(filter, this._collection.id, items => {
			this._listView.setObject(items).show()
			this._autoScroll();
		});
	}

	_onResetFilter() {

	}

	_onIconsView() {
		this._listType.set("icons");
	}

	_onCardsView() {
		this._listType.set("cards");
	}

	_onLinksView() {
		this._listType.set("links");
	}

	// --------------------------------------------------------------------------------------------
	// SIDE MENU HANDLERS

	_onEditCollection() {

	}

	_onRemoveCollection() {

	}

	_onNewObject() {
		this._moveToPage("form.htm", "0");
	}

	_onListClick(ev) {
		const li = ev.target.getParentByTag("li");
		if (li == null) {
			return;
		}
		const objectId = li.getAttr("id");
		this.setPageAttr("object-id", objectId);

		if (ev.ctrlKey) {
			this._moveToPage("@link/reader", objectId);
		}
		else {
			this._moveToPage("@link/form", objectId);
		}
	}

	// --------------------------------------------------------------------------------------------

	_moveToPage(pageName, objectId) {
		// store selected object ID on global context to make it available to next pages
		this.setContextAttr("objectId", objectId);
		WinMain.assign(pageName);
	}

	_autoScroll() {
		var objectId = this.getPageAttr("object-id");
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
