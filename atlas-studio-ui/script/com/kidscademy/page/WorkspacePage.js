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

		const sideMenu = this.getByCss(".side-bar .menu");
		sideMenu.on(this, {
			"&collections-list": this._onCollectionsList,
			"&export-all-collections": this._onExportAllCollections,
			"&links-meta-list": this._onLinksMetaList
		});
		const exportAnchor = sideMenu.getByCssClass("export-all-collections");
		exportAnchor.setAttr("href", "export-all-atlas-collections.xsp");

		this._contentView = this.getByClass(com.kidscademy.FrameView);
		this.selectView("collections-list");

		this._collectionsList = this.getByClass(com.kidscademy.workspace.CollectionsList);
		this._linksMetaList = this.getByClass(com.kidscademy.workspace.LinksMetaList);

		this._collectionsList.onCreate(this);
		this._linksMetaList.onCreate(this);
	}

	selectView(viewName) {
		this._contentView.select(viewName);
	}

	_onUnload() {
		this._collectionsList.onDestroy(this);
		this._linksMetaList.onDestroy(this);
	}

	// --------------------------------------------------------------------------------------------
	// SIDE MENU HANDLERS

	_onCollectionsList() {
		this.selectView("collections-list");
	}

	_onExportAllCollections() {

	}

	_onLinksMetaList() {
		this.selectView("links-meta-list");

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
