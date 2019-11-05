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

		const actions = this.getByCss(".side-bar .actions");
		actions.on(this, {
			"&update-index": this._onUpdateIndex
		});

		this._searchInput = this.getByName("search-input");
		this._searchInput.focus();

		this._infoView = this.getByCssClass("info");

		this._searchResult = this.getByCssClass("search-result");
		this._searchResult.on("click", this._onItemClick, this);
		this._searchResult.hide();

		this._listType = new com.kidscademy.CssFlags(this._searchResult, "icons", "cards");
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);

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

	_onItemClick(ev) {
		const li = ev.target.getParentByTag("li");
		if (li == null) {
			return;
		}
		const object = li.getUserData();

		if (ev.ctrlKey) {
			this._moveToPage("@link/reader", object);
		}
		else {
			this._moveToPage("@link/form", object);
		}
	}

	_moveToPage(pageName, object) {
		this.setContextAttr("collection", object.collection);
		this.setContextAttr("objectId", object.id);
		WinMain.assign(pageName);
	}

	_onUpdateIndex() {
		AtlasService.updateIndex(() => js.ua.System.alert('done'));
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
