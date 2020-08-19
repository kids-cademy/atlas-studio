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

	_onImport() {
		this._formPage.importFromLink("definition", definition => this._textarea.setValue(definition));
	}

	_onRemoveAll() {
		js.ua.System.confirm("@string/confirm-definition-remove", ok => {
			if (ok) {
				this._textarea.reset();
			}
		});
	}

	toString() {
		return "com.kidscademy.form.DefinitionControl";
	}
};
