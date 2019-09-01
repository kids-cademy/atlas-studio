$package("com.kidscademy.atlas");

/**
 * Links section from object form. It has a list view that displays all objects links and a 
 * form data for link creation and update. Links section also allows for link object remove.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.atlas.LinksControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Parent form page.
		 * @type {com.kidscademy.atlas.FormPage}
		 */
		this._formPage = null;

		/**
		 * Link objects collection.
		 * @type {Array}
		 */
		this._links = null;
		/**
		 * Index on link objects collection for currently edited link, -1 if not in edit mode.
		 */
		this._editIndex = -1;

		/**
		 * List view for link objects.
		 * @type {js.dom.Element}
		 */
		this._linksView = this.getByCssClass("list-view");
		this._linksView.on("click", this._onLinksViewClick, this);

		this._editor = this.getByCssClass("editor");
		this._formData = this.getByClass(com.kidscademy.FormData);

		/**
		 * Actions manager.
		 * @type {com.kidscademy.Actions}
		 */
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);

		this._showEditor(false);
	}

	onCreate(formPage) {
		this._formPage = formPage;
	}

	onStart() {
	}

	// --------------------------------------------------------------------------------------------
	// CONTROL INTERFACE

	setValue(links) {
		this._links = links;
		this._updateView();
		// ensure editor is closed
		this._showEditor(false);
	}

	getValue() {
		return typeof this._links === "undefined" ? [] : this._links;
	}

	isValid() {
		return true;
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onAdd() {
		this._editIndex = -1;
		this._showEditor(true);
		this._formData.reset();
	}

	_onBrowse() {
		const url = this._formData.getValue("url");
		if (url != null) {
			WinMain.open(url);
		}
	}

	_onDone() {
		if (!this._formData.isValid()) {
			return;
		}

		if (this._editIndex === -1) {
			// edit index is not set therefore we are in append mode
			AtlasService.createLink(this._formData.getObject(), link => {
				this._links.push(link);
				this._updateView();
			});
		}
		else {
			// edit index is set therefore we are in edit mode
			const editLink = this._links[this._editIndex];
			AtlasService.createLink(this._formData.getObject(), link => {
				editLink.url = link.url;
				editLink.name = link.name;
				editLink.description = link.description;
				editLink.iconPath = link.iconPath;
				this._updateView();
			});
		}

		this._showEditor(false);
	}

	_onRemove() {
		if (this._formData.isValid() && this._editIndex !== -1) {
			this._links.splice(this._editIndex, 1);
			this._updateView();
			this._showEditor(false);
		}
	}

	_onClose() {
		this._showEditor(false);
	}

	// --------------------------------------------------------------------------------------------

	_onLinksViewClick(ev) {
		const linkView = ev.target.getParentByTag("li");
		if (linkView != null) {
			this._editIndex = linkView.getChildIndex();
			this._showEditor(true);
			this._formData.setObject(linkView.getUserData());
		}
	}

	_updateView() {
		this._linksView.setObject(this._links);
	}

	_showEditor(show) {
		this._actions.show(show, "browse", "done", "remove", "close");
		this._editor.show(show);
		if (show) {
			this._formData.scrollIntoView();
			this._formData.focus("url");
		}
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.LinksControl";
	}
};
