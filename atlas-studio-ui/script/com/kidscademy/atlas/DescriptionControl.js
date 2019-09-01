$package("com.kidscademy.atlas");

com.kidscademy.atlas.DescriptionControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Parent form page.
		 * @type {com.kidscademy.atlas.FormPage}
		 */
		this._formPage = null;

		this._textarea = this.getByTag("textarea");

		this._linksSelect = this.getByClass(com.kidscademy.atlas.LinkSelect);

		/**
		 * Actions manager.
		 * @type {com.kidscademy.Actions}
		 */
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
	}

	onCreate(formPage) {
		this._formPage = formPage;
	}

	onStart() {
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
				js.ua.System.alert("No provider link for description.");
				break;

			case 1:
				load(links[0]);
				break;

			default:
				this._linksSelect.open(links, load);
		}
	}

	_onInsertPicture() {

	}

	_onRemove() {
		this._textarea.reset();
	}

	_onClose() {
		this._linksSelect.close();
	}

	// --------------------------------------------------------------------------------------------

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.DescriptionControl";
	}
};
