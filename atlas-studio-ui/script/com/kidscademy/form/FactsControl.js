$package("com.kidscademy.form");

com.kidscademy.form.FactsControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._factsView = this.getByCssClass("facts-view");
		this._factsView.on("click", this._onFactsClick, this);

		this._editor = this.getByCssClass("editor");
		this._termInput = this._editor.getByName("term");
		this._definitionInput = this._editor.getByName("definition");
		this._itemOnEdit = null;

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);

		this._showEditor(false);
	}

	// --------------------------------------------------------------------------------------------
	// CONTROL INTERFACE

	setValue(facts) {
		this._factsView.setObject(facts);
	}

	getValue() {
		return this._factsView.getListData();
	}

	isValid() {
		return true;
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onImport() {
		this._fireEvent("input");
		this._formPage.importFromLink("facts", facts => this.setValue(facts));
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
		if (this._itemOnEdit != null) {
			const fact = this._itemOnEdit.getUserData();
			fact.title = this._termInput.getValue();
			fact.text = this._definitionInput.getValue();
			this._itemOnEdit.setObject(fact);
			this._itemOnEdit = null;
		}
		else {
			this._factsView.addObject({
				id: 0,
				title: this._termInput.getValue(),
				text: this._definitionInput.getValue()
			});
		}
		this._showEditor(false);
	}

	_onMoveToDefinition() {
		const fact = this._itemOnEdit.getUserData();
		this._formPage._getDefinitionControl().setValue(fact.text);
		this._onRemove();
	}

	_onMoveToDescription() {
		const fact = this._itemOnEdit.getUserData();
		this._formPage._getDescriptionControl().addParagraph(fact.text);
		this._onRemove();
	}

	_onRemove() {
		js.ua.System.confirm("@string/confirm-fact-remove", ok => {
			if (ok) {
				this._itemOnEdit.remove();
				this._itemOnEdit = null;
				this._showEditor(false);
				this._fireEvent("input");
			}
		});
	}

	_onRemoveAll() {
		js.ua.System.confirm("@string/confirm-all-facts-remove", ok => {
			if (ok) {
				const object = this._formPage.getObject();
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
		if (ev.target.getTag() === "h1") {
			this._showEditor(true);
			this._itemOnEdit = ev.target.getParentByTag("li");
			this._termInput.setValue(this._itemOnEdit.getByTag("h1").getText());
			this._definitionInput.setValue(this._itemOnEdit.getByTag("p").getText());
		}
	}

	_showEditor(show) {
		this._actions.show(show, "done", "move-to-definition", "move-to-description", "remove", "close");
		this._editor.show(show);
		if (show) {
			this._termInput.focus();
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
