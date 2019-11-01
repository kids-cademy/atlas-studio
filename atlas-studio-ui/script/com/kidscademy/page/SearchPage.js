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
		this._searchInput.on("keydown", this._onInputKey, this);

		this.getByName("search-submit").on("click", this._onSearch, this);
		this.getByName("search-reset").on("click", this._onReset, this);

		this._searchResult = this.getByCssClass("search-result");
		this._searchResult.on("click", this._onItemClick, this);
		this._searchInput.reset().focus();

		this._searchInput.setValue(this.getContextAttr("search"));
		this._onSearch();
	}

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

	_onInputKey(ev) {
		switch (ev.key) {
			case js.event.Key.ENTER:
				ev.halt();
				this._onSearch();
				break;

			case js.event.Key.ESCAPE:
			case js.event.Key.DELETE:
				ev.halt();
				this._onReset();
				break;
		}
	}

	_onSearch() {
		const search = this._searchInput.getValue();
		this.setContextAttr("search", search);
		if (search) {
			AtlasService.search(search, items => {
				this._searchResult.setObject(items)
				this._searchInput.focus();
			});
		}
	}

	_onReset() {
		this._searchInput.reset();
		this._searchResult.removeChildren();
		this.setContextAttr("search", null);
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
