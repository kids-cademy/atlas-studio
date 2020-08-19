$package("com.kidscademy.translate");

com.kidscademy.translate.FactsControl = class extends js.dom.Control {
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

    setValue(facts) {
        this._factsView.setObject(facts);
    }

    getValue() {
        return this._factsView.getListData();
    }

    isValid() {
        return true;
    }

    _onDone() {
        const fact = this._itemOnEdit.getUserData();
        fact.title = this._termInput.getValue();
        fact.text = this._definitionInput.getValue();
        this._itemOnEdit.setObject(fact);
        this._onClose();
    }

    _onClose() {
        this._itemOnEdit = null;
        this._showEditor(false);
    }

    _onFactsClick(ev) {
        if (ev.target.getTag() === "h1") {
            this._showEditor(true);
            this._itemOnEdit = ev.target.getParentByTag("li");
            this._termInput.setValue(this._itemOnEdit.getByTag("h1").getText());
            this._definitionInput.setValue(this._itemOnEdit.getByTag("p").getText());
        }
    }

    _showEditor(show) {
        this._actions.show(show, "done", "close");
        this._editor.show(show);
        if (show) {
            this._termInput.focus();
        }
    }

    toString() {
        return "com.kidscademy.translate.FactsControl";
    }
};
