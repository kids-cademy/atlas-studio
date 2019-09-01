$package("com.kidscademy.atlas");

com.kidscademy.atlas.DefinitionControl = class extends js.dom.Control {
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

	setValue(definition) {
		if (!definition) {
			this._textarea.reset();
			return;
		}
		this._textarea.setValue(definition);
	}

	getValue() {
		return this._textarea.getValue();
	}

	isValid() {
		return this._textarea.isValid();
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	/**
	 * Import object definition from a provider link. 
	 * 
	 * Get from parent page all links that provides object definitions. Alert if there is none. 
	 * If there are more than one link display them and let user choose one.
	 */
	_onImport() {
		const load = (link) => {
			AtlasService.importObjectDefinition(link, definition => {
				this._textarea.setValue(definition);
			});
		}

		const links = this._formPage.getLinks("definition");
		switch (links.length) {
			case 0:
				js.ua.System.alert("No provider link for definition.");
				break;

			case 1:
				load(links[0]);
				break;

			default:
				this._linksSelect.open(links, load);
		}
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
		return "com.kidscademy.atlas.DefinitionControl";
	}
};
