$package("com.kidscademy.form");

com.kidscademy.form.FactsControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Facts dictionary.
		 * 
		 * @type {Object}
		 */
		this._facts = null;

		this._factsView = this.getByCssClass("facts-view");
		this._factsView.on("click", this._onFactsClick, this);

		this._editor = this.getByCssClass("editor");
		this._termInput = this._editor.getByName("term");
		this._definitionInput = this._editor.getByName("definition");
		this._termOnEdit = null;

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);

		this._showEditor(false);
	}

	// --------------------------------------------------------------------------------------------
	// CONTROL INTERFACE

	setValue(facts) {
		function empty(object) {
			for (let property in object) {
				if (object.hasOwnProperty(property)) {
					return false;
				}
			}
			return true;
		}

		this._facts = facts;
		if (!empty(facts)) {
			this._factsView.setObject(facts);
		}
	}

	getValue() {
		return this._facts;
	}

	isValid() {
		return true;
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onImport() {
		this._fireEvent("input");
		const load = (link) => ApiService.getFacts(link, facts => this.setValue(facts));
		this._formPage.importFromLink("facts", load);
	}

	_onAdd() {
		if (this._editor.isVisible()) {
			return;
		}
		this._showEditor(true);
		this._termInput.reset();
		this._definitionInput.reset();
	}

	_onDone() {
		if (this._termOnEdit) {
			delete this._facts[this._termOnEdit];
			this._termOnEdit = null;
		}
		this._facts[this._termInput.getValue()] = this._definitionInput.getValue();

		this._factsView.setObject(this._facts);
		this._showEditor(false);
	}

	_onMoveToDefinition() {
		const value = this._facts[this._termInput.getValue()];
		this._formPage._getDefinitionControl().setValue(value);
		this._onRemove();
	}

	_onMoveToDescription() {
		const value = this._facts[this._termInput.getValue()];
		this._formPage._getDescriptionControl().addParagraph(value);
		this._onRemove();
	}

	_onRemove() {
		js.ua.System.confirm("@string/confirm-fact-remove", ok => {
			if (ok) {
				delete this._facts[this._termInput.getValue()];
				this._termOnEdit = null;
				this._factsView.setObject(this._facts);
				this._showEditor(false);
				this._fireEvent("input");
			}
		});
	}

	_onRemoveAll() {
		js.ua.System.confirm("@string/confirm-all-facts-remove", ok => {
			if (ok) {
				const object = this._formPage.getObject();
				this._facts = null;
				this._factsView.resetObject();
				this._fireEvent("input");
			}
		});
	}

	_onClose() {
		this._showEditor(false);
	}

	// --------------------------------------------------------------------------------------------

	_onFactsClick(ev) {
		if (ev.target.getTag() === "dt") {
			this._showEditor(true);
			this._termOnEdit = ev.target.getText();
			this._termInput.setValue(ev.target.getText());
			this._definitionInput.setValue(ev.target.getNextSibling().getText());
		}
	}

	_showEditor(show) {
		this._actions.show(show, "done", "move-to-definition", "move-to-description", "remove", "close");
		this._editor.show(show);
		if (show) {
			this._definitionInput.focus();
		}
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.FactsControl";
	}
};

$preload(com.kidscademy.form.FactsControl);
