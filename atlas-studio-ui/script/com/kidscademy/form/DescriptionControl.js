$package("com.kidscademy.form");

com.kidscademy.form.DescriptionControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._textarea = this.getByTag("textarea");
		this._textarea.on("change", this._onChange, this);

		this._linksSelect = this.getByClass(com.kidscademy.form.LinkSelect);

		/**
		 * Actions manager.
		 * @type {com.kidscademy.Actions}
		 */
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
	}

	addParagraph(paragraph) {
		this._setDirty();
		var description = this._textarea.getValue();
		if (!description) {
			this._textarea.setValue(paragraph);
			return;
		}

		description += "\r\n\r\n";
		description += paragraph;
		this._textarea.setValue(description);
	}

	// --------------------------------------------------------------------------------------------
	// CONTROL INTERFACE

	setValue(description) {
		if (description == null) {
			this._textarea.reset();
			return;
		}
		this._textarea.setValue(description.replace(/<p>/g, "").replace(/<\/p>/g, "\n\n"));
	}

	getValue() {
		const description = this._textarea.getValue();
		if (!description) {
			return null;
		}
		return "<p>" + description.trim().replace(/\n\n/g, "</p><p>") + "</p>";
	}

	isValid() {
		return true;
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	/**
	 * Import description from a provider link. 
	 * 
	 * Get from parent page all links that provides description. Alert if there is none. 
	 * If there are more than one link display them and let user choose one.
	 */
	_onImport() {
		this._setDirty();
		const load = (link) => {
			AtlasService.importObjectDescription(link, description => {
				description = description.replace(/<p>/g, "").replace(/<\/p>/g, "\n\n");
				var text = this._textarea.getValue();
				if (text) {
					text += "\n\n";
					text += description;
				}
				else {
					text = description;
				}
				this._textarea.setValue(text);
			});
		}

		const links = this._formPage.getLinks("description");
		switch (links.length) {
			case 0:
				js.ua.System.alert("@string/no-description-provider");
				break;

			case 1:
				load(links[0]);
				break;

			default:
				this._linksSelect.open(links, load);
		}
	}

	_onInsertPicture() {
		this._setDirty();
	}

	_onRemoveAll() {
		js.ua.System.confirm("@string/confirm-description-remove", ok => {
			if (ok) {
				this._setDirty();
				this._textarea.reset();
			}
		});
	}

	_onClose() {
		this._linksSelect.close();
	}

	// --------------------------------------------------------------------------------------------

	_onChange(ev) {
		this._setDirty();
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.DescriptionControl";
	}
};
