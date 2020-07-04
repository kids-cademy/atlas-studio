$package("com.kidscademy.form");

com.kidscademy.form.DefinitionControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._textarea = this.getByTag("textarea");
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
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
		const load = (link) => ApiService.getDefinition(link, definition => this._textarea.setValue(definition));
		this._formPage.importFromLink("definition", load);
	}

	_onRemoveAll() {
		js.ua.System.confirm("@string/confirm-definition-remove", ok => {
			if (ok) {
				this._textarea.reset();
			}
		});
	}

	_onClose() {
		this._linksSelect.close();
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.DefinitionControl";
	}
};
