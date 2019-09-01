$package("com.kidscademy.atlas");

/**
 * CollectionPage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.atlas.CollectionPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of CollectionPage class.
	 */
	constructor() {
		super();

		this._listView = this.getByCssClass("list-view");
		this._listView.on("click", this._onListClick, this);

		const actions = this.getByCss(".side-bar .actions");
		actions.on(this, {
			"&new-object": this._onNewObject
		});

		const collectionName = "musical-instruments";
		AtlasService.getCollectionItems(collectionName, items => this._listView.setObject(items));
	}

	_onListClick(ev) {
		const li = ev.target.getParentByTag("li");
		WinMain.assign("form.htm", { id: li.getAttr("id") });
	}

	_onNewObject() {
		WinMain.assign("form.htm", { id: 0 });
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.CollectionPage";
	}
};

WinMain.createPage(com.kidscademy.atlas.CollectionPage);
