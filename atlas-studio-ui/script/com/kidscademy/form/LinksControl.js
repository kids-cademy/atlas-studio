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
		 * Link objects collection.
		 * @type {Array}
		 */
		this._links = [];

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
		this._urlInput = this._editor.getByName("url");
		this._urlInput.on("paste", this._onUrlPaste, this);
		this._descriptionInput = this._editor.getByName("description");
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
		if (!this._formData.isValid() || this._editIndex === -1) {
			return;
		}
		js.ua.System.confirm("@string/confirm-link-remove", ok => {
			if (ok) {
				this._links.splice(this._editIndex, 1);
				this._updateView();
				this._showEditor(false);
			}
		});
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

	_onUrlPaste(ev) {
		const objectDisplay = this._formPage.getObject().display;
		if (!objectDisplay) {
			js.ua.System.alert("@string/object-no-display");
			return;
		}

		const url = ev.getData();
		const domain = /^(?:http|https|ftp|file):\/\/(?:[^.]+\.)*([^.]+\.[^:/]+).*$/.exec(url)[1];
		if (!domain) {
			return;
		}

		const descriptionHandler = com.kidscademy.form.LinksControl.DomainDescription[domain];
		if (descriptionHandler) {
			descriptionHandler(url, objectDisplay, (description) => this._descriptionInput.setValue(description));
		}
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

com.kidscademy.form.LinksControl.DomainDescription = {
	"wikipedia.org": function (url, object, callback) {
		callback(`Wikipedia article about ${object.toLowerCase()}.`);
	},

	"britannica.com": function (url, object, callback) {
		callback(`${object} article on Britannica.`);
	},

	"thefreedictionary.com": function (url, object, callback) {
		callback(`${object} definition on dictionary.`);
	},

	"youtube.com": function (url, object, callback) {
		const xhr = new XMLHttpRequest();
		xhr.open('GET', `https://noembed.com/embed?url=${url}`);
		xhr.onload = () => {
			if (xhr.response) {
				const response = JSON.parse(xhr.response);
				if (response.title) {
					callback(response.title);
				}
			}
		};
		xhr.send();
	},

	"kiddle.co": function (url, object, callback) {
		callback(`${object} facts for kids.`);
	},

	"wikihow.com": function (url, object, callback) {
		AtlasService.getWikiHowTitle(url, callback);
	}
};
