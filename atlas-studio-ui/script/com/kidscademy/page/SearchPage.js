$package("com.kidscademy.page");

/**
 * SearchPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.SearchPage = class extends com.kidscademy.Page {
	constructor() {
		super();

		this._sidebar.on("update-index", this._onUpdateIndex, this);

		this._searchInput = this.getByName("search-input");
		this._searchInput.setValue(this.getPageAttr("search-input"));
		this._searchInput.focus();

		this._searchResult = this.getByCssClass("search-result");
		this._searchResult.setLayout(this.getPageAttr("list-layout"));
		this._searchResult.on("click", this._onItemClick, this);
		this._searchResult.on("contextmenu", this._onContextMenu, this);

		this._contextMenu = this.getByClass(com.kidscademy.ContextMenu).bind(this);

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._actions.fire("search-submit");

		this._loadingInfoView = this.getByCssClass("loading-info");
	}

	_onUnload() {
		this.setPageAttr("search-input", this._searchInput.getValue());
		this.setPageAttr("list-layout", this._searchResult.getLayout());
	}

	// --------------------------------------------------------------------------------------------
	// FILTER ACTION HANDLERS

	_onSearchSubmit() {
		const search = this._searchInput.getValue();
		if (search) {
			const timestamp = Date.now();
			AtlasService.search(search, items => {
				this._loadingInfoView.setObject({
					objectsCount: items.length,
					ellapsedTime: Date.now() - timestamp
				}).show();
				this._searchResult.setObject(items).show();
				this._searchInput.focus();
			});
		}
	}

	_onSearchReset() {
		this._searchInput.reset();
		this._loadingInfoView.hide();
		this._searchResult.removeChildren();
	}

	_onIconsView() {
		this._searchResult.setLayout("icons");
	}

	_onTilesView() {
		this._searchResult.setLayout("tiles");
	}

	_onDetailsView() {
		this._searchResult.setLayout("details");
	}

	_onCardsView() {
		this._searchResult.setLayout("cards");
	}

	// --------------------------------------------------------------------------------------------
	// SIDE MENU HANDLERS

	_onUpdateIndex() {
		AtlasService.updateIndex(() => js.ua.System.alert('done'));
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

	_onOpenCollection(objectView) {
		const object = objectView.getUserData();
		WinMain.assign("@link/collection", { collection: object.collection.id });
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
		return "com.kidscademy.page.SearchPage";
	}
};

WinMain.createPage(com.kidscademy.page.SearchPage);
