$package("com.kidscademy.page");

/**
 * SearchPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.SearchPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of SearchPage class.
	 */
	constructor() {
		super();

		const menu = this.getByCss(".side-bar .menu");
		menu.on(this, {
			"&update-index": this._onUpdateIndex
		});

		this._searchInput = this.getByName("search-input");
		this._searchInput.focus();

		this._infoView = this.getByCssClass("info");

		this._searchResult = this.getByCssClass("search-result");
		this._searchResult.on("click", this._onItemClick, this);
		this._searchResult.on("contextmenu", this._onContextMenu, this);

		this._listType = new com.kidscademy.CssFlags(this._searchResult, "icons", "cards");
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._contextMenu = this.getByClass(com.kidscademy.ContextMenu).bind(this);

		this._searchInput.setValue(this.getPageAttr("search-input"));
		this._listType.set(this.getPageAttr("list-type"));
		this._actions.fire("search-submit");
	}

	_onUnload() {
		this.setPageAttr("search-input", this._searchInput.getValue());
		this.setPageAttr("list-type", this._listType.get());
	}

	// --------------------------------------------------------------------------------------------
	// FILTER ACTION HANDLERS

	_onSearchSubmit() {
		const search = this._searchInput.getValue();
		if (search) {
			const timestamp = Date.now();
			AtlasService.search(search, items => {
				this._infoView.setObject({
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
		this._infoView.hide();
		this._searchResult.removeChildren();
	}

	_onIconsView() {
		this._listType.set("icons");
	}

	_onCardsView() {
		this._listType.set("cards");
	}

	// --------------------------------------------------------------------------------------------
	// SIDE MENU HANDLERS

	_onUpdateIndex() {
		AtlasService.updateIndex(() => js.ua.System.alert('done'));
	}

	// --------------------------------------------------------------------------------------------
	// CONTEXT MENU HANDLERS

	_onEditObject(objectView) {
		this._moveToPage(objectView, false);
	}

	_onPreviewObject(objectView) {
		this._moveToPage(objectView, true);
	}

	// --------------------------------------------------------------------------------------------

	_onItemClick(ev) {
		const li = ev.target.getParentByTag("li");
		if (li != null) {
			this._moveToPage(li, ev.ctrlKey);
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

	_moveToPage(objectView, reader) {
		const object = objectView.getUserData();

		this.setContextAttr("collection", object.collection);
		this.setContextAttr("objectId", object.id);

		WinMain.assign(reader ? "@link/reader" : "@link/form");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.page.SearchPage";
	}
};

WinMain.createPage(com.kidscademy.page.SearchPage);
