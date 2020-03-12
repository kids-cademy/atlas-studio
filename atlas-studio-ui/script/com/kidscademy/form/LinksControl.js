$package("com.kidscademy.form");

/**
 * Links section from object form. It has a list view that displays all objects links and a 
 * form data for link creation and update. Links section also allows for link object remove.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.form.LinksControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Link view currently in edit mode or null.
		 * @type {js.dom.Element}
		 */
		this._editLink = null;

		/**
		 * List view for link objects.
		 * @type {js.dom.Element}
		 */
		this._linksView = this.getByCssClass("list-view");
		this._linksView.on("click", this._onLinksViewClick, this);
		this._linksView.on("dragstart", this._onDragStart, this);
		this._linksView.on("dragover", this._onDragOver, this);
		this._linksView.on("drop", this._onDrop, this);

		this._editor = this.getByCssClass("editor");
		this._urlInput = this._editor.getByName("url");
		this._urlInput.on("paste", this._onUrlPaste, this);
		this._definitionInput = this._editor.getByName("definition");
		this._formData = this.getByClass(com.kidscademy.FormData);

		/**
		 * Actions manager.
		 * @type {com.kidscademy.Actions}
		 */
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);

		this._showEditor(false);
	}

	// --------------------------------------------------------------------------------------------
	// CONTROL INTERFACE

	setValue(links) {
		this._linksView.setObject(links);
		// ensure editor is closed
		this._showEditor(false);
	}

	getValue() {
		return this._linksView.getChildren().map(linkView => linkView.getUserData("value"));
	}

	isValid() {
		return true;
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onAdd() {
		this._editLink = null;
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

		if (this._editLink == null) {
			// edit link is not set therefore we are in append mode
			AtlasService.createLink(this._formData.getObject(), link => {
				this._linksView.addObject(link);
			});
		}
		else {
			// edit link is set therefore we are in edit mode
			// since link URL can be changed need to recreate link and update currently edited link view
			AtlasService.createLink(this._formData.getObject(), link => {
				this._editLink.setObject(link);
			});
		}

		this._showEditor(false);
	}

	_onRemove() {
		if (!this._formData.isValid() || this._editLink == null) {
			return;
		}
		js.ua.System.confirm("@string/confirm-link-remove", ok => {
			if (ok) {
				this._editLink.remove();
				this._showEditor(false);
			}
		});
	}

	_onClose() {
		this._showEditor(false);
	}

	// --------------------------------------------------------------------------------------------
	// DRAG AND DROP


	_onDragStart(ev) {
		const li = ev.target.getParentByTag("li");
		ev.setData({
			index: li.getChildIndex()
		});
	}

	_onDragOver(ev) {
		ev.prevent();
	}

	_onDrop(ev) {
		ev.prevent();
		const data = ev.getData();
		const sourceElement = this._linksView.getByIndex(data.index);
		const targetElement = ev.target.getParentByTag("li");

		if (targetElement == null) {
			return;
		}
		if (targetElement === sourceElement) {
			return;
		}

		if (ev.ctrlKey) {
			const siblingElement = targetElement.getNextSibling();
			if (siblingElement == null) {
				targetElement.getParent().addChild(sourceElement);
			}
			else {
				siblingElement.insertBefore(sourceElement);
			}
		}
		else {
			targetElement.insertBefore(sourceElement);
		}
		this._fireEvent("input");
	}

	// --------------------------------------------------------------------------------------------

	_onLinksViewClick(ev) {
		this._editLink = ev.target.getParentByTag("li");
		if (this._editLink != null) {
			this._showEditor(true);
			this._formData.setObject(this._editLink.getUserData());
		}
	}

	_showEditor(show) {
		this._actions.show(show, "browse", "done", "remove", "close");
		this._editor.show(show);
		if (show) {
			this._formData.scrollIntoView();
			this._formData.focus("url");
		}
	}

	_onUrlPaste(ev) {
		const objectDisplay = this._formPage.getObject().display;
		if (!objectDisplay) {
			js.ua.System.alert("@string/alert-object-no-display");
			return;
		}
		const url = ev.getData();
		AtlasService.getLinkDefinition(url, objectDisplay, definition => this._definitionInput.setValue(definition));
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.LinksControl";
	}
};
